package TestEncode;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.util.Scanner;

/**
 * ??URL??????
 * */

public class Test {
    public static void main(String[] args) {
        String username = "任务";
        try {

//            char[] chars = username.toCharArray();
//            StringBuffer stringBuffer = new StringBuffer();
//            for (int i = 0; i < chars.length; i++) {
//                stringBuffer.append((int) chars[i]);
//                stringBuffer.append(" ");
//            }

            URL url = new URL("http://180.76.156.218:8090/zh_en/" + URLEncoder.encode(username) + "/");
            URLConnection conn = url.openConnection();// 打开链接
            InputStream is = conn.getInputStream();// 获取数据流
            // URLConnection is =url.openStream();//缩写
            Scanner sc = new Scanner(is, StandardCharsets.UTF_8);// 扫描数据流并存放在sc里,编码方式 GBK
            while (sc.hasNextLine()) {// 判断是否存在下一行
                username = sc.nextLine();
                System.out.println(username);// 输出当前行内容
//                System.out.println(URLEncoder.encode(username,"utf-8"));
//                System.out.println(URLDecoder.decode(URLEncoder.encode(username,"utf-8"), "UTF-8"));
            }
            sc.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}