package com.aiwsport.web.controller;

import com.aiwsport.core.StepServerException;
import com.aiwsport.core.StepServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.service.StepService;
import com.aiwsport.web.utlis.AES;
import com.aiwsport.web.utlis.HttpRequestor;
import com.aiwsport.web.verify.ParamObjVerify;
import com.aiwsport.web.verify.ParamVerify;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.*;

/**
 * 服务操作
 *
 * @author yangjian
 */
@RestController
public class ServerController {

    @Autowired
    private StepService stepService;

    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/step/onlogin.json")
    public String onlogin(@ParamVerify(isNotBlank = true)String code) {
        String appid = "wx169ddfe67114165d";
        String secret = "a9cb222c3a61f6ec58376d6c2853b015";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=";
        String url0 = "&grant_type=authorization_code";
        String userInfo = "";
        JSONObject jsonObject = null;
        try {
            userInfo = new HttpRequestor().doGet(url+code+url0);    //url+code+url0
            System.out.println(userInfo);
            jsonObject = JSONObject.parseObject(userInfo);
            User user = stepService.login(jsonObject);
            jsonObject.put("coinnum", user.getCoinnum()+"");
            jsonObject.put("userid", user.getId()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(jsonObject);
    }

    @RequestMapping("/step/decrypt.json")
    public ResultMsg decrypt(String encryptedData, String iv, @ParamVerify(isNotBlank = true) String sessionKey,
                             @ParamVerify(isNotBlank = true, isNumber = true) String userId, String token,Integer days) throws Exception{
        int toDayStep = stepService.decrypt(encryptedData, iv, sessionKey, Integer.parseInt(userId), token, days);
        if (toDayStep == -1) {
            return new ResultMsg(false, 403,"系统有点忙, 请刷新下");
        }

        return new ResultMsg("decryptOK", toDayStep);
    }

    @RequestMapping("/step/change_coin.json")
    public String changeCoin(@ParamVerify(isNotBlank = true, isNumber = true) String step,
                             @ParamVerify(isNotBlank = true) String openId,
                             @ParamVerify(isNotBlank = true, isNumber = true) String userId) throws Exception{
        User user = stepService.changeCoin(step, openId, userId);
        return JSONObject.toJSONString(user);
    }

    @RequestMapping("/step/get_address.json")
    public ResultMsg getAddress(@ParamVerify(isNotBlank = true, isNumber = true) String userId) throws Exception{
        Address address = stepService.getAddress(userId);
        if (address == null) {
            address = new Address();
        }

        return new ResultMsg("getAddressOK", address);
    }

    @RequestMapping("/step/get_user.json")
    public ResultMsg getUser(@ParamVerify(isNotBlank = true, isNumber = true) String userId) throws Exception{
        User user = stepService.getUser(userId);
        if (user == null) {
            user = new User();
        }

        return new ResultMsg("getUserOK", user);
    }

    @RequestMapping("/step/get_good.json")
    public ResultMsg getGood(@ParamVerify(isNotBlank = true, isNumber = true) String goodId) throws Exception{
        Goods goods = stepService.getGood(goodId);
        if (goods == null) {
            goods = new Goods();
        }

        return new ResultMsg("getGoodOK", goods);
    }

    @RequestMapping("/step/get_goods_by_type.json")
    public ResultMsg getGoodsByType(@ParamVerify(isNotBlank = true, isNumber = true) String type) throws Exception{
        List<Goods> goods = stepService.getGoodsByType(type);
        if (goods == null) {
            goods = new ArrayList<Goods>();
        }
        return new ResultMsg("getGoodOK", goods);
    }

    @RequestMapping("/step/get_good_change_log.json")
    public ResultMsg getGoodChangeLog(@ParamVerify(isNotBlank = true, isNumber = true) String goodId) throws Exception{
        List<QueryGoodChangeShow> queryGoodChangeShows = stepService.getGoodChangeLog(goodId);
        return new ResultMsg("getGoodChangLogOK", queryGoodChangeShows);
    }

    @RequestMapping("/step/get_user_change_log.json")
    public ResultMsg getUserChangeLog(@ParamVerify(isNotBlank = true, isNumber = true) String userId) throws Exception{
        List<QueryGoodChangeShow> queryGoodChangeShows = stepService.getUserChangeLog(userId);
        return new ResultMsg("getUserChangLogOK", queryGoodChangeShows);
    }

    @RequestMapping("/step/save_address.json")
    public ResultMsg saveAddress(String addressId, @ParamVerify(isNotBlank = true, isNumber = true) String userId, String addressInfo, String telNum, String userName) throws Exception{
        int id = 0;
        try{
            if (StringUtils.isNotBlank(addressId)) {
                id = stepService.updateAddress(addressId, userId, addressInfo, telNum, userName);
            } else {
                id = stepService.createAddress(userId, addressInfo, telNum, userName);
            }
        } catch (Exception e) {
            logger.error("save_address is error", e);
        }

        return new ResultMsg("保存收货地址id成功", id == 0 ? "系统异常,请重试" : id);
    }

    @RequestMapping("/step/change_good.json")
    public ResultMsg changeGood(@ParamVerify(isNotBlank = true, isNumber = true) String userId,
                                @ParamVerify(isNotBlank = true, isNumber = true) String goodId) throws Exception{
        return stepService.changeGood(userId, goodId);
    }

    public ResultMsg createActive(String userId) throws Exception {
        int id = 0;
        try{
            id = stepService.createActive(userId);
        } catch (Exception e) {
            logger.error("save_address is error", e);
        }

        return new ResultMsg("报名成功", id == 0 ? "系统异常,请重试" : id);
    }

    public ResultMsg zanActive(Integer userId, Integer zanUserId) throws Exception {
        return stepService.zanActive(userId, zanUserId);
    }

    public ResultMsg getActiveTop() throws Exception {
        List<Activestep> activesteps = stepService.getActiveTop();
        return new ResultMsg("getActiveTopOk", activesteps);
    }

    public ResultMsg getActivext(Integer userId) throws Exception {
        List<Activext> activexts = stepService.getActivext(userId);
        return new ResultMsg("getActivextOk", activexts);
    }


    @RequestMapping("/test.json")
    public ResultMsg test() throws Exception{
        return new ResultMsg("服务启动成功", 9276);
    }
}
