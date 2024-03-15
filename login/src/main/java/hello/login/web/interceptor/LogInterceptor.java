package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI(); //요청 URL 받아옴
        String uuid = UUID.randomUUID().toString(); //UUID 생성
        request.setAttribute(LOG_ID, uuid); //UUID 값을 logId 라는 이름으로 request 에 저장

        //@RequestMapping: HandlerMethod
        //정적 리소스: ResourceHttpRequestHandler

        //핸들러 타입 체크: handler 객체가 HandlerMethod 의 인스턴스인지 확인한다.
        //이는 요청이 실제 컨트롤러 메서드에 의해 처리되는 경우에만 특정 로직을 실행하고자 할 때 유용
        if (handler instanceof HandlerMethod) { //HandlerMethod 는 요청을 처리하는 실제 메서드를 나타냄
            //호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
            HandlerMethod hm = (HandlerMethod) handler; //핸들러(컨트롤러)를 HandlerMethod 로 캐스팅
        }
        log.info("REQUEST  [{}][{}][{}]", uuid, requestURI, handler);
        return true; //false 이면 다음 인터셉터나 컨트롤러가 호출 되지 않는다.
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();  //요청 URL 받아옴
        String logId = (String)request.getAttribute(LOG_ID); //request 에서 logId 로 저장되어 있는 UUID 를 가져옴

        log.info("RESPONSE [{}][{}]", logId, requestURI);

        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
