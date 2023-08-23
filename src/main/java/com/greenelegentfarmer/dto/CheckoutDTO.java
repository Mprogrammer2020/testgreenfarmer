package com.greenelegentfarmer.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.greenelegentfarmer.entity.Box;
import com.greenelegentfarmer.entity.UserAddress;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.entity.UserSubscription;

public class CheckoutDTO implements DTOToEntity<UserOrder> {

	private Long subscriptionId;
	@NotNull
	private Long boxId;
	@NotBlank
	private String frequency;
	private Long addressId;
	@Valid
	private UserAddress address;
	@NotNull
	private String source;
	@NotNull
	private List<Long> items;
	
	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	public UserAddress getAddress() {
		return address;
	}

	public void setAddress(UserAddress address) {
		this.address = address;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Long> getItems() {
		return items;
	}

	public void setItems(List<Long> items) {
		this.items = items;
	}

	@Override
	public UserOrder toEntity() {
		UserOrder order = new UserOrder();

		UserSubscription subscription = new UserSubscription();
		subscription.setId(subscriptionId);

		Box box = new Box();
		UserAddress address = new UserAddress();

		box.setId(boxId);
		address.setId(addressId);

		subscription.setBox(box);
		subscription.setFrequency(frequency);
		
		order.setSubscription(subscription);

		return order;
	}

}
