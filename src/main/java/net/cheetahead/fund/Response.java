package net.cheetahead.fund;

import net.cheetahead.fund.entity.ResponseObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class Response {

    @Pointcut("within(net.cheetahead.fund.controller.*)")
    public void httpResponse() {}

    @Around("httpResponse()")
    public ResponseObject handleController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ResponseObject responseObject = new ResponseObject();
        try {
            Object object = proceedingJoinPoint.proceed();
            if (null == object) {
                responseObject.setData(null);
                responseObject.setSuccess(false);
            } else if (object instanceof Boolean) {
                responseObject.setSuccess(((Boolean) object).booleanValue());
            } else if (object instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) object;
                responseObject.setSuccess(!map.isEmpty());
                responseObject.setData(object);
            } else {
                responseObject.setSuccess(true);
                responseObject.setData(object);
            }
        } catch (Throwable e) {
            responseObject = handleException(e);
        }
        return responseObject;
    }

    private ResponseObject handleException(Throwable throwable) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setSuccess(false);
        responseObject.setMessage(throwable.getMessage());
        return responseObject;
    }

}
