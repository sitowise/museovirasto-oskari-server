package fi.nls.oskari.service.styles;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import fi.nls.oskari.annotation.Oskari;
import fi.nls.oskari.cache.JedisManager;
import fi.nls.oskari.db.DatasourceHelper;
import fi.nls.oskari.domain.map.wfs.WFSLayerConfiguration;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.wfs.WFSLayerConfigurationService;
import fi.nls.oskari.wfs.WFSLayerConfigurationServiceIbatisImpl;

@Oskari
public class SldStylesServiceMybatisImpl extends SldStylesService {

    private static final Logger LOG = LogFactory.getLogger(SldStylesServiceMybatisImpl.class);
    private SqlSessionFactory factory = null;
    private WFSLayerConfigurationService wfsLayerService = new WFSLayerConfigurationServiceIbatisImpl();

    public SldStylesServiceMybatisImpl() {
    }

    private SqlSessionFactory getFactory() {
        if(factory != null) {
            return factory;
        }
        final DatasourceHelper helper = DatasourceHelper.getInstance();
        final DataSource dataSource = helper.getDataSource(helper.getOskariDataSourceName());
        if(dataSource != null) {
            factory = initializeMyBatis(dataSource);
        } else {
            LOG.error("Couldn't get datasource for", getClass().getName());
        }
        return factory;
    }

    private SqlSessionFactory initializeMyBatis(final DataSource dataSource) {
        final TransactionFactory transactionFactory = new JdbcTransactionFactory();
        final Environment environment = new Environment("development", transactionFactory, dataSource);

        final Configuration configuration = new Configuration(environment);
        configuration.getTypeAliasRegistry().registerAlias(SldStyle.class);
        configuration.setLazyLoadingEnabled(true);
        configuration.addMapper(SldStylesMapper.class);

        return new SqlSessionFactoryBuilder().build(configuration);
    }

    /**
     * Tries to load sld styles from the database
     * @return null if no styles in db
     */
    public List<SldStyle> selectAll() {


        final SqlSession session = getFactory().openSession();
        try {
            final SldStylesMapper mapper = session.getMapper(SldStylesMapper.class);
            return mapper.selectAll();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load sld styles", e);
        } finally {
            session.close();
        }
    }

    public int saveStyle(SldStyle style) {


        final SqlSession session = getFactory().openSession();
        try {
            final SldStylesMapper mapper = session.getMapper(SldStylesMapper.class);

            mapper.saveStyle(style);

            session.commit();
            LOG.debug("Saved new style with id", style.getId());

            return style.getId();

        } catch (Exception e) {
            throw new RuntimeException("Failed to insert sld style", e);
        } finally {
            session.close();
        }
    }

    public int updateStyle(SldStyle style) {
        final SqlSession session = getFactory().openSession();
        try {
            final SldStylesMapper mapper = session.getMapper(SldStylesMapper.class);

            mapper.updateStyle(style);

            session.commit();
            LOG.debug("Saved new style for id", style.getId());

            List<WFSLayerConfiguration> layers = this.wfsLayerService.findWFSLayersWithStyle(style.getId());
            for(WFSLayerConfiguration layer : layers) {
                JedisManager.delAll(WFSLayerConfiguration.KEY + layer.getLayerId());
                JedisManager.delAll(WFSLayerConfiguration.IMAGE_KEY + layer.getLayerId());
            }

            return style.getId();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update sld style", e);
        } finally {
            session.close();
        }
    }

}