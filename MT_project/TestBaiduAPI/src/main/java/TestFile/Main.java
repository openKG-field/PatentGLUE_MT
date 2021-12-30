package TestFile;

import TestFile.BaiduTranslateDemo;
import com.google.common.util.concurrent.RateLimiter;

import java.io.*;
import java.util.concurrent.CountDownLatch;

/**
 * 直接运行main方法即可输出翻译结果
 */
public class Main {

    public static void main(String[] args) throws Exception {
        BaiduTranslateDemo baiduTranslateDemo = new BaiduTranslateDemo();
        // translateZhToEn(baiduTranslateDemo);
        // translateEnToZh(baiduTranslateDemo);
        translateTxtInfo(baiduTranslateDemo);
    }

    /**
     * 中文翻译为英文
     */
    public static void translateZhToEn(BaiduTranslateDemo baiduTranslateDemo) {

        String source = "今天天气真不错";
        String result;
        try {
            result = baiduTranslateDemo.translateZhToEn(source);
            if (result == null) {
                System.out.println("翻译出错");
                return;
            }
            System.out.println(source + "：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 英文翻译为中文
     */
    public static void translateEnToZh(BaiduTranslateDemo baiduTranslateDemo) {

        String source = "it's a nice day today";
        String result;
        try {
            result = baiduTranslateDemo.translateEnToZh(source);
            if (result == null) {
                System.out.println("翻译出错。");
                return;
            }
            System.out.println(source + "：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取txt文件内容翻译为中文
     */
    public static void translateTxtInfo(BaiduTranslateDemo baiduTranslateDemo) {

        String sorceFilePath = "D:\\Chrome-Edge\\IDEA\\MT\\MT\\MT\\Testword\\1.txt";
        //输出结果文件
        String resultFilePath = "D:\\Chrome-Edge\\IDEA\\MT\\MT\\MT\\Testword\\2.txt";
        // 构建指定文件
        File resultFile = new File(resultFilePath);
        FileReader fr = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        OutputStream out = null;
        String result;
        String line = "";
        try {
            // 根据文件创建文件的输入流
            fr = new FileReader(sorceFilePath);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(sorceFilePath)),"UTF-8"));
            fw = new FileWriter(new File(resultFilePath));
            // 写入中文字符时会出现乱码
            bw = new BufferedWriter(new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(resultFilePath)), "UTF-8")));

            // 根据文件创建文件的输出流
            out = new FileOutputStream(resultFile);
            // 创建字节数组
            byte[] data = new byte[1024];
            StringBuffer resultBuffer=new StringBuffer();
            // 读取内容，放到字节数组里面
            while ((line = br.readLine()) != null) {
                String message = line.trim();
                System.out.println(message);
                // 英文翻译为中文
                if(message.trim()!=null&& !message.trim().equals("")) {
                    if(message.trim().indexOf("Answer")>-1) {
                        resultBuffer.append(message).append("\t\n");
                    }else {
                        result = baiduTranslateDemo.translateZhToEn(message.trim());
                        System.out.println(result);
                        resultBuffer.append(result).append("\t\n");
                    }

                }

            }
            // 把内容转换成字节数组
            byte[] resultdata = resultBuffer.toString().getBytes();
            // 向文件写入内容
            out.write(resultdata);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输入流
                br.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}