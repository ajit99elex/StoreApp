package com.retail.storeapp.repository;

import com.retail.storeapp.entity.BillDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BillDetailsRepository extends JpaRepository<BillDetails, Integer>, JpaSpecificationExecutor<BillDetails> {
}
