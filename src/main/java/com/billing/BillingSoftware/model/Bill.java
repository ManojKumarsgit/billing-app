package com.billing.BillingSoftware.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class Bill {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "bill_generator")
    @SequenceGenerator(name = "bill_generator", sequenceName = "bill_no", initialValue = 1000)
	private int billNo;
	private long cusPhone;
	private LocalDate billingDate;
	 @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 private List<BillDetails> billDetails;
}
