package com.letv.portal.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.letv.common.result.ResultObject;
import com.letv.common.session.Executable;
import com.letv.common.session.Session;
import com.letv.common.session.SessionServiceImpl;
import com.letv.portal.controller.cloudvm.Util;
import com.letv.portal.proxy.ILoginProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CloudvmInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(CloudvmInterceptor.class);

    @Autowired
    private SessionServiceImpl sessionService;

    public String[] checkUrls;//还没发现可以直接配置不拦截的资源，所以在代码里面来排除

    public void setCheckUrls(String[] checkUrls) {
        this.checkUrls = checkUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object arg2) throws Exception {
        String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");

        //特殊url过滤
        if (null != checkUrls && checkUrls.length >= 1) {
            for (String url : checkUrls) {
                if (requestUrl.contains(url)) {
                    if (!Util.session(sessionService).getVMManager().isAuthority()) {
                        boolean isAjaxRequest = (request.getHeader("x-requested-with") != null) ? true : false;
                        if (isAjaxRequest) {
                            responseJson(request, response, "对不起，您没有权限。");
                        } else {
                            response.sendRedirect(request.getContextPath()+"/dashboard");
                        }
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {
    }

    private void responseJson(HttpServletRequest req, HttpServletResponse res, String message) {
        PrintWriter out = null;
        try {
            res.setContentType("text/html;charset=UTF-8");
            out = res.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ResultObject resultObject = new ResultObject(0);
        resultObject.addMsg(message);
        out.append(JSON.toJSONString(resultObject, SerializerFeature.WriteMapNullValue));
        out.flush();
    }

}