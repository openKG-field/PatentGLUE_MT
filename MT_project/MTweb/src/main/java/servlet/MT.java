package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.net.URL;

public class MT extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {


        request.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        String userInput = request.getParameter("userInput");
        // System.out.println(userInput);
        if (userInput == null || userInput.equals("")) {
            request.setAttribute("userInput", null);
            request.setAttribute("nmtOutput", null);
            if (type.equals("1"))
                request.getRequestDispatcher("nmt_Ch2En.jsp").forward(request, response);
            else
                request.getRequestDispatcher("nmt_En2Ch.jsp").forward(request, response);
        }

        String nmtOutput = "";

        if (type.equals("2")) {
            try {
                // "http://180.76.156.218:8090/zh_en/" + URLEncoder.encode(userInput) + "/"
                assert userInput != null;
                String result = URLEncoder.encode(userInput, StandardCharsets.UTF_8);
                URL url = new URL("http://180.76.156.218:8090/zh_en/" + result + "/");
                // System.out.println(URLDecoder.decode(URLEncoder.encode(userInput,"utf-8"), "UTF-8"));

                URLConnection conn = url.openConnection();
                InputStream is = conn.getInputStream();
                Scanner sc = new Scanner(is, StandardCharsets.UTF_8);
                while (sc.hasNextLine()) {
                    nmtOutput = sc.nextLine();
                    System.out.println(nmtOutput);
                }
                sc.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {
            try {
                // "http://180.76.156.218:8090/en_zh/" + URLEncoder.encode(userInput) + "/"
                assert userInput != null;
                String result = URLEncoder.encode(userInput, StandardCharsets.UTF_8);
                URL url = new URL("http://180.76.156.218:8090/en_zh/" + result + "/");
                // System.out.println(URLDecoder.decode(URLEncoder.encode(userInput,"utf-8"), "UTF-8"));

                URLConnection conn = url.openConnection();
                InputStream is = conn.getInputStream();
                Scanner sc = new Scanner(is, StandardCharsets.UTF_8);
                while (sc.hasNextLine()) {
                    nmtOutput = sc.nextLine();
                    System.out.println(nmtOutput);

                }
                sc.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("userInput", userInput);
        request.setAttribute("nmtOutput", nmtOutput);
        if (type.equals("1"))
            request.getRequestDispatcher("nmt_En2Ch.jsp").forward(request, response);
        else
            request.getRequestDispatcher("nmt_Ch2En.jsp").forward(request, response);
    }

}
