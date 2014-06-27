package com.docscience.pathfinder.font.driver.afp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxin
 *
 */
public class SFBeginCodePage {
	
	private static final int NAME_LENGTH = 8;
	
	private String name;
	private List<MODCATriplet> tripletList = new ArrayList<MODCATriplet>();
	private TLocalDateAndTimeStamp creationDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_BCP) {
			throw new IllegalArgumentException("invalid structure field id for begin code page");
		}
		if (field.getSFParameterLength() == 0) {
			// BFN can contain nothing.
			return;
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		name = parameters.getString(0, NAME_LENGTH).trim();
		MODCATriplet[] triplets = MODCATriplet.parseTriplets(parameters, NAME_LENGTH, parameters.size() - NAME_LENGTH);
		for (int i=0; i<triplets.length; ++i) {
			MODCATriplet triplet = triplets[i];
			switch (triplet.getTID()) {
			case MODCATriplet.ID_LOCAL_DATA_AND_TIME_STAMP: {
				creationDate = new TLocalDateAndTimeStamp();
				creationDate.setTriplet(triplet);
				break;
			}
			case MODCATriplet.ID_RESOURCE_MANAGEMENT: {
				// do nothing here for now
				break;
			}
			case MODCATriplet.ID_OBJECT_ORIGIN_IDENTIFIER: {
				// do nothing here for now
				break;
			}
			case MODCATriplet.ID_USER_COMMENT: {
				// do nothing here for now
				break;
			}
			default:
				// ignore other triplets
			}
			this.tripletList.add(triplet);
		}
	}
	
	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_BCP);
		if (name != null) {
			field.getSFParameterData().putString(name, NAME_LENGTH);
		}
		// add more triplets here
		return field;
	}
	
}
