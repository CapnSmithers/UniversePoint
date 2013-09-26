package edu.mines.msmith1.universepoint.dto;

public class Player {
	private long id;
	private String name;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	// This will be used by the view classes
	@Override
	public String toString() {
		return getName();
	}
}
