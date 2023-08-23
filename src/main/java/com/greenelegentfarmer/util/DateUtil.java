package com.greenelegentfarmer.util;

import java.util.List;
import java.util.Arrays;
import java.time.DayOfWeek;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {

	public static List<DayOfWeek> getWeekDays() {
		return Arrays.asList(DayOfWeek.values());
	}

}
