package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof UserException) { //UserException 이 발생했을 때
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept"); //accept 헤더 정보를 가져옴
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //HTTP 응답 상태 코드를 400(Bad Request)으로 설정

                if ("application/json".equals(acceptHeader)) { //accept 헤더 정보가 application/json 이라면
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass()); //예외의 클래스 타입을 넣음
                    errorResult.put("message", ex.getMessage()); //예외 메시지를 넣음
                    String result = objectMapper.writeValueAsString(errorResult); //ObjectMapper 를 사용하여 예외 정보를 문자로 변환

                    response.setContentType("application/json"); //응답의 Content-Type을 application/json으로 설정
                    response.setCharacterEncoding("utf-8"); //문자 인코딩을 UTF-8로 설정
                    response.getWriter().write(result); //응답 본문에 result 를 넣음

                    return new ModelAndView(); //빈 ModelAndView 객체를 반환하여 뷰 렌더링 없이 직접 응답을 처리
                } else {
                    //TEXT/HTML
                    return new ModelAndView("error/400");
                }
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}