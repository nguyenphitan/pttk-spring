package com.nguyenphitan.service.admin.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenphitan.entity.Discount;
import com.nguyenphitan.entity.OrderAccount;
import com.nguyenphitan.entity.OrderDetail;
import com.nguyenphitan.payload.StatisticalResponse;
import com.nguyenphitan.payload.TotalEachOrderAccount;
import com.nguyenphitan.repository.OrderAccountRepository;
import com.nguyenphitan.repository.OrderDetailRepository;
import com.nguyenphitan.service.admin.DiscountService;
import com.nguyenphitan.service.admin.StatisticalService;

@Service
public class StatisticalServiceImpl implements StatisticalService {
	
	@Autowired
	private DiscountService discountService;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private OrderAccountRepository orderAccountRepository;

	@Override
	public List<StatisticalResponse> getMonthStatistical(String month, HttpServletRequest request) {
		List<Discount> discounts = discountService.getAlls();

		// Mỗi order_account là một hóa đơn.

		// Lấy ra danh sách order account:
		List<OrderAccount> orderAccounts = orderAccountRepository.findAll();

		// Lấy ra danh sách order_detail:
		List<OrderDetail> listOrderDetails = orderDetailRepository.findAll();

		// List tổng tiền của các order_account: (list tổng tiền của các hóa đơn)
		List<TotalEachOrderAccount> listTotals = new ArrayList<TotalEachOrderAccount>();

		// Tính tổng tiền theo từng order_account_id: (Mỗi lần order sẽ sinh ra một
		// order_account_id khác nhau):
		for (OrderDetail orderDetail : listOrderDetails) {
			Integer orderAccountId = orderDetail.getOrderAccountId();
			Long totalPrice = orderDetail.getOrderQuantity() * orderDetail.getProductPrice();
			Boolean check = false;

			// Tính tổng tiền của mỗi hóa đơn (mỗi order_account là một hóa đơn)
			for (TotalEachOrderAccount totalObj : listTotals) {
				// Nếu đã tồn tại order account id -> update total
				if (totalObj.getId() == orderAccountId) {
					totalObj.setTotal(totalObj.getTotal() + totalPrice);
					check = true;
				}
			}

			// Nếu chưa tồn tại order account id -> thì thêm vào danh sách
			if (!check) {
				TotalEachOrderAccount totalObj = new TotalEachOrderAccount(orderAccountId, totalPrice, null);
				listTotals.add(totalObj);
			}
		}

		// Map ngày cho từng tổng tiền của mỗi hóa đơn:
		for (OrderAccount orderAccount : orderAccounts) {
			for (TotalEachOrderAccount totalObj : listTotals) {
				// Nếu status = 1 và id = order_account_id -> map ngày
				if (orderAccount.getId() == totalObj.getId() && orderAccount.getOrderStatus() == 1) {
					totalObj.setDate(orderAccount.getOrderDate());
					break;
				}
			}
		}

		// Tính toán tổng giá trị sau khi trừ discount:
		for (TotalEachOrderAccount totalObj : listTotals) {
			Long totalPrice = totalObj.getTotal();
			for (int i = 0; i < discounts.size(); i++) {
				Double nextValue = discounts.get(i).getValue();
				if (totalPrice < nextValue) {
					if (i > 0) {
						totalPrice -= Math.round((totalPrice * discounts.get(i - 1).getDiscount() / 100));
						totalObj.setTotal(totalPrice);
						break;
					} else {
						break;
					}
				}
			}
		}

		// Đến đây sẽ thu được: (order_account_id, totalPrice, date) - listTotals
		// -> tính tổng doanh thu theo ngày
		List<StatisticalResponse> listStatisticalResponses = new ArrayList<StatisticalResponse>();
		for (TotalEachOrderAccount totalObj : listTotals) {
			Boolean check = false;
			if (totalObj.getDate() == null) {
				// Bỏ qua những đơn hàng chưa hoàn thành (status = 0 -> sẽ không có date)
				continue;
			}
			for (StatisticalResponse statisticalResponse : listStatisticalResponses) {

				// Lấy ra ngày, tháng, năm của totalObj:
				Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
				cal1.setTime(totalObj.getDate());
				int totalObjYear = cal1.get(Calendar.YEAR);
				int totalObjMonth = cal1.get(Calendar.MONTH);
				int totalObjDay = cal1.get(Calendar.DAY_OF_MONTH);

				// Lấy ra ngày, tháng, năm của statisticalResponse:
				Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
				cal2.setTime(statisticalResponse.getDate());
				int statisticalResponseYear = cal2.get(Calendar.YEAR);
				int statisticalResponseMonth = cal2.get(Calendar.MONTH);
				int statisticalResponseDay = cal2.get(Calendar.DAY_OF_MONTH);

				// Nếu đã có ngày đó -> update:
				if (totalObjYear == statisticalResponseYear && totalObjMonth == statisticalResponseMonth
						&& totalObjDay == statisticalResponseDay) {
					check = true;
					// Update:
					statisticalResponse.setTurnover(statisticalResponse.getTurnover() + totalObj.getTotal());
					break;
				}
			}

			// Nếu không có ngày đó -> thêm mới:
			if (!check) {
				StatisticalResponse statisticalResponse = new StatisticalResponse(totalObj.getDate(),
						totalObj.getTotal());
				listStatisticalResponses.add(statisticalResponse);
			}
		}

		// Đến đây mình sẽ được một list chứa tổng doanh thu của các ngày: (ngày, tổng
		// doanh thu)
		List<StatisticalResponse> listClone = new ArrayList<>(listStatisticalResponses);
		Long totalTurnover = 0L;
		for (int i = 0; i < listClone.size(); i++) {
			StatisticalResponse statistical = listClone.get(i);
			if (statistical.getDate() != null) {
				String pattern = "dd/MM/yyyy HH:mm:ss";

				DateFormat df = new SimpleDateFormat(pattern);
				String date = df.format(statistical.getDate());
				String statisticalMonth = date.substring(3, 5);
				if (!statisticalMonth.equals(month)) {
					listStatisticalResponses.remove(statistical);
				} else { // Tính tổng doanh thu:
					totalTurnover += statistical.getTurnover();
				}
			}

		}

		// Thêm những ngày không có doanh thu vào danh sách:
//		switch(month) {
//			case "01":
//			case "03":
//			case "05":
//			case "07":
//			case "08":
//			case "10":
//			case "12":
//				for(int i = 1 ; i <= 31 ; i++) {	
//					// Kiểm tra xem trong list thống kê tháng đó còn thiếu ngày nào (ngày không có doanh thu) thì thêm vào ngày đó với doanh thu là 0.
//					Boolean check = false;
//					int year = 0;
//					for(StatisticalResponse statistical : listStatisticalResponses) {
//						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//						cal.setTime(statistical.getDate());
//						int day = cal.get(Calendar.DAY_OF_MONTH);
//						year = cal.get(Calendar.YEAR);
//						if(day == i) {
//							check = true;
//						}
//					}
//					
//					if( !check ) {
//						Date date = new Date(year, Integer.parseInt(month), i);
//						listStatisticalResponses.add( new StatisticalResponse(date, 0L));
//					}
//				}
//			
//				
//			default:
//				System.out.println("default");
//				break;
//		}

		HttpSession session = request.getSession();
		session.setAttribute("statisticals", listStatisticalResponses);
		session.setAttribute("totalTurnover", totalTurnover);
		session.setAttribute("month", month);

		return listStatisticalResponses;
	}

}
