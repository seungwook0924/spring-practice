package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController { //뷰 템플릿을 호출하는 컨트롤러**
    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello").addObject("data", "hello!");
        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) { //model : Spring MVC 프레임워크에서 컨트롤러와 뷰 사이에서 데이터를 전달하는 역할
        model.addAttribute("data", "hello!!");
        return "response/hello"; // 뷰의 이름을 반환 -> templates/response/hello.html
    }

    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!!");
        //리턴이 void : 요청 URL을 참고해서 논리 뷰 이름으로 사용
    }
}
