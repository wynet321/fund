package net.canglong.fund;

import java.util.Map;
import net.canglong.fund.entity.ResponseObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import lombok.extern.log4j.Log4j2;

// @Aspect is intentionally disabled to avoid wrapping all responses.
@Log4j2
public class ResponseHandler {

  @Pointcut("within(net.canglong.fund.controller.*)")
  public void httpResponse() {
  }

  // AOP wrapper disabled; keep method for reference if re-enabled in future
  // @Around("httpResponse()")
  public ResponseObject handleController(ProceedingJoinPoint proceedingJoinPoint) {
    ResponseObject responseObject = new ResponseObject();
    try {
      Object object = proceedingJoinPoint.proceed();
      if (null == object) {
        responseObject.setData(null);
        responseObject.setSuccess(false);
      } else if (object instanceof Boolean boolean1) {
        responseObject.setSuccess(boolean1);
      } else if (object instanceof Map<?, ?> map) {
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
    // Do not leak internal exception messages to clients
    responseObject.setMessage("Internal server error");
    log.error("Unhandled exception in controller: {}", throwable.getMessage(), throwable);
    return responseObject;
  }

}
