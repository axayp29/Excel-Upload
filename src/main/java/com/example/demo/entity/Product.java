package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@GeneratedValue
	private int id;
	
	private long employeeId;

	private String productName;
	
	private Double price;
	
	private String category;
	
	private boolean isDelete;
	
	
	
	
}



