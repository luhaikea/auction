package com.lhk.auction.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class PmsProductSaleAttr implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    String id;
    @Column
    String productId;
    @Column
    String saleAttrName;
    @Column
    String saleAttrValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSaleAttrName() {
        return saleAttrName;
    }

    public void setSaleAttrName(String saleAttrName) {
        this.saleAttrName = saleAttrName;
    }

    public String getSaleAttrValue() {
        return saleAttrValue;
    }

    public void setSaleAttrValue(String saleAttrValue) {
        this.saleAttrValue = saleAttrValue;
    }
}
