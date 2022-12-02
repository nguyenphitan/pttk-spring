package com.nguyenphitan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenphitan.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
