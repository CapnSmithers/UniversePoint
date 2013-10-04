package edu.mines.msmith1.universepoint.dto;

/**
 * POJO representation of offensive_stat table. Implicitly represents a
 * single point.
 * @author vanxrice
 */
public class OffensiveStat extends BaseDTO {
	private Game game;
	private Player player, assistingPlayer;

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

	@Override
	public String toString() {
		// TODO I don't think we will use this.
		return null;
	}

}
