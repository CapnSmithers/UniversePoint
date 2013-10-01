package edu.mines.msmith1.universepoint.test.dao;

import org.junit.Test;

import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dao.GameDAO;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.Game;
import edu.mines.msmith1.universepoint.dto.Team;
import edu.mines.msmith1.universepoint.test.DatabaseBaseTest;

public class GameDAOTest extends DatabaseBaseTest {
	GameDAO gameDAO;
	Game game;
	Team team1;
	Team team2;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		gameDAO = new GameDAO(context);
		gameDAO.open();
		
		// add two teams to db
		TeamDAO teamDAO = new TeamDAO(context);
		teamDAO.open();
		team1 = new Team();
		team1.setName("Team 1");
		team1 = teamDAO.createTeam(team1);
		team2 = new Team();
		team2.setName("Team 2");
		team2 = teamDAO.createTeam(team2);
		
		game = new Game();
		game.setTeam1(team1);
		game.setTeam2(team2);
	}
	
	@Test
	public void testInsert() {
		Game result = gameDAO.createGame(game);
		assertEquals(team1.getId(), result.getTeam1().getId());
		assertEquals(team2.getId(), result.getTeam2().getId());
		assertNotNull(game.getId());
	}
	
	@Test
	public void testUpdate() {
		Game result = gameDAO.createGame(game);
		result.setTeam1(team2);
		result.setTeam2(team1);
		Game updated = gameDAO.updateGame(result);
		assertEquals(result.getTeam1().getId(), updated.getTeam1().getId());
		assertEquals(result.getTeam2().getId(), updated.getTeam2().getId());
	}
	
	@Test
	public void testDelete() {
		Game result = gameDAO.createGame(game);
		assertNotNull(result);
		gameDAO.deleteGame(result);
		Game deleted = gameDAO.getGameById(result.getId());
		assertNull(deleted);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		
		gameDAO.close();
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
		sqLiteHelper.truncateTables();
		sqLiteHelper.close();
	}
}
