CREATE TABLE IF NOT EXISTS PLAYERS (
  PLAYER_ID 	INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  USER_ID 		VARCHAR(50),
  COUNTRY  		VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS GAMES (
  GAME_ID 		INTEGER IDENTITY PRIMARY KEY,
  WINNER_ID 	INTEGER,
  LOSER_ID  	INTEGER,
  GAME_DATE		DATE,
  GAME_TYPE		VARCHAR(50)
);