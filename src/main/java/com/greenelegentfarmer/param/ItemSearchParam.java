package com.greenelegentfarmer.param;

public class ItemSearchParam extends PagedSearchParam {

	private String name;
	private Boolean enabled;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
