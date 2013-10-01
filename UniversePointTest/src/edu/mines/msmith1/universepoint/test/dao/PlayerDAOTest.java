package edu.mines.msmith1.universepoint.test.dao;

import org.junit.Test;

import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dao.PlayerDAO;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.Player;
import edu.mines.msmith1.universepoint.dto.Team;
import edu.mines.msmith1.universepoint.test.DatabaseBaseTest;

public class PlayerDAOTest extends DatabaseBaseTest {
	PlayerDAO playerDAO;
	Player player;
	Team team;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();

		playerDAO = new PlayerDAO(context);
		playerDAO.open();
		
		// add team to db
		team = new Team();
		team.setName("Test Team");
		TeamDAO teamDAO = new TeamDAO(context);
		teamDAO.open();
		team = teamDAO.createTeam(team);
		teamDAO.close();
		
		player = new Player();
		player.setName("Test Person");
		player.setTeam(team);
	}
	
	@Test
	public void testInsert() {
		Player result = playerDAO.createPlayer(player);
		assertEquals("Test Person", result.getName());
		assertNotNull(result.getId());
	}
	
	@Test
	public void testUpdate() {
		Player result = playerDAO.createPlayer(player);
		result.setName("New Name");
		Player updated = playerDAO.updatePlayer(result);
		assertEquals("New Name", updated.getName());
		assertEquals(result.getId(), updated.getId());
	}
	
	@Test
	public void testDelete() {
		Player result = playerDAO.createPlayer(player);
		assertNotNull(result);
		playerDAO.deletePlayer(result);
		Player deleted = playerDAO.getPlayerById(result.getId());
		assertNull(deleted);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		
		playerDAO.close();
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
		sqLiteHelper.truncateTables();
		sqLiteHelper.close();
	}
}
