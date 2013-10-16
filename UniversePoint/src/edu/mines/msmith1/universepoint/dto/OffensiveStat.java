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
	public String toString() { // we had to use string literals because using the Resources.getSystem().getString() was throwing null pointer exceptions.
		StringBuilder sb = new StringBuilder();
		if (getPlayer() != null) {
			sb.append(getPlayer().getName() + " scored a point.");
		}
		if (getAssistingPlayer() != null) {
			sb.append(getAssistingPlayer().getName() + " made an assist.");
		}
		if (getTurnoverPlayer() != null) {
			sb.append(getTurnoverPlayer().getName() + " had a turnover.");
		}
		if (sb.toString().length() == 0) {
			sb.append(team.getName() + " scored a point.");
		}
		return sb.toString();
	}

}
