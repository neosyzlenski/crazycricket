/**
 * 
 */
package com.bfm.acs.crazycricket.data;

/**
 * Modal class for players info
 * 
 * @author Kapil
 * Created: June 30, 2016
 *
 */
public class PlayerInfo{
	private long playerId;
	private String userId;
	private String country;
	
	public PlayerInfo(String userId, String country){
		this.userId = userId;
		this.country = country;
	}

	public PlayerInfo(long playerId, String userId, String country){
		this.playerId = playerId;
		this.userId = userId;
		this.country = country;
	}
	
	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
