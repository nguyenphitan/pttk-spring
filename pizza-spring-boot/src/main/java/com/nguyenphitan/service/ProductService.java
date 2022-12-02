package com.nguyenphitan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nguyenphitan.entity.Product;

@Service
public interface ProductService {

	List<Product> getAllProductByCategoryId(int id);
	
	List<Product> getAllProductBySearchName(String keyword);
}
