package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.type1.Type1CharString;
import com.docscience.pathfinder.font.driver.type2.Type2CharString;
import com.docscience.pathfinder.font.driver.type2.Type2CharStringException;

/**
 * @author wxin
 *
 */
public class CFFWrapper {
	
	private CFFRandomReader cffRandomReader;
	private CFFFile cffFile;

	private CFFHeader      cffHeader;
	private CFFNameIndex   cffNames;
	private CFFStringIndex cffStrings;
	@SuppressWarnings("unused")
	private CFFIndexData   cffTopIndex;
	private CFFDictData    cffTopDict;
    private CFFDictData[]  cffPrivateDicts;
    private CFFDictData[]  cffFDArray;
	private CFFFDSelect    cffFDSelect;

	private int            cffCharStringType;
	private CFFSubrIndex   cffGlobalSubrs;
    private CFFSubrIndex[] cffLocalSubrsArray;
	private CFFSubrIndex   cffCharStrings;
	
	private CFFCharsets    cffCharsets;
	private CFFEncodings   cffEncodings;
	
    private int[]          cffCID2GIDMap; 
    private boolean        cidFont;
    private int            cidCount;
    private double[]       defaultWidths;
    private double[]       nominalWidths;
    
	public CFFWrapper(CFFRandomReader reader) throws CFFFormatException, IOException {
		cffRandomReader = reader;
		load();
	}
	
	private void load() throws CFFFormatException, IOException {
        cffFile = new CFFFile();
        cffFile.read(cffRandomReader);

        cffHeader      = cffFile.getHeader();
        cffNames       = cffFile.getNameIndex();
        cffTopIndex    = cffFile.getTopIndex();
        cffStrings     = cffFile.getStringIndex();
        cffGlobalSubrs = cffFile.getGlobalSubrIndex();
        
        // load Top Dict
        cffTopDict = new CFFDictData();
        cffTopDict.read(
        		cffFile.getTopIndex().getDataPosition(0), 
        		cffFile.getTopIndex().getDataLength(0), 
        		cffRandomReader); // we don't support multi-master font for now!
        
        // load CharStringType
        CFFDictData.Value v;
        v = cffTopDict.get(CFFDictData.Key.KEY_CHARSTRINGTYPE);
        if (v != null) {
            if (!v.isNumber() || (v.intValue() != 1 && v.intValue() != 2)) {
                throw new CFFFormatException("bad cff CharStringType value: " + v.toString());
            }
            cffCharStringType = v.intValue();
        }
        else {
            cffCharStringType = 2; // default type is Type2
        }

        // load Charstrings 
        v = cffTopDict.get(CFFDictData.Key.KEY_CHARSTRINGS);
        if (v == null || !v.isNumber()) {
            throw new CFFFormatException("bad charstings offset in private dict");
        }
        cffCharStrings = new CFFSubrIndex(cffCharStringType);
        cffCharStrings.read(v.intValue(), cffRandomReader);
               
        // load Charsets 
        v = cffTopDict.get(CFFDictData.Key.KEY_CHARSET);
        if (v != null) {
            if (!v.isNumber()) {
                throw new CFFFormatException("bad charsets offset in top dict");
            }
            int offset = v.intValue();
            switch(offset) {
            case 0:
                cffCharsets = CFFCharsets.ISOAdobeCharsets;
                break;
            case 1:
                cffCharsets = CFFCharsets.ExpertCharsets;
                break;
            case 2:
                cffCharsets = CFFCharsets.ExpertSubsetCharsets;
                break;
            default:
                cffCharsets = new CFFCharsets(cffCharStrings.getNumSubrs());
                cffCharsets.read(v.intValue(), cffRandomReader);
                break;
            }
        }
        else {
            cffCharsets = CFFCharsets.ISOAdobeCharsets;
        }
        
        if (cffTopDict.contains(CFFDictData.Key.KEY_ROS)) {
        	cidFont = true;
            loadCIDFont();
        }
        else {
        	cidFont = false;
            loadFont();
        }
	}

