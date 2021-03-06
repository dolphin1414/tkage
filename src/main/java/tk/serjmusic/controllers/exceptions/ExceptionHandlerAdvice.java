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

package tk.serjmusic.controllers.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import tk.serjmusic.services.exceptions.AlreadyExistsException;
import tk.serjmusic.services.exceptions.CanNotFindException;
import tk.serjmusic.services.exceptions.PersistentLayerProblemsException;

/**
 * The exception handler for controllers.
 *
 * @author Roman Kondakov
 */

@ControllerAdvice
public class ExceptionHandlerAdvice {
    
    private static final Logger logger = Logger.getLogger(ExceptionHandlerAdvice.class);
    HttpHeaders responseHeaders = new HttpHeaders();
    
    {
        responseHeaders.add("Content-Type", "application/json");
    }
    
    /**
     * Handler for {@link CanNotFindException}.
     * 
     * @param ex exception
     * @return NOT_FOUND status
     */
    @ExceptionHandler
    public ResponseEntity<String> handleException(CanNotFindException ex) {
        logger.info("Not found exception ", ex);
        return new ResponseEntity<String>(toErrorMessage(ex), responseHeaders,
                HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handler for {@link CanNotFindException}.
     * 
     * @param ex AlreadyExistsException
     * @return CONFLICT status
     */
    @ExceptionHandler
    public ResponseEntity<String> handleException(AlreadyExistsException ex) {
        logger.info("Entity already exists ", ex);
        return new ResponseEntity<String>(toErrorMessage(ex), responseHeaders,
                HttpStatus.CONFLICT);
    }
    
    /**
     * Handler for {@link PersistentLayerProblemsException}.
     * 
     * @param ex PersistentLayerProblemsException
     * @return CONFLICT status
     */
    @ExceptionHandler
    public ResponseEntity<String> handleException(PersistentLayerProblemsException ex) {
        logger.warn("Can not handle request. ", ex);
        return new ResponseEntity<String>(toErrorMessage(ex), responseHeaders,
                HttpStatus.CONFLICT);
    }
    
    /**
     * Handler for {@link PersistentLayerProblemsException}.
     * 
     * @param ex IllegalArgumentException
     * @return BAD_REQUEST status
     */
    @ExceptionHandler
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        logger.info("Bad request ", ex);
        return new ResponseEntity<String>(toErrorMessage(ex), responseHeaders,
                HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handler for all other exceptions.
     * 
     * @param ex exception
     * @return INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler
    public ResponseEntity<String> handleException(Throwable ex) {
        logger.warn("Unexpectable exception: ", ex);
        return new ResponseEntity<String>(toErrorMessage(ex), responseHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Find root cause of the exception.
     * 
     * @param e - exception
     * @return - root cause
     */
    private Throwable getRootCause(Throwable e) {
        Throwable cause = null; 
        Throwable result = e;
        while (null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }
    
    /**
     * Conver exception to JSON  format.
     * 
     * @param e - eexception
     * @return JSON string
     */
    private String toErrorMessage(Throwable e) {
        return "{ \"error\": \"" + getRootCause(e).getMessage() 
                + "\", \"exception\": \"" + getRootCause(e).getClass() + "\" }";
    }
}
