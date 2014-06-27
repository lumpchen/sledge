/**
 * Created by IntelliJ IDEA.
 * User: ydu
 * Date: Jul 31, 2003
 * Time: 1:45:28 PM
 * To change this template use Options | File Templates.
 */
package com.docscience.pathfinder.font.driver;

import java.io.IOException;

@SuppressWarnings("serial")
public class FontException extends IOException {
	
	public FontException() {
		super();
	}
	
	public FontException(String msg) {
		super(msg);
	}
	
	public FontException(String msg, Throwable e) {
		super(msg, e);
	}

}
