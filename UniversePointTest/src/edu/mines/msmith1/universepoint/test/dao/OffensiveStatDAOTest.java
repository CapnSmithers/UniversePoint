package edu.mines.msmith1.universepoint.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dao.OffensiveStatDAO;
import edu.mines.msmith1.universepoint.dto.Game;
import edu.mines.msmith1.universepoint.dto.OffensiveStat;
import edu.mines.msmith1.universepoint.dto.Player;
import edu.mines.msmith1.universepoint.dto.Team;
import edu.mines.msmith1.universepoint.test.DatabaseBaseTest;
import edu.mines.msmith1.universepoint.test.util.DatabaseUtil;

public class OffensiveStatDAOTest extends DatabaseBaseTest {
	OffensiveStatDAO offensiveStatDAO;
	OffensiveStat offensiveStat;
	Game game;
	Team team1, team2;
	Player player, assistingPlayer;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		offensiveStatDAO = new OffensiveStatDAO(context);
		offensiveStatDAO.open();
		
		// add teams to db
		team1 = DatabaseUtil.addTeamToDatabase(context, "Test 1");
		team2 = DatabaseUtil.addTeamToDatabase(context, "Test 2");
		
		// add game
		game = DatabaseUtil.addGameToDatabase(context, team1, team2);
		
		// add players
		player = DatabaseUtil.addPlayerToDatabase(context, "Player 1", team1);
		assistingPlayer = DatabaseUtil.addPlayerToDatabase(context, "Player 2", team1);
		
		// create offensive stat
		offensiveStat = new OffensiveStat();
		offensiveStat.setGame(game);
		offensiveStat.setTeam(team1);
		offensiveStat.setPlayer(player);
		offensiveStat.setAssistingPlayer(assistingPlayer);
	}
	
	@Test
	public void testInsert() {
		OffensiveStat result = offensiveStatDAO.createOffensiveStat(offensiveStat);
		assertEquals(game.getId(), result.getGame().getId());
		assertEquals(player.getId(), result.getPlayer().getId());
		assertEquals(assistingPlayer.getId(), result.getAssistingPlayer().getId());
		assertNotNull(result.getId());
	}
	
	@Test
	public void testInsertWithoutAssistingPlayer() {
		OffensiveStat offensiveStatWithoutAssist = offensiveStat;
		offensiveStatWithoutAssist.setAssistingPlayer(null);
		OffensiveStat result = offensiveStatDAO.createOffensiveStat(offensiveStatWithoutAssist);
		assertNotNull(result.getId());
		assertNull(result.getAssistingPlayer());
	}
	
	@Test
	public void testInsertList() {
		List<OffensiveStat> offensiveStats = new ArrayList<OffensiveStat>();
		offensiveStats.add(offensiveStat);
		offensiveStats.add(offensiveStat);
		List<OffensiveStat> result = offensiveStatDAO.createOffensiveStats(offensiveStats);
		assertEquals(2, result.size());
		assertNotNull(result.get(0).getId());
		assertNotNull(result.get(1).getId());
	}
	
	@Test
	public void testDelete() {
		OffensiveStat result = offensiveStatDAO.createOffensiveStat(offensiveStat);
		assertNotNull(result);
		offensiveStatDAO.deleteOffensiveStat(result);
		OffensiveStat deleted = offensiveStatDAO.getOffensiveStatById(result.getId());
		assertNull(deleted);
	}
	
	@Test
	public void testGetAllOffensiveStatsForPlayer() {
		offensiveStatDAO.createOffensiveStat(offensiveStat);
		offensiveStatDAO.createOffensiveStat(offensiveStat);
		List<OffensiveStat> result = offensiveStatDAO.getAllOffensiveStatsForPlayer(player);
		assertNotNull(result);
		assertEquals(2, result.size());
	}
	
	@Test
	public void testGetScoreForTeam() {
		OffensiveStat stat1 = offensiveStatDAO.createOffensiveStat(offensiveStat);
		List<OffensiveStat> result = offensiveStatDAO.getScoreForTeam(team1, game);
		assertEquals(1, result.size());
		assertEquals(stat1.getId(), result.get(0).getId());
		assertEquals(team1.getId(), result.get(0).getTeam().getId());
	}
	
	@Test
	public void testInsertOffensiveStatWithoutPlayer() {
		OffensiveStat anonOffensiveStat = offensiveStat;
		anonOffensiveStat.setPlayer(null);
		anonOffensiveStat.setAssistingPlayer(null);
		OffensiveStat result = offensiveStatDAO.createOffensiveStat(anonOffensiveStat);
		assertNotNull(result);
		assertNull(result.getPlayer());
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		
		offensiveStatDAO.close();
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
		sqLiteHelper.truncateTables();
		sqLiteHelper.close();
	}
}
