package com.greenelegentfarmer.service;

import java.util.List;
import java.util.stream.Collectors;
import com.greenelegentfarmer.entity.User;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.util.CrudService;
import com.greenelegentfarmer.entity.UserAddress;
import com.greenelegentfarmer.util.SpecificationFactory;
import com.greenelegentfarmer.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserAddressService extends CrudService<UserAddress> implements SpecificationFactory<UserAddress> {

	private UserAddressRepository repository;

	public UserAddressService(UserAddressRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Autowired
	private UserService userService;

	@Override
	public UserAddress add(UserAddress type) {
		type.setUser(userService.whoAmI());
		return super.add(type);
	}

	public List<UserAddress> myAddresses() {
		User user = userService.whoAmI();

		return repository.findByUserIdOrderByIdDesc(user.getId());
	}

	public UserAddress primaryAddress() {
		List<UserAddress> primaryAddressList = myAddresses().stream()
				.filter(address -> address.getPrimaryA() != null && address.getPrimaryA()).collect(Collectors.toList());
		return primaryAddressList.size() == 0 ? null : primaryAddressList.get(0);
	}

	public boolean primaryExists() {
		return primaryAddress() != null;
	}
}
