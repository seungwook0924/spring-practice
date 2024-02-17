package hello.servlet.web.frontcontroller.v4;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
     private Map<String, ControllerV4> controllerMap = new HashMap<>();
     public FrontControllerServletV4() {
         controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
         controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
         controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
     }

     @Override
     protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String requestURI = request.getRequestURI(); //경로를 가져옴
         ControllerV4 controller = controllerMap.get(requestURI); //경로에 맞는 컨트롤러 맵핑
         if (controller == null) {
             response.setStatus(HttpServletResponse.SC_NOT_FOUND);
             return;
         }

         Map<String, String> paramMap = createParamMap(request); //request의 파라미터를 모두 저장
         Map<String, Object> model = new HashMap<>(); //모델 생성

         String viewName = controller.process(paramMap, model);// 해당 컨트롤러 호출을 통해 논리적 뷰 경로이름을 받아옴
         MyView view = viewResolver(viewName); //뷰 리졸버를 통해 실제 뷰 경로를 받아옴
         view.render(model, request, response); //MyView의 render 메서드를 통해서 뷰를 렌더링
     }
     private Map<String, String> createParamMap(HttpServletRequest request) {
         Map<String, String> paramMap = new HashMap<>();
         request.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));

         return paramMap;
     }
     private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
     }
}