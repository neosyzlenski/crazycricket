package com.bfm.acs.crazycricket.data;

import com.bfm.acs.crazycricket.CrazyCricketProtos.Game;

import java.util.List;
import java.util.Map;

/**
 * Interface for data store
 * 
 * @author Kapil
 * Created: Jun 30, 2016
 *
 */
public interface DataStore {	
	/**
	 * Save the game details into database
	 * 
	 * @param game
	 */
	void save(Game game);
	
	/**
	 * Fetches the leaders in descending order of win counts. <br>
	 * If start/end dates are not null, limits the results for games within the date range
	 * 
	 * @param startDate
	 * @param endDate
	 * @return list of leader names and their win counts
	 */
	List<Map<String, Long>> getLeaderBoard(String startDate, String endDate);
	
	/**
	 * Fetches the country leaders in descending order of win counts. <br>
	 * If start/end dates are not null, limits the results for games within the date range
	 * 
	 * @param startDate
	 * @param endDate
	 * @return list of leader country names and their win counts
	 */
	List<Map<String, Long>> getNationalLeaderBoard(String startDate, String endDate);
}
