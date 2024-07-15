package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class); //현재 처리 중인 메서드 파라미터에 @Login 어노테이션이 적용되어 있는지 여부를 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType()); //파라미터의 타입이 Member 클래스 또는 그 하위 클래스의 인스턴스인지 확인
        return hasLoginAnnotation && hasMemberType; //둘 다 만족한다면 true 리턴, true -> resolveArgument() 메서드 호출
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(); //HttpServletRequest로 캐스팅하여 실제 서블릿 요청에 접근
        HttpSession session = request.getSession(false); //세션이 존재하지 않을 경우 새로 생성하지 않도록 지정
        if (session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER); //세션의 loginId 키를 이용하여 member 객체를 가져와서 리턴
    }
}
