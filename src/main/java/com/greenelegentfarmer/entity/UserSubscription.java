package com.greenelegentfarmer.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSubscription {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime subscribedOn;
	
	private LocalDateTime delivery;

	private String frequency;
	
	private String status;
	
	private Float subscriptionFee;
	
	private Float deliveryFee;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "box_id")
	private Box box;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_subscription_items", joinColumns = @JoinColumn(name = "subscription_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
	private List<Item> items;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getSubscribedOn() {
		return subscribedOn;
	}

	public void setSubscribedOn(LocalDateTime subscribedOn) {
		this.subscribedOn = subscribedOn;
	}

	public LocalDateTime getDelivery() {
		return delivery;
	}

	public void setDelivery(LocalDateTime delivery) {
		this.delivery = delivery;
	}
	
	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
