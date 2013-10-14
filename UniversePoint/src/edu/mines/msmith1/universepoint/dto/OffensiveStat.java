package edu.mines.msmith1.universepoint.dto;

/**
 * POJO representation of offensive_stat table. Implicitly represents a
 * single point.
 * @author vanxrice
 */
public class OffensiveStat extends BaseDTO {
	private Game game;
	private Player player, assistingPlayer, turnoverPlayer;
	private Team team;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getAssistingPlayer() {
		return assistingPlayer;
	}

	public void setAssistingPlayer(Player assistingPlayer) {
		this.assistingPlayer = assistingPlayer;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Player getTurnoverPlayer() {
		return turnoverPlayer;
	}

	public void setTurnoverPlayer(Player turnoverPlayer) {
		this.turnoverPlayer = turnoverPlayer;
	}

	@Override
	public String toString() {
		// TODO I don't think we will use this.
		return null;
	}

}
