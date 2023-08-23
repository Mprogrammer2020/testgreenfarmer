package com.greenelegentfarmer.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.greenelegentfarmer.entity.Activity;
import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.service.ActivityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

//@Aspect
//@Component
public class CrudAspect {

	@Autowired
	private ActivityService activityService;

	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public void logGetRequests() { }
	
	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void logPostRequests() { }

	
	@Before("logGetRequests() || logPostRequests()")
	public void testingLoader(JoinPoint joinPoint) throws InterruptedException {
		Thread.sleep(1000);
	}
	
	@After("logGetRequests() || logPostRequests()")
	public void logAllMethodCallsAdvice(JoinPoint joinPoint) {
		//saveActivity(fromJoinPoint(joinPoint));
	}
	
	
	public void saveActivity(Activity activity) {
		//Working better than @Async
		new Thread() { @Override public void run() {
				activityService.add(activity);
			}
		}.start();	
	}

	
	private Activity fromJoinPoint(JoinPoint joinPoint) {

		Activity activity = new Activity();
		activity.setAction(joinPoint.getStaticPart().toShortString());
		activity.setDescription(joinPoint.getTarget().toString());

		StringBuilder stringBuilder = new StringBuilder(250);
		for (Object arg : joinPoint.getArgs()) {
			
			if(arg instanceof BindingResult) {
				stringBuilder.append("Binding Result Error..").append(" | ");
			}
			else {
				stringBuilder.append(arg).append(" | ");	
			}
		}
		activity.setArgs(stringBuilder.toString());
		activity.setUser(getUser());

		return activity;
	}
	
	private User getUser() {
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
			return null;
		else
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
