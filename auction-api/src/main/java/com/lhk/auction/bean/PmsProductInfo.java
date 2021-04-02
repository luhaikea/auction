package com.lhk.auction.bean;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @param
 * @return
 */
public class PmsProductInfo implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    String id;
    @Column
    String productName;
    @Column
    BigDecimal earnest;
    @Column
    BigDecimal startingPrice;
    @Column
    String productDesc;
    @Column
    String catalog3Id;
    @Column
    String productDefaultImg;
    @Column
    String startTime;
    @Column
    String endTime;
    @Column
    String buyer;
    @Column
    String approveStatus;

    @Transient
    BigDecimal topPrice;  //出价最高
    @Transient
    Integer numBid;  //订单的数量
    @Transient
    List<PmsProductImage> pmsProductImages;
    @Transient
    List<PmsProductAttrValue> pmsProductAttrValues;
    @Transient
    List<PmsProductSaleAttr> pmsProductSaleAttrs;
    @Transient
    List<OmsOrder> omsOrders;

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public List<OmsOrder> getOmsOrders() {
        return omsOrders;
    }

    public void setOmsOrders(List<OmsOrder> omsOrders) {
        this.omsOrders = omsOrders;
    }

    public BigDecimal getTopPrice() {
        return topPrice;
    }

    public void setTopPrice(BigDecimal topPrice) {
        this.topPrice = topPrice;
    }

    public Integer getNumBid() {
        return numBid;
    }

    public void setNumBid(Integer numBid) {
        this.numBid = numBid;
    }

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<PmsProductImage> getPmsProductImages() {
        return pmsProductImages;
    }

    public void setPmsProductImages(List<PmsProductImage> pmsProductImages) {
        this.pmsProductImages = pmsProductImages;
    }

    public List<PmsProductAttrValue> getPmsProductAttrValues() {
        return pmsProductAttrValues;
    }

    public void setPmsProductAttrValues(List<PmsProductAttrValue> pmsProductAttrValues) {
        this.pmsProductAttrValues = pmsProductAttrValues;
    }

    public List<PmsProductSaleAttr> getPmsProductSaleAttrs() {
        return pmsProductSaleAttrs;
    }

    public void setPmsProductSaleAttrs(List<PmsProductSaleAttr> pmsProductSaleAttrs) {
        this.pmsProductSaleAttrs = pmsProductSaleAttrs;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }
}
