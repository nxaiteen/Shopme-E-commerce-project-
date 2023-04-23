package com.shopme.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	@Column(length = 64, nullable = false)
	private String password;
	
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	
	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;
	
	@Column(length = 64)
	private String photos;
	
	private boolean enabled;
	
	// Множеству пользователей может соответствовать множество ролей и множеству ролей соответствует  множество пользователей
	@ManyToMany(fetch = FetchType.EAGER) //FetchType.EAGER - роли будут загружаться вместе с пользователем
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles = new HashSet<>();

	
	public User() {
	}
	
	public User(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstname=" + firstName
				+ ", lastname=" + lastName + ", roles=" + roles + "]";
	}
	
	//Означает, что геттер не является каким-либо полем в базе данных
	@Transient
	public String getPhotosImagePath( ) {
		if(id == null || photos == null)
			return "/images/default-user.png";
		
		return "/user-photos/" + this.id + "/" + this.photos;
	}
	
	@Transient
	public String getFullName( ) {
		return firstName + " " + lastName;
	}
}
