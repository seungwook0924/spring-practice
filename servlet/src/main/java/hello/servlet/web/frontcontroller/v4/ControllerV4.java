package hello.servlet.web.frontcontroller.v4;
import java.util.Map;
public interface ControllerV4 {
    /**
     * @param paramMap -> 임시저장
     * @param model -> request에 실제로 넘길 값들
     * @return viewName
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}