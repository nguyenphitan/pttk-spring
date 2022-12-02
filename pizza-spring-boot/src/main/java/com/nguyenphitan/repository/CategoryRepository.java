package com.nguyenphitan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenphitan.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
