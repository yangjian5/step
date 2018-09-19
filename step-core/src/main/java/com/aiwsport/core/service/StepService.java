package com.aiwsport.core.service;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.enums.GoodChangeType;
import com.aiwsport.core.mapper.*;
import com.aiwsport.core.utils.AES;
import com.aiwsport.core.utils.CommonUtil;
import com.aiwsport.core.utils.DataTypeUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
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

    private static Logger logger = LogManager.getLogger();

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
        int newCoinNum = Integer.parseInt(step);
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

    public Integer decrypt(String encryptedData, String iv, String sessionKey,Integer userId, String token,Integer days){
        int toDayStep = CommonUtil.decrypt(encryptedData, iv, sessionKey, token, days);

        if (toDayStep == -1) {
            return toDayStep;
        }

        List<StepChangeLog> stepChangeLogs = null;
        try {
            String aa = DataTypeUtils.formatCurDateTime_yyyy_mm_dd()+" 00:00:00";
            String bb = DataTypeUtils.formatCurDateTime();
            stepChangeLogs = stepChangeLogMapper.selectAByUserIdToday(userId, DataTypeUtils.formatCurDateTime_yyyy_mm_dd()+" 00:00:00", DataTypeUtils.formatCurDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (stepChangeLogs == null) {
            return toDayStep;
        }

        if (stepChangeLogs.size() < 1) {
            return toDayStep;
        }

        for (StepChangeLog stepChangeLog : stepChangeLogs) {
            toDayStep = toDayStep - stepChangeLog.getStepnum();
        }

        return toDayStep;
    }


    public int createAddress(String userId, String addressInfo, String telNum, String userName){
        Address address = addressMapper.selectByUserId(Integer.parseInt(userId));
        if (address != null) {
            return 0;
        }

        Address adderss = new Address();
        adderss.setUserid(Integer.parseInt(userId));
        adderss.setAddressinfo(addressInfo);
        adderss.setTelnum(telNum);
        adderss.setUsername(userName);
        return addressMapper.insert(adderss);
    }

    public Address getAddress(String userId){
        return addressMapper.selectByUserId(Integer.parseInt(userId));
    }

    public User getUser(String userId){
        return userMapper.selectByPrimaryKey(Integer.parseInt(userId));
    }

    public Goods getGood(String goodId){
        return goodsMapper.selectByPrimaryKey(Integer.parseInt(goodId));
    }

    public List<Goods> getGoodsByType(String type){
        return goodsMapper.selectByType(type);
    }

    public List<QueryGoodChangeShow> getUserChangeLog(String userId){
        List<GoodChangeLog> goodChangeLogs = goodChangeLogMapper.selectByUserId(Integer.parseInt(userId));
        return buildChangeLog(goodChangeLogs);
    }

    public List<QueryGoodChangeShow> getGoodChangeLog(String goodId){
        List<GoodChangeLog> goodChangeLogs = goodChangeLogMapper.selectByGoodId(Integer.parseInt(goodId));
        return buildChangeLog(goodChangeLogs);
    }

    private List<QueryGoodChangeShow> buildChangeLog(List<GoodChangeLog> goodChangeLogs) {
        List<QueryGoodChangeShow> queryGoodChangeShows = new ArrayList<QueryGoodChangeShow>();

        if (goodChangeLogs == null) {
            return queryGoodChangeShows;
        }

        for (GoodChangeLog goodChangeLog : goodChangeLogs) {
            QueryGoodChangeShow queryGoodChangeShow = new QueryGoodChangeShow();
            queryGoodChangeShow.setGoodid(goodChangeLog.getGoodid());
            queryGoodChangeShow.setCreatetime(goodChangeLog.getCreatetime().split(" ")[0]);
            queryGoodChangeShow.setUserid(goodChangeLog.getUserid());
            if (StringUtils.isNotBlank(goodChangeLog.getStatus())) {
                switch (goodChangeLog.getStatus()){
                    case "1":
                        queryGoodChangeShow.setStatus("发货");
                        break;
                    case "2":
                        queryGoodChangeShow.setStatus("未发货");
                        break;
                    case "3":
                        queryGoodChangeShow.setStatus("已完成");
                        break;
                    default:
                        break;
                }
            }
            User user = userMapper.selectByPrimaryKey(goodChangeLog.getUserid());
            if (user != null) {
                queryGoodChangeShow.setNickname(user.getNickname());
            }

            Goods goods = goodsMapper.selectByPrimaryKey(goodChangeLog.getGoodid());
            if (goods != null) {
                queryGoodChangeShow.setGoodName(goods.getGoodName());
                queryGoodChangeShows.add(queryGoodChangeShow);
            }
        }
        return queryGoodChangeShows;
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

            // 是否添加了地址
            Address address = addressMapper.selectByUserId(Integer.parseInt(userId));
            if (address == null) {
                return new ResultMsg(false, 403,"请先添加地址 我的->地址管理");
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
            goodChangeLog.setStatus(String.valueOf(GoodChangeType.notSend.getIndex()));
            goodChangeLogMapper.insert(goodChangeLog);
        } catch (Exception e) {
            logger.error("兑换失败,重试一下或请联系客服", e);
            return new ResultMsg(false, 403,"兑换失败,重试一下或请联系客服");
        }

        return new ResultMsg("兑换成功,在我的兑换中查看物品寄送进", 1);
    }
}