	private void loadCIDFont() throws CFFFormatException, IOException {
		CFFDictData.Value v;
		// CID font
		cidFont = true;
		
		v = cffTopDict.get(CFFDictData.Key.KEY_CIDCOUNT);
		if (v != null) {
		    cidCount = v.intValue();
		}
		else {
		    cidCount = 65536;
		}
		
		// load FDSelect
		v = cffTopDict.get(CFFDictData.Key.KEY_FDSELECT);
		if (v == null) {
		    throw new CFFFormatException("lost FDSelect in top dict of cid font");
		}
		cffFDSelect = new CFFFDSelect(cffCharStrings.getNumSubrs());
		cffFDSelect.read(v.intValue(), cffRandomReader);
		
		// load FDArray
		v = cffTopDict.get(CFFDictData.Key.KEY_FDARRAY);
		if (v == null) {
		    throw new CFFFormatException("lost FDArray in top dict of cid font");
		}
		CFFIndexData fdArrayIndex = new CFFIndexData();
		fdArrayIndex.read(v.intValue(), cffRandomReader);
		
		// set PrivateDict & LocalSubrs
		final int numFDs = fdArrayIndex.getNumData();
		cffFDArray    = new CFFDictData[numFDs];
		cffPrivateDicts    = new CFFDictData[numFDs];
		cffLocalSubrsArray = new CFFSubrIndex[numFDs];
		defaultWidths  = new double[numFDs];
		nominalWidths  = new double[numFDs];
		for (int i=0; i<fdArrayIndex.getNumData(); ++i) {
		    long offset = fdArrayIndex.getDataPosition(i);
		    long length = fdArrayIndex.getDataLength(i);
		    cffFDArray[i] = new CFFDictData();
		    cffFDArray[i].read(offset, length, cffRandomReader);
		    
		    v = cffFDArray[i].get(CFFDictData.Key.KEY_PRIVATE);
		    if (v == null) {
		        throw new CFFFormatException("bad private dict offset in FDArray dict");
		    }
		    long cffPriOffset = (long) v.arrayValue()[1];
		    long cffPriLength = (long) v.arrayValue()[0];
		    
		    cffPrivateDicts[i] = new CFFDictData();
		    cffPrivateDicts[i].read(cffPriOffset, cffPriLength, cffRandomReader);
		    
		    v = cffPrivateDicts[i].get(CFFDictData.Key.KEY_SUBRS);
		    if (v == null) {
		        cffLocalSubrsArray[i] = null;    
		    }
		    else {
		        cffLocalSubrsArray[i] = new CFFSubrIndex();
		        cffLocalSubrsArray[i].read(cffPriOffset + v.intValue(), cffRandomReader);
		    }
		    
		    v = cffPrivateDicts[i].get(CFFDictData.Key.KEY_DEFAULTWIDTHX);
		    if (v != null) {
		        defaultWidths[i] = v.doubleValue();
		    }
		    v = cffPrivateDicts[i].get(CFFDictData.Key.KEY_NOMINALWIDTHX);
		    if (v != null) {
		        nominalWidths[i] = v.doubleValue();
		    }
		}
	
		// load cid2gidMap from cffCharsets
		cffCID2GIDMap = new int[cidCount];
		for (int gid=0; gid<cffCharsets.getNumGlyphs(); ++gid) {
		    int cid = cffCharsets.getGlyphNameID(gid);
		    cffCID2GIDMap[cid] = gid;
		}
	}

	private void loadFont() throws CFFFormatException, IOException {
		CFFDictData.Value v;
		cidCount = cffStrings.getNumStrings() + CFFStringIndex.STD_STRINGS_NUM;
		
		// load PrivateDict and It's Position
		v = cffTopDict.get(CFFDictData.Key.KEY_PRIVATE);
		if (!v.isArray()) {
		    throw new CFFFormatException("bad private dict offset and length in top dict");
		}
		long cffPriOffset = (long) v.arrayValue()[1];
		long cffPriLength = (long) v.arrayValue()[0];
		cffPrivateDicts = new CFFDictData[] {new CFFDictData()};
		cffPrivateDicts[0].read(cffPriOffset, cffPriLength, cffRandomReader);
		
		// load Encoding
		v = cffTopDict.get(CFFDictData.Key.KEY_ENCODING);
		if (v != null) {
		    if (!v.isNumber()) {
		        throw new CFFFormatException("bad encoding offset in top dict");
		    }
		    int offset = v.intValue();
		    switch(offset) {
		    case 0:
		        cffEncodings = CFFEncodings.StandardEncoding;
		        break;
		    case 1:
		        cffEncodings = CFFEncodings.ExpertEncoding;
		        break;
		    default:
		        cffEncodings = new CFFEncodings(cffCharsets);
		        cffEncodings.read(v.intValue(), cffRandomReader);
		        break;
		    }
		}
		else {
		    cffEncodings = CFFEncodings.StandardEncoding;
		}
		        
		// load LocalSubrs
		v = cffPrivateDicts[0].get(CFFDictData.Key.KEY_SUBRS);
		if (v == null || !v.isNumber()) {
		    throw new CFFFormatException("bad local subr offset in private dict");
		}
		cffLocalSubrsArray = new CFFSubrIndex[] {new CFFSubrIndex(cffCharStringType)};
		cffLocalSubrsArray[0].read(cffPriOffset + v.intValue(), cffRandomReader);
			
		// set nominalWidth and defaultWidth;
		v = cffPrivateDicts[0].get(CFFDictData.Key.KEY_DEFAULTWIDTHX);
		if (v != null) {
		    defaultWidths = new double[] {v.doubleValue()};
		}
		v = cffPrivateDicts[0].get(CFFDictData.Key.KEY_NOMINALWIDTHX);
		if (v != null) {
		    nominalWidths = new double[] {v.doubleValue()};
		}
	}

