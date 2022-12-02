package com.nguyenphitan.payload;

import javax.validation.constraints.NotBlank;

import com.nguyenphitan.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimekeepingResponse {
	@NotBlank
	private Account seller;
	
	@NotBlank
	private Boolean status;
	
}
