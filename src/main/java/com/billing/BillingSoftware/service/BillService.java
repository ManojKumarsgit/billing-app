package com.billing.BillingSoftware.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.billing.BillingSoftware.DTO.BillReportDTO;
import com.billing.BillingSoftware.model.Bill;
import com.billing.BillingSoftware.repository.BillReportRepository;

@Service
public class BillService {
	 private final BillReportRepository billRepository;

	    public BillService(BillReportRepository billRepository) {
	        this.billRepository = billRepository;
	    }

	    public List<BillReportDTO> getBillReport(Long billNo) {
	        List<Bill> bills = billRepository.findByCusPhone(billNo);

	        return bills.stream()
	                .flatMap(bill -> bill.getBillDetails().stream().map(detail ->
	                        new BillReportDTO(
	                                bill.getBillNo(),
	                                bill.getCusPhone(),
	                                bill.getBillingDate(),
	                                detail.getItem(),
	                                detail.getMrp(),
	                                detail.getQuantity(),
	                                detail.getTotal()
	                        )))
	                .collect(Collectors.toList());
	}
}
