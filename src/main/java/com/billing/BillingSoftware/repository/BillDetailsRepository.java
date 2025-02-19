package com.billing.BillingSoftware.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.billing.BillingSoftware.model.Bill;
import com.billing.BillingSoftware.model.BillDetails;


import java.lang.*;


@Repository
public interface BillDetailsRepository extends JpaRepository<BillDetails, Long> {

	int countByBill(Bill bill);
	
	@Query("SELECT COUNT(bd) FROM BillDetails bd WHERE bd.bill = :bill")
	int countBillDetailsByBill(Bill bill);
	
	 @Query("SELECT SUM(b.total) FROM BillDetails b")
	    double getTotalSales();
	 


	    @Query("SELECT SUM(d.total) FROM BillDetails d WHERE d.bill.billNo = :billNo")
	    Double getBillTotal(@Param("billNo") long i);
}
