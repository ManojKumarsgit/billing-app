package com.billing.BillingSoftware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.BillingSoftware.DTO.SalesReportDTO;
import com.billing.BillingSoftware.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
	@Autowired
    private  OrderRepository orderRepository;

    public List<Object[]> getSalesReport(LocalDate startDate, LocalDate endDate) {
//        return orderRepository.getSalesReport(startDate, endDate);
        return orderRepository.getSalesReport(startDate, endDate);
    }
}
