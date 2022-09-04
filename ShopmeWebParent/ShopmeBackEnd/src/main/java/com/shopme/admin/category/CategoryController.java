package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;


@Controller
public class CategoryController {

	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories") 
	public String listFirstPage(Model model, @Param("keyword") String keyword) {
	
		Category category = new Category();
		List<Category> listCategories = service.listAll(keyword);
		
		model.addAttribute("category", category);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("keyword", keyword);
		
		return "categories/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The category ID " + id + " has been deleted successfully");
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateUserEnabledStatus( Model model,
			@PathVariable("status") boolean enabled, 
			@PathVariable("id") Integer id, 
			@Param("keyword") String keyword,
			RedirectAttributes redirectAttributes) {
		
		service.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		model.addAttribute("keyword", keyword);
		
		if(!keyword.equals("null"))
			return "redirect:/categories" + "&keyword=" + keyword;
		else
			return "redirect:/categories";
	}
	

}
