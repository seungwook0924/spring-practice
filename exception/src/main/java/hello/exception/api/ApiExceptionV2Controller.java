package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Slf4j
@RestController
public class ApiExceptionV2Controller {
    @ResponseStatus(HttpStatus.BAD_REQUEST) //상태 코드 지정
    @ExceptionHandler(IllegalArgumentException.class) //처리할 예외 지정
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage()); //ErrorResult 객체를 JSON으로 반환

//        컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러 밖으로 던져진다. 예외가 발생했으로 ExceptionResolver 가 작동한다. 가장 우선순위가 높은
//        ExceptionHandlerExceptionResolver 가 실행된다.
//        ExceptionHandlerExceptionResolver 는 해당 컨트롤러에 IllegalArgumentException 을 처리할 수 있는 @ExceptionHandler 가 있는지 확인한다.
//        illegalExHandle() 를 실행한다. @RestController 이므로 illegalExHandle()에도 @ResponseBody가 적용된다. 따라서 HTTP 컨버터가 사용되고,
//        응답이 다음과 같은 JSON으로 반환된다. @ResponseStatus(HttpStatus.BAD_REQUEST)를 지정했으므로 HTTP 상태 코드 400으로 응답한다.
    }

    @ExceptionHandler //처리할 예외를 생략할 수 있음
    public ResponseEntity<ErrorResult> userExHandle(UserException e) { //UserException 예외 처리
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage()); //ErrorResult 에 오류 메시지를 담음
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST); //ResponseEntity에 오류 메시지와 상태코드를 담고 전송(JSON 형태)

//        @ExceptionHandler 에 예외를 지정하지 않으면 해당 메서드 파라미터 예외를 사용한다.
//        여기서는 UserException 을 사용한다.
//        ResponseEntity 를 사용해서 HTTP 메시지 바디에 직접 응답한다. 물론 HTTP 컨버터가 사용된다.
//        ResponseEntity를 사용하면 HTTP 응답 코드를 프로그래밍해서 동적으로 변경할 수 있다. @ResponseStatus는 애노테이션이므로 HTTP 응답 코드를 동적으로 변경할 수 없다.
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //상태코드 설정
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) { //Exception 처리 -> 이전에 처리하지 못한 예외나 공통 예외 처리는 여기서 담당
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류"); //ErrorResult 객체를 JSON으로 반환

//        throw new RuntimeException("잘못된 사용자")이 코드가 실행되면서, 컨트롤러 밖으로 RuntimeException 이 던져진다.
//        RuntimeException은 Exception 의 자식 클래스이다. 따라서 이 메서드가 호출된다.
//        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 로 HTTP 상태 코드를 500으로 응답한다.
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}