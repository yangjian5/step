package com.aiwsport.core.service;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.enums.GoodChangeType;
import com.aiwsport.core.mapper.*;
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
    private GoodChangeLogMapper goodChangeLogMapper;


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
        try {
            Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(goodId));
            if (goods.getCount() < 1) {
                return new ResultMsg(false, 403,"库存有限,我们正在加紧备货");
            }

            // 校验币数
            User user = userMapper.selectByPrimaryKey(Integer.parseInt(userId));
            if (user.getCoinnum() < goods.getSalecoin()) {
                return new ResultMsg(false, 403,"火币不足,去邀请好友");
            }

            // 扣减库存
            goods.setCount(goods.getCount()-1);
            goodsMapper.updateByPrimaryKey(goods);

            // 扣减币数
            user.setCoinnum(user.getCoinnum()-goods.getSalecoin());
            userMapper.updateByPrimaryKey(user);

            // 增加记录
            GoodChangeLog goodChangeLog = new GoodChangeLog();
            goodChangeLog.setUserid(Integer.parseInt(userId));
            goodChangeLog.setCreatetime(DataTypeUtils.formatCurDateTime());
            goodChangeLog.setGoodid(Integer.parseInt(goodId));
            goodChangeLog.setStatus(GoodChangeType.sended.name());
            goodChangeLogMapper.insert(goodChangeLog);
        } catch (Exception e) {

            return new ResultMsg(false, 403,"兑换失败,重试一下或请联系客服");
        }

        return new ResultMsg("兑换成功成功", 0);
    }
}
