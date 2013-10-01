package edu.mines.msmith1.universepoint.dto;

public class Player extends BaseDTO {
	private String name;
	private Team team;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	// This will be used by the view classes
	@Override
	public String toString() {
		return getName();
	}
}
