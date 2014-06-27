package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public interface PSStructure {

	public PSObject getPSObject();
	
	public void setPSObject(PSObject psObject) throws PSStructureException;
	
}
