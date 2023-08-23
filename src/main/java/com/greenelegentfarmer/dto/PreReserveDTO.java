package com.greenelegentfarmer.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

public class PreReserveDTO implements DTOToEntity<PreReserve> {

	@NotNull
	private Long productStockId;

	@NotNull
	private Boolean notifyOnly;

	public Long getProductStockId() {
		return productStockId;
	}

	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
	}

	public Boolean getNotifyOnly() {
		return notifyOnly;
	}

	public void setNotifyOnly(Boolean notifyOnly) {
		this.notifyOnly = notifyOnly;
	}

	@Override
	public PreReserve toEntity() {
		PreReserve preReserve=new PreReserve();
		

		preReserve.setNotifyOnly(notifyOnly);
		return preReserve;
	}
}

class PreReserve{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(updatable = false)
	@CreationTimestamp
	private Date createdDate;
	
	
	private String status;
	
	@NotNull
	private Boolean notifyOnly;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getNotifyOnly() {
		return notifyOnly;
	}

	public void setNotifyOnly(Boolean notifyOnly) {
		this.notifyOnly = notifyOnly;
	}

}
