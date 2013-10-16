package edu.mines.msmith1.universepoint.dto;

import android.view.View;

/**
 * Base class for DTO objects. Subclasses are required to implement toString() to reduce
 * {@link View} complexity.
 * @author vanxrice
 */
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
