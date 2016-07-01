package com.bfm.acs.crazycricket.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.bfm.acs.crazycricket.CrazyCricketProtos.Game;

/**
 * HSQL DB implementation for persistent data storage
 * 
 * @author Kapil
 * Created: Jun 30, 2016
 *
 */
@Service
public class HsqlDataStoreImpl implements DataStore{
	private static final Logger LOG = LoggerFactory.getLogger(HsqlDataStoreImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/* (non-Javadoc)
	 * @see com.bfm.acs.crazycricket.data.DataStore#save(com.bfm.acs.crazycricket.CrazyCricketProtos.Game)
	 */
	@Override
	public void save(Game game) {
		LOG.info(String.format("Saving game: %s, %s, %s, %s", game.getWinner(), game.getLoser(), 
								new Date(game.getGameDate()), game.getType().name()));
		
		PlayerInfo winner = new PlayerInfo(game.getWinner().getUserId(), game.getWinner().getCountry());
		winner = savePlayer(winner);
		PlayerInfo loser = new PlayerInfo(game.getLoser().getUserId(), game.getLoser().getCountry());
		loser = savePlayer(loser);
		
		StringBuilder sql = new StringBuilder("INSERT INTO GAMES(WINNER_ID, LOSER_ID, GAME_DATE, GAME_TYPE) ");
		sql.append("VALUES(? , ?, ?, ?)");
		
		jdbcTemplate.update(sql.toString(), winner.getPlayerId(), loser.getPlayerId(), 
							new Date(game.getGameDate()), game.getType().name());
		
	    LOG.info("Game saved");
	}
	
	/**
	 * Helper method to save a player only once
	 * 
	 * @param player
	 * @return new player object with playerId populated
	 */
	private PlayerInfo savePlayer(PlayerInfo player){
		Long winnerId = new Long(0);
		
		//Check if player exists already
		try{
			winnerId = jdbcTemplate.queryForObject("SELECT PLAYER_ID FROM PLAYERS WHERE USER_ID = '" +  
													player.getUserId() + "'",	Long.class);
		}catch(EmptyResultDataAccessException e){
			StringBuilder sql = new StringBuilder("MERGE INTO PLAYERS B USING (VALUES(?, ?)) ");
			sql.append("AS A(USER_ID, COUNTRY) ON (A.USER_ID = B.USER_ID AND A.COUNTRY = B.COUNTRY) ");
			sql.append("WHEN NOT MATCHED THEN INSERT(USER_ID, COUNTRY) VALUES A.USER_ID, A.COUNTRY");
			
			jdbcTemplate.update(sql.toString(), player.getUserId(), player.getCountry());
		    
			winnerId = jdbcTemplate.queryForObject("SELECT PLAYER_ID FROM PLAYERS WHERE USER_ID = '" +  
					player.getUserId() + "'",	Long.class);
		}
		
		return new PlayerInfo(winnerId.longValue(), player.getUserId(), player.getCountry());
	}

	/*
	 * (non-Javadoc)
	 * @see com.bfm.acs.crazycricket.data.DataStore#getLeaderBoard(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Long>> getLeaderBoard(final String startDate, final String endDate) {
		List<Map<String, Long>> leaders = new ArrayList<Map<String, Long>>();
		final boolean hasRange = startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("");
		final String whereDateRange = " WHERE GAME_DATE BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD') "; 
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT USER_ID, COUNT(*) COUNTS FROM ");
		sql.append("(SELECT USER_ID, WINNER_ID FROM PLAYERS P LEFT OUTER JOIN ");
		sql.append("(SELECT WINNER_ID, LOSER_ID, GAME_DATE, GAME_TYPE FROM GAMES " + 
					(hasRange ? whereDateRange : "") + ") G ");
		sql.append("ON (P.PLAYER_ID = G.WINNER_ID)");
		sql.append(") WHERE WINNER_ID IS NOT NULL GROUP BY USER_ID ORDER BY COUNT(*) DESC, USER_ID ASC");
		
		
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString());
				if(hasRange){
					ps.setString(1, startDate);
					ps.setString(2, endDate);
				}
				return ps;
			}
		};
		
		leaders = jdbcTemplate.query(psc, new RowMapper<Map<String, Long>>(){

			@Override
			public Map<String, Long> mapRow(ResultSet rs, int index) throws SQLException {
				Map<String, Long> leader = new HashMap<String, Long>();
				leader.put(rs.getString("USER_ID"), rs.getLong("COUNTS"));
				
				return leader;
			}
		});
		
		return leaders;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bfm.acs.crazycricket.data.DataStore#getNationalLeaderBoard(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Long>> getNationalLeaderBoard(String startDate, String endDate) {
		List<Map<String, Long>> leaders = new ArrayList<Map<String, Long>>();
		final boolean hasRange = startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("");
		final String whereDateRange = " WHERE GAME_DATE BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD') "; 
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNTRY, COUNT(*) COUNTS FROM ");
		sql.append("(SELECT COUNTRY, WINNER_ID FROM (SELECT PLAYER_ID, COUNTRY FROM PLAYERS GROUP BY COUNTRY, PLAYER_ID) P LEFT OUTER JOIN ");
		sql.append("(SELECT WINNER_ID, LOSER_ID, GAME_DATE, GAME_TYPE FROM GAMES " + 
					(hasRange ? whereDateRange : "" ) + ") G ");
		sql.append("ON (P.PLAYER_ID = G.WINNER_ID)");
		sql.append(") WHERE WINNER_ID IS NOT NULL GROUP BY COUNTRY ORDER BY COUNT(*) DESC, COUNTRY ASC");
		
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString());
				if(hasRange){
					ps.setString(1, startDate);
					ps.setString(2, endDate);
				}
				return ps;
			}
		};
		
		leaders = jdbcTemplate.query(psc, new RowMapper<Map<String, Long>>(){

			@Override
			public Map<String, Long> mapRow(ResultSet rs, int index) throws SQLException {
				Map<String, Long> leader = new HashMap<String, Long>();
				leader.put(rs.getString("COUNTRY"), rs.getLong("COUNTS"));
				
				return leader;
			}
		});
		
		return leaders;
	}
}
