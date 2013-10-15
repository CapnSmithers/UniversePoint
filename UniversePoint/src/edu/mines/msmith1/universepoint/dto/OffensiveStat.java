package edu.mines.msmith1.universepoint.dto;

import android.content.res.Resources;
import edu.mines.msmith1.universepoint.R;

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
		StringBuilder sb = new StringBuilder();
		Resources resources = Resources.getSystem();
		if (getPlayer() != null) {
			sb.append(getPlayer().getName() + resources.getString(R.string.scoredAPoint));
		}
		if (getAssistingPlayer() != null) {
			sb.append(getAssistingPlayer().getName() + resources.getString(R.string.assisted));
		}
		if (getTurnoverPlayer() != null) {
			sb.append(getTurnoverPlayer().getName() + resources.getString(R.string.turnover));
		}
		return sb.toString();
	}

}
