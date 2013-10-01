package edu.mines.msmith1.universepoint.test.dao;

import org.junit.Test;

import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.Team;
import edu.mines.msmith1.universepoint.test.DatabaseBaseTest;

public class TeamDAOTest extends DatabaseBaseTest {
	TeamDAO teamDAO;
	Team team;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		teamDAO = new TeamDAO(context);
		teamDAO.open();
		
		team = new Team();
		team.setName("Test Team");
	}
	
	@Test
	public void testInsert() {
		Team result = teamDAO.createTeam(team);
		assertEquals("Test Team", result.getName());
		assertNotNull(result.getId());
	}
	
	@Test
	public void testUpdate() {
		Team result = teamDAO.createTeam(team);
		result.setName("New Name");
		Team updated = teamDAO.updateTeam(result);
		assertEquals("New Name", updated.getName());
		assertEquals(result.getId(), updated.getId());
	}
	
	@Test
	public void testDelete() {
		Team result = teamDAO.createTeam(team);
		assertNotNull(result);
		teamDAO.deleteTeam(result);
		Team deleted = teamDAO.getTeamById(result.getId());
		assertNull(deleted);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		
		teamDAO.close();
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
		sqLiteHelper.truncateTables();
		sqLiteHelper.close();
	}
}
