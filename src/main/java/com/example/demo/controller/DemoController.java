package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductTemplate;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.ProductTemplateRepository;

@Controller
public class DemoController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductTemplateRepository productTemplateRepository;

//	@Autowired
//	private ExcelFileUploadService excelFileUploadService;
	
	
	
	@GetMapping(value = "demo")
	public String getDemoPage() {
		
		return "demo";
	}
	

	@PostMapping(value = "/upload")
	@ResponseBody
	public List<ProductTemplate> ExceluploadController(@RequestPart("excelFile") MultipartFile files) throws IOException {

		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);
			
			// delete from the template table
			productTemplateRepository.deleteAll();
			
			
			List<ProductTemplate> productTemplates = new ArrayList<>(); 

			
			for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
				if (index > 0) {
					ProductTemplate producttemplate = new ProductTemplate();

					XSSFRow row = worksheet.getRow(index);
					
					if(row != null && row.getCell(0) != null)
					{
						Integer id = (int) row.getCell(0).getNumericCellValue();

						producttemplate.setEmployeeId(id.toString());
						producttemplate.setProductName(row.getCell(1) != null ? getCellValueAsString(row.getCell(1)) : "");
						producttemplate.setPrice(row.getCell(2) != null ? getCellValueAsString(row.getCell(2)) : "0");
						producttemplate.setCategory(row.getCell(3) != null ? getCellValueAsString(row.getCell(3)) : "");
						producttemplate.setIsDelete(row.getCell(4) != null ? getCellValueAsString(row.getCell(4)) : "");

						productTemplates.add(producttemplate);
					}
					
				}
			
			}

			productTemplateRepository.saveAll(productTemplates);
			
			workbook.close();
			
			return productTemplates;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();

	}
	
	
	
	
	@PostMapping(value = "/saveData")
	@ResponseBody
	public String saveData(@RequestBody List<String> myArray) {

		try {

			convertTemplateToEntity(myArray);
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ERROR";

	}
	
	
	
	private String getCellValueAsString(Cell cell) {
	    String cellValue = "";

	    if (cell != null) {
	        switch (cell.getCellType()) {
	            case STRING:
	                cellValue = cell.getStringCellValue();
	                break;
	            case NUMERIC:
	                cellValue = String.valueOf(cell.getNumericCellValue());
	                break;
	            case BOOLEAN:
	                cellValue = String.valueOf(cell.getBooleanCellValue());
	                break;
	            case FORMULA:
	                cellValue = cell.getCellFormula();
	                break;
	            default:
	                // Handle other cell types as needed
	                cellValue = "";
	        }
	    }

	    return cellValue;
	}

	
	
	private void convertTemplateToEntity(List<String> employeeId) {

		List<Product> products = new ArrayList<>();

		employeeId.forEach(id -> {

			ProductTemplate producttemplate = productTemplateRepository.findByEmployeeId(id);

			if (producttemplate != null) {

				Product product = new Product();

				product.setEmployeeId(
						producttemplate.getEmployeeId() != null ? Long.parseLong(producttemplate.getEmployeeId()) : 0);
				product.setProductName(
						producttemplate.getProductName() != null ? producttemplate.getProductName() : "");
				product.setPrice(
						producttemplate.getPrice() != null ? Double.parseDouble(producttemplate.getPrice()) : 0.0);
				product.setCategory(producttemplate.getCategory() != null ? producttemplate.getCategory() : "");

				if (producttemplate.getIsDelete() != null) {
					if (producttemplate.getIsDelete().equalsIgnoreCase("true")
							|| producttemplate.getIsDelete().equalsIgnoreCase("false")) {
						producttemplate.setIsDelete(producttemplate.getIsDelete().toLowerCase());
						product.setDelete(Boolean.parseBoolean(producttemplate.getIsDelete()));
					}
				}
				products.add(product);
			}
		});

		productRepository.saveAll(products);

	}

}
