package com.lhk.auction.member.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lhk.auction.bean.UmsMember;
import com.lhk.auction.bean.UmsMemberReceiveAddress;
import com.lhk.auction.member.mapper.UmsMemberMapper;
import com.lhk.auction.member.mapper.UmsMemberReceiveAddressMapper;
import com.lhk.auction.service.MemberService;
import com.lhk.auction.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsMemberMapper umsMemberMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public UmsMember login(UmsMember umsMember) {

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis!=null){
                //需要在写注册功能是保证不能有相同的密码
                String umsMemberStr = jedis.get("user:" + umsMember.getPassword() + ":info");
                if(StringUtils.isNotBlank(umsMemberStr)){
                    //密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                } else{
                    //密码错误
                    //缓存中没有 开数据库查询
                    UmsMember umsMemberFromDb = loginFromDb(umsMember);
                    if(umsMemberFromDb!=null){
                        //带过期时间
                        jedis.setex("user:" + umsMember.getPassword() + ":info",60*60*24, JSON.toJSONString(umsMemberFromDb));
                    }
                    return umsMemberFromDb;
                }
            } else{
                //Jedis连接不上了 开启数据库  redis失效
                UmsMember umsMemberFromDb = loginFromDb(umsMember);
                if(umsMemberFromDb!=null){
                    //带过期时间
                    jedis.setex("user:" + umsMember.getPassword() + ":info",60*60*24,JSON.toJSONString(umsMemberFromDb));
                }
                return umsMemberFromDb;
            }

        } finally {
            jedis.close();
        }

    }

    @Override
    public void saveUserTokenToCache(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+memberId+":token", 60*60*2, token);
        jedis.close();
    }

    @Override
    public List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId) {

        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses =  umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
        return umsMemberReceiveAddresses;
    }

    @Override
    public void addUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        umsMemberReceiveAddressMapper.insert(umsMemberReceiveAddress);
    }

    @Override
    public UmsMember checkWeiboMember(String sourceUid) {

        UmsMember umsMember = new UmsMember();
        umsMember.setSourceUid(sourceUid);
        UmsMember umsMember1 = umsMemberMapper.selectOne(umsMember);
        return umsMember1;
    }

    @Override
    public UmsMember saveUmsMember(UmsMember weiboUmsMember) {
        int i = umsMemberMapper.insertSelective(weiboUmsMember);
        return weiboUmsMember;
    }

    @Override
    public int  registUmsMember(UmsMember umsMember) {
        int i = umsMemberMapper.insertSelective(umsMember);
        return i;
    }

    @Override
    public boolean usernameExitCheck(String username) {

        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        List<UmsMember> select = umsMemberMapper.select(umsMember);

        if(select.size() == 0){
            return  false;
        }else {
            return true;
        }

    }

    @Override
    public UmsMember getUmsMemberById(String buyer) {
        return umsMemberMapper.selectByPrimaryKey(buyer);
    }

    public UmsMember loginFromDb(UmsMember umsMember) {

        List<UmsMember> umsMembers = umsMemberMapper.select(umsMember);
        if(umsMembers.size()>0){
            return umsMembers.get(0);
        }

        return null;

    }

}
