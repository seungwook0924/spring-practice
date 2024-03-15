package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init"); //필터가 생성된 시점 로깅
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //HTTP 요청이 오면 doFilter 가 호출
        HttpServletRequest httpRequest = (HttpServletRequest) request; //HttpServletRequest 로 캐스팅
        String requestURI = httpRequest.getRequestURI(); //요청 URL 얻어옴
        String uuid = UUID.randomUUID().toString(); //UUID 생성

        try {
            log.info("REQUEST  [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response); //다음 필터를 호출하거나, 필터가 없으면 서블릿, 컨트롤러 호출
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy"); //필터가 종료된 시점을 로깅
    }
}