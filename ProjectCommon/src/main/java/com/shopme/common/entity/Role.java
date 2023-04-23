package com.shopme.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация
	private Integer id;

	@Column(length = 40, nullable = false, unique = true)
	private String name;

	@Column(length = 150, nullable = false)
	private String description;
	
	
	public Role() {

	}
	
	public Role(Integer id) {
		this.id = id;
	}



	public Role(String name) {
		this.name = name;
	}

	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
}
