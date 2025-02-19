package com.billing.BillingSoftware.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.BillingSoftware.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findById(long id);

	Product findByName(String item);
}

