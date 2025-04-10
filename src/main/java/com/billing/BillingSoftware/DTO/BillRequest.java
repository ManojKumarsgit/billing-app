package com.billing.BillingSoftware.DTO;

import java.util.List;

import lombok.Data;

@Data
public class BillRequest {
    private long cusPhone;
    private List<BillDetailDTO> billDetails;
    private double gst;
    private double discount;
	public long getCusPhone() {
		return cusPhone;
	}   
	
}
