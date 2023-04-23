package com.shopme.admin.category;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

public class CategoryCsvExporter {
	
	public void setResponseHeader(HttpServletResponse response,
			String contentType, String extension) throws IOException {
		
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String timestamp = dateFormatter.format(new Date());
		String fileName = "categories_" + timestamp + extension;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
	
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {

		setResponseHeader(response, "text/csv", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Category ID", "Category Image", "Category Name", "Alias", "Enabled"};
		String[] fieldMapping = {"id", "image", "name", "alias", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (Category category : listCategories) {
			csvWriter.write(category, fieldMapping);
		}
		
		csvWriter.close();
	}
}
