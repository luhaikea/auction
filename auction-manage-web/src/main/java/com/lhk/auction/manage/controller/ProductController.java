package com.lhk.auction.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.bean.PmsAttrInfo;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.bean.UmsMember;
import com.lhk.auction.manage.util.PmsUploadUtil;
import com.lhk.auction.service.MemberService;
import com.lhk.auction.service.OrderService;
import com.lhk.auction.service.PmsProductService;
import org.csource.common.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProductController {

    @Reference
    PmsProductService pmsProductService;
    @Reference
    MemberService memberService;
    @Reference
    OrderService orderService;

    @RequestMapping("/aggreeApprove")
    @ResponseBody
    public Map<String, String> aggreeApprove(@RequestBody PmsProductInfo pmsProductInfo) {

        Map<String, String> ret = new HashMap<String, String>();
        if (pmsProductInfo == null) {
            ret.put("type", "error");
            ret.put("msg", "请填写正确的属性信息！");
            return ret;
        }
        String result = pmsProductService.aggreeApprove(pmsProductInfo);
        ret.put("type", "success");
        ret.put("msg", "已同意上架！");

        //同意上架后应该更新搜索引擎(在搜索系统更新) redis
        //调用服务通知有新物品上架
        pmsProductService.sendUpdateSearchMsg(pmsProductInfo.getId());

        return ret;
    }

    @RequestMapping("/editPmsProductInfo")
    @ResponseBody
    public Map<String, String> editPmsProductInfo(@RequestBody PmsProductInfo pmsProductInfo) {

        Map<String, String> ret = new HashMap<String, String>();
        if (pmsProductInfo == null) {
            ret.put("type", "error");
            ret.put("msg", "请填写正确的属性信息！");
            return ret;
        }
        String result = pmsProductService.editPmsProductInfo(pmsProductInfo);
        ret.put("type", "success");
        ret.put("msg", "修改成功！");

        //修改物品后应该更新搜索引擎(在搜索系统更新) redis
        //调用服务通知有物品更新
        pmsProductService.sendUpdateSearchMsg(pmsProductInfo.getId());

        return ret;
    }

    //用于修改物品
    @RequestMapping(value = "/getPmsProductInfoById", method = RequestMethod.POST)
    @ResponseBody
    public PmsProductInfo getPmsProductInfoById(@RequestBody PmsProductInfo pmsProductInfo) {

        String pmsProductId = pmsProductInfo.getId();
        if (pmsProductId == null) {
            return null;
        }
        return pmsProductService.getPmsProductInfoByIdAllInfo(pmsProductId);
    }

    @RequestMapping("/savePmsProductInfo")
    @ResponseBody
    public Map<String, String> savePmsProductInfo(@RequestBody PmsProductInfo pmsProductInfo) {

        Map<String, String> ret = new HashMap<String, String>();
        if (pmsProductInfo == null) {
            ret.put("type", "error");
            ret.put("msg", "请填写正确的属性信息！");
            return ret;
        }
        String result = pmsProductService.savePmsProductInfo(pmsProductInfo);
        ret.put("type", "success");
        ret.put("msg", "保存成功！");
        return ret;
    }

    @RequestMapping("/getProductList")
    public String getProductList() {
        return "productList";
    }

    @RequestMapping(value = "/getProductApprove", method = RequestMethod.GET)
    public String getProductApprove() {
        return "productApprove";
    }

    @RequestMapping(value = "/getProductStatus", method = RequestMethod.GET)
    public String getProductStatus() {
        return "productStatus";
    }

    //这里要返回已经审批过了的
    @RequestMapping("/productList")
    @ResponseBody
    public List<PmsProductInfo> productList(String catalog3Id) {

        if (catalog3Id == null) {
            return null;
        }
        List<PmsProductInfo> pmsProductInfo = pmsProductService.getPmsProductInfo(catalog3Id);
        List<PmsProductInfo> pmsProductInfoRes = new ArrayList<PmsProductInfo>();
        for (PmsProductInfo productInfo : pmsProductInfo) {
            if (productInfo.getApproveStatus().equals("1")) {
                pmsProductInfoRes.add(productInfo);
            }
        }
        return pmsProductInfoRes;
    }

    //这里要返回未审批的
    @RequestMapping("/productListNoApprove")
    @ResponseBody
    public List<PmsProductInfo> productListNoApprove(String catalog3Id) {

        if (catalog3Id == null) {
            return null;
        }
        List<PmsProductInfo> pmsProductInfo = pmsProductService.getPmsProductInfo(catalog3Id);
        List<PmsProductInfo> pmsProductInfoRes = new ArrayList<PmsProductInfo>();
        for (PmsProductInfo productInfo : pmsProductInfo) {
            if (productInfo.getApproveStatus().equals("0")) {
                pmsProductInfoRes.add(productInfo);
            }
        }
        return pmsProductInfoRes;
    }

    @RequestMapping("/fileUpload")
    @ResponseBody
    public Map<String, Object> fileUpload(@RequestParam("theFiles") MultipartFile[] multipartFile) throws IOException, MyException {
        //将图片或者音视频上传到分布式文件系统

        Map<String, Object> ret = new HashMap<String, Object>();
        if (multipartFile == null) {
            ret.put("type", "error");
            ret.put("msg", "保存失败");
            return ret;
        }

        List<String> imgUrls = new ArrayList<>();

        for (int i = 0; i < multipartFile.length; i++) {
            String imgUrl = "http://112.126.91.227/";

            if (multipartFile[i] != null && !multipartFile[i].isEmpty()) {
                try {

                    String filename = multipartFile[i].getOriginalFilename();
                    String extName = filename.substring(filename.lastIndexOf(".") + 1);
                    // 保存文件
                    PmsUploadUtil pmsUploadUtil = new PmsUploadUtil();
                    imgUrl += pmsUploadUtil.uploadFile(multipartFile[i].getBytes(), extName);
                    imgUrls.add(imgUrl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ret.put("type", "success");
        ret.put("imgUrls", imgUrls);

        //将图片的存储路径返回给页面
        return ret;
    }

    @RequestMapping("/getProductStatusSelect")
    @ResponseBody
    public List<Map<String, String>> getProductStatusSelect() {

        List<Map<String, String>> ret = new ArrayList<>();
        Map<String, String> m1 = new HashMap<>();
        m1.put("id", "0");
        m1.put("name", "全部");
        ret.add(m1);
        Map<String, String> m2 = new HashMap<>();
        m2.put("id", "1");
        m2.put("name", "未开拍");
        ret.add(m2);
        Map<String, String> m3 = new HashMap<>();
        m3.put("id", "2");
        m3.put("name", "正在竞拍");
        ret.add(m3);
        Map<String, String> m4 = new HashMap<>();
        m4.put("id", "3");
        m4.put("name", "已结束");
        ret.add(m4);
        return ret;
    }

    @RequestMapping("/productStatus")
    @ResponseBody
    public List<PmsProductInfo> productStatus(String productStatus) throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PmsProductInfo> pmsProductInfos = pmsProductService.getAllPmsProductInfo();
        for (PmsProductInfo pmsProductInfo : pmsProductInfos) {
            if (pmsProductInfo.getApproveStatus().equals("1")) {

                if(pmsProductInfo.getBuyer().equals("-1")){
                    String startTime = pmsProductInfo.getStartTime();
                    Date startDate = df.parse(startTime);
                    Date nowDate = new Date();
                    if (startDate.getTime() > nowDate.getTime()) {
                        pmsProductInfo.setBuyer("未开拍");
                    }else {
                        pmsProductInfo.setBuyer("正在竞拍");
                    }

                } else if(pmsProductInfo.getBuyer().equals("0")){
                    pmsProductInfo.setBuyer("拍品未拍出");
                } else{
                    UmsMember umsMember = memberService.getUmsMemberById(pmsProductInfo.getBuyer());
                    pmsProductInfo.setBuyer("获拍人:"+umsMember.getNickname());
                }
            } else{
                pmsProductInfo.setBuyer("未上架");
            }
        }
        List<PmsProductInfo> res = new ArrayList<>();

        if (productStatus == null || productStatus.equals("0")) {
            for (PmsProductInfo pmsProductInfo : pmsProductInfos) {
                if (pmsProductInfo.getApproveStatus().equals("1")) {
                    res.add(pmsProductInfo);
                }
            }
        } else if (productStatus.equals("1")) {
            for (PmsProductInfo pmsProductInfo : pmsProductInfos) {
                //2021-01-27 00:00:00
                if (pmsProductInfo.getApproveStatus().equals("1")) {
                    String startTime = pmsProductInfo.getStartTime();
                    Date startDate = df.parse(startTime);
                    Date nowDate = new Date();
                    if (startDate.getTime() > nowDate.getTime()) {
                        res.add(pmsProductInfo);
                    }
                }
            }
        } else if (productStatus.equals("2")) {
            for (PmsProductInfo pmsProductInfo : pmsProductInfos) {
                if (pmsProductInfo.getApproveStatus().equals("1")) {
                    String startTime = pmsProductInfo.getStartTime();
                    String endTime = pmsProductInfo.getEndTime();
                    Date startDate = df.parse(startTime);
                    Date endDate = df.parse(endTime);
                    Date nowDate = new Date();
                    if (startDate.getTime() < nowDate.getTime() && endDate.getTime() > nowDate.getTime()) {
                        res.add(pmsProductInfo);
                    }
                }
            }
        } else if (productStatus.equals("3")) {
            for (PmsProductInfo pmsProductInfo : pmsProductInfos) {
                if (pmsProductInfo.getApproveStatus().equals("1")) {
                    String endTime = pmsProductInfo.getEndTime();
                    Date endDate = df.parse(endTime);
                    Date nowDate = new Date();
                    if (endDate.getTime() < nowDate.getTime()) {
                        res.add(pmsProductInfo);
                    }
                }
            }
        }

        return res;
    }

    @RequestMapping("/orderList")
    @ResponseBody
    public List<OmsOrder> orderList(String pmsProductId) {

        List<OmsOrder> orderByProductId = orderService.getOrderByProductId(pmsProductId);
        for (OmsOrder omsOrder : orderByProductId) {

            String earnestStatus = omsOrder.getEarnestStatus();
            if(earnestStatus.equals("0")){
                omsOrder.setEarnestStatus("未支付");
            } else if(earnestStatus.equals("1")){
                omsOrder.setEarnestStatus("已支付");
            }

            String finalPayStatus = omsOrder.getFinalPayStatus();
            if(finalPayStatus.equals("0")){
                omsOrder.setFinalPayStatus("待参拍");
            } else if (finalPayStatus.equals("1")){
                omsOrder.setFinalPayStatus("已支付");
            } else if (finalPayStatus.equals("2")){
                omsOrder.setFinalPayStatus("未支付");
            }

            String auctionStatus = omsOrder.getAuctionStatus();
            if(auctionStatus.equals("0")){
                omsOrder.setAuctionStatus("待参拍");
            } else if(auctionStatus.equals("1")){
                omsOrder.setAuctionStatus("正在竞拍");
            } else if(auctionStatus.equals("2")){
                omsOrder.setAuctionStatus("获拍");
            } else if(auctionStatus.equals("3")){
                omsOrder.setAuctionStatus("未获拍");
            }
        }
        return orderByProductId;
    }

}
