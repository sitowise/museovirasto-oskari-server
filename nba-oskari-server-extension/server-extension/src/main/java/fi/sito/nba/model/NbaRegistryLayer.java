package fi.sito.nba.model;

public class NbaRegistryLayer {
	private int registryId;
	private String registryName;
	private int layerId;
	private Boolean toOpen;
	private Boolean toHighlight;
	private String mainItemIdAttr;
	private String subItemIdAttr;
	private String itemType;
	private String gfiAttributes;
	private String classification;
	
	public int getRegistryId() {
		return registryId;
	}
	public void setRegistryId(int registryId) {
		this.registryId = registryId;
	}
	public String getRegistryName() {
		return registryName;
	}
	public void setRegistryName(String registryName) {
		this.registryName = registryName;
	}
	public int getLayerId() {
		return layerId;
	}
	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}
	public Boolean getToOpen() {
		return toOpen;
	}
	public void setToOpen(Boolean toOpen) {
		this.toOpen = toOpen;
	}
	public Boolean getToHighlight() {
		return toHighlight;
	}
	public void setToHighlight(Boolean toHighlight) {
		this.toHighlight = toHighlight;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getGfiAttributes() {return gfiAttributes;}
	public  void setGfiAttributes(String gfiAttributes) { this.gfiAttributes = gfiAttributes; }
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getMainItemIdAttr() {return mainItemIdAttr;}
	public void setMainItemIdAttr(String mainItemIdAttr) {this.mainItemIdAttr = mainItemIdAttr;}
	public String getSubItemIdAttr() {return subItemIdAttr;}
	public void setSubItemIdAttr(String subItemIdAttr) {this.subItemIdAttr = subItemIdAttr;}
}
