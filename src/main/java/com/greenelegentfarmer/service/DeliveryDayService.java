package com.greenelegentfarmer.service;

import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.entity.DeliveryDay;
import com.greenelegentfarmer.repository.DeliveryDayRepository;
import com.greenelegentfarmer.util.CrudService;
import com.greenelegentfarmer.util.DateUtil;

@Service
public class DeliveryDayService extends CrudService<DeliveryDay> {

	public DeliveryDayService(DeliveryDayRepository repository) {
		super(repository);
		this.repository = repository;
	}

	public void setInitialData() {
		DateUtil.getWeekDays().forEach(weekDay -> {
			DeliveryDay day = new DeliveryDay();
			day.setDay(weekDay.name().toUpperCase());
			day.setEnabled(false);
			this.add(day);
		});
	}

	public LocalDateTime getNextDelivery() {
		LocalDateTime startFrom = LocalDateTime.now().plusDays(3);
		
		List<String> deliveryDays=getAll().stream()
				.filter(day -> day.getEnabled()!=null && day.getEnabled())
				.map(day -> day.getDay())
				.collect(Collectors.toList());
		
		for (int days=1;days<=7;days++) {
			String dayofweek=DayOfWeek.of(startFrom.plusDays(days).getDayOfWeek().getValue()).toString();
			if(deliveryDays.contains(dayofweek))
				return startFrom.plusDays(days);
		}

		return null;
	}
	
	public LocalDateTime getNextDelivery(String frequency) {
		LocalDateTime startFrom=LocalDateTime.now();
		
		if("weekly".equals(frequency)) {
			startFrom=startFrom.plusDays(7);
		}
		else if("biweekly".equals(frequency)) {
			startFrom=startFrom.plusDays(14);
		}
		else if("monthly".equals(frequency)) {
			startFrom=startFrom.plusMonths(1);
		}
		
		List<String> deliveryDays=getAll().stream()
				.filter(day -> day.getEnabled()!=null && day.getEnabled())
				.map(day -> day.getDay())
				.collect(Collectors.toList());
		
		for (int days=1;days<=7;days++) {
			String dayofweek=DayOfWeek.of(startFrom.plusDays(days).getDayOfWeek().getValue()).toString();
			if(deliveryDays.contains(dayofweek))
				return startFrom.plusDays(days);
		}

		return null;
	}
}
