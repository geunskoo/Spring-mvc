package com.geunskoo.servlet.web.frontcontroller.v5;

import com.geunskoo.servlet.web.frontcontroller.ModelView;
import com.geunskoo.servlet.web.frontcontroller.MyView;
import com.geunskoo.servlet.web.frontcontroller.v3.ControllerV3;
import com.geunskoo.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.geunskoo.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.geunskoo.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import com.geunskoo.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.geunskoo.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.geunskoo.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import com.geunskoo.servlet.web.frontcontroller.v5.adaptor.ControllerV3HandlerAdaptor;
import com.geunskoo.servlet.web.frontcontroller.v5.adaptor.ControllerV4HandlerAdaptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMapping = new HashMap<>();
    private final List<MyHandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdaptor();
    }

    private void initHandlerAdaptor() {
        handlerAdaptors.add(new ControllerV3HandlerAdaptor());
        handlerAdaptors.add(new ControllerV4HandlerAdaptor());
    }

    private void initHandlerMappingMap() {
        handlerMapping.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMapping.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMapping.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //V4
        handlerMapping.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMapping.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMapping.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object handler = getHandler(request);

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdaptor adaptor = getHandlerAdaptor(handler);
        ModelView mv = adaptor.handle(request, response, handler);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), request, response);
    }

    private MyHandlerAdaptor getHandlerAdaptor(Object handler) {
        for (MyHandlerAdaptor adaptor : handlerAdaptors) {
            if (adaptor.supports(handler)) {
                return adaptor;
            }
        }
        throw new IllegalArgumentException("handler adaptor를 찾을 수 없습니다. handler = " + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMapping.get(requestURI);
    }
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
