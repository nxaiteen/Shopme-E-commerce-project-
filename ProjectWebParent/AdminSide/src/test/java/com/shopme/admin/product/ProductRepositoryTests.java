package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repo.findAll();
		
		iterableProducts.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 2;
		Product product = repo.findById(id).get();
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = repo.findById(id).get();
		product.setPrice(499);
		
		repo.save(product);
		
		Product updatedProduct = entityManager.find(Product.class, id);
		
		assertThat(updatedProduct.getPrice()).isEqualTo(499);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		repo.deleteById(id);
		
		Optional<Product> result = repo.findById(id);
		
		assertThat(!result.isPresent());
	}
}
