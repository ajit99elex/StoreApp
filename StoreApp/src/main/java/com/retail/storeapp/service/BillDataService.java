package com.retail.storeapp.service;

import com.retail.storeapp.entity.Bill;
import com.retail.storeapp.entity.BillDetails;
import com.retail.storeapp.entity.Product;
import com.retail.storeapp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("billService")
public class BillDataService {
    private static final Logger logger = LoggerFactory.getLogger(BillDataService.class);

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillDetailsRepository billDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("generateBill")
    public String generateBill(@RequestBody Bill bill){
        try {
            bill.setPurchaseDate(new Date());
            Optional<Bill> billOptional = Optional.ofNullable(billRepository.save(bill));
            billOptional.get().getBillDetailsList().forEach(
                    billDetails -> {
                        billDetails.setBill(billOptional.get());
                        billDetails.setProduct(productRepository.getOne(billDetails.getProduct().getProductId()));
                        billDetailsRepository.save(billDetails);
                    }
            );

            return "Bill created with id-"+billOptional
                    .map(Bill::getBillId)
                    .map(String::valueOf)
                    .orElse("Unable to fetch id of created bill");
        } catch (Exception e) {
            logger.error("bill.tostring:"+bill.toString());
            logger.error("Exception occurred while saving bill object", e);
        }
        return "Unable to generate bill";
    }

    @GetMapping("searchBill")
    public List<Bill> findProduct(
            @RequestParam Integer billId,
            @RequestParam String customerName,
            @RequestParam String purchaseDate) {
        List<Bill> bills = new ArrayList<>();
        Specification specification = null;

        if (!StringUtils.isEmpty(customerName)) {
            RequestSpecificationCriteria requestSpecificationCriteria
                    = new RequestSpecificationCriteria(new SearchCriteria("customerName", "STRING:=LIKE", customerName.toLowerCase()));
            specification = Specification.where(requestSpecificationCriteria);
        }

        if (!StringUtils.isEmpty(billId)) {
            RequestSpecificationCriteria requestSpecificationCriteria
                    = new RequestSpecificationCriteria(new SearchCriteria("billId", "INTEGER:=", billId));
            if (specification == null)
                specification = Specification.where(requestSpecificationCriteria);
            else
                specification = specification.and(requestSpecificationCriteria);
        }

        if (!StringUtils.isEmpty(purchaseDate)) {//date should be in MM/dd/yyyy
            RequestSpecificationCriteria requestSpecificationCriteria
                    = new RequestSpecificationCriteria(new SearchCriteria("purchaseDate", "STRING:=", purchaseDate));
            if (specification == null)
                specification = Specification.where(requestSpecificationCriteria);
            else
                specification = specification.and(requestSpecificationCriteria);
        }

        bills = billRepository.findAll(specification);
        return bills;
    }

    @GetMapping("getAllBills")
    public List<Bill> showAllBills(){
        return billRepository.findAll();
    }
}
