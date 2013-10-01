package edu.mines.msmith1.universepoint.dto;

public abstract class BaseDTO {
	long id;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	abstract public String toString();
}
