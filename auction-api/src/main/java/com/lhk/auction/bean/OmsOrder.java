package com.lhk.auction.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OmsOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String productId;
    @Column
    private String memberId;
    @Column
    private String orderSn;
    @Column
    private String orderSnFinal;
    @Column
    private Date createTime;
    @Column
    private BigDecimal bidPrice;
    @Column
    private String earnestStatus;
    @Column
    private String finalPayStatus;
    @Column
    private String auctionStatus;
    @Column
    private String receiveId;
    @Transient
    private PmsProductInfo pmsProductInfo;

    public String getOrderSnFinal() {
        return orderSnFinal;
    }

    public void setOrderSnFinal(String orderSnFinal) {
        this.orderSnFinal = orderSnFinal;
    }

    public PmsProductInfo getPmsProductInfo() {
        return pmsProductInfo;
    }

    public void setPmsProductInfo(PmsProductInfo pmsProductInfo) {
        this.pmsProductInfo = pmsProductInfo;
    }

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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getEarnestStatus() {
        return earnestStatus;
    }

    public void setEarnestStatus(String earnestStatus) {
        this.earnestStatus = earnestStatus;
    }

    public String getFinalPayStatus() {
        return finalPayStatus;
    }

    public void setFinalPayStatus(String finalPayStatus) {
        this.finalPayStatus = finalPayStatus;
    }

    public String getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(String auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }
}
