package com.greenelegentfarmer.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import com.greenelegentfarmer.entity.User;
import com.greenelegentfarmer.model.SubAdminModel;

import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.greenelegentfarmer.util.CrudService;
import com.greenelegentfarmer.util.MailUtil;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.greenelegentfarmer.param.UserSearchParam;
import com.greenelegentfarmer.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import com.greenelegentfarmer.specification.UserSpecification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Service
public class UserService extends CrudService<User> implements UserDetailsService,UserSpecification {

	private static final String USEROLE="ROLE_USER";
	
	@Lazy
	@Autowired
	private MailUtil emailUtil;
	
	private UserRepository userRepository;
	public UserService(UserRepository repository) {
		super(repository);
		this.userRepository=repository;
	}
	
	public User whoAmI() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional=getByUsername(username);
		
		if (userOptional.isPresent()) 
			return userOptional.get();
		 else 
			throw new UsernameNotFoundException("User not found with email/phone: " + username);		
	}
	
	public Optional<User> getByUsername(String username) {
		return userRepository.findByEmailOrPhone(username);
	}
	
	public List<User> getWaitList() {
		return userRepository.findByStatus("PENDING_ADMIN_APPROVAL");
	}
	
	public List<User> getAvailableUsers() {
		return userRepository.findByUnLockedTrueAndRole(USEROLE);
	}
	
	public User register(User user) {
		user.setRole(USEROLE);
		user.setStatus("PASSWORD_SET");
		user.setEnabled(true);
		user.setUnLocked(true);
		user.setCreatedDate(new Date());
		return add(user);
	}
	
	public long countByRole() {
		return userRepository.countByRole(USEROLE);
	}
	
	public Page<User> search(UserSearchParam param) {
		
		Sort sort=Sort.by("id").descending();
		
		Pageable pageable = PageRequest.of(param.getPage(), param.getSize(), sort);
		
		Specification<User> specification=filterSpecification(param);
				
		return userRepository.findAll(specification,pageable);
	}

	public User addSubAdmin(SubAdminModel subAdmin) throws MessagingException {
		User user = new User();
		user.setEmail(subAdmin.getEmail());
		user.setEnabled(true);
		user.setPhone(subAdmin.getPhone());
		user.setRole(subAdmin.getRole());
		user.setFirstName(subAdmin.getName());
		user.setCreatedDate(new Date());
		user.setUnLocked(true);
		add(user);
		emailUtil.sendLoginCredentials(user);
		return user;
	}
}
