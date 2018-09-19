package com.aiwsport.web.controller;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.Address;
import com.aiwsport.core.entity.Goods;
import com.aiwsport.core.entity.QueryGoodChangeShow;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.service.StepService;
import com.aiwsport.web.utlis.AES;
import com.aiwsport.web.utlis.HttpRequestor;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/onlogin.json")
    public String onlogin(String code) {
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

    @RequestMapping("/decrypt.json")
    public ResultMsg decrypt(String encryptedData, String iv, String sessionKey, String userId, String token,Integer days) throws Exception{
        int toDayStep = stepService.decrypt(encryptedData, iv, sessionKey, Integer.parseInt(userId), token, days);
        if (toDayStep == -1) {
            return new ResultMsg(false, 403,"系统有点忙, 请刷新下");
        }

        return new ResultMsg("decryptOK", toDayStep);
    }

    @RequestMapping("/change_coin.json")
    public String changeCoin(String step, String openId, String userId) throws Exception{
        User user = stepService.changeCoin(step, openId, userId);
        return JSONObject.toJSONString(user);
    }

    @RequestMapping("/get_address.json")
    public ResultMsg getAddress(String userId) throws Exception{
        Address address = stepService.getAddress(userId);
        if (address == null) {
            address = new Address();
        }

        return new ResultMsg("getAddressOK", address);
    }

    @RequestMapping("/get_user.json")
    public ResultMsg getUser(String userId) throws Exception{
        User user = stepService.getUser(userId);
        if (user == null) {
            user = new User();
        }

        return new ResultMsg("getUserOK", user);
    }

    @RequestMapping("/get_good.json")
    public ResultMsg getGood(String goodId) throws Exception{
        Goods goods = stepService.getGood(goodId);
        if (goods == null) {
            goods = new Goods();
        }

        return new ResultMsg("getGoodOK", goods);
    }

    @RequestMapping("/get_goods_by_type.json")
    public ResultMsg getGoodsByType(String type) throws Exception{
        List<Goods> goods = stepService.getGoodsByType(type);
        if (goods == null) {
            goods = new ArrayList<Goods>();
        }
        return new ResultMsg("getGoodOK", goods);
    }

    @RequestMapping("/get_good_change_log.json")
    public ResultMsg getGoodChangeLog(String goodId) throws Exception{
        List<QueryGoodChangeShow> queryGoodChangeShows = stepService.getGoodChangeLog(goodId);
        return new ResultMsg("getGoodChangLogOK", queryGoodChangeShows);
    }

    @RequestMapping("/get_user_change_log.json")
    public ResultMsg getUserChangeLog(String userId) throws Exception{
        List<QueryGoodChangeShow> queryGoodChangeShows = stepService.getUserChangeLog(userId);
        return new ResultMsg("getUserChangLogOK", queryGoodChangeShows);
    }

    @RequestMapping("/save_address.json")
    public ResultMsg saveAddress(String addressId, String userId, String addressInfo, String telNum, String userName) throws Exception{
        int id = 0;
        if (StringUtils.isNotBlank(addressId)) {
            id = stepService.updateAddress(addressId, userId, addressInfo, telNum, userName);
        } else {
            id = stepService.createAddress(userId, addressInfo, telNum, userName);
        }

        return new ResultMsg("保存收货地址id成功", id == 0 ? "系统异常,请重试" : id);
    }

    @RequestMapping("/change_good.json")
    public ResultMsg changeGood(String userId, String goodId) throws Exception{
        return stepService.changeGood(userId, goodId);
    }

    @RequestMapping("/test.json")
    public ResultMsg test() throws Exception{
        return new ResultMsg("服务启动成功", 9276);
    }
}
