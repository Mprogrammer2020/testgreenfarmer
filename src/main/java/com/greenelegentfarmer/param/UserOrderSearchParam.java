package com.greenelegentfarmer.param;

import java.time.LocalDate;

public class UserOrderSearchParam extends PagedSearchParam {
	
	private Integer box;
	private String search;
	private String status;
	private LocalDate from;
	private LocalDate to;

	public Integer getBox() {
		return box;
	}

	public void setBox(Integer box) {
		this.box = box;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}
}
