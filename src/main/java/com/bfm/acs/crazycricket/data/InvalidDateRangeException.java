/**
 * 
 */
package com.bfm.acs.crazycricket.data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for returning appropriate message to the client in case of invalid 
 * start and/or end dates
 * 
 * @author Kapil
 * Created: Jul 27, 2016
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDateRangeException extends Exception{
	private static final long serialVersionUID = -3804743221911222471L;

	public InvalidDateRangeException(String message){
		super(message);
	}
}