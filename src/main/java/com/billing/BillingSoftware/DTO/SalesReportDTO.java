package com.billing.BillingSoftware.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesReportDTO {
	private int sno; // Serial Number
    private long billno;
    private double total;
    private double grandTotal;
    private LocalDate date;
    
    public SalesReportDTO(int sno, long billno, double total, double grandTotal, LocalDate date) {
        this.sno = sno;
        this.billno = billno;
        this.total = total;
        this.grandTotal = grandTotal;
        this.date = date;
    }
}
