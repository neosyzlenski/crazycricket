/**
 * 
 */
package com.bfm.acs.crazycricket.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfm.acs.crazycricket.data.DataStore;

/**
 * REST Controller for handling crazy requests
 * 
 * @author Kapil
 * Created: Jul 1, 2016
 *
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
public class CrazyCricketService {
	@Autowired
	DataStore dataStore;
    
	@RequestMapping(value = "/leaderboard")
	public List<Map<String, Long>> getLeaderBoardInRange(
			@RequestParam(name = "start", required = false) String startDate, 
			@RequestParam(name = "end", required = false) String endDate){
		return dataStore.getLeaderBoard(startDate, endDate);
	}
	
	@RequestMapping(value = "/national_leaderboard")
	public List<Map<String, Long>> getNationalLeaderBoardInRange(
			@RequestParam(name = "start", required = false) String startDate, 
			@RequestParam(name = "end", required = false) String endDate){
		return dataStore.getNationalLeaderBoard(startDate, endDate);
	}
}
