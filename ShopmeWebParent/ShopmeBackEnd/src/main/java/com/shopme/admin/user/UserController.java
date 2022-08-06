package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/users") //Используем @GetMapping, потому что запросы будут осущ. через HTTP Get method
	public String listAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);
		
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		user.setEnabled(true); //По умолчанию будет стоять галочка напротив Enabled
		
		model.addAttribute("user", user); //Помещаем пользователя в модель, что Thymeleaf  и Spring MVC связали его с формой регистрации
		model.addAttribute("listRoles",listRoles); // Помещаем в модель список возможных ролей
		model.addAttribute("pageTitle", "Create New User"); //Помещаем в модель название страницы (отобразится после нажатия на Create New User)
		
		return "user_form";
	}
	
	//После сохранения нового пользователя происходит переадресация на главную страницу
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) { //Используем RedirectAttributes, потому что на странице адресации будут изменения
		service.save(user);
		System.out.println(user);
		
		redirectAttributes.addFlashAttribute("message", "The user have been saved successfully.");
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit Use (ID: " + id + ")");
			model.addAttribute("listRoles",listRoles);
			
			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			List<Role> listRoles = service.listRoles();
			redirectAttributes.addFlashAttribute("message",
					"The user ID " + id + " has been deleted successfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/users";
	}
}
