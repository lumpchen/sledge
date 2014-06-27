package com.docscience.pathfinder.font.driver.type1;

import com.docscience.pathfinder.font.driver.ps.PSAccess;
import com.docscience.pathfinder.font.driver.ps.PSArray;
import com.docscience.pathfinder.font.driver.ps.PSBoolean;
import com.docscience.pathfinder.font.driver.ps.PSDictionary;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSReal;
import com.docscience.pathfinder.font.driver.ps.PSStructure;

/**
 * @author wxin
 *
 */
public class Type1PrivateDictionary implements PSStructure {

	protected PSDictionary psDict = new PSDictionary();
		
	@Override
	public PSObject getPSObject() {
		return psDict;
	}
	
	@Override
	public void setPSObject(PSObject psObject) {
		if (!psObject.isDictionary()) {
			throw new IllegalArgumentException("psObject must be dictionary");
		}
		psDict = (PSDictionary) psObject;
	}

	public void setRD(PSArray array) {
		if (!array.isExecutable()) {
			throw new IllegalArgumentException("array is not executable");
		}
		psDict.put(PSName.RD, array);
	}
	
	public void setND(PSArray array) {
		if (!array.isExecutable()) {
			throw new IllegalArgumentException("array is not executable");
		}
		psDict.put(PSName.ND, array);
	}
	
	public void setNP(PSArray array) {
		if (!array.isExecutable()) {
			throw new IllegalArgumentException("array is not executable");
		}
		psDict.put(PSName.NP, array);
	}
	
	public void addSubrs(int index, byte[] subrs) {
		assert(false) : "not implement yet";
	}
	
	public void addOtherSubrs(int index, byte[] subrs) {
		assert(false) : "not implement yet";
	}
	
	public void setUniqueID(int uid) {
		psDict.put(PSName.UniqueID, PSInteger.valueOf(uid));
	}
	
	public void setBlueValues(int[] values) {
		psDict.put(PSName.BlueValues, PSArray.convert(values));
	}
	
	public void setOtherBlues(int[] values) {
		psDict.put(PSName.OtherBlues, PSArray.convert(values));
	}
	
	public void setFamilyBlues(int[] values) {
		psDict.put(PSName.FamilyBlues, PSArray.convert(values));
	}
	
	public void setFamilyOtherBlues(int[] values) {
		psDict.put(PSName.FamilyOtherBlues, PSArray.convert(values));
	}
	
	public void setBlueScale(double value) {
		psDict.put(PSName.BlueScale, new PSReal(value));
	}
	
	public void setBlueShift(int value) {
		psDict.put(PSName.BlueShift, PSInteger.valueOf(value));
	}
	
	public void setBlueFuzz(int value) {
		psDict.put(PSName.BlueFuzz, PSInteger.valueOf(value));
	}
	
	public void setStdHW(double[] values) {
		psDict.put(PSName.StdHW, PSArray.convert(values));
	}
	
	public void setStdVW(double[] values) {
		psDict.put(PSName.StdVW, PSArray.convert(values));
	}
	
	public void setStemSnapH(double[] values) {
		psDict.put(PSName.StemSnapH, PSArray.convert(values));
	}
	
	public void setStemSnapV(double[] values) {
		psDict.put(PSName.StemSnapV, PSArray.convert(values));
	}
	
	public void setForceBold(boolean value) {
		psDict.put(PSName.ForceBold, PSBoolean.valueOf(value));
	}
	
	public void setLanguageGroup(int value) {
		psDict.put(PSName.LanguageGroup, PSInteger.valueOf(value));
	}
	
	public void setPassword(int value) {
		psDict.put(PSName.password, PSInteger.valueOf(value));
	}
	
	public void setLenIV(int value) {
		psDict.put(PSName.lenIV, PSInteger.valueOf(value));
	}
	
	public void setMinFeature(PSArray array) {
		psDict.put(PSName.MinFeature, array);
	}
	
	public void setRndStemUp(boolean value) {
		psDict.put(PSName.RndStemUp, PSBoolean.valueOf(value));
	}
	
	public void setExpansionFactor(double value) {
		psDict.put(PSName.ExpansionFactor, new PSReal(value));
	}
	
	/**
	 * define RD as "{string currentfile exch readstring pop} executeonly"
	 */
	public void defineRD() {
		PSArray psArray = new PSArray();
		psArray.setExecutable(true);
		psArray.setAccess(PSAccess.AccessExecuteOnly);
		psArray.add(new PSName("string", true));
		psArray.add(new PSName("currentfile", true));
		psArray.add(new PSName("exch", true));
		psArray.add(new PSName("readstring", true));
		psArray.add(new PSName("pop", true));
		psDict.put(PSName.RD, psArray);
	}
	
	/**
	 * define "-|" as "{string currentfile exch readstring pop} executeonly"
	 */
	public void defineRDAlter() {
		PSArray psArray = new PSArray();
		psArray.setExecutable(true);
		psArray.setAccess(PSAccess.AccessExecuteOnly);
		psArray.add(new PSName("string", true));
		psArray.add(new PSName("currentfile", true));
		psArray.add(new PSName("exch", true));
		psArray.add(new PSName("readstring", true));
		psArray.add(new PSName("pop", true));
		psDict.put(new PSName("-|"), psArray);		
	}
	
	/**
	 * define ND as "{noaccess def} executeonly"
	 */	
	public void defineND() {
		PSArray psArray = new PSArray();
		psArray.setExecutable(true);
		psArray.setAccess(PSAccess.AccessExecuteOnly);
		psArray.add(new PSName("noaccess", true));
		psArray.add(new PSName("def", true));
		psDict.put(PSName.ND, psArray);
	}

	/**
	 * define "|-" as "{noaccess def} executeonly"
	 */	
	public void defineNDAlter() {
		PSArray psArray = new PSArray();
		psArray.setExecutable(true);
		psArray.setAccess(PSAccess.AccessExecuteOnly);
		psArray.add(new PSName("noaccess", true));
		psArray.add(new PSName("def", true));
		psDict.put(new PSName("|-"), psArray);
	}
	
	/**
	 * define NP as "{noaccess put} executeonly"
	 */
	public void defineNP() {
		PSArray psArray = new PSArray();
		psArray.setExecutable(true);
		psArray.setAccess(PSAccess.AccessExecuteOnly);
		psArray.add(new PSName("noaccess", true));
		psArray.add(new PSName("put", true));
		psDict.put(PSName.NP, psArray);
	}
	
	/**
	 * define "|" as "{noaccess put} executeonly"
	 */
	public void defineNPAlter() {
		PSArray psArray = new PSArray();
		psArray.setExecutable(true);
		psArray.setAccess(PSAccess.AccessExecuteOnly);
		psArray.add(new PSName("noaccess", true));
		psArray.add(new PSName("put", true));
		psDict.put(new PSName("|"), psArray);
	}
	
	/**
	 * define MinFeature as "{16 16}"
	 */
	public void defineMinFeature() {
		PSArray psArray = new PSArray();
		psArray.setExecutable(true);
		psArray.add(PSInteger.valueOf(16));
		psArray.add(PSInteger.valueOf(16));
		psDict.put(PSName.MinFeature, psArray);
	}
	
	/**
	 * define password as "5839"
	 */
	public void definePassword() {
		psDict.put(PSName.password, PSInteger.valueOf(5839));
	}
	
}
