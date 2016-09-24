/**
* This is free and unencumbered software released into the public domain.
*
* Anyone is free to copy, modify, publish, use, compile, sell, or
* distribute this software, either in source code form or as a compiled
* binary, for any purpose, commercial or non-commercial, and by any
* means.
*
* In jurisdictions that recognize copyright laws, the author or authors
* of this software dedicate any and all copyright interest in the
* software to the public domain. We make this dedication for the benefit
* of the public at large and to the detriment of our heirs and
* successors. We intend this dedication to be an overt act of
* relinquishment in perpetuity of all present and future rights to this
* software under copyright law.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
* OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
* ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*
* For more information, please refer to <http://unlicense.org/>
*/

package tk.serjmusic.utils.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect for easier logging methods invocation using AOP. Every method annotated 
 * with {@link Loggable} will be logged during it's invocation in a pretty format including 
 * it's name, params, result, thrown exceptions and duration of invocation.
 *
 * @author Roman Kondakov
 */
@Component("aopLogger")
@Aspect
public class LoggingAspect {
    
    private Logger logger = Logger.getLogger(getClass());
    
    
    @Pointcut("@annotation(tk.serjmusic.utils.logging.Loggable)")
    public void annotationPointCutDefinition(){
    }
 
    @Pointcut("execution(* *(..))")
    public void atExecution(){}
    
    /**
     * Log every invocation of method annotated with {@link Loggable}.
     * @param joinPoint method joinpoint
     * @return result of loggable method
     * @throws Throwable exception of loggable method
     */
    @Around("annotationPointCutDefinition() && atExecution()") 
    @SuppressWarnings("deprecation")
    public Object watchLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();
        Loggable loggable = method.getAnnotation(Loggable.class);
        Object result = null;
        long start = System.currentTimeMillis();
        if (loggable.level() >= logger.getEffectiveLevel().toInt()) {
            try {
                result = joinPoint.proceed();
            } catch (Throwable ex) {
                result = ex;
                throw ex;
            } finally {
                Long duration = System.currentTimeMillis() - start;
                String message = logMethodParameters(joinPoint, method, duration, result);
                logger.log(Priority.toPriority(loggable.level()), message);
            }
        }
        return result;
    }
    
    /**
     * Return method parameters message for logging purposes.
     * 
     * @param joinPoint current joinpoint
     * @param method invoked method
     * @param duration method invocation duration
     * @param result reurned from method
     */
    private String logMethodParameters(ProceedingJoinPoint joinPoint, Method method, long duration, 
            Object result) {
        StringBuilder message = new StringBuilder();
        Object[] parameters = joinPoint.getArgs();
        Class<?>[] classes = method.getParameterTypes();
        message.append(" ")
               .append(duration)
               .append("ms; ")
               .append(joinPoint.getTarget())
               .append(".")
               .append(method.getName())
               .append("(");
        for (int i = 0; i < method.getParameterCount(); i++) {
            message.append(classes[i].getSimpleName())
                   .append(" ")
                   .append(parameters[i])
                   .append(", ");
        }
        message.append(") ")
               .append("Result: ")
               .append(result)
               .append(";");
        return message.toString();
    }
}
