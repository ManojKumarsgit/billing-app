package com.billing.BillingSoftware.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.BillingSoftware.DTO.BillReportDTO;
import com.billing.BillingSoftware.model.Bill;
import com.billing.BillingSoftware.model.BillDetails;

@Repository
public interface BillReportRepository extends JpaRepository<Bill, Long> {
	List<Bill> findByCusPhone(Long billNo);
}