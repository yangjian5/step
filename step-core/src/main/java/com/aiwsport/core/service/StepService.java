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
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ActivestepMapper activestepMapper;

    @Autowired
    private ActivextMapper activextMapper;

    @Autowired
    private ActivedataMapper activedataMapper;

    @Autowired
    private ShareMapper shareMapper ;

    @Autowired
    private CommentMapper commentMapper ;

    @Autowired
    private ActivelogMapper activelogMapper ;

    private static Logger logger = LogManager.getLogger();

    public User login(JSONObject userInfo, String province,
                      String avatarUrl, String nickName, String country, String city, String gender) {
        // 获取openid {"session_key":"VEX3GZ5cG31i1+DeLyqHyg==","openid":"ov8p35OEtLxO7nILiHq6rmBCpkv4"}
        String openId = userInfo.getString("openid");
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            // 创建用户
            User userNew = new User();
            userNew.setOpenid(openId);
            userNew.setCoinnum(0);
            userNew.setAvatarurl(avatarUrl);
            userNew.setCity(city);
            userNew.setGender(gender);
            userNew.setNickname(nickName);
            userNew.setProvince(province);
            userMapper.insert(userNew);
            return userNew;
        }
        return user;
    }

    @Transactional
    public User changeCoin(String step, String jiaChenStep, String rewardStep, String openId, String userId) throws Exception{
        User user = userMapper.getByOpenId(openId);

        int step1 = Integer.parseInt(step);
        int jiaChenStep1 = Integer.parseInt(jiaChenStep);
        int rewardStep1 = Integer.parseInt(rewardStep);

        int sumStep = step1 + jiaChenStep1 + rewardStep1;

        BigDecimal newCoinNum = BigDecimal.valueOf(sumStep).multiply(BigDecimal.valueOf(0.0005));

        BigDecimal sumCoin = newCoinNum.add(BigDecimal.valueOf(user.getCoinnum()));

        double sumCoinResult = sumCoin.setScale(4,  RoundingMode.HALF_UP).doubleValue();

        user.setCoinnum(sumCoinResult);
        userMapper.updateByPrimaryKey(user);

        StepChangeLog stepChangeLog = new StepChangeLog();
        stepChangeLog.setUserid(Integer.parseInt(userId));
        stepChangeLog.setCoinnum(newCoinNum.doubleValue());
        stepChangeLog.setStepnum(step1);
        stepChangeLog.setJiachenstep(jiaChenStep1);
        stepChangeLog.setRewardstep(rewardStep1);
        stepChangeLog.setCreatetime(DataTypeUtils.formatCurDateTime());
        stepChangeLogMapper.insert(stepChangeLog);

        List<Activestep> activesteps = activestepMapper.selectByUserId(Integer.parseInt(userId));
        if (activesteps != null && activesteps.size() > 0) {
            for (Activestep activestep : activesteps) {
                Activedata activedata = activedataMapper.selectByActiveStepId(activestep.getId());
                if ("4".equals(activestep.getType())) {
                    activedata.setSumstep(activedata.getSumstep()+sumStep);
                    activedataMapper.updateByPrimaryKey(activedata);
                }

                if ("1,2,3".contains(activestep.getType())) {
                    activedata.setDaystep(activedata.getDaystep()+sumStep);
                    activedataMapper.updateByPrimaryKey(activedata);
                }

            }
        }

        // 奖励步数
        List<Share> addRewardShares = shareMapper.selectByMuserAndIsAddReward(Integer.parseInt(userId));
        // 删除首次奖励
        for (Share addRewardShare : addRewardShares) {
            addRewardShare.setIsaddreward("0");
            shareMapper.updateByPrimaryKey(addRewardShare);
        }

        return user;
    }

    public Map<String, String> decrypt(String encryptedData, String iv, String sessionKey, Integer userId, String token, Integer days){
        int toDayStep = CommonUtil.decrypt(encryptedData, iv, sessionKey, token, days);

        if (toDayStep == -1) {
            return null;
        }

        Map<String, String> dayStepMap = new HashMap<String, String>();
        dayStepMap.put("daySumStep", toDayStep+"");


        if (toDayStep > 30000) {
            toDayStep = 30000;
        }

        List<StepChangeLog> stepChangeLogs = null;
        try {
            stepChangeLogs = stepChangeLogMapper.selectAByUserIdToday(userId, DataTypeUtils.formatCurDateTime_yyyy_mm_dd()+" 00:00:00", DataTypeUtils.formatCurDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (stepChangeLogs == null) {
            dayStepMap.put("toDayStep", toDayStep+"");
            buildStep(userId, dayStepMap, toDayStep);
            return dayStepMap;
        }

        if (stepChangeLogs.size() < 1) {
            dayStepMap.put("toDayStep", toDayStep+"");
            buildStep(userId, dayStepMap, toDayStep);
            return dayStepMap;
        }

        for (StepChangeLog stepChangeLog : stepChangeLogs) {
            toDayStep = toDayStep - stepChangeLog.getStepnum();
        }

        if (toDayStep < 0) {
            toDayStep = 0;
        }
        dayStepMap.put("toDayStep", toDayStep+"");
        buildStep(userId, dayStepMap, toDayStep);
        return dayStepMap;
    }

    private void buildStep (Integer mUserId, Map<String, String> dayStepMap, int toDayStep) {
        List<Share> JiaChenShares =  shareMapper.selectByMuser(mUserId);
        if (JiaChenShares == null) {
            dayStepMap.put("JiaChen", "0");
            dayStepMap.put("JiaChenStep", "0");
        } else {
            int count = JiaChenShares.size();

            BigDecimal JiaChen = BigDecimal.valueOf(0.02).multiply(BigDecimal.valueOf(count));

            BigDecimal JiaChenStep = JiaChen.multiply(BigDecimal.valueOf(toDayStep));

            dayStepMap.put("JiaChen", JiaChen.multiply(BigDecimal.valueOf(100)).intValue()+"");
            dayStepMap.put("JiaChenStep", JiaChenStep.intValue()+"");
        }

        // 奖励步数
        List<Share> addRewardShares = shareMapper.selectByMuserAndIsAddReward(mUserId);
        if (addRewardShares == null) {
            dayStepMap.put("rewardStep", "0");
        } else {
            dayStepMap.put("rewardStep", addRewardShares.size()*3000+"");
        }

        // 删除首次奖励
//        for (Share addRewardShare : addRewardShares) {
//            addRewardShare.setIsaddreward("0");
//            shareMapper.updateByPrimaryKey(addRewardShare);
//        }
    }


    public int createAddress(String userId, String addressInfo, String telNum, String userName, String cityInfo){
        Address address = addressMapper.selectByUserId(Integer.parseInt(userId));
        if (address != null) {
            return 0;
        }

        Address adderss = new Address();
        adderss.setUserid(Integer.parseInt(userId));
        adderss.setAddressinfo(addressInfo);
        adderss.setCityinfo(cityInfo);
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

    public int updateAddress(String addressId, String userId, String addressInfo, String telNum, String userName, String cityInfo){
        Address adderss = new Address();
        adderss.setId(Integer.parseInt(addressId));
        adderss.setUserid(Integer.parseInt(userId));
        adderss.setAddressinfo(addressInfo);
        adderss.setCityinfo(cityInfo);
        adderss.setTelnum(telNum);
        adderss.setUsername(userName);
        return addressMapper.updateByPrimaryKey(adderss);
    }

    @Transactional
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
            BigDecimal surplusCoin = BigDecimal.valueOf(user.getCoinnum()).subtract(BigDecimal.valueOf(goods.getSalecoin()));
            double surplus = surplusCoin.setScale(4,  RoundingMode.HALF_UP).doubleValue();
            user.setCoinnum(surplus);
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

    @Transactional
    public ResultMsg createActive(String userId, String type) throws Exception{
        String nowTimeyyyy_mm_dd = DataTypeUtils.formatCurDateTime_yyyy_mm_dd();
        boolean flag = DataTypeUtils.hourMinuteBetween(DataTypeUtils.formatCurDateTime(), nowTimeyyyy_mm_dd+" 22:00:00", nowTimeyyyy_mm_dd+" 23:59:59");
        if (flag) {// 时间段内不能报名
            return new ResultMsg("createActiveError", "请在每天的22点之前参与报名");
        }

        User user = userMapper.selectByPrimaryKey(Integer.parseInt(userId));
        Activestep activestep = activestepMapper.selectByUserIdAndType(Integer.parseInt(userId), type);
        if (activestep != null) {
            return new ResultMsg("createActiveError", "您已报名");
        }

        switch (type){
            case "1":// 10000步
                if (user.getCoinnum() < 30) {
                   return new ResultMsg("createActiveError", "能量不足");
                }
                BigDecimal surplusCoin = BigDecimal.valueOf(user.getCoinnum()).subtract(BigDecimal.valueOf(30));
                double surplus = surplusCoin.setScale(4,  RoundingMode.HALF_UP).doubleValue();
                user.setCoinnum(surplus);
                break;
            case "2":// 15000步
                if (user.getCoinnum() < 50) {
                    return new ResultMsg("createActiveError", "能量不足");
                }
                BigDecimal surplusCoin1 = BigDecimal.valueOf(user.getCoinnum()).subtract(BigDecimal.valueOf(50));
                double surplus1 = surplusCoin1.setScale(4,  RoundingMode.HALF_UP).doubleValue();
                user.setCoinnum(surplus1);
                break;
            case "3":// 20000步
                if (user.getCoinnum() < 70) {
                    return new ResultMsg("createActiveError", "能量不足");
                }
                BigDecimal surplusCoin2 = BigDecimal.valueOf(user.getCoinnum()).subtract(BigDecimal.valueOf(70));
                double surplus2 = surplusCoin2.setScale(4,  RoundingMode.HALF_UP).doubleValue();
                user.setCoinnum(surplus2);
                break;
            case "4":// 全区
                if (user.getCoinnum() < 10) {
                    return new ResultMsg("createActiveError", "能量不足");
                }
                BigDecimal surplusCoin3 = BigDecimal.valueOf(user.getCoinnum()).subtract(BigDecimal.valueOf(10));
                double surplus3 = surplusCoin3.setScale(4,  RoundingMode.HALF_UP).doubleValue();
                user.setCoinnum(surplus3);
                break;
            case "5":// 早起
                if (user.getCoinnum() < 10) {
                    return new ResultMsg("createActiveError", "能量不足");
                }
                BigDecimal surplusCoin4 = BigDecimal.valueOf(user.getCoinnum()).subtract(BigDecimal.valueOf(10));
                double surplus4 = surplusCoin4.setScale(4,  RoundingMode.HALF_UP).doubleValue();
                user.setCoinnum(surplus4);
                break;
            default:
                break;
        }

        Activestep newActivestep = new Activestep();
        newActivestep.setUserid(Integer.parseInt(userId));
        newActivestep.setStatus("1");
        newActivestep.setType(type);
        newActivestep.setCreatetime(DataTypeUtils.formatCurDateTime());
        String endTime = DataTypeUtils.formatTimeStamp_yyyy_mm_dd(DataTypeUtils.addOrMinusDay(DataTypeUtils.getCurrentDate(), 1));
        if ("4".equals(type)) {
            endTime = "2018-10-31";
            newActivestep.setShowdesc("系统补充:主人还没有填写自我展示~");
            newActivestep.setTitle("系统补充:主人还未添加标题~");
        }

        newActivestep.setEndtime(endTime+" 23:59:59");
        int activestepId = activestepMapper.insert(newActivestep);

        Activedata activedata = new Activedata();
        activedata.setActivesstepid(newActivestep.getId());
        activedata.setType(type);
        activedata.setStatus("1");
        activedata.setDaystep(0);
        activedata.setSumstep(0);
        activedata.setIssign("0");
        int activedataId = activedataMapper.insert(activedata);

        userMapper.updateByPrimaryKey(user);
        return new ResultMsg("createActiveOk", "报名成功");
    }

    public int isJoinActive(String userId, String type) throws Exception{
        String nowTimeyyyy_mm_dd = DataTypeUtils.formatCurDateTime_yyyy_mm_dd();
        boolean flag = DataTypeUtils.hourMinuteBetween(DataTypeUtils.formatCurDateTime(), nowTimeyyyy_mm_dd+" 22:00:00", nowTimeyyyy_mm_dd+" 23:59:59");
        if (flag) {// 时间段内不能报名
            return 4;
        }

        Activestep activestep = activestepMapper.selectByUserIdAndType(Integer.parseInt(userId), type);
        if (activestep == null) {
            return 0;
        }

        if ("5".equals(type)) {// 早起挑战赛
            boolean flag1 = DataTypeUtils.hourMinuteBetween(DataTypeUtils.formatCurDateTime(), nowTimeyyyy_mm_dd+" 04:00:00", nowTimeyyyy_mm_dd+" 06:00:00");
            if (flag1) {
                Activedata activedata = activedataMapper.selectByActiveStepId(activestep.getId());
                if ("0".equals(activedata.getIssign())) {
                    return 3;
                }
            }
        }

        return 1;
    }

    @Transactional
    public ResultMsg zanActive(Integer userId, Integer zanUserId) throws Exception{
        List<Activext> activexts = activextMapper.selectByUserAndZanUser(userId, zanUserId);
        if (activexts !=null && activexts.size()>0) {
            return new ResultMsg(false, 403,"您今天已经赞过他了,明天再来吧~");
        }

        Activext activext = new Activext();
        activext.setUserid(userId);
        activext.setZanuserid(zanUserId);
        activext.setCreatetime(DataTypeUtils.formatCurDateTime());
        activextMapper.insert(activext);

        Activestep activestep = activestepMapper.selectByUserIdAndType(userId, "4");
        Activedata activedata = activedataMapper.selectByActiveStepId(activestep.getId());
        activedata.setSumstep(activedata.getSumstep()+500);
        activedataMapper.updateByPrimaryKey(activedata);
        return new ResultMsg("+ 500步", 1);
    }

    public List<QueryActivestepShow> getActiveTop(String userId, String opType) throws Exception{
        List<Activedata> activedatas = new ArrayList<Activedata>();
        Activestep activestep = activestepMapper.selectByUserIdAndType(Integer.parseInt(userId), "4");
        int index = -1;
        if (activestep != null) {
            Activedata activedata = activedataMapper.selectByActiveStepId(activestep.getId());
            if (activedata != null) {
                index = activedataMapper.selectCount(activedata.getSumstep());
                activedatas.add(activedata);
            }
        }

        if (!"search".equals(opType)) {
            List<Activedata> allActivedatas = activedataMapper.selectTop("4");
            if (allActivedatas != null && allActivedatas.size() > 0) {
                activedatas = allActivedatas;
            }
        }


        List<QueryActivestepShow> queryActivestepShows = new ArrayList<QueryActivestepShow>();
        for (Activedata activedata : activedatas) {
            QueryActivestepShow queryActivestepShow = new QueryActivestepShow();
            Activestep singleActivestep = activestepMapper.selectByPrimaryKey(activedata.getActivesstepid());
            if (singleActivestep == null) {
                continue;
            }

            if (index != -1) {
                queryActivestepShow.setIndex(index);
            }

            queryActivestepShow.setUserid(singleActivestep.getUserid());
            queryActivestepShow.setShowtitle(singleActivestep.getTitle());
            User user = userMapper.selectByPrimaryKey(singleActivestep.getUserid());
            if (user == null) {
                continue;
            }

            queryActivestepShow.setNickname(user.getNickname());
            queryActivestepShow.setAvatarurl(user.getAvatarurl());
            queryActivestepShow.setSumstep(activedata.getSumstep());
            List<Activext> activexts = activextMapper.selectByUserId(singleActivestep.getUserid());
            if (activexts == null) {
                continue;
            }

            List<User> zanUserList = new ArrayList<User>();
            for (Activext activext : activexts) {
                User zanUser = userMapper.selectByPrimaryKey(activext.getZanuserid());
                zanUserList.add(zanUser);
            }

            if (zanUserList.size() > 0) {
                queryActivestepShow.setZanUser(zanUserList);
            }
            queryActivestepShows.add(queryActivestepShow);
        }

        return queryActivestepShows;
    }

    public List<Activext> getActivext(Integer userId) throws Exception{
        List<Activext> activexts = activextMapper.selectByUserId(userId);
        return activexts;
    }

    public List<Activestep> getActiveInfo(String type) throws Exception{
        List<Activestep> activesteps = activestepMapper.selectByType(type);
        return activesteps;
    }

    public int getActiveInfoCount(String type) throws Exception{
        return activestepMapper.selectByTypeCount(type);
    }

    public Activedata selectByActiveStepId(Integer activeStepId){
        return activedataMapper.selectByActiveStepId(activeStepId);
    }

    public int selectFinish(String type, Integer dayStep){
        return activedataMapper.selectFinishCount(type, dayStep);
    }

    public int selectFinish5Count(String type){
        return activedataMapper.selectFinish5Count(type);
    }

    public int doSign(Integer userId) throws Exception{
        boolean flag = DataTypeUtils.hourMinuteBetween(DataTypeUtils.formatCurDateTime(), DataTypeUtils.formatCurDateTime_yyyy_mm_dd()+" 04:00:00", DataTypeUtils.formatCurDateTime_yyyy_mm_dd()+" 06:00:00");
        if (!flag) {
            return 2;
        }

        Activestep activestep = activestepMapper.selectByUserIdAndType(userId, "5");
        Activedata activedata = activedataMapper.selectByActiveStepId(activestep.getId());
        activedata.setIssign("1");
        int isSuccess = activedataMapper.updateByPrimaryKey(activedata);
        return isSuccess;
    }

    public int addShare(Integer muserId, Integer suserId) throws Exception{
        int isSuccess = 0;
        Share share = shareMapper.selectBySuser(suserId);
        if (share != null) {
            return isSuccess;
        }

        Share addShare = new Share();
        addShare.setMuserid(muserId);
        addShare.setSuserid(suserId);
        addShare.setIsaddreward("1");
        addShare.setCreatetime(DataTypeUtils.formatCurDateTime());
        isSuccess = shareMapper.insert(addShare);
        return isSuccess;
    }

    public List<Share> getSharesByIsAddReward(Integer muserId) throws Exception{
        return shareMapper.selectByMuserAndIsAddReward(muserId);
    }

    public List<Share> getSharesByMuserId(Integer muserId) throws Exception{
        return shareMapper.selectByMuser(muserId);
    }

    public List<String> getShareUsersByMuserId(Integer muserId) throws Exception{
        List<Share> shares = shareMapper.selectByMuser(muserId);
        List<String> userUrls = new ArrayList<String>();

        for (int i=0; i<4; i++) {
            if (shares.size()-1 < i) {
                if (i==0) {
                    userUrls.add("/image/mei.png");
                } else if (i==1) {
                    userUrls.add("/image/peng.png");
                } else if (i==2) {
                    userUrls.add("/image/you.png");
                } else if (i==3) {
                    userUrls.add("/image/tanhao.png");
                }
            } else {
                Share share = shares.get(i);
                User user = userMapper.selectByPrimaryKey(share.getSuserid());
                if (user == null) {
                    userUrls.add("/image/touxiang.png");
                } else {
                    userUrls.add(user.getAvatarurl());
                }
            }
        }
        return userUrls;
    }

    public Activestep getActivestepFor4(Integer userId) {
        return activestepMapper.selectByUserIdAndType(userId, "4");
    }

    public int updateActivestep(Activestep activestep) {
        return activestepMapper.updateByPrimaryKey(activestep);
    }


    public int createComment(Integer userId, Integer activeStepId, String content) throws Exception{
        Comment comment = new Comment();
        comment.setActivestepid(activeStepId);
        comment.setContent(content);
        comment.setUserid(userId);
        comment.setCreatetime(DataTypeUtils.formatCurDateTime());
        comment.setStatus("1");
        return commentMapper.insert(comment);
    }

    public int delComment(Integer userId, Integer activeStepId){

        return 0;
    }

    public List<Comment> getComment(Integer activeStepId){
        List<Comment> comments = commentMapper.selectByActiveStepId(activeStepId);
        return comments;
    }

    public int updateActiveData(Activedata activedata){
        return activedataMapper.updateByPrimaryKey(activedata);
    }

    public int updateUser(User user){
        return userMapper.updateByPrimaryKey(user);
    }

    public int createActiveLog(Activelog activelog){
        return activelogMapper.insert(activelog);
    }


}
