package com.nguyenphitan.service.admin.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nguyenphitan.entity.Account;
import com.nguyenphitan.entity.Banner;
import com.nguyenphitan.entity.Category;
import com.nguyenphitan.entity.Discount;
import com.nguyenphitan.entity.OrderAccount;
import com.nguyenphitan.entity.OrderDetail;
import com.nguyenphitan.entity.Product;
import com.nguyenphitan.repository.AccountRepository;
import com.nguyenphitan.repository.BannerRepository;
import com.nguyenphitan.repository.OrderAccountRepository;
import com.nguyenphitan.repository.OrderDetailRepository;
import com.nguyenphitan.repository.ProductRepository;
import com.nguyenphitan.service.admin.AdminService;
import com.nguyenphitan.service.admin.DiscountService;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private DiscountService discountService;

	@Autowired
	private OrderAccountRepository orderAccountRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public List<Account> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		return accounts;
	}
	
private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	
	@Autowired
	ProductRepository productRepository;

	@Override
	public void create(Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException {
		Path staticPath = Paths.get("src/main/resources/static");
        Path imagePath = Paths.get("img");
        // Kiểm tra tồn tại hoặc tạo thư mục /static/images
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(multipartFile.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
    	
    	Category category = new Category();
    	category.setId(categoryId);
    	Product product = new Product();
    	product.setCategory(category);
    	product.setContent(content);
    	product.setPrice(price);
    	product.setTitle(title);
    	product.setThumbnail(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        productRepository.save(product);
	}

	@Override
	public void update(Integer id, Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException {
		Path staticPath = Paths.get("src/main/resources/static");
        Path imagePath = Paths.get("img");
        // Kiểm tra tồn tại hoặc tạo thư mục /static/images
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(multipartFile.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
    	
        Category category = new Category();
    	category.setId(categoryId);
        Product toUpdateProduct = productRepository.findProductById(id);
        toUpdateProduct.setTitle(title);
        toUpdateProduct.setThumbnail(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        toUpdateProduct.setContent(content);
        toUpdateProduct.setPrice(price);
        toUpdateProduct.setCategory(category);
        productRepository.save(toUpdateProduct);
	}

	@Override
	public List<Account> getAllSellers() {
		List<Account> accounts = accountRepository.findByRole("SELLER");
		return accounts;
	}

	@Override
	public int addSeller(int id) {
		Integer result = accountRepository.addSeller("SELLER", id);
		return result;
	}

	@Override
	public void deleteSeller(int id) {
		Integer result = accountRepository.addSeller("USER", id);
	}
	
	@Autowired
	private BannerRepository bannerRepository;

	@Override
	public void uploadBanner(MultipartFile multipartFile, Integer bannerId) throws IOException {
		Path staticPath = Paths.get("src/main/resources/static");
        Path imagePath = Paths.get("img");
        
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(multipartFile.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
        
        Banner banner = null;
        if( bannerId != -1 ) {
        	banner = bannerRepository.getById(bannerId);
        	banner.setImage(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        } else {
        	banner = new Banner();
        	banner.setImage(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        	banner.setUsedStatus("NONE");
        }
        
		
		bannerRepository.save(banner);
	}
	
	@Override
	public void getAllBills(ModelAndView modelAndView) {
		List<Discount> discounts = discountService.getAlls();

        List<OrderAccount> orderAccounts = orderAccountRepository.findAll();
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<Double> totalPrice = new ArrayList<>();

        for (OrderAccount oc : orderAccounts) {
            Double total = 0D;
            for (OrderDetail od : orderDetails) {
                if (oc.getId() == od.getOrderAccountId()) {
                    total += (od.getProductPrice() * od.getOrderQuantity());
                }
            }
            for(int i = 0 ; i < discounts.size() ; i++) {
				Double nextValue = discounts.get(i).getValue();
				if( total < nextValue ) {
					if( i > 0 ) {
						total -= (total * discounts.get(i-1).getDiscount()/100);
						break;						
					}
					else {
						break;
					}
				}
			}
            totalPrice.add(total);
        }

        modelAndView.addObject("orderAccounts", orderAccounts);
        modelAndView.addObject("orderDetails", orderDetails);
        modelAndView.addObject("totalPrice", totalPrice);
	}

}
