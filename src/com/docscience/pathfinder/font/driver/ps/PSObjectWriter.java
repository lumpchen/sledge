package com.docscience.pathfinder.font.driver.ps;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author wxin
 *
 */
public class PSObjectWriter extends PSWriter {

	private boolean wellFormated = false;
	
	public PSObjectWriter(OutputStream ostream) {
		super(ostream);
	}
	
	public void setWellFormated(boolean b) {
		wellFormated = b;
	}
	
	public void psObject(PSObject psObject) throws IOException {
		switch(psObject.getType()) {
		case PSObject.TYPE_NULL:
			executeName("null");
			break;
		case PSObject.TYPE_BOOLEAN:
			bool(((PSBoolean) psObject).booleanValue());
			break;
		case PSObject.TYPE_INTEGER:
			integer(((PSInteger) psObject).intValue());
			break;
		case PSObject.TYPE_REAL:
			real(((PSReal) psObject).doubleValue());
			break;
		case PSObject.TYPE_NAME:
			if (psObject.isExecutable()) {
				executeName(((PSName) psObject).getName());				
			}
			else {
				literalName(((PSName) psObject).getName());
			}
			break;
		case PSObject.TYPE_ARRAY:
			writeArray((PSArray) psObject);
			break;
		case PSObject.TYPE_DICTIONARY:
			writeDictionary((PSDictionary) psObject);
			break;
		case PSObject.TYPE_PACKEDARRAY:
			assert(false) : "not implemented yet";
			break;
		case PSObject.TYPE_STRING: 
			{
			PSString string = (PSString) psObject;
			string(string.getBytes(), string.getFormat());
			}
			break;
		default:
			throw new IllegalArgumentException("incorrect object for write: " + psObject.toString());
		}
		
		if (psObject.isComposite()) {
			PSAccess access = ((PSCompositeObject) psObject).getAccess();
			if (access == PSAccess.AccessReadOnly) {
				executeName("readonly");
			}
			else if (access == PSAccess.AccessExecuteOnly) {
				executeName("executeonly");
			}
			else if (access == PSAccess.AccessNone) {
				executeName("noaccess");
			}
		}
	}

	private void writeDictionary(PSDictionary psDict) throws IOException {
		Iterator<Entry<PSObject, PSObject>> itr = psDict.entrySet().iterator();
		if (psDict.size() == 0) {
			integer(1);
		}
		else {
			integer(psDict.getCapacity());
		}
		executeName("dict");
		executeName("dup");
		executeName("begin");
		if (wellFormated) {
			newLine();
		}
		while (itr.hasNext()) {
			Entry<PSObject, PSObject> entry = itr.next();
			psObject(entry.getKey());
			psObject(entry.getValue());
			executeName("def");
			if (wellFormated) {
				newLine();
			}
		}
		if (wellFormated) {
			newLine();
		}
		executeName("end");
	}

	private void writeArray(PSArray psArray) throws IOException {
		if (!psArray.isExecutable()) {
			if (psArray.isIndexFormat()) {
				integer(psArray.size());
				executeName("array");
				if (wellFormated) {
					newLine();
				}
				for (int i=0; i<psArray.size(); ++i) {
					executeName("dup");
					integer(i);
					psObject(psArray.get(i));
					executeName("put");
					if (wellFormated) {
						newLine();
					}
				}
			}
			else {
				beginArray();
				for (int i=0; i<psArray.size(); ++i) {
					psObject(psArray.get(i));
				}
				endArray();
			}
		}
		else {
			beginProcedure();
			for (int i=0; i<psArray.size(); ++i) {
				psObject(psArray.get(i));
			}				
			endProcedure();
		}
	}



}