	public CFFFile getCFFFile() {
		return cffFile;
	}

	public CFFHeader getHeader() {
		return cffHeader;
	}

	public CFFNameIndex getNames() {
		return cffNames;
	}

	public CFFStringIndex getStrings() {
		return cffStrings;
	}

	public CFFDictData getTopDict() {
		return cffTopDict;
	}
	
	public CFFDictData[] getPrivateDicts() {
		return (CFFDictData[]) cffPrivateDicts.clone();
	}

	public CFFDictData[] getFDArray() {
		return (CFFDictData[]) cffFDArray.clone();
	}

	public CFFFDSelect getFDSelect() {
		return cffFDSelect;
	}

	public int getCharStringType() {
		return cffCharStringType;
	}

	public CFFSubrIndex getGlobalSubrs() {
		return cffGlobalSubrs;
	}

	public CFFSubrIndex[] getLocalSubrsArray() {
		return (CFFSubrIndex[]) cffLocalSubrsArray.clone();
	}

	public CFFSubrIndex getCharStrings() {
		return cffCharStrings;
	}

	public CFFCharsets getCharsets() {
		return cffCharsets;
	}

	public CFFEncodings getEncodings() {
		return cffEncodings;
	}

	public int[] getCID2GIDMap() {
		return (int[]) cffCID2GIDMap.clone();
	}

	public boolean isCIDFont() {
		return cidFont;
	}

	public int getCIDCount() {
		return cidCount;
	}

	public double[] getDefaultWidths() {
		return (double[]) defaultWidths.clone();
	}

	public double[] getNominalWidths() {
		return (double[]) nominalWidths.clone();
	}

	public Type2CharString getType2CharString(int codepoint) throws Type2CharStringException {
		Type2CharString t2cs = null;
		if (cidFont) {
			int cid = codepoint;
			int gid = cffCID2GIDMap[cid]; // convert cid to gid in cff.
            int fid = cffFDSelect.getFDIndex(gid);
            byte[] cstr = cffCharStrings.getSubr(gid);
            t2cs = new Type2CharString(cffGlobalSubrs, cffLocalSubrsArray[fid], cstr);
		}
		else {
			byte[] cstr = cffCharStrings.getSubr(codepoint);
            t2cs = new Type2CharString(cffGlobalSubrs, cffLocalSubrsArray[0], cstr);
		}
		return t2cs;
	}
	
	public Type1CharString toType1CharString(Type2CharString t2cs, int fid) {
        Type1CharString t1cs = new Type1CharString();
        
        if (!Double.isNaN(t2cs.getDeltaWidth())) {
            t1cs.hsbw(0, (int) (t2cs.getDeltaWidth() + nominalWidths[fid]));
        }
        else {
            t1cs.hsbw(0, (int) defaultWidths[fid]);
        }
        
        for (int i=0; i<t2cs.getNumHStems(); ++i) {
            double[] hstems = t2cs.getHStem(i);
            for (int j=0; j<hstems.length; j += 2) {
                t1cs.hstem((int) hstems[j], (int) hstems[j+1]);
            }
        }
        
        for (int i=0; i<t2cs.getNumVStems(); ++i) {
            double[] vstems = t2cs.getVStem(i);
            for (int j=0; j<vstems.length; j += 2) {
                t1cs.vstem((int) vstems[j], (int) vstems[j+1]);
            }
        }
        
        int x = 0, y = 0;
        Type2CharString.PathNode[] path = t2cs.getPathNodes();
        for (int i=0; i<path.length; ++i) {
            Type2CharString.PathNode node = path[i];
            switch(node.tag) {
            case Type2CharString.PATH_MOVETO:
                t1cs.rmoveto((int) (node.x1 - x), (int) (node.y1 - y));
                x = (int) node.x1;
                y = (int) node.y1;
                break;
            case Type2CharString.PATH_LINETO:
                t1cs.rlineto((int) (node.x1 - x), (int) (node.y1 - y));
                x = (int) node.x1;
                y = (int) node.y1;
                break;
            case Type2CharString.PATH_CURVETO:
                int dx1 = (int) (node.x1 - x);
                int dy1 = (int) (node.y1 - y);
                int dx2 = (int) (node.x2 - node.x1);
                int dy2 = (int) (node.y2 - node.y1);
                int dx3 = (int) (node.x3 - node.x2);
                int dy3 = (int) (node.y3 - node.y2);
                t1cs.rrcurveto(dx1, dy1, dx2, dy2, dx3, dy3);
                x = (int) (node.x3);
                y = (int) (node.y3);
                break;
            case Type2CharString.PATH_CLOSE:
                t1cs.closepath();
                break;
            default:
                assert(false) : "never goes here";
            }
        }
        
        if (t2cs.getSEAC() != null) {
            Type2CharString.SEAC seac = t2cs.getSEAC();
            t1cs.seac(0, (int) (seac.adx), (int) (seac.ady), seac.bchar, seac.achar);
        }
        
        t1cs.endchar();
        
        return t1cs;
	}
	
	public int getCFFGID(int codepoint) {
		if (cidFont) {
			return cffCID2GIDMap[codepoint];
		}
		else {
			return codepoint;
		}
	}
	
	public int getCFFFID(int codepoint) {
		if (cidFont) {
			return cffFDSelect.getFDIndex(cffCID2GIDMap[codepoint]);
		}
		else {
			return 0;
		}
	}
	
	public String getRegistry() {
        CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_ROS);
        return cffStrings.getStringBySID((int) v.arrayValue()[0]);
	}
	
	public String getOrdering() {
        CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_ROS);
        return cffStrings.getStringBySID((int) v.arrayValue()[1]);
	}
	
	public int getSupplement() {
        CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_ROS);
        return (int) v.arrayValue()[2];
	}
	
	public double[] getFontBBox() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_FONTBBOX);
		return v.arrayValue();
	}
	
	public double[] getFontMatrix() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_FONTMATRIX);
		if (v == null) {
			return null;
		}
		return v.arrayValue();
	}
	
	public int[] getUIDBase() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_UIDBASE);
		if (v == null) {
			return null;
		}
		return new int[] {v.intValue()};
    }
	
	public int[] getXUID() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_XUID);
		if (v == null) {
			return null;
		}
	    double[] arr = v.arrayValue();
	    int[] xuid = new int[arr.length];
	    for (int i=0; i<arr.length; ++i) {
	    	xuid[i] = (int) arr[i];
	    }
	    return xuid;
    }

	public int[] getPaintType() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_PAINTTYPE);
        if (v == null) {
            return null;
        }
		return new int[] {v.intValue()};
	}

	public double[] getStrokeWidth() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_STROKEWIDTH);
        if (v == null) {
            return null;
        }
		return new double[] {v.doubleValue()};
	}

	public String getFamilyName() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_FAMILYNAME);
        if (v == null) {
        	return null;
        }
		return cffStrings.getStringBySID(v.intValue());
	}

	public String getFullName() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_FULLNAME);
        if (v == null) {
        	return null;
        }
		return cffStrings.getStringBySID(v.intValue());
	}

	public String getNotice() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_NOTICE);
        if (v == null) {
        	return null;
        }
		return cffStrings.getStringBySID(v.intValue());
	}

	public String getWeight() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_WEIGHT);
        if (v == null) {
        	return null;
        }
		return cffStrings.getStringBySID(v.intValue());
	}

	public String getVersion() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_VERSION);
        if (v == null) {
        	return null;
        }
		return cffStrings.getStringBySID(v.intValue());	
	}

	public String getCopyright() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_COPYRIGHT);
        if (v == null) {
        	return null;
        }
		return cffStrings.getStringBySID(v.intValue());
	}

	public boolean[] getIsFixedPitch() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_ISFIXEDPITCH);
        if (v == null) {
        	return null;
        }
		return new boolean[]{v.booleanValue()};
	}

	public double[] getItalicAngle() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_ITALICANGLE);
        if (v == null) {
        	return null;
        }
		return new double[]{v.doubleValue()};
	}

	public double[] getUnderlinePosition() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_UNDERLINEPOSITION);
        if (v == null) {
        	return null;
        }
		return new double[]{v.doubleValue()};
	}

	public double[] getUnderlineThickness() {
		CFFDictData.Value v = cffTopDict.get(CFFDictData.Key.KEY_UNDERLINETHICKNESS);
        if (v == null) {
        	return null;
        }
		return new double[]{v.doubleValue()};
	}
	
}
