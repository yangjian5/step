package com.aiwsport.core.service;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.Address;
import com.aiwsport.core.entity.Goods;
import com.aiwsport.core.entity.StepChangeLog;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.mapper.AddressMapper;
import com.aiwsport.core.mapper.GoodsMapper;
import com.aiwsport.core.mapper.StepChangeLogMapper;
import com.aiwsport.core.mapper.UserMapper;
import com.aiwsport.core.utils.DataTypeUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangjian9 on 2018/9/10.
 */
@Service
public class StepService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StepChangeLogMapper stepChangeLogMapper;

    @Autowired
    private GoodsMapper goodsMapper;


    @Autowired
    private AddressMapper addressMapper;

    public User login(JSONObject userInfo) {
        // 获取openid {"session_key":"VEX3GZ5cG31i1+DeLyqHyg==","openid":"ov8p35OEtLxO7nILiHq6rmBCpkv4"}
        String openId = userInfo.getString("openid");
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            // 创建用户
            User userNew = new User();
            userNew.setOpenid(openId);
            userNew.setCoinnum(0);
            userMapper.insert(userNew);
            return userNew;
        }
        return user;
    }

    public User changeCoin(String step, String openId, String userId) throws Exception{
        User user = userMapper.getByOpenId(openId);
        int newCoinNum = (int) Math.round(Integer.parseInt(step) * 0.003);
        user.setCoinnum(user.getCoinnum()+newCoinNum);
        userMapper.updateByPrimaryKey(user);

        StepChangeLog stepChangeLog = new StepChangeLog();
        stepChangeLog.setUserid(Integer.parseInt(userId));
        stepChangeLog.setCoinnum(newCoinNum);
        stepChangeLog.setStepnum(Integer.parseInt(step));
        stepChangeLog.setCreatetime(DataTypeUtils.formatCurDateTime());
        stepChangeLogMapper.insert(stepChangeLog);
        return user;
    }

    public int createAddress(String userId, String addressInfo, String telNum, String userName){
        Address adderss = new Address();
        adderss.setUserid(Integer.parseInt(userId));
        adderss.setAddressinfo(addressInfo);
        adderss.setTelnum(telNum);
        adderss.setUsername(userName);
        return addressMapper.insert(adderss);
    }

    public int updateAddress(String addressId, String userId, String addressInfo, String telNum, String userName){
        Address adderss = new Address();
        adderss.setId(Integer.parseInt(addressId));
        adderss.setUserid(Integer.parseInt(userId));
        adderss.setAddressinfo(addressInfo);
        adderss.setTelnum(telNum);
        adderss.setUsername(userName);
        return addressMapper.updateByPrimaryKey(adderss);
    }

    public ResultMsg changeGood(String userId, String goodId){
        // 增加记录



        Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(goodId));
        if (goods.getCount() < 1) {
            return new ResultMsg(false, 403,"火币不足,去邀请好友");
        }

        // 校验币数

        User user = userMapper.selectByPrimaryKey(Integer.parseInt(userId));
        if (user.getCoinnum() < goods.getSalecoin()) {
            return new ResultMsg(false, 403,"火币不足,去邀请好友");
        }

        // 扣减币数
        goods.setCount(goods.getCount()-1);
        user.setCoinnum(user.getCoinnum()-goods.getSalecoin());


        return new ResultMsg("兑换成功成功", 0);
    }
}
