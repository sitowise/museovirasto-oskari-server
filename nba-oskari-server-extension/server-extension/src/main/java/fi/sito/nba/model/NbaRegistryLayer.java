package fi.sito.nba.model;

public class NbaRegistryLayer {
	private int registryId;
	private String registryName;
	private int layerId;
	private Boolean toOpen;
	private Boolean toHighlight;
	private String itemIdAttribute;
	private String itemType;
	
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
	public String getItemIdAttribute() {
		return itemIdAttribute;
	}
	public void setItemIdAttribute(String itemIdAttribute) {
		this.itemIdAttribute = itemIdAttribute;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
}
