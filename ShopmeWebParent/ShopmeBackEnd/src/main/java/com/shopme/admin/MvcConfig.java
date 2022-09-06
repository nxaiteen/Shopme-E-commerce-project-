package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	//Для отображения фотографий пользователей
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		String dirName = "user-photos";
		Path userPhotosDir = Paths.get(dirName);
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + dirName + "/**")
				.addResourceLocations("file:/" + userPhotosPath + "/");
		
		
		String categoryImageDirName = "../category-images";
		Path categoryImageDir = Paths.get(categoryImageDirName);
		String categoryImagesPath = categoryImageDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/category-images/**")
				.addResourceLocations("file:/" + categoryImagesPath + "/");
	}
	
	
}
