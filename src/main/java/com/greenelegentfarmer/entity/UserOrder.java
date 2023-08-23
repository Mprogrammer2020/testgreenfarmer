package com.greenelegentfarmer.entity;

import java.util.List;
import javax.persistence.Id;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime orderDate;
	
	private LocalDateTime deliveryDate;
	
	@Embedded
	private UserOrderAddress address;
	
	private String status;
	
	private String paymentStatus;
	
	private String sourceId;
	
	private String chargeId;
	
	private Float deliveryFee;

	private Boolean customized;
	
	private Float subscriptionFee;
	
	@ManyToOne
	@JoinColumn(name = "subscription_id")
	private UserSubscription subscription;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_order_items", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
	private List<Item> items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	
	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public UserOrderAddress getAddress() {
		return address;
	}

	public void setAddress(UserOrderAddress address) {
		this.address = address;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public Float getSubscriptionFee() {
		return subscriptionFee;
	}

	public void setSubscriptionFee(Float subscriptionFee) {
		this.subscriptionFee = subscriptionFee;
	}

	public Float getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(Float deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	
	public Boolean getCustomized() {
		return customized;
	}

	public void setCustomized(Boolean customized) {
		this.customized = customized;
	}

	public UserSubscription getSubscription() {
		return subscription;
	}

	public void setSubscription(UserSubscription subscription) {
		this.subscription = subscription;
	}
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}	
}
