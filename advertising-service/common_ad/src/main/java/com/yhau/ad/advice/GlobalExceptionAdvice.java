package com.yhau.ad.advice;

import com.yhau.ad.exception.AdException;
import com.yhau.ad.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@RestController
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = AdException.class)
    public CommonResponse<String> handlerAdException(HttpServletRequest req, AdException ex) {
        CommonResponse<String> response = new CommonResponse<>(-1, "business error");
        response.setData(ex.getMessage());
        return response;
    }
}
