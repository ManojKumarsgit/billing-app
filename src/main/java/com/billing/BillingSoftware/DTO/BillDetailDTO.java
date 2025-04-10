package com.billing.BillingSoftware.DTO;

import lombok.Data;

@Data
public class BillDetailDTO {
    private String item;
    private double mrp;
    private int quantity;
    private double total;
}
