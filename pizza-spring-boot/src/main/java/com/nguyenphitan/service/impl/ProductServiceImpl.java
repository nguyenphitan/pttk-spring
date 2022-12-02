package com.nguyenphitan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nguyenphitan.entity.Product;
import com.nguyenphitan.repository.ProductRepository;
import com.nguyenphitan.service.ProductService;

@Component
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	public List<Product> getAllProductByCategoryId(int id){
		return productRepository.findAllByCategoryId(id);
	}
	
	public List<Product> getAllProductBySearchName(String keyword){
		return productRepository.searchByName(keyword);
	}
}
