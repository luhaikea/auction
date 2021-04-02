package com.lhk.auction.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lhk.auction.bean.*;
import com.lhk.auction.manage.mapper.*;
import com.lhk.auction.mq.ActiveMQUtil;
import com.lhk.auction.service.OrderService;
import com.lhk.auction.service.PmsProductService;
import com.lhk.auction.util.RedisUtil;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.List;
import java.util.UUID;

@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    PmsProductAttrValueMapper pmsProductAttrValueMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public List<PmsProductInfo> getPmsProductInfo(String catalog3Id) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);

        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);

        return pmsProductInfos;
    }

    @Override
    public String editPmsProductInfo(PmsProductInfo pmsProductInfo) {

        List<PmsProductImage> pmsProductImages = pmsProductInfo.getPmsProductImages();
        if(pmsProductImages.size()>0){
            pmsProductInfo.setProductDefaultImg(pmsProductImages.get(0).getImgUrl());
        }

        //删除旧信息
        String productId = pmsProductInfo.getId();

        Example example = new Example(PmsProductInfo.class);
        example.createCriteria().andEqualTo("id", pmsProductInfo.getId());
        pmsProductInfoMapper.updateByExampleSelective(pmsProductInfo, example);

        PmsProductAttrValue pmsProductAttrValue = new PmsProductAttrValue();
        pmsProductAttrValue.setProductId(productId);
        pmsProductAttrValueMapper.delete(pmsProductAttrValue);
        List<PmsProductAttrValue> pmsProductAttrValues = pmsProductInfo.getPmsProductAttrValues();
        for (PmsProductAttrValue pmsProductAttrValue1 : pmsProductAttrValues) {
            pmsProductAttrValue1.setProductId(productId);
            pmsProductAttrValueMapper.insertSelective(pmsProductAttrValue1);
        }


        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(productId);
        pmsProductSaleAttrMapper.delete(pmsProductSaleAttr);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductInfo.getPmsProductSaleAttrs();
        for (PmsProductSaleAttr pmsProductSaleAttr1 : pmsProductSaleAttrs) {
            pmsProductSaleAttr1.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr1);
        }

        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(productId);
        pmsProductImageMapper.delete(pmsProductImage);
        for (PmsProductImage pmsProductImage1 : pmsProductImages) {
            pmsProductImage1.setProductId(productId);
            pmsProductImageMapper.insertSelective(pmsProductImage1);
        }

        return "success";
    }

    @Override
    public String aggreeApprove(PmsProductInfo pmsProductInfo) {

        pmsProductInfo.setApproveStatus("1");
        Example example = new Example(PmsProductInfo.class);
        example.createCriteria().andEqualTo("id", pmsProductInfo.getId());
        pmsProductInfoMapper.updateByExampleSelective(pmsProductInfo, example);
        return "success";
    }

    @Override
    public void sendUpdateSearchMsg(String id) {

        // SEARCH_UPDATE_QUEUE：由搜索服务消费
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }

        try {

            Queue payment_check_queue = session.createQueue("SEARCH_UPDATE_QUEUE");
            MessageProducer producer = session.createProducer(payment_check_queue);
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("productId", id);
            producer.send(mapMessage);
            session.commit();
        } catch (Exception ex) {
            try {
                session.rollback();
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        }
    }

    @Override
    public String savePmsProductInfo(PmsProductInfo pmsProductInfo) {

        List<PmsProductImage> pmsProductImages = pmsProductInfo.getPmsProductImages();
        if(pmsProductImages.size()>0){
            pmsProductInfo.setProductDefaultImg(pmsProductImages.get(0).getImgUrl());
        }
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        String productId = pmsProductInfo.getId();

        List<PmsProductAttrValue> pmsProductAttrValues = pmsProductInfo.getPmsProductAttrValues();
        for (PmsProductAttrValue pmsProductAttrValue : pmsProductAttrValues) {
            pmsProductAttrValue.setProductId(productId);
            pmsProductAttrValueMapper.insertSelective(pmsProductAttrValue);
        }

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductInfo.getPmsProductSaleAttrs();
        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrs) {
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
        }


        for (PmsProductImage pmsProductImage : pmsProductImages) {
            pmsProductImage.setProductId(productId);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        return "success";
    }

    @Override
    public PmsProductInfo getPmsProductInfoById(String productId) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        //链接缓存
        Jedis jedis = redisUtil.getJedis();
        //查询缓存
        String productKey = "product:" + productId + ":info";
        String productKeyJson = jedis.get(productKey);

        if (StringUtils.isNoneBlank(productKeyJson)) {
            pmsProductInfo = JSON.parseObject(productKeyJson, PmsProductInfo.class);
        } else {
            //这里是缓存中没有对应的值  有两种情况一个是确实没有，另一个是缓存击穿（缓存失效）这时需要用加分布式锁的方式去访问DB
            //设置分布式锁
            //锁是锁一个sku其他skubu不影响
            //将锁的值设置成唯一的，代表这个唯一的线程  防止线程删除其他线程的锁
            //也就是一个线程得到锁然后去访问数据库，还没来得及回来删除自己的锁，自己的锁就过期了，另外一个线程有获得了锁，这时前面那个线程有复活删除了这个线程的锁
            String token = UUID.randomUUID().toString();
            String ok = jedis.set("product:" + productId + ":lock", token, "nx", "px", 10 * 1000);
            if (StringUtils.isNoneBlank(ok) && ok.equals("OK")) {
                //设置成功，有权在10秒的过期时间内访问数据库
                pmsProductInfo = getPmsProductInfoByIdFromDB(productId);

                if (pmsProductInfo != null) {
                    jedis.set(productKey, JSON.toJSONString(pmsProductInfo));
                } else {
                    //数据库中不存在该sku 为了防止缓存穿透
                    jedis.setex(productKey, 60 * 3, JSON.toJSONString(""));//加缓存时间
                }

                //此处应该释放锁
                //先确定是不是自己的锁再释放    先进行非空判断是为了防止空指针异常
                //还有一种极限情况就是在执行下面这个判断时锁过期了 也就是lockToken是自己锁的值，但jedis.del()删的确是其他线程的锁
                // 所以下面这个还存在问题  解决：jedis.eval("lua")可用lua脚本，在查询到key的同时删除该key，防止高并发下的意外发生

                String lockToken = jedis.get("product:" + productId + ":lock");
                if (StringUtils.isNoneBlank(lockToken) && lockToken.equals(token)) {
                    jedis.del("product:" + productId + ":lock");
                }

            } else {
                //设置失败 有节点已经加锁  自旋（该线程下睡眠几秒后，重新尝试访问访问本方法）
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //不能直接getSkuById(skuId)必须是return
                return getPmsProductInfoById(productId);
            }
        }

        jedis.close();
        return pmsProductInfo;
    }


    public PmsProductInfo getPmsProductInfoByIdFromDB(String productId) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setId(productId);
        PmsProductInfo pmsProductInfoSelect = pmsProductInfoMapper.selectOne(pmsProductInfo);

        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(productId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        if (pmsProductImages != null) {
            pmsProductInfoSelect.setPmsProductImages(pmsProductImages);
        }

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(productId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        pmsProductInfoSelect.setPmsProductSaleAttrs(pmsProductSaleAttrs);

        return pmsProductInfoSelect;
    }

    @Override
    public List<PmsProductInfo> getAllProduct() {
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.selectAll();

        for (PmsProductInfo pmsProductInfo : pmsProductInfos) {
            PmsProductAttrValue pmsProductAttrValue = new PmsProductAttrValue();
            pmsProductAttrValue.setProductId(pmsProductInfo.getId());
            List<PmsProductAttrValue> select = pmsProductAttrValueMapper.select(pmsProductAttrValue);

            pmsProductInfo.setPmsProductAttrValues(select);
        }
        return pmsProductInfos;
    }

    @Override
    public PmsProductInfo getSearchProductById(String productId) {

        PmsProductInfo pmsProductInfo = pmsProductInfoMapper.selectByPrimaryKey(productId);

        PmsProductAttrValue pmsProductAttrValue = new PmsProductAttrValue();
        pmsProductAttrValue.setProductId(pmsProductInfo.getId());
        List<PmsProductAttrValue> select = pmsProductAttrValueMapper.select(pmsProductAttrValue);
        pmsProductInfo.setPmsProductAttrValues(select);

        return pmsProductInfo;
    }


    @Override
    public List<PmsProductInfo> getAllPmsProductInfo() {
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.selectAll();
        return pmsProductInfos;
    }

    @Override
    public void updatePmsProductInfo(PmsProductInfo pmsProductInfo) {

        Example example = new Example(PmsProductInfo.class);
        example.createCriteria().andEqualTo("id", pmsProductInfo.getId());

        pmsProductInfoMapper.updateByExampleSelective(pmsProductInfo, example);
    }

    @Override
    public PmsProductInfo getPmsProductInfoByIdAllInfo(String productId) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setId(productId);
        PmsProductInfo pmsProductInfoSelect = pmsProductInfoMapper.selectOne(pmsProductInfo);

        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(productId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        if (pmsProductImages != null) {
            pmsProductInfoSelect.setPmsProductImages(pmsProductImages);
        }

        PmsProductAttrValue pmsProductAttrValue = new PmsProductAttrValue();
        pmsProductAttrValue.setProductId(productId);
        List<PmsProductAttrValue> pmsProductAttrValues = pmsProductAttrValueMapper.select(pmsProductAttrValue);
        if(pmsProductAttrValues != null){
            pmsProductInfoSelect.setPmsProductAttrValues(pmsProductAttrValues);
        }

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(productId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        pmsProductInfoSelect.setPmsProductSaleAttrs(pmsProductSaleAttrs);

        return pmsProductInfoSelect;
    }

}
