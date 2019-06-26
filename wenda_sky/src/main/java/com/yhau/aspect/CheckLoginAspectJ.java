package com.yhau.aspect;

import com.yhau.config.web.HostHandler;
import com.yhau.core.util.ResponseUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Aspect
@Component
public class CheckLoginAspectJ {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private static final String TAG = "AspectJ.CheckLogin";
    @Resource
    private HostHandler hostHandler;

    @Pointcut("execution(@com.yhau.aspect.CheckLogin * *(..))")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void before(JoinPoint point) {
        logger.info(TAG, "CheckLoginAspectJ.before");
    }

    @Around("pointCut()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info(TAG, "checkLogin");
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckLogin checkLogin = method.getAnnotation(CheckLogin.class);
//        String content = checkLogin.param();

        String json = null;
        Object obj = null;
        if (hostHandler.getUser() == null) {
            json = ResponseUtil.getJSONString(999);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(json);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } else {
            return obj = joinPoint.proceed();
        }
        return obj;
    }

    @After("pointCut()")
    public void after(JoinPoint point) {
        logger.info(TAG, "CheckLoginAspectJ.after");
    }

    @AfterThrowing(value = "pointCut()", throwing = "ex")
    public void afterThrowing(Throwable ex) {
        logger.info(TAG, "CheckLoginAspectJ.afterThrowing.ex = " + ex.getMessage());
    }
}
