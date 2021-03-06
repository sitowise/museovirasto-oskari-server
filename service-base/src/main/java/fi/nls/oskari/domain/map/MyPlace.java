package fi.nls.oskari.domain.map;

public class MyPlace {
    
    private long id;
    private String uuid;
    private long categoryId;
    private String name;
    private String desc;
    private String link;
    private String imageUrl;
    private String attentionText;
    private String attributes;

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAttentionText() {
        return attentionText;
    }

    public void setAttentionText(String attentionText) {
        this.attentionText = attentionText;
    }

    public boolean isOwnedBy(final String uuid) {
        if(uuid == null || getUuid() == null) {
            return false;
        }
        return getUuid().equals(uuid);
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
    
}
