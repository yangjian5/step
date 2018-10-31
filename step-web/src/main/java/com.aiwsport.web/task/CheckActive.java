package com.aiwsport.web.task;

import com.aiwsport.core.entity.Activedata;
import com.aiwsport.core.entity.Activelog;
import com.aiwsport.core.entity.Activestep;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.service.StepService;
import com.aiwsport.core.utils.DataTypeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.List;

/**
 * 活动奖励检查
 *
 * @author yangjian9
 */
@Configuration
@EnableScheduling
public class CheckActive {
    private static Logger logger = LogManager.getLogger();

    @Autowired
    private StepService stepService;

    @Scheduled(cron = "0 0 23 * * ?") // 每天23点执行
//    @Scheduled(cron = "0 0/1 * * * ?") // 每1分钟执行一次
    public void checkUserActive(){
        try {
            // type 1 达标的人数 参数的人数
            buildActive("1", 10000, 30);
        } catch (Exception e) {
            logger.error("checkUserActive is error type is 1", e);
        }

        try {
            // type 2 达标的人
            buildActive("2", 15000, 50);
        } catch (Exception e) {
            logger.error("checkUserActive is error type is 2", e);
        }

        try {
            // type 3 达标的人
            buildActive("3", 20000, 70);
        } catch (Exception e) {
            logger.error("checkUserActive is error type is 3", e);
        }

        try {
            // type 5 达标的人
            buildActive("5", 20000, 10);
        } catch (Exception e) {
            logger.error("checkUserActive is error type is 5", e);
        }
    }

    private void buildActive(String type, int stepLimit, int joinCoin) throws Exception{
        logger.info("buildActive is start type is " + type);

        // 计算总人数*报名费/达标人数 = 每人分得的奖励
        List<Activestep> activesteps = stepService.getActiveInfo(type);

        int finishCount = 0;
        if ("5".equals(type)) {
            finishCount = stepService.selectFinish5Count(type);
        } else {
            finishCount = stepService.selectFinish(type, stepLimit);
        }

        logger.info("buildActive finishCount is " + finishCount);

        if (finishCount == 0) {
            return;
        }

        int sumPer = activesteps.size();
        BigDecimal coin1 = BigDecimal.valueOf(finishCount*joinCoin).divide(BigDecimal.valueOf(sumPer));

        for (Activestep activestep : activesteps) {
            try {
                Activedata activedata = stepService.selectByActiveStepId(activestep.getId());
                if (activedata == null) {
                    continue;
                }

                // 插入activelog表
                Activelog activelog = new Activelog();

                if ("5".equals(type)) {
                    if (activedata.getIssign().equals("1")){ // 达标  插入activelog表, 更新user的coin,
                        activelog.setIsfinish("1");

                        // 更新user的coin
                        User user = stepService.getUser(String.valueOf(activestep.getUserid()));
                        user.setCoinnum(BigDecimal.valueOf(user.getCoinnum()).add(coin1).doubleValue());
                        stepService.updateUser(user);
                    } else { //b 未达标 插入activelog表, 修改activestep activedata status 为0
                        activelog.setIsfinish("0");
                    }
                } else {
                    if (activedata.getDaystep() > 10000){ // 达标  插入activelog表, 更新user的coin,
                        activelog.setIsfinish("1");

                        // 更新user的coin
                        User user = stepService.getUser(String.valueOf(activestep.getUserid()));
                        user.setCoinnum(BigDecimal.valueOf(user.getCoinnum()).add(coin1).doubleValue());
                        stepService.updateUser(user);
                    } else { //b 未达标 插入activelog表, 修改activestep activedata status 为0
                        activelog.setIsfinish("0");
                    }
                }

                activelog.setUserid(activestep.getUserid());
                activelog.setActivestepid(activestep.getId());
                activelog.setType(type);
                activelog.setCoin(coin1.doubleValue());
                activelog.setCreatetime(DataTypeUtils.formatCurDateTime());
                stepService.createActiveLog(activelog);

                // 修改activestep activedata status 为0
                activedata.setStatus("0");
                stepService.updateActiveData(activedata);

                activestep.setStatus("0");
                stepService.updateActivestep(activestep);
            } catch (Exception e) {
                logger.error("buildActive is error activestep is " + activestep.getId(), e);
            }
            logger.info("buildActive is end type is " + type);
        }
    }

}
