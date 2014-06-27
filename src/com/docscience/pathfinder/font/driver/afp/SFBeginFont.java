package com.docscience.pathfinder.font.driver.afp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxin
 *
 */
public class SFBeginFont {
		
	private static final int NAME_LENGTH = 8;
	
	private String name;
	private TLocalDateAndTimeStamp creationDate;
	private TResourceManagement resourceManagement;
	
	private List<MODCATriplet> tripletList = new ArrayList<MODCATriplet>(); // should be removed when all triplet is supported.

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public TLocalDateAndTimeStamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(TLocalDateAndTimeStamp creationDate) {
		this.creationDate = creationDate;
	}

	public TResourceManagement getResourceManagement() {
		return resourceManagement;
	}

	public void setResourceManagement(TResourceManagement resourceManagement) {
		this.resourceManagement = resourceManagement;
	}

	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_BFN) {
			throw new IllegalArgumentException("invalid structure field id for begin font");
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
				resourceManagement = new TResourceManagement();
				resourceManagement.setTriplet(triplet);
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
		field.setSFID(MODCAStructureField.ID_BFN);
		
		MODCAByteBuffer parameters = field.getSFParameterData();
		if (name != null) {
			parameters.putString(name, NAME_LENGTH);
		}
		if (creationDate != null) {
//			creationDate.getTriplet().save(parameters);
		}
		if (resourceManagement != null) {
//			resourceManagement.getTriplet().save(parameters);
		}
		// add more triplets here
		return field;
	}
	
}
