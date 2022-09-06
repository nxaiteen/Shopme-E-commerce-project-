package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
//	@Query("SELECT u FROM Category u WHERE CONCAT(u.id, ' ', u.name, ' ', u.alias) LIKE %?1%")
//	public List<Category> findAll(String keyword);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories();
	
	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	public Long countById(Integer id);
}
