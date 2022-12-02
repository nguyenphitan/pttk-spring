package com.nguyenphitan.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nguyenphitan.entity.Category;
import com.nguyenphitan.repository.CategoryRepository;
import com.nguyenphitan.service.admin.CategoryService;

@Component
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
