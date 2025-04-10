package com.billing.BillingSoftware.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillReportDTO {
	 	
		public BillReportDTO(int billNo2, long cusPhone, LocalDate billingDate, String item2, double mrp2, int quantity2,
			double total2) {
			this.billno = (long)billNo2;
			this.date= billingDate;
			this.phone=cusPhone;
			this.item = item2;
			this.mrp = mrp2;
			this.quantity=quantity2;
			this.total=total2;
	}
		public BillReportDTO(int billNo2, long cusPhone, LocalDate billingDate, long id2, String item2, double mrp2,
				int quantity2, double total2) {
			this.billno = (long)billNo2;
			this.date= billingDate;
			this.phone=cusPhone;
			this.id = id2;
			this.item = item2;
			this.mrp = mrp2;
			this.quantity=quantity2;
			this.total=total2;
		}
		private Long id;
		private Long billno;
	    private LocalDate date;
	    private Long phone;
	    private String item;
	    private Double mrp;
	    private Integer quantity;
	    private Double total;
	 
}
