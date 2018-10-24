package com.aiwsport.web.controller;

import com.aiwsport.core.entity.Activestep;
import com.aiwsport.core.service.StepService;
import com.aiwsport.web.utlis.FtpFileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangjian9 on 2018/10/21.
 */
@CrossOrigin
@Controller
public class FtpFileUploadController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StepService stepService;

    //ftp处理文件上传
    @RequestMapping(value="/upload_img.do")
    public @ResponseBody String uploadImg(@RequestParam("file") MultipartFile file, Integer userId, String realUrl, HttpServletRequest request) throws IOException {
        String imgName = "";
        try{
            if (realUrl.contains("www.aiwsport.com")) {
                return "";
            }

            String fileName = file.getOriginalFilename();
            InputStream inputStream=file.getInputStream();
            imgName = "step"+System.currentTimeMillis()+fileName.substring(fileName.lastIndexOf("."));
            System.out.println("ftp "+imgName);
            Boolean flag= FtpFileUtil.uploadFile(imgName,inputStream);
            if(!flag){
                return "fail";
            }

            // 图片地址入库
            Activestep activestep = stepService.getActivestepFor4(userId);

            String showUrl = activestep.getShowurl();
            if (StringUtils.isNotBlank(showUrl)) {
                String[] urls = showUrl.split(",");
                if (urls.length > 2) {
                    return "fail";
                }

                activestep.setShowurl(showUrl+",https://www.aiwsport.com/test/"+imgName);
            } else {
                activestep.setShowurl("https://www.aiwsport.com/test/"+imgName);
            }

            stepService.updateActivestep(activestep);
        } catch (Exception e) {
            return "fail";
        }
        return imgName;
    }

    @RequestMapping(value="/step/del_img.do")
    public @ResponseBody String delImg(String urlImg, Integer userId) throws IOException {
        // 图片删除
        try{
            if (!urlImg.contains("www.aiwsport.com")) {
                return "sucess";
            }

            Activestep activestep = stepService.getActivestepFor4(userId);
            if (activestep == null) {
                return "fail";
            }

            String newUrl = "";
            String showUrl = activestep.getShowurl();
            if (showUrl.contains(urlImg)) {
                String[] urls = showUrl.split(",");
                for (String url : urls) {
                    if (!url.equals(urlImg)) {
                        newUrl += url+",";
                    }
                }
            }

            if (newUrl.contains(",")) {
                newUrl = newUrl.substring(0, newUrl.lastIndexOf(","));
            }

            activestep.setShowurl(newUrl);
            stepService.updateActivestep(activestep);
        } catch (Exception e) {
            logger.error("delImg is error ", e);
            return "fail";
        }

        logger.warn("del urlImg is " + urlImg);
        return "sucess";
    }
}
