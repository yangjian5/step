//import com.aiwsport.core.entity.User;
//import com.aiwsport.web.utlis.HttpRequestor;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.httpclient.Header;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.methods.GetMethod;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import javax.imageio.ImageIO;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.RenderingHints;
//import java.awt.geom.Ellipse2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
///**
// * Created by yangjian9 on 2018/9/18.
// */
//public class Mytest {
//
//
//        private Font font = new Font("华文彩云", Font.PLAIN, 40);// 添加字体的属性设置
//
//        private Graphics2D g = null;
//
//        private int fontsize = 0;
//
//        private int x = 0;
//
//        private int y = 0;
//
//        /**
//         * 导入本地图片到缓冲区
//         */
//        public BufferedImage loadImageLocal(String imgName) {
//            try {
//                return ImageIO.read(new File(imgName));
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//            }
//            return null;
//        }
//
//        /**
//         * 导入网络图片到缓冲区
//         */
//        public BufferedImage loadImageUrl(String imgName) {
//            try {
//                URL url = new URL(imgName);
//                return ImageIO.read(url);
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//            }
//            return null;
//        }
//
//        /**
//         * 生成新图片到本地
//         */
//        public void writeImageLocal(String newImage, BufferedImage img) {
//            if (newImage != null && img != null) {
//                try {
//                    File outputfile = new File(newImage);
//                    ImageIO.write(img, "png", outputfile);
//                } catch (IOException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }
//
//        /**
//         * 设定文字的字体等
//         */
//        public void setFont(String fontStyle, int fontSize) {
//            this.fontsize = fontSize;
//            this.font = new Font(fontStyle, Font.PLAIN, fontSize);
//        }
//
//        /**
//         * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
//         */
//        public BufferedImage modifyImage(BufferedImage img, Object content, int x,
//                                         int y) {
//
//            try {
//                int w = img.getWidth();
//                int h = img.getHeight();
//                g = img.createGraphics();
//                g.setBackground(Color.WHITE);
//                g.setColor(Color.orange);//设置字体颜色
//                if (this.font != null)
//                    g.setFont(this.font);
//                // 验证输出位置的纵坐标和横坐标
//                if (x >= h || y >= w) {
//                    this.x = h - this.fontsize + 2;
//                    this.y = w;
//                } else {
//                    this.x = x;
//                    this.y = y;
//                }
//                if (content != null) {
//                    g.drawString(content.toString(), this.x, this.y);
//                }
//                g.dispose();
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            return img;
//        }
//
//        /**
//         * 修改图片,返回修改后的图片缓冲区（输出多个文本段） xory：true表示将内容在一行中输出；false表示将内容多行输出
//         */
//        public BufferedImage modifyImage(BufferedImage img, Object[] contentArr,
//                                         int x, int y, boolean xory) {
//            try {
//                int w = img.getWidth();
//                int h = img.getHeight();
//                g = img.createGraphics();
//                g.setBackground(Color.WHITE);
//                g.setColor(Color.RED);
//                if (this.font != null)
//                    g.setFont(this.font);
//                // 验证输出位置的纵坐标和横坐标
//                if (x >= h || y >= w) {
//                    this.x = h - this.fontsize + 2;
//                    this.y = w;
//                } else {
//                    this.x = x;
//                    this.y = y;
//                }
//                if (contentArr != null) {
//                    int arrlen = contentArr.length;
//                    if (xory) {
//                        for (int i = 0; i < arrlen; i++) {
//                            g.drawString(contentArr[i].toString(), this.x, this.y);
//                            this.x += contentArr[i].toString().length()
//                                    * this.fontsize / 2 + 5;// 重新计算文本输出位置
//                        }
//                    } else {
//                        for (int i = 0; i < arrlen; i++) {
//                            g.drawString(contentArr[i].toString(), this.x, this.y);
//                            this.y += this.fontsize + 2;// 重新计算文本输出位置
//                        }
//                    }
//                }
//                g.dispose();
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            return img;
//        }
//
//        /**
//         * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
//         *
//         * 时间:2007-10-8
//         *
//         * @param img
//         * @return
//         */
//        public BufferedImage modifyImageYe(BufferedImage img) {
//
//            try {
//                int w = img.getWidth();
//                int h = img.getHeight();
//                g = img.createGraphics();
//                g.setBackground(Color.WHITE);
//                g.setColor(Color.blue);//设置字体颜色
//                if (this.font != null)
//                    g.setFont(this.font);
//                g.drawString("/Users/yangjian9/Desktop/data.png", w - 85, h - 5);
//                g.dispose();
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            return img;
//        }
//
//        public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d) {
//
//            try {
//                int w = b.getWidth();
//                int h = b.getHeight();
//
//
//                g = d.createGraphics();
//                g.drawImage(b, 100, 10, w, h, null);
//                g.dispose();
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            return d;
//        }
//
//
//
//    //以下图片处理成圆形接口
//
//    /**
//     * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像
//     *
//     * @param inputImage
//     * @param maxWidth
//     *            ：压缩后宽度
//     * @param maxHeight
//     *            ：压缩后高度
//     * @throws java.io.IOException
//     *             return
//     */
//    public static BufferedImage scaleByPercentage(BufferedImage inputImage, int newWidth, int newHeight) throws Exception {
//        // 获取原始图像透明度类型
//        int type = inputImage.getColorModel().getTransparency();
//        int width = inputImage.getWidth();
//        int height = inputImage.getHeight();
//        // 开启抗锯齿
//        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        // 使用高质量压缩
//        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        BufferedImage img = new BufferedImage(newWidth, newHeight, type);
//        Graphics2D graphics2d = img.createGraphics();
//        graphics2d.setRenderingHints(renderingHints);
//        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
//        graphics2d.dispose();
//        return img;
//    }
//
//    /**
//     * 通过网络获取图片
//     *
//     * @param url
//     * @return
//     */
//    public static BufferedImage getUrlByBufferedImage(String url) {
//        try {
//            URL urlObj = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
//            // 连接超时
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setConnectTimeout(25000);
//            // 读取超时 --服务器响应比较慢,增大时间
//            conn.setReadTimeout(25000);
//            conn.setRequestMethod("GET");
//            conn.addRequestProperty("Accept-Language", "zh-cn");
//            conn.addRequestProperty("Content-type", "image/jpeg");
//            conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
//            conn.connect();
//            BufferedImage bufImg = ImageIO.read(conn.getInputStream());
//            conn.disconnect();
//            return bufImg;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
//     *
//     * @param url
//     *            用户头像地址
//     * @return
//     * @throws IOException
//     */
//    public static BufferedImage convertCircular(BufferedImage bi1) throws IOException {
//
////		BufferedImage bi1 = ImageIO.read(new File(url));
//
//        // 这种是黑色底的
////		BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_RGB);
//
//        // 透明底的图片
//        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
//        Graphics2D g2 = bi2.createGraphics();
//        g2.setClip(shape);
//        // 使用 setRenderingHint 设置抗锯齿
//        g2.drawImage(bi1, 0, 0, null);
//        // 设置颜色
//        g2.setBackground(Color.green);
//        g2.dispose();
//        return bi2;
//    }
//
//
//
//
//
//   // https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epnRkt8ueSLqcIUPr7XKaNwmn3y0OvoyFjaNRXwByvtKeZ1JlCdCbuqrELauJflWuPkvVcF7s48Bw/132
//        public static void main(String[] args) throws Exception{
////            Mytest tt = new Mytest();
////            BufferedImage d = tt.loadImageLocal("/Users/yangjian9/Desktop/data.png");
////		BufferedImage b = tt.loadImageUrl("https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epnRkt8ueSLqcIUPr7XKaNwmn3y0OvoyFjaNRXwByvtKeZ1JlCdCbuqrELauJflWuPkvVcF7s48Bw/132");
////            tt.writeImageLocal("/Users/yangjian9/Desktop/data.png",tt.modifyImage(d,"sbyj",90,90)
////            );
////
////            tt.writeImageLocal("/Users/yangjian9/Desktop/tx2.png", tt.modifyImagetogeter(b, d));
////            //将多张图片合在一起
////            System.out.println("success");
////
////
////
////
////            //调用把图片变成圆形
////
////
////
////            BufferedImage url =
////                    getUrlByBufferedImage("https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epnRkt8ueSLqcIUPr7XKaNwmn3y0OvoyFjaNRXwByvtKeZ1JlCdCbuqrELauJflWuPkvVcF7s48Bw/132");
////            // 处理图片将其压缩成正方形的小图
////            BufferedImage convertImage = scaleByPercentage(url, 100, 100);
////            // 裁剪成圆形 （传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的）
////            convertImage = convertCircular(url);
////            // 生成的图片位置
////            String imagePath = "/Users/yangjian9/Desktop/yx.png";
////            ImageIO.write(convertImage, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath));
////            System.out.println("ok");
//
//            Mytest tt = new Mytest();
//            BufferedImage d = tt.loadImageLocal("/Users/yangjian9/Desktop/data.png");
//            BufferedImage url =
//                    getUrlByBufferedImage("https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epnRkt8ueSLqcIUPr7XKaNwmn3y0OvoyFjaNRXwByvtKeZ1JlCdCbuqrELauJflWuPkvVcF7s48Bw/132");
//            BufferedImage convertImage = scaleByPercentage(url, 100, 100);
//            convertImage = convertCircular(url);
//
//
//            tt.writeImageLocal("/Users/yangjian9/Desktop/data.png",tt.modifyImage(d,"sbyj",90,90));
//            tt.writeImageLocal("/Users/yangjian9/Desktop/rh.png", tt.modifyImagetogeter(convertImage, d));
//            System.out.printf("融合成功");
//
//        }
//
//    }
//
