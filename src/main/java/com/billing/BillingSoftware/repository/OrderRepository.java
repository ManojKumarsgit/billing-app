package com.billing.BillingSoftware.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.billing.BillingSoftware.DTO.SalesReportDTO;
import com.billing.BillingSoftware.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Bill, Long> {
	@Query(value = "SELECT ROW_NUMBER() OVER (ORDER BY b.billing_date) AS sno, b.bill_no, SUM(d.total) AS total, SUM(d.total) AS grand_total, b.billing_date AS date " +
            "FROM bill b JOIN bill_details d ON b.bill_no = d.billno " +
            "WHERE b.billing_date BETWEEN :startDate AND :endDate " +
            "GROUP BY b.bill_no, b.billing_date " +
            "ORDER BY b.billing_date ASC", 
    nativeQuery = true)
		List<Object[]> getSalesReport(
		    @Param("startDate") LocalDate startDate, 
		    @Param("endDate") LocalDate endDate
		);
	
	

}
