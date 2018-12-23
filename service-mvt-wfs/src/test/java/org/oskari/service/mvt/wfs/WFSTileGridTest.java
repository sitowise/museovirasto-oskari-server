package org.oskari.service.mvt.wfs;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class WFSTileGridTest {

    @Test
    public void testETRSTM35FIN() {
        WFSTileGrid etrsTM35fin = new WFSTileGrid(new double[] { -548576, 6291456, 1548576, 8388608 }, 15);

        double[] actuals = etrsTM35fin.getTileExtent(new TileCoord(0, 0, 0));
        double[] expecteds = { -548576, 6291456, 1548576, 8388608 };
        assertArrayEquals(expecteds, actuals, 0);

        double[] actuals2 = etrsTM35fin.getTileExtent(new TileCoord(1, 0, 1));
        double[] expecteds2 = { -548576, 6291456, -548576 + 4096*256, 8388608 - 4096*256 };
        assertArrayEquals(expecteds2, actuals2, 0);

        double[] actuals3 = etrsTM35fin.getTileExtent(new TileCoord(1, 1, 1));
        double[] expecteds3 = { -548576 + 4096*256, 6291456, 1548576, 8388608 - 4096*256 };
        assertArrayEquals(expecteds3, actuals3, 0);
    }

}
