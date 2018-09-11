package com.aiwsport.web.exception;

import com.aiwsport.core.ConfigServerException;
import com.aiwsport.core.ConfigServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xin.pang
 */
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionController {
    private Logger logger = LogManager.getLogger();

    @ExceptionHandler(value = Exception.class)
    public ResultMsg handleAllException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        ConfigServerException configServerException;
        if (exception instanceof ConfigServerException) {
            configServerException = (ConfigServerException) exception;
        } else if (exception instanceof ServletRequestBindingException) {
            configServerException = new ConfigServerException(ConfigServerExceptionFactor.MISSING_PARAM, exception.getMessage());
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            String paramName = ((MethodArgumentTypeMismatchException) exception).getParameter().getParameterName();
            String errorMsg = ConfigServerExceptionFactor.CONFIG_PARAM_TYPE_MISMATCH.getErrorMsg() + " [" + paramName + "]";
            configServerException = new ConfigServerException(ConfigServerExceptionFactor.CONFIG_PARAM_TYPE_MISMATCH, errorMsg);
        } else if (exception instanceof BindException) {
            BindException result = (BindException) exception;
            FieldError fieldError = result.getFieldError();
            configServerException = new ConfigServerException(ConfigServerExceptionFactor.BIND_ERROR, fieldError.getField()+"绑定失败，请校验参数类型");
        } else if (exception instanceof NumberFormatException) {
            configServerException = new ConfigServerException(ConfigServerExceptionFactor.PATTERN_ERROR, exception.getMessage());
        } else {
            configServerException = new ConfigServerException(ConfigServerExceptionFactor.DEFAULT);
            logger.error(ExceptionUtils.getStackTrace(exception));
        }

        ResultMsg resultMsg = new ResultMsg(false, configServerException.getFactor().getErrorCode(), configServerException.getMessage());
        return resultMsg;
    }
}
