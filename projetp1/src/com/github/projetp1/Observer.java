package com.github.projetp1;

/**
 * This Interface is to be complied by classes who wants to be notified of the changes
 * occuring in a different class.
 */
public interface Observer
{
	
	/**
	 * Adds an observateur.
	 *
	 * @param obs the obs
	 */
	public void addObservateur(Observateur obs);

	/**
	 * Update observateur.
	 */
	public void updateObservateur();

	/**
	 * Deletes the observateur.
	 */
	public void delObservateur();
}