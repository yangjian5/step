package com.aiwsport.web.controller;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.Address;
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
            jsonObject.put("coinNum", user.getCoinnum()+"");
            jsonObject.put("userId", user.getId()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(jsonObject);
    }

    @RequestMapping("/decrypt.json")
    public String decrypt(String encryptedData, String iv, String sessionKey, String token,Integer days) throws Exception{
        JSONObject flagObj = null;
        try {
            byte[] resultByte  = AES.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey),
                    Base64.decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                JSONObject stepJson = JSONObject.parseObject(new String(resultByte, "UTF-8"));
                JSONArray stepInfoList =  (JSONArray) stepJson.get("stepInfoList");

                if(stepInfoList.size()>0){
                    flagObj = (JSONObject)stepInfoList.get(0);
                    for(int i=0;i<stepInfoList.size();i++){
                        // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        JSONObject job = stepInfoList.getJSONObject(i);
                        if ((Integer)job.get("timestamp") > (Integer)flagObj.get("timestamp")) {
                            flagObj = job;
                        }
                    }
                }
            }else{
                logger.info("解密失败");
            }
        }catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Object toDayStep = flagObj.get("step");
        return toDayStep == null ? "fail" : toDayStep.toString();
    }

    @RequestMapping("/change_coin.json")
    public String changeCoin(String step, String openId, String userId) throws Exception{
        User user = stepService.changeCoin(step, openId, userId);
        return JSONObject.toJSONString(user);
    }

    @RequestMapping("/save_address.json")
    public ResultMsg saveAddress(String addressId, String userId, String addressInfo, String telNum, String userName) throws Exception{
        int id = 0;
        if (StringUtils.isNotBlank(addressId)) {
            id = stepService.updateAddress(addressId, userId, addressInfo, telNum, userName);
        } else {
            id = stepService.createAddress(userId, addressInfo, telNum, userName);
        }

        return new ResultMsg("保存收货地址成功", id);
    }

    @RequestMapping("/test.json")
    public ResultMsg test() throws Exception{
        return new ResultMsg("服务启动成功", 9276);
    }
}
