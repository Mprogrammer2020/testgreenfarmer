package com.greenelegentfarmer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import com.greenelegentfarmer.dto.PreReserveDTO;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.service.UserService;

@Component
public class Validation {

	@Autowired
	private UserService userService;

	public void validateSwapOrder(BindingResult result) throws Exception {

		User user = userService.whoAmI();


		if (result.hasErrors())
			return;

	}

	public void validatePreReserve(PreReserveDTO reserve, BindingResult result) throws Exception {

		if (reserve.getProductStockId() == null) {
			result.rejectValue("stock", null, "Please provide product stock id");
		}

		if (result.hasErrors())
			return;

		if (result.hasErrors())
			return;

	}

}
