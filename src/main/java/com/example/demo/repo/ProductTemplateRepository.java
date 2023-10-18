package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProductTemplate;

public interface ProductTemplateRepository extends JpaRepository<ProductTemplate, Integer> {

	ProductTemplate findByEmployeeId(String empId);
	
}
