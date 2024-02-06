package hello.servlet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + request); //request 객체 출력
        System.out.println("response = " + response); //response 객체 출력

        String username = request.getParameter("username"); //username 파라미터 받아옴
        System.out.println("username = " + username); // username에 들어간 값 출력

        response.setContentType("text/plain"); //contentType을 text/plain 으로 설정
        response.setCharacterEncoding("utf-8"); //문자 인코딩 설정
        response.getWriter().write("hello " + username); //웹 브라우저에 출력

    }
}
