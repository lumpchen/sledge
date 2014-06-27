package com.docscience.pathfinder.font.driver.ps;

import com.docscience.pathfinder.font.driver.FontException;

/**
 * @author wxin
 *
 */
public class PSStructureException extends FontException {

	private static final long serialVersionUID = 2776806864601969518L;

	public PSStructureException(String message) {
		super(message);
	}
	
	public PSStructureException(String message, Throwable e) {
		super(message);
		initCause(e);
	}
	
}
