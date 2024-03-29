package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useRepresentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.User;

import java.util.List;

import com.shopme.common.entity.Role;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1); // Роль админа в таблице БД имеет индекс 1
		User userNick = new User("nxaiteennn1221219@mail.ru", "12345", "Nikita", "Romanov");
		userNick.addRole(roleAdmin); // Заполняем таблицу users_roles (индекс userNick и его роль (1 - админ))
		
		User savedUser = repo.save(userNick); // Запись в таблицу users
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	//Запись всех пользователей в таблицы БД
	@Test
	public void testListAllUsers( ) {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	//Получение пользователя по ID
	@Test
	public void testGetUserById( ) {
		User userNick = repo.findById(1).get();
		System.out.println(userNick);
		assertThat(userNick).isNotNull();
	}
	
	//Обновление информации о пользователе в БД
	@Test
	public void testUpdateUserDetails( ) {
		User userNick = repo.findById(1).get();
		userNick.setEnabled(true);
		userNick.setEmail("nikita@codejava.net");
		
		repo.save(userNick);
	}
	
	//Изменение роли пользователя в БД

	
	//Удаление пользователя по ID
	
	@Test
	public void testGetUserByEmail() {
		String email = "testemail@test.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById( ) {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser( ) {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser( ) {
		Integer id = 6;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage( ) {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
}
