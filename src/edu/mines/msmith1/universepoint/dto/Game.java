package edu.mines.msmith1.universepoint.dto;


public class Game extends BaseDTO {
	private Team team1, team2;

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}
	
	@Override
	public String toString() {
		return team1.getName() + " versus " + team2.getName();
	}
}
