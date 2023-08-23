package com.greenelegentfarmer.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"password","status","accountNonLocked","accountNonExpired","credentialsNonExpired","authorities"})
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@CreationTimestamp
	@Column(updatable = false)
	private Date createdDate;
	private String firstName;
	private String lastName;
	private String imagePath;
	private String role;
	@Column(unique = true)
	private String email;
	private String countryCode;
	@NotBlank
	private String phone;
	private String password;
	
	@ColumnDefault("false")
	private Boolean enabled;
	@ColumnDefault("false")
	private Boolean unLocked;
	
	private String status;
	
	private String customerId;//for 3rd party payment integration
	private String primaryCardId;//for 3rd party payment integration
	
	@JsonManagedReference
	@Fetch(value = FetchMode.JOIN)
	@Where(clause = "primaryA=true")
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER, targetEntity = UserAddress.class,cascade = CascadeType.ALL)
	private List<UserAddress> addresses;
	
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public boolean isAccountNonLocked() {
		return unLocked;
	}
	public void setUnLocked(Boolean unLocked) {
		this.unLocked = unLocked;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPrimaryCardId() {
		return primaryCardId;
	}
	public void setPrimaryCardId(String primaryCardId) {
		this.primaryCardId = primaryCardId;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority(this.role));
	}
	@Override
	public String getUsername() {
		return email!=null ? email : phone;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public List<UserAddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<UserAddress> addresses) {
		this.addresses = addresses;
	}
}
