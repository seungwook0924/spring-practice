package hello.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) { //전달된 예외가 IllegalArgumentException 인지 확인
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); //클라이언트에게 HTTP 상태 코드 400(Bad Request)와 함께 예외 메시지를 전송
                return new ModelAndView(); //빈 객체를 리턴하여 정상 동작 유도(뷰 처리 건너뜀)
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}
