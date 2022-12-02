package com.nguyenphitan.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Data
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private Date createdAt;
	private Date updatedAt;
	
	@OneToMany(mappedBy = "category")
	private List<Product> products = new ArrayList<>();

	public Category(int id) {
		this.id = id;
	}

	public Category() {
	}
}
