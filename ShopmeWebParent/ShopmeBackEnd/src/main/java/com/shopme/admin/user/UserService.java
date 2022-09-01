package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 4;
	
	@Autowired //Для реализации UserRepository в режиме runtime (позволить Spring внедрить экземпляр класса во время выполнения)
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//Функция для получения пользователя по e-mail
	public User getByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
	
	//Функция, возвращающая список всех пользователей 
	public List<User> listAll() {
		return (List<User>) userRepo.findAll(Sort.by("id").ascending());
	}
	
	//Функция для обеспечения пагинации страниц и сортировки пользователей
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
		
		if(keyword != null) {
			return userRepo.findAll(keyword, pageable);
		}
		
		return userRepo.findAll(pageable);
	}
	
	//Функция, возвращающая список ролей пользователей 
	public List<Role> listRoles(){
		return (List<Role>) roleRepo.findAll();
	}
	
	//Функция для сохранения пользователя 
	public User save(User user) {
		//Проверка на то, обновляется ли информация об уже существующем пользователе
		boolean isUpdatingUser = (user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			//Если в поле для пароля ничего не написано, значит поользователь не хочет менять пароль и он остаётся без изменений
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		
		return userRepo.save(user);
	}
	
	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findById(userInForm.getId()).get();
		
		if (!userInDB.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		
		if (userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		
		userInDB.setFirstname(userInForm.getFirstname());
		userInDB.setLastname(userInForm.getLastname());
		
		return userRepo.save(userInDB);
	}
	
	//Функция для кодирования пароля пользователя
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	//Функция, которая возвращает true, если адрес уникальный
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if (userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if (isCreatingNew) {
			if (userByEmail != null) return false;
		} else if (userByEmail.getId() != id) {
			return false;
		}
		
		return true;
	}

	//Функция для получения пользователя по ID
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
	}
	
	//Функция для удаления пользователя по ID
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id); 
		}
		
		userRepo.deleteById(id);
	}
	
	//Функция для обновления статуса пользователя
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}
}
