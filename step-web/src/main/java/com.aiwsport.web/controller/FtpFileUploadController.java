package com.aiwsport.web.controller;

import com.aiwsport.core.entity.Activestep;
import com.aiwsport.core.service.StepService;
import com.aiwsport.web.utlis.FtpFileUtil;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private StepService stepService;

    //ftp处理文件上传
    @RequestMapping(value="/upload_img.do")
    public @ResponseBody String uploadImg(@RequestParam("file") MultipartFile file, Integer userId, Integer index, HttpServletRequest request) throws IOException {
        String imgName = "";
        try{
            String fileName = file.getOriginalFilename();
            if (fileName.contains("www.aiwsport.com")) {
                return "";
            }

            InputStream inputStream=file.getInputStream();
            imgName = "step"+System.currentTimeMillis()+fileName.substring(fileName.lastIndexOf("."));
            System.out.println("ftp "+imgName);
            Boolean flag= FtpFileUtil.uploadFile(imgName,inputStream);
            if(flag==true){
                System.out.println("ftp上传成功！");
            }

            // 图片地址入库
            Activestep activestep = stepService.getActivestepFor4(userId);

            String showUrl = activestep.getShowurl();
            if (StringUtils.isNotBlank(showUrl)) {
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
}
