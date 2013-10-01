package edu.mines.msmith1.universepoint.dto;

/**
 * POJO representation of Team table.
 * @author vanxrice
 */
public class Team extends BaseDTO {
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
