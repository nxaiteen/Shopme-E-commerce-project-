package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useRepresentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.User;
import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1); // Роль админа в таблице БД имеет индекс 1
		User userNick = new User("nxaiteen19@mail.ru", "12345", "Nikita", "Romanov");
		userNick.addRole(roleAdmin); // Заполняем таблицу users_roles (индекс userNick и его роль (1 - админ))
		
		User savedUser = repo.save(userNick); // Запись в таблицу users
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userIvan = new User("ivan@gmail.com", "12bhbj", "Ivan", "Rozanov");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		userIvan.addRole(roleEditor);
		userIvan.addRole(roleAssistant);
		
		User savedUser = repo.save(userIvan); // Запись в таблицу users
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
		userNick.setEmail("testemail@test.com");
		
		repo.save(userNick);
	}
	
	//Изменение роли пользователя в БД
	@Test
	public void testUpdateUserRoles( ) {
		User userIvan = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userIvan.getRoles().remove(roleEditor);
		userIvan.addRole(roleSalesperson);
		
		repo.save(userIvan);
	}
	
	//Удаление пользователя по ID
	@Test
	public void testDeleteUser( ) {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
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
}
