package com.greenelegentfarmer.param;

import java.time.LocalDate;

import com.greenelegentfarmer.entity.User;

public class UserOrderTransactionParam extends PagedSearchParam {

	private User user;
	private String status;
	private LocalDate from;
	private LocalDate to;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
