package com.billing.BillingSoftware.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.billing.BillingSoftware.model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

	List<Bill> findByBillingDateAndCusPhone(LocalDate date, Long phone);
	
	 

	    @Query("SELECT COUNT(DISTINCT b.cusPhone) FROM Bill b")
	    long countDistinctCustomers();

	    List<Bill> findTop5ByOrderByBillingDateDesc();
}