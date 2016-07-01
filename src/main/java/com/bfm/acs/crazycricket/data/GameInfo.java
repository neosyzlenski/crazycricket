/**
 * 
 */
package com.bfm.acs.crazycricket.data;

import java.sql.Date;

/**
 * Modal class for Game details
 * 
 * @author Kapil
 * Created: June 30, 2016
 *
 */
public class GameInfo{
	private Long gameId;
	private Long winnerId;
	private Long loserId;
	private Date gameDate;
	private String gameType;
	
	/**
	 * @param winnerId
	 * @param loserId
	 * @param gameDate
	 * @param gameType
	 */
	public GameInfo(Long winnerId, Long loserId, Date gameDate, String gameType) {
		this.winnerId = winnerId;
		this.loserId = loserId;
		this.gameDate = gameDate;
		this.gameType = gameType;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(Long winnerId) {
		this.winnerId = winnerId;
	}

	public Long getLoserId() {
		return loserId;
	}

	public void setLoserId(Long loserId) {
		this.loserId = loserId;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
}
