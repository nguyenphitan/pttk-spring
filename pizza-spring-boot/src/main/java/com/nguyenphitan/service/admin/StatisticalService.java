package com.nguyenphitan.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nguyenphitan.payload.StatisticalResponse;

public interface StatisticalService {
	List<StatisticalResponse> getMonthStatistical(String month, HttpServletRequest request);
}
