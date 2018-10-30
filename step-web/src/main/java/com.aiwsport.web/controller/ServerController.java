package com.aiwsport.web.controller;

import com.aiwsport.core.StepServerException;
import com.aiwsport.core.StepServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.service.StepService;
import com.aiwsport.web.utlis.AES;
import com.aiwsport.web.utlis.HttpRequestor;
import com.aiwsport.web.utlis.ImageUtils;
import com.aiwsport.web.utlis.ParseUrl;
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
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

    @RequestMapping(value = "/onlogin.json")
    public JSONObject onlogin(@ParamVerify(isNotBlank = true)String code, String province,
                          String avatarUrl, String nickName, String country, String city, String gender) {
        String url1 = "https://api.weixin.qq.com/sns/jscode2session?appid=wx169ddfe67114165d&secret=e26e1b29d8fc04e461d3277c919100aa&js_code=";
        String url2 = "&grant_type=authorization_code";
        String userInfo = "";
        JSONObject jsonObject = null;
        try {
            long start = System.currentTimeMillis();
//            userInfo = new HttpRequestor().doGet(url1+code+url2);
            userInfo = ParseUrl.getDataFromUrl((url1+code+url2));
            System.out.println("------userInfo------" + userInfo);
            long end = System.currentTimeMillis();
            System.out.println("------cost------" + (end-start));
            jsonObject = JSONObject.parseObject(userInfo);
//            nickName = nickName.replaceAll("[^\\u0000-\\uFFFF]", "?");
            User user = stepService.login(jsonObject, province, avatarUrl, nickName, country, city, gender);
            jsonObject.put("coinnum", user.getCoinnum()+"");
            jsonObject.put("userid", user.getId()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @RequestMapping("/step/decrypt.json")
    public ResultMsg decrypt(String encryptedData, String iv, @ParamVerify(isNotBlank = true) String sessionKey,
                             @ParamVerify(isNotBlank = true, isNumber = true) String userId, String token,Integer days) throws Exception{
        Map<String, String> dayStepMap = stepService.decrypt(encryptedData, iv, sessionKey, Integer.parseInt(userId), token, days);

        if (dayStepMap == null) {
            return new ResultMsg(false, 403,"系统有点忙, 请刷新下");
        }

        return new ResultMsg("decryptOK", dayStepMap);
    }

    @RequestMapping("/step/change_coin.json")
    public String changeCoin(@ParamVerify(isNotBlank = true, isNumber = true) String step,
                             @ParamVerify(isNotBlank = true, isNumber = true) String jiaChenStep,
                             @ParamVerify(isNotBlank = true, isNumber = true) String rewardStep,
                             @ParamVerify(isNotBlank = true) String openId,
                             @ParamVerify(isNotBlank = true, isNumber = true) String userId) throws Exception{
        User user = stepService.changeCoin(step, jiaChenStep, rewardStep, openId, userId);
        return JSONObject.toJSONString(user);
    }

    @RequestMapping("/step/get_address.json")
    public ResultMsg getAddress(@ParamVerify(isNotBlank = true, isNumber = true) String userId) throws Exception{
        Address address = stepService.getAddress(userId);
        if (address == null) {
            return new ResultMsg(false, 403,"getAddressError");
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
    public ResultMsg saveAddress(String addressId, @ParamVerify(isNotBlank = true, isNumber = true) String userId,
                                 String addressInfo, String telNum, String userName, String cityInfo) throws Exception{
        int id = 0;
        try{
            if (StringUtils.isNotBlank(addressId)) {
                id = stepService.updateAddress(addressId, userId, addressInfo, telNum, userName, cityInfo);
            } else {
                id = stepService.createAddress(userId, addressInfo, telNum, userName, cityInfo);
            }
        } catch (Exception e) {
            logger.error("save_address is error", e);
        }

        return new ResultMsg("saveAddressOk", id == 0 ? "系统异常,请检查地址是否含有特殊字符~" : "保存收货地址成功");
    }

    @RequestMapping("/step/change_good.json")
    public ResultMsg changeGood(@ParamVerify(isNotBlank = true, isNumber = true) String userId,
                                @ParamVerify(isNotBlank = true, isNumber = true) String goodId) throws Exception{
        return stepService.changeGood(userId, goodId);
    }


    @RequestMapping("/step/is_join_active.json")
    public ResultMsg isJoinActive(String userId, String enterType) throws Exception {
        int flag = 0;
        try{
            flag = stepService.isJoinActive(userId, enterType);
        } catch (Exception e) {
            logger.error("create_active is error", e);
        }

        return new ResultMsg("isJoinActiveOk", flag);
    }

    @RequestMapping("/step/create_active.json")
    public ResultMsg createActive(String userId, String enterType) throws Exception {
        int id = 0;
        try{
            return stepService.createActive(userId, enterType);
        } catch (Exception e) {
            logger.error("createActive is error", e);
        }

        return new ResultMsg("createActiveError", "系统异常,请重试");
    }

    @RequestMapping("/step/zan_active.json")
    public ResultMsg zanActive(Integer userId, Integer zanUserId) throws Exception {
        return stepService.zanActive(userId, zanUserId);
    }

    @RequestMapping("/step/get_active_top.json")
    public ResultMsg getActiveTop(String userId) throws Exception {
        List<QueryActivestepShow> activesteps = stepService.getActiveTop(userId);
        return new ResultMsg("getActiveTopOk", activesteps);
    }

    public ResultMsg getActivext(Integer userId) throws Exception {
        List<Activext> activexts = stepService.getActivext(userId);
        return new ResultMsg("getActivextOk", activexts);
    }

    @RequestMapping("/step/get_active_info.json")
    public ResultMsg getActiveInfo(String enterType) throws Exception {
        List<Activestep> activesteps = stepService.getActiveInfo(enterType);
        int activeCount = activesteps.size();

        JSONObject jsonObject = new JSONObject();
        if (activesteps == null || activeCount < 1) {
            jsonObject.put("joinUserCount", 100);
            jsonObject.put("reward", 0);
        } else {
            jsonObject.put("joinUserCount", activeCount + 100);
            int reward = 0;
            switch (enterType){
                case "1":// 10000步
                    reward = activeCount * 30 + 100;
                    break;
                case "2":// 15000步
                    reward = activeCount * 50 + 200;
                    break;
                case "3":// 20000步
                    reward = activeCount * 70 + 300;
                    break;
                case "4":// 全区
                    reward = activeCount * 10 + 2000;
                    break;
                case "5":// 早起
                    reward = activeCount * 10 + 100;
                    break;
                default:
                    break;
            }
            jsonObject.put("reward", reward);
        }
        return new ResultMsg("getActiveInfoOk", jsonObject);
    }

    @RequestMapping("/step/do_sign.json")
    public ResultMsg doSign(String userId) throws Exception{
        int isScuess = stepService.doSign(Integer.parseInt(userId));
        if (isScuess == 0) {
            return new ResultMsg(false, 403,"打卡失败请重试,或联系客服");
        }

        if (isScuess == 2) {
            return new ResultMsg(false, 403,"已经打卡,奖励k随后发放");
        }

        return new ResultMsg("doSignOk", "打卡成功");
    }

    @RequestMapping("/step/add_share.json")
    public ResultMsg addShare(Integer mUserId, Integer sUserId) throws Exception{
        if (mUserId == sUserId) {
            return new ResultMsg(false, 403,"不能邀请自己");
        }

        int isScuess = stepService.addShare(mUserId, sUserId);
        if (isScuess == 0) {
            return new ResultMsg(false, 403,"好友加成失败请重试,或联系客服");
        }

        return new ResultMsg("addShareOk", "好友邀请成功");
    }

    @RequestMapping("/step/get_share_user.json")
    public ResultMsg getShareUser(Integer mUserId) throws Exception{
        List<String> userUrls = stepService.getShareUsersByMuserId(mUserId);
        return new ResultMsg("getShareUserOk", userUrls);
    }

    @RequestMapping("/step/add_show_info.json")
    public ResultMsg addShowInfo(Integer userId, String title, String showDesc) throws Exception{
        Activestep activestep = stepService.getActivestepFor4(userId);

        if (StringUtils.isNotBlank(title)) {
            activestep.setTitle(title);
        } else {
            activestep.setTitle("系统补充:主人还未添加标题~");
        }

        if (StringUtils.isNotBlank(showDesc)) {
            activestep.setShowdesc(showDesc);
        } else {
            activestep.setShowdesc("系统补充:主人还没有填写自我展示~");
        }

        int isScuess = stepService.updateActivestep(activestep);
        if (isScuess == 0) {
            return new ResultMsg(false, 403,"添加个人展示失败,请联系客服");
        }

        return new ResultMsg("addShowInfoOk", "添加个人展示成功");
    }

    @RequestMapping("/step/get_step_for_4.json")
    public ResultMsg getStepFor4(Integer userId) throws Exception{
        Activestep activestep = stepService.getActivestepFor4(userId);
        return new ResultMsg("getStepFor4Ok", activestep);
    }

    @RequestMapping("/test.json")
    public ResultMsg test() throws Exception{
        return new ResultMsg("服务启动成功", 9276);
    }

//    @RequestMapping(value = "/build_img.json",produces = MediaType.IMAGE_JPEG_VALUE)
//    @ResponseBody
//    public byte[] getImage(Integer daySumStep, String userId) throws Exception {
//        if ("0".equals(userId)) {
//            return null;
//        }
//
//        User user = stepService.getUser(userId);
//        BufferedImage bufferedImage = ImageUtils.buildImg(user.getNickname()+" 今日行走 "+daySumStep+" 步",
//                user.getAvatarurl(),
//                ResourceUtils.getURL("classpath:").getPath()+"/img/paobu.jpg");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(bufferedImage, "jpeg", baos);
//        baos.flush();
//        byte[] imageInByte = baos.toByteArray();
//        baos.close();
//
//        return imageInByte;
//    }
}
