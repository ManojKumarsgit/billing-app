package com.billing.BillingSoftware.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class BillDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String item;
	private double mrp;
	private int quantity;
	private double total;
	
	@ManyToOne
	@JoinColumn(name = "billno", nullable = false)
	private Bill bill;
}
