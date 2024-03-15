package hello.login.web.interceptor;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false); //세션이 이미 존재하지 않는 경우 새로 만들지 않고, null 을 반환
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {  //세션이 없거나 세션을 찾을 수 없다면
            log.info("미인증 사용자 요청");
            response.sendRedirect("/login?redirectURL=" + requestURI); //로그인으로 기존에 접속했던 url 을 파라미터로 넘겨서 redirect
            return false;  //false 이면 다음 인터셉터나 컨트롤러가 호출 되지 않는다.
        }
        return true; //다음 인터셉터나 컨트롤러 호출
    }
}
