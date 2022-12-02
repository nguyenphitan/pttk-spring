package com.nguyenphitan.service.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nguyenphitan.entity.Account;

public interface AdminService {
	// Load all bill
	void getAllBills(ModelAndView modelAndView);
	
	void uploadBanner(MultipartFile multipartFile, Integer bannerId) throws IOException;

	List<Account> getAllAccounts();
	
	// Lấy tất cả các account có role là SELLER
	List<Account> getAllSellers();
	
	// Thêm Seller:
	int addSeller(int id);
	
	// Xóa Seller:
	void deleteSeller(int id);
	
	// create
	void create(Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException;
	
	// update
	void update(Integer id, Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException;
	
}
