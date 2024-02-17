package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String, Object> handlerMappingMap = new HashMap<>(); //핸들러(컨트롤러)의 맵핑 정보를 저장하는 Map
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>(); //어댑터의 리스트를 담고있는 리스트
    public FrontControllerServletV5() {
        initHandlerMappingMap(); //핸들러(컨트롤러)의 맵핑 정보를 저장하는 Map에 v3, v4의 핸들러를 저장
        initHandlerAdapters(); //어댑터의 리스트를 담고있는 리스트에 v3, v4의 어댑터를 저장
    }
    private void initHandlerMappingMap() {
        //v3
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //v4
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter()); //v3
        handlerAdapters.add(new ControllerV4HandlerAdapter()); //V4
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request); //uri에 맵핑된 핸들러를 가져옴
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        MyHandlerAdapter adapter = getHandlerAdapter(handler); //핸들러 어댑터들 중에서 이용 가능한 핸들러 어댑터를 저장
        ModelView mv = adapter.handle(request, response, handler); //선택된 어댑터의 handle 메서드를 통해 ModelView를 반환받고 저장
        MyView view = viewResolver(mv.getViewName()); //뷰 리졸버를 통해 실제 뷰의 경로를 저장
        view.render(mv.getModel(), request, response); //뷰를 렌더링
    }
    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }
    private MyHandlerAdapter getHandlerAdapter(Object handler) { //핸들러 어댑터들 중에서 매개변수로 들어온 핸들러에 대해 지원 가능 핸들러 찾기
        for (MyHandlerAdapter adapter : handlerAdapters) { //어댑터 리스트를 통해 알맞는 어댑터를 가져옴
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
