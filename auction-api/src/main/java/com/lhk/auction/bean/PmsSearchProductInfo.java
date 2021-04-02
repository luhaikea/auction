package com.lhk.auction.bean;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @param
 * @return
 */
public class PmsSearchProductInfo implements Serializable {

    @Id
    private String id;
    private String productName;
    private BigDecimal earnest;
    private BigDecimal startingPrice;
    private String productDesc;
    private String catalog3Id;
    private String productDefaultImg;
    private Integer hotScore;

    List<PmsProductAttrValue> pmsProductAttrValues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getEarnest() {
        return earnest;
    }

    public void setEarnest(BigDecimal earnest) {
        this.earnest = earnest;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getProductDefaultImg() {
        return productDefaultImg;
    }

    public void setProductDefaultImg(String productDefaultImg) {
        this.productDefaultImg = productDefaultImg;
    }

    public Integer getHotScore() {
        return hotScore;
    }

    public void setHotScore(Integer hotScore) {
        this.hotScore = hotScore;
    }

    public List<PmsProductAttrValue> getPmsProductAttrValues() {
        return pmsProductAttrValues;
    }

    public void setPmsProductAttrValues(List<PmsProductAttrValue> pmsProductAttrValues) {
        this.pmsProductAttrValues = pmsProductAttrValues;
    }
}
