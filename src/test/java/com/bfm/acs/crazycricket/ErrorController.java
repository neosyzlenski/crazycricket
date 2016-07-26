/**
 * 
 */
package com.bfm.acs.crazycricket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bfm.acs.crazycricket.data.InvalidDateRangeException;

/**
 * Error controller for test configuration
 * 
 * @author Kapil
 * Created: Jul 27, 2016
 *
 */
@ControllerAdvice
public class ErrorController extends BasicErrorController {
	
	public ErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	@Override
    @ExceptionHandler(InvalidDateRangeException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        return super.error(request);
    }
}