package com.nguyenphitan.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.nguyenphitan.custom.CustomUserDetails;
import com.nguyenphitan.entity.Account;
import com.nguyenphitan.entity.Category;
import com.nguyenphitan.entity.OrderAccount;
import com.nguyenphitan.entity.OrderDetail;
import com.nguyenphitan.jwt.JwtTokenProvider;
import com.nguyenphitan.payload.LoginRequest;
import com.nguyenphitan.payload.RegisterRequest;
import com.nguyenphitan.payload.UpdateAccountRequest;
import com.nguyenphitan.repository.AccountRepository;
import com.nguyenphitan.repository.CategoryRepository;
import com.nguyenphitan.repository.OrderAccountRepository;
import com.nguyenphitan.repository.OrderDetailRepository;
import com.nguyenphitan.service.AccountService;
import com.nguyenphitan.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	OrderAccountRepository orderAccountRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Override
	public String getRegister(Model model, HttpServletRequest request) {
		model.addAttribute("categoris", categoryRepository.findAll());
		// Ki???m tra token:
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		// 1. N???u ???? ????ng nh???p -> quay v??? trang ch???
		if (token != null) {
			return "redirect:/";
		}
		// 2. N???u ch??a ????ng nh???p -> v??o trang ????ng k??
		return "register";
	}

	@Override
	public void handleRegister(RegisterRequest registerRequest) {
		Date d = new Date();
		Account account = new Account();
		account.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		account.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		account.setFullname(registerRequest.getFullname());
		account.setAddress(registerRequest.getAddress());
		account.setEmail(registerRequest.getEmail());
		account.setPhone(registerRequest.getPhone());
		account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		account.setRole("USER");
		accountRepository.save(account);
	}

	@Override
	public String createAccount(Model model, String fullname, String email, String address, String phone,
			String password) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		boolean accountCheck = accountService.checkAccountExisted(phone);

		if (!accountCheck) {
			model.addAttribute("fullname", fullname);
			model.addAttribute("email", email);
			model.addAttribute("address", address);
			model.addAttribute("phone", phone);
			model.addAttribute("phoneExisted", "S??? ??i???n tho???i ???? ???????c ????ng k?? vui l??ng th??? v???i s??? kh??c");

			return "register";
		}

		// X??? l?? service li??n quan ?????n ????ng k??.
		RegisterRequest registerRequest = new RegisterRequest(phone, password, fullname, email, address, password);
		handleRegister(registerRequest);

		return "redirect:/auth/login";
	}

	@Override
	public String getLogin(Model model, HttpServletRequest request) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		// Ki???m tra token:
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		// 1. N???u ???? ????ng nh???p -> quay v??? trang ch???
		if (token != null) {
			return "redirect:/";
		}
		// 2. N???u ch??a ????ng nh???p -> v??o trang ????ng nh???p
		return "login";
	}

	@Override
	public String checkLogin(Model model, String phone, String password, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();

		// get all category.
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		// X??? l?? service li??n quan ?????n ????ng nh???p.
		String token = handleLogin(phone, password, request);

		// N???u ????ng nh???p th??nh c??ng -> l???y ra Account t??? jwt:
		if (token != null) {
			Integer accountId = tokenProvider.getAccountIdFromJWT(token);
			Account account = accountRepository.getById(accountId);
			session.setAttribute("account", account);
			session.setAttribute("fullname", account.getFullname());
			session.setMaxInactiveInterval(60 * 60 * 24);
			return "redirect:/";
		} else {
			model.addAttribute("phone", phone);
			model.addAttribute("error", "Vui l??ng ki???m tra l???i s??? ??i???n tho???i ho???c m???t kh???u");
		}

		return "login";
	}

	@Override
	public String handleLogin(String phone, String password, HttpServletRequest request) {
		String jwt = null;
		try {
			// T???o ra LoginRequest t??? username v?? password nh???n ???????c t??? client
			LoginRequest loginRequest = new LoginRequest(phone, password);
			// X??c th???c th??ng tin ng?????i d??ng Request l??n:
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword()));

			// N???u kh??ng x???y ra exception t???c l?? th??ng tin h???p l???
			// Set th??ng tin authentication v??o Security Context
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Tr??? v??? jwt cho ng?????i d??ng
			jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());

			// L??u jwt v??o session:
			HttpSession session = request.getSession();
			session.setAttribute("token", jwt);

			// L???y ra id ng?????i d??ng -> l???y ra role -> ????a l??n session:
			Integer accountId = tokenProvider.getAccountIdFromJWT(jwt);
			Account account = accountRepository.getById(accountId);
			String roleAccount = account.getRole();
			session.setAttribute("roleAccount", roleAccount);

		} catch (Exception e) {
			System.out.println(e);
		}

		return jwt;
	}

	@Override
	public void handleLogout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		// X??a token kh???i session:
		session.removeAttribute("token");
		// X??a account kh???i session:
		session.removeAttribute("account");
		// X??a fullname kh???i session:
		session.removeAttribute("fullname");
		// X??a role:
		session.removeAttribute("roleAccount");
		// X??a t???t c??? cookies:
		for (Cookie cookie : request.getCookies()) {
			cookie.setValue("");
			cookie.setMaxAge(0);
			cookie.setPath("/");

			response.addCookie(cookie);
		}
	}

	@Override
	public void handleUpdateAccount(UpdateAccountRequest updateAccountRequest) {
		Date d = new Date();
		Account account = new Account();
		account.setId(updateAccountRequest.getId());
		account.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		account.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		account.setFullname(updateAccountRequest.getFullname());
		account.setAddress(updateAccountRequest.getAddress());
		account.setEmail(updateAccountRequest.getEmail());
		account.setPhone(updateAccountRequest.getPhone());
		account.setPassword(passwordEncoder.encode(updateAccountRequest.getPassword()));
		account.setRole("USER");
		accountRepository.save(account);
	}

	@Override
	public ModelAndView purchaseHistory(Model model, int id) {
		ModelAndView modelAndView = new ModelAndView("history");

		// get all category.
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		DecimalFormat df = new DecimalFormat(",###");

		List<OrderAccount> orderAccounts = orderAccountRepository.findOrderAccountByAccountId(id);
		List<OrderDetail> orderDetails = orderDetailRepository.findAll();
		List<String> totalPrice = new ArrayList<>();

		for (OrderAccount oc : orderAccounts) {
			long total = 0;
			for (OrderDetail od : orderDetails) {
				if (oc.getId() == od.getOrderAccountId()) {
					total += od.getProductPrice() * od.getOrderQuantity();
				}
			}
			totalPrice.add(df.format(total));
		}

		modelAndView.addObject("orderAccounts", orderAccounts);
		modelAndView.addObject("orderDetails", orderDetails);
		modelAndView.addObject("totalPrice", totalPrice);

		return modelAndView;
	}

}
