package com.greenelegentfarmer.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class BoxItem {

	@NotNull
	private Long boxId;
	@NotEmpty
	private List<Long> items;
	
	public Long getBoxId() {
		return boxId;
	}
	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}
	public List<Long> getItems() {
		return items;
	}
	public void setItems(List<Long> items) {
		this.items = items;
	}
}
