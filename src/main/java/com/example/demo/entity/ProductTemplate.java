package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProductTemplate {

	@Id
	@GeneratedValue
	private int id;
	
	private String employeeId;

	private String productName;
	
	private String price;
	
	private String category;
	
	private String isDelete;
}
