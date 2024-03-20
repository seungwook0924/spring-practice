package hello.exception.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception"; //예외
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type"; //예외 타입
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message"; //오류 메시지
    public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri"; //클라이언트 요청 URI
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name"; //오류가 발생한 서블릿 이름
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code"; //HTTP 상태 코드

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE) //같은 URL 매핑이어도 더 자세하기 떄문에 우선순위 높음
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {
        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION); //요청에서 발생한 예외 객체를 추출
        result.put("status", request.getAttribute(ERROR_STATUS_CODE)); //예외 발생 시의 HTTP 상태 코드를 결과를 맵에 추가
        result.put("message", ex.getMessage()); //발생한 예외의 메시지를 결과를 맵에 추가
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE); //요청에서 오류 상태 코드를 추출
        return new ResponseEntity(result, HttpStatus.valueOf(statusCode)); //결과 맵과 HTTP 상태 코드를 사용하여 ResponseEntity 객체를 생성하고 반환, 이 객체는 스프링 MVC에 의해 JSON 형식으로 클라이언트에게 전송
    }

    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex= {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE)); //ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType());
    }
}
