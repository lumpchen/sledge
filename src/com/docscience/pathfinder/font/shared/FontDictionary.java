package com.docscience.pathfinder.font.shared;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class FontDictionary implements Serializable {

	public static final String COPYRIGHT_NOTICE = "Copyright Notice";
	public static final String FAMILY_NAME = "Family Name";
	public static final String SUBFAMILY_NAME = "Subfamily Name";
	public static final String UNIQUE_IDENTIFIER = "Unique Identifier";
	public static final String FULL_NAME = "Full Name";
	public static final String VERSION = "Version";
	public static final String POSTSCRIPT_NAME = "PostScript Name";
	public static final String TRADEMARK = "Trademark";
	public static final String MANUFACTURER = "Manufacturer";
	public static final String DESIGNER = "Designer";
	public static final String DESCRIPTION = "Description";
	public static final String VENDOR_URL = "Vendor URL";
	public static final String DESIGNER_URL = "Designer URL";
	public static final String LICENSE_DESCRIPTION = "License Description";
	public static final String LICENSE_URL = "License URL";
	public static final String COMPATIBLE_FULL_NAME = "Compatible Full Name";
	public static final String SAMPLE_TEXT = "Sample Text";
	public static final String PREFERRED_FAMILY_NAME = "Preferred Family Name";
	public static final String PREFERRED_SUBFAMILY_NAME = "Preferred Subfamily Name";
	public static final String POSTSCRIPT_CID_FINDFONT_NAME = "PostScript CID FindFont Name";

	private String locale = Font.EN_US;

	private HashMap<String, String> names = new HashMap<String, String>();

	public FontDictionary() {
		// do nothing here
	}
	
	public FontDictionary(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}
		
	public Map<String, String> getNames() {
		return Collections.unmodifiableMap(names);
	}

	public String getName(String key) {
		return names.get(key);
	}

	public void setName(String key, String name) {
		names.put(key, name);
	}

	public String getCopyrightNotice() {
		return getName(COPYRIGHT_NOTICE);
	}

	public void setCopyrightNotice(String name) {
		setName(COPYRIGHT_NOTICE, name);
	}
	
	public String getFamilyName() {
		return getName(FAMILY_NAME);
	}

	public void setFamilyName(String name) {
		setName(FAMILY_NAME, name);
	}
	
	public String getSubfamilyName() {
		return getName(SUBFAMILY_NAME);
	}

	public void setSubfamilyName(String name) {
		setName(SUBFAMILY_NAME, name);
	}
	
	public String getUniqueIdentifier() {
		return getName(UNIQUE_IDENTIFIER);
	}

	public void setUniqueIdentifier(String name) {
		setName(UNIQUE_IDENTIFIER, name);
	}

	public String getFullName() {
		return getName(FULL_NAME);
	}
	
	public void setFullName(String name) {
		setName(FULL_NAME, name);
	}

	public String getVersion() {
		return getName(VERSION);
	}
	
	public void setVersion(String name) {
		setName(VERSION, name);
	}

	public String getPostScriptFontName() {
		return getName(POSTSCRIPT_NAME);
	}

	public void setPostScriptFontName(String name) {
		setName(POSTSCRIPT_NAME, name);
	}

	public String getTrademark() {
		return getName(TRADEMARK);
	}

	public void setTrademark(String name) {
		setName(TRADEMARK, name);
	}

	public String getManufacturer() {
		return getName(MANUFACTURER);
	}

	public void setManufacturer(String name) {
		setName(MANUFACTURER, name);
	}

	public String getDesigner() {
		return getName(DESIGNER);
	}

	public void setDesigner(String name) {
		setName(DESIGNER, name);
	}

	public String getDescription() {
		return getName(DESCRIPTION);
	}

	public void setDescription(String name) {
		setName(DESCRIPTION, name);
	}

	public String getVendorUrl() {
		return getName(VENDOR_URL);
	}

	public void setVendorUrl(String name) {
		setName(VENDOR_URL, name);
	}

	public String getDesignerUrl() {
		return getName(DESIGNER_URL);
	}

	public void setDesignerUrl(String name) {
		setName(DESIGNER_URL, name);
	}

	public String getLicenseDescription() {
		return getName(LICENSE_DESCRIPTION);
	}

	public void setLicenseDescription(String name) {
		setName(LICENSE_DESCRIPTION, name);
	}

	public String getLicenseUrl() {
		return getName(LICENSE_URL);
	}

	public void setLicenseUrl(String name) {
		setName(LICENSE_URL, name);
	}

	public String getCompatibleFullName() {
		return getName(COMPATIBLE_FULL_NAME);
	}

	public void setCompatibleFullName(String name) {
		setName(COMPATIBLE_FULL_NAME, name);
	}

	public String getSampleText() {
		return getName(SAMPLE_TEXT);
	}

	public void setSampleText(String name) {
		setName(SAMPLE_TEXT, name);
	}

	public String getPreferredFamilyName() {
		return getName(PREFERRED_FAMILY_NAME);
	}
	
	public void setPreferredFamilyName(String name) {
		setName(PREFERRED_FAMILY_NAME, name);
	}
	
	public String getPreferredSubfamilyName() {
		return getName(PREFERRED_SUBFAMILY_NAME);
	}
	
	public void setPreferredSubfamilyName(String name) {
		setName(PREFERRED_SUBFAMILY_NAME, name);
	}
	
	public String getPostScriptCidFindFontName() {
		return getName(POSTSCRIPT_CID_FINDFONT_NAME);
	}
	
	public void setPostScriptCidFindFontName(String name) {
		setName(POSTSCRIPT_CID_FINDFONT_NAME, name);
	}

}
