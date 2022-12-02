package com.nguyenphitan.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.nguyenphitan.entity.Account;
import com.nguyenphitan.entity.Product;
import com.nguyenphitan.repository.AccountRepository;
import com.nguyenphitan.repository.BannerRepository;
import com.nguyenphitan.repository.CategoryRepository;
import com.nguyenphitan.repository.OrderAccountRepository;
import com.nguyenphitan.repository.OrderDetailRepository;
import com.nguyenphitan.repository.ProductRepository;
import com.nguyenphitan.service.ProductService;
import com.nguyenphitan.service.admin.AdminService;

@RestController
@RequestMapping("/admin")
//@CrossOrigin("http://localhost:8088/admin/**")
public class AdminController {

    @Autowired
    OrderAccountRepository orderAccountRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AdminService adminAccountService;

    @Autowired
    AdminService sellerService;
    
    @Autowired
    private AdminService adminService;

    @GetMapping("/bill")
    public ModelAndView getBill() {
        ModelAndView modelAndView = new ModelAndView("admin/bill");
        adminService.getAllBills(modelAndView);
        return modelAndView;
    }

    @Transactional
    @GetMapping("/bill/complete/{id}")
    public RedirectView checkComplete(@PathVariable int id) {
        orderAccountRepository.updateStatus(id);
        return new RedirectView("/admin/bill");
    }


    /*
     * Hiển thị ra danh sách tất cả người dùng (danh sách tất cả account)
     * Created by: NPTAN (24/04/2022)
     * Version: 1.0
     */
    @GetMapping("/account")
    public ModelAndView getAccounts() {
        ModelAndView modelAndView = new ModelAndView("admin/account");
        List<Account> accounts = adminAccountService.getAllAccounts();
        modelAndView.addObject("accounts", accounts);
        return modelAndView;
    }

    @Autowired
	AdminService bannerService;

	@Autowired
	BannerRepository bannerRepository;

	@GetMapping("/banner")
	public String getBanner(Model model) {
		model.addAttribute("banners", bannerRepository.findAll());
		return "/admin/banner";
	}

	@GetMapping("/banner/create")
	public String createBanner() {
		return "/admin/formBanner";
	}

	@PostMapping("/banner/save")
	public String handleCreateBanner(
			@RequestParam("image") MultipartFile multipartFile,
			@RequestParam("bannerId") Integer bannerId
	) throws IOException {
		bannerService.uploadBanner(multipartFile, bannerId);
		return "redirect:/admin/banner";
	}

	@GetMapping("/banner/{id}/update")
	public String updateBanner(@PathVariable Integer id, Model model) {
		model.addAttribute("banner", bannerRepository.findById(id).get());
		return "/admin/formBanner";
	}

	@GetMapping("/banner/{id}/delete")
	public String deleteBanner(@PathVariable Integer id, Model model) {
		bannerRepository.delete(bannerRepository.findById(id).get());
		return "redirect:/admin/banner";
	}

	// set used status
	@Transactional
	@GetMapping("/banner/{id}/usedstatus")
	public String changeUsedStatus(@PathVariable int id) {
		bannerRepository.updateUsedStatus(id);
		return "redirect:/admin/banner";
	}

	// set used status
	@Transactional
	@GetMapping("/banner/{id}/nonestatus")
	public String changeNoneStatus(@PathVariable int id) {
		bannerRepository.updateNoneStatus(id);
		return "redirect:/admin/banner";
	}

	// get view usedstatus
	@GetMapping("/banner/view")
	public String getViewBanner(Model model) {
		model.addAttribute("banners", bannerRepository.getViewBanner());
		return "/admin/banner/view";
	}
	
	@Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    AdminService adminProductService;

    @GetMapping("/products")
    ModelAndView getAllProducts() {
        ModelAndView modelAndView = new ModelAndView("admin/product");
        List<Product> products = productRepository.findAll();
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/products/create")
    ModelAndView createProductView() {
        ModelAndView modelAndView = new ModelAndView("admin/formProduct");
        modelAndView.addObject("categories", categoryRepository.findAll());
        modelAndView.addObject("newProduct", new Product());
        return modelAndView;
    }

    @PostMapping("/products/create")
    ModelAndView createProduct(
    		@RequestParam("price") Long price,
    		@RequestParam("title") String title, 
    		@RequestParam("content") String content,
    		@RequestParam("categoryId") Integer categoryId,
    		@RequestParam("thumbnail") MultipartFile multipartFile
    	) throws IOException {
    	adminProductService.create(price, title, content, categoryId, multipartFile);
        return new ModelAndView("redirect:" + "/admin/products");
    }

    @GetMapping("/products/{id}/delete")
    ModelAndView delete(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return new ModelAndView("redirect:" + "/admin/products");
    }

    @GetMapping("/products/{id}/update")
    ModelAndView updateProductView(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/formProduct");
        modelAndView.addObject("categories", categoryRepository.findAll());
        modelAndView.addObject("newProduct", new Product());
        modelAndView.addObject("updateProduct", productRepository.findProductById(id));
        return modelAndView;
    }

    @PostMapping("/products/{id}/update")
    ModelAndView updateProduct(
    		@PathVariable Integer id,
    		@RequestParam("price") Long price,
    		@RequestParam("title") String title, 
    		@RequestParam("content") String content,
    		@RequestParam("categoryId") Integer categoryId,
    		@RequestParam("thumbnail") MultipartFile multipartFile
    ) throws IOException {
    	adminProductService.update(id, price, title, content, categoryId, multipartFile);
        return new ModelAndView("redirect:" + "/admin/products");
    }
	
	/*
	 * Hiển thị ra danh sách nhân viên
	 * Created by: NPTAN (24/04/2022)
	 * Version: 1.0
	 */
	@GetMapping("/seller")
	public ModelAndView getSellers() {
		ModelAndView modelAndView = new ModelAndView("admin/seller");
		List<Account> sellers = sellerService.getAllSellers();
		modelAndView.addObject("sellers", sellers);
		return modelAndView;
	}
	
	/*
	 * Thêm nhân viên (add role SELLER cho tài khoản)
	 * Created by: NPTAN (24/04/2022)
	 * Version: 1.0
	 */
	@PostMapping("/seller")
	public Integer addSeller(@RequestBody int id) {
		Integer response = sellerService.addSeller(id);
		return response;
	}
	
	/*
	 * Xóa nhân viên (chỉ xóa role, không xóa tài khoản)
	 * Created by: NPTAN (24/04/2022)
	 * Version: 1.0 
	 */
	@DeleteMapping("/seller/{id}")
	public void deleteSeller(@PathVariable("id") int id) {
		sellerService.deleteSeller(id);
	}

}
