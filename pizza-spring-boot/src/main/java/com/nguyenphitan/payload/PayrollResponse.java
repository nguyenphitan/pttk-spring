package com.nguyenphitan.payload;

import java.util.Date;
import java.util.List;

import com.nguyenphitan.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayrollResponse {
	private Account seller;
	private List<Date> dates;
	private Integer totalDate;
}
