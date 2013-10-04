package edu.mines.msmith1.universepoint.test.util;

import android.content.Context;
import edu.mines.msmith1.universepoint.dao.GameDAO;
import edu.mines.msmith1.universepoint.dao.PlayerDAO;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.Game;
import edu.mines.msmith1.universepoint.dto.Player;
import edu.mines.msmith1.universepoint.dto.Team;

public class DatabaseUtil {
	public static Team addTeamToDatabase(Context context, String teamName) {
		TeamDAO teamDAO = new TeamDAO(context);
		teamDAO.open();
		Team team = new Team();
		team.setName(teamName);
		team = teamDAO.createTeam(team);
		teamDAO.close();
		return team;
	}
	
	public static Game addGameToDatabase(Context context, Team team1, Team team2) {
		GameDAO gameDAO = new GameDAO(context);
		gameDAO.open();
		Game game = new Game();
		game.setTeam1(team1);
		game.setTeam2(team2);
		game = gameDAO.createGame(game);
		gameDAO.close();
		return game;
	}
	
	public static Player addPlayerToDatabase(Context context, String playerName, Team team) {
		PlayerDAO playerDAO = new PlayerDAO(context);
		playerDAO.open();
		Player player = new Player();
		player.setName(playerName);
		player.setTeam(team);
		player = playerDAO.createPlayer(player);
		playerDAO.close();
		return player;
	}
}
