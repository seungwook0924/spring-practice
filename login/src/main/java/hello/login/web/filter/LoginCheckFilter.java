package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {
    private static final String[] whitelist = {"/", "/members/add", "/login", "/ logout","/css/*"}; //이 URL 에 대해서는 필터를 적용하지 않음
    @Override
     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         HttpServletRequest httpRequest = (HttpServletRequest) request; // HttpServletRequest 로 캐스팅
         String requestURI = httpRequest.getRequestURI(); //요청 URL 가져오기
         HttpServletResponse httpResponse = (HttpServletResponse) response;// HttpServletResponse 로 캐스팅
         try {
                 log.info("인증 체크 필터 시작 {}", requestURI);
                 if (isLoginCheckPath(requestURI)) { //url 과 whitelist 와 매치되는 것이 없다면 true
                        log.info("인증 체크 로직 실행 {}", requestURI);
                        HttpSession session = httpRequest.getSession(false); //세션이 이미 존재하지 않는 경우 새로 만들지 않고, null 을 반환
                        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) { //세션이 없거나 세션을 찾을 수 없다면
                            log.info("미인증 사용자 요청 {}", requestURI);
                            httpResponse.sendRedirect("/login?redirectURL=" + requestURI); //로그인으로 기존에 접속했던 url 을 파라미터로 넘겨서 redirect
                            return; //여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝
                        }
                 }
                 //필터링 후 이상이 없다면
                 chain.doFilter(request, response); //다음 필터를 호출하거나 서블릿, 컨트롤러 호출
         } catch (Exception e) {
                 throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
         } finally {
                 log.info("인증 체크 필터 종료 {}", requestURI);
         }
    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI); //문자열 탐색 메소드(매치되는 것이 있다면 true, 여기서는 매치되는 것이 없다면 true)
    }
}
