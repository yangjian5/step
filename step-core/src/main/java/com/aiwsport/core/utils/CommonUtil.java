package com.aiwsport.core.utils;

import com.aiwsport.core.constant.JerryConfigConstant;
import com.aiwsport.core.entity.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;

/**
 * 公用工具类
 *
 * @author yangjian9
 */
public class CommonUtil {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 构建实体基础属性
     *
     * @param obj
     * @return
     */
    public static void buildBaseInfo(Object obj) {
        try {
            MethodUtils.invokeMethod(obj, "setOpId", 9276L);
            MethodUtils.invokeMethod(obj, "setOpName", "admin");
            MethodUtils.invokeMethod(obj, "setVaildState", JerryConfigConstant.ValidState.YES);
            MethodUtils.invokeMethod(obj, "setModifyTime", DataTypeUtils.formatCurDateTime());
        } catch (NoSuchMethodException e) {
            logger.warn("build baseinfo method is not exist");
        } catch (Exception e) {
            logger.error("build baseinfo is error :", e);
        }
    }

    public static void writeFile(String path, String content) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
        } catch (Exception e) {
            logger.error("writeFile is error: " + e);
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                logger.error("writeFile close is error: " + e);
            }
        }
    }

    public static int decrypt(String encryptedData, String iv, String sessionKey, String token,Integer days){
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
        if (flagObj == null) {
            return -1;
        }
        int toDayStep = (int)flagObj.get("step");
        return toDayStep;
    }


    public static void main(String args[]) {
//        try {
//            OperationVersionLog operationVersionLog = new OperationVersionLog();
//            Method method = operationVersionLog.getClass().getMethod("getVaildState");
//            if (method == null) {
//                System.out.println("123123213131231");
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }

    /**
     * @param args
     * @throws ParseException
     */
//    public static void main(String[] args) throws ParseException {
//        //exportImg2("万魔声学","d:/logo.jpg");
////        exportImg1();
//        Font font = new Font("微软雅黑", Font.PLAIN, 35);                     //水印字体
//        String srcImgPath="/Users/yangjian9/Desktop/asd.jpg"; //源图片地址
//        String tarImgPath="/Users/yangjian9/Desktop/aaa.jpg"; //待存储的地址
//        String waterMarkContent="图片来源：http://blog.csdn.net/zjq_1314520";  //水印内容
//        Color color=new Color(255,255,255,128);                               //水印图片色彩以及透明度
//        new CommonUtil().addWaterMark(srcImgPath, tarImgPath, waterMarkContent, color,font);
//    }


//    public static void exportImg1(){
//        int width = 100;
//        int height = 100;
//        String s = "你好";
//
//        File file = new File("/Users/yangjian9/Desktop/asd.jpg");
//
//        Font font = new Font("Serif", Font.BOLD, 10);
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = (Graphics2D)bi.getGraphics();
//        g2.setBackground(Color.WHITE);
//        g2.clearRect(0, 0, width, height);
//        g2.setPaint(Color.RED);
//
//        FontRenderContext context = g2.getFontRenderContext();
//        Rectangle2D bounds = font.getStringBounds(s, context);
//        double x = (width - bounds.getWidth()) / 2;
//        double y = (height - bounds.getHeight()) / 2;
//        double ascent = -bounds.getY();
//        double baseY = y + ascent;
//
//        g2.drawString(s, (int)x, (int)baseY);
//
//        try {
//            ImageIO.write(bi, "jpg", file);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

//    public static void exportImg2(String username,String headImg){
//        try {
//            //1.jpg是你的 主图片的路径
//            InputStream is = new FileInputStream("/Users/yangjian9/Desktop/asd.jpg");
//
//
//            //通过JPEG图象流创建JPEG数据流解码器
//            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
//            //解码当前JPEG数据流，返回BufferedImage对象
//            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
//            //得到画笔对象
//            Graphics g = buffImg.getGraphics();
//
//            //创建你要附加的图象。
//            //小图片的路径
//            ImageIcon imgIcon = new ImageIcon(headImg);
//
//            //得到Image对象。
//            Image img = imgIcon.getImage();
//
//            //将小图片绘到大图片上。
//            //5,300 .表示你的小图片在大图片上的位置。
//            g.drawImage(img,15,15,null);
//
//            //设置颜色。
//            g.setColor(Color.BLACK);
//
//
//            //最后一个参数用来设置字体的大小
//            Font f = new Font("宋体",Font.PLAIN,25);
//            Color mycolor = Color.red;//new Color(0, 0, 255);
//            g.setColor(mycolor);
//            g.setFont(f);
//
//            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
//            g.drawString(username,10,13);
//
//            g.dispose();
//
//
//            OutputStream os;
//
//            //os = new FileOutputStream("d:/union.jpg");
//            String shareFileName = "test" + System.currentTimeMillis() + ".jpg";
//            os = new FileOutputStream(shareFileName);
//            //创键编码器，用于编码内存中的图象数据。
//            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
//            en.encode(buffImg);
//
//            is.close();
//            os.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ImageFormatException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

//    public void addWaterMark(String srcImgPath, String tarImgPath, String waterMarkContent,Color markContentColor,Font font) {
//
//        try {
//            // 读取原图片信息
//            File srcImgFile = new File(srcImgPath);//得到文件
//            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
//            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
//            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
//            // 加水印
//            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = bufImg.createGraphics();
//            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
//            g.setColor(markContentColor); //根据图片的背景设置水印颜色
//            g.setFont(font);              //设置字体
//
//            //设置水印的坐标
//            int x = srcImgWidth - 2*getWatermarkLength(waterMarkContent, g);
//            int y = srcImgHeight - 2*getWatermarkLength(waterMarkContent, g);
//            g.drawString(waterMarkContent, 20, 20);  //画出水印
//            g.dispose();
//            // 输出图片
//            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
//            ImageIO.write(bufImg, "jpg", outImgStream);
//            System.out.println("添加水印完成");
//            outImgStream.flush();
//            outImgStream.close();
//
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {
//        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
//    }

}
