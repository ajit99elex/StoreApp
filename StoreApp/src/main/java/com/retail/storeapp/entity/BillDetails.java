package com.retail.storeapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class BillDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long billDetailsId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private BigDecimal appliedDiscount;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="purchasedProductId")
    public Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="billId")
    private Bill bill;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAppliedDiscount() {
        return appliedDiscount;
    }

    public void setAppliedDiscount(BigDecimal appliedDiscount) {
        this.appliedDiscount = appliedDiscount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonIgnore
    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
