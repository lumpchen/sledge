package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public final class PSName extends PSObject {

	private static final int EXECUTABLE = 1;
	private static final int NATIVED = 2;

	public static final PSName Encoding = new PSName("Encoding", NATIVED);
	public static final PSName FamilyName = new PSName("FamilyName", NATIVED);
	public static final PSName FontBBox = new PSName("FontBBox", NATIVED);
	public static final PSName FontID = new PSName("FontID", NATIVED);
	public static final PSName FontInfo = new PSName("FontInfo", NATIVED);
	public static final PSName FontMatrix = new PSName("FontMatrix", NATIVED);
	public static final PSName FontName = new PSName("FontName", NATIVED);
	public static final PSName FontType = new PSName("FontType", NATIVED);
	public static final PSName FullName = new PSName("FullName", NATIVED);
	public static final PSName isFixedPitch = new PSName("isFixedPitch", NATIVED);
	public static final PSName ItalicAngle = new PSName("ItalicAngle", NATIVED);
	public static final PSName LanguageLevel = new PSName("LanguageLevel", NATIVED);
	public static final PSName Notice = new PSName("Notice", NATIVED);
	public static final PSName PaintType = new PSName("PaintType", NATIVED);
	public static final PSName StrokeWidth = new PSName("StrokeWidth");
	public static final PSName UnderlinePosition = new PSName("UnderlinePosition", NATIVED);
	public static final PSName UnderlineThickness = new PSName("UnderlineThickness", NATIVED);
	public static final PSName UniqueID = new PSName("UniqueID", NATIVED);
	public static final PSName version = new PSName("version", NATIVED);
	public static final PSName Weight = new PSName("Weight", NATIVED);
	public static final PSName WMode = new PSName("WMode", NATIVED);
	public static final PSName XUID = new PSName("XUID", NATIVED);
	public static final PSName Metrics = new PSName("Metrics", NATIVED);
	public static final PSName Metrics2 = new PSName("Metrics2", NATIVED);
	public static final PSName CDevProc = new PSName("CDevProc", NATIVED);
	public static final PSName CharStrings = new PSName("CharStrings", NATIVED);
	public static final PSName Private = new PSName("Private", NATIVED);
	public static final PSName WeightVector = new PSName("WeightVector", NATIVED);
	public static final PSName BlueValues = new PSName("BlueValues", NATIVED);
	public static final PSName OtherBlues = new PSName("OtherBlues", NATIVED);
	public static final PSName FamilyBlues = new PSName("FamilyBlues", NATIVED);
	public static final PSName FamilyOtherBlues = new PSName("FamilyOtherBlues", NATIVED);
	public static final PSName BlueScale = new PSName("BlueScale", NATIVED);
	public static final PSName BlueShift = new PSName("BlueShift", NATIVED);
	public static final PSName BlueFuzz = new PSName("BlueFuzz", NATIVED);
	public static final PSName StdHW = new PSName("StdHW", NATIVED);
	public static final PSName StdVW = new PSName("StdVW", NATIVED);
	public static final PSName StemSnapH = new PSName("StemSnapH", NATIVED);
	public static final PSName StemSnapV = new PSName("StemSnapV", NATIVED);
	public static final PSName ForceBold = new PSName("ForceBold", NATIVED);
	public static final PSName LanguageGroup = new PSName("LanguageGroup", NATIVED);
	public static final PSName password = new PSName("password", NATIVED);
	public static final PSName lenIV = new PSName("lenIV", NATIVED);
	public static final PSName MinFeature = new PSName("MinFeature", NATIVED);
	public static final PSName RndStemUp = new PSName("RndStemUp", NATIVED);
	public static final PSName RD = new PSName("RD", NATIVED);
	public static final PSName ND = new PSName("ND", NATIVED);
	public static final PSName NP = new PSName("NP", NATIVED);
	public static final PSName ExpansionFactor = new PSName("ExpansionFactor", NATIVED);
	public static final PSName sfnts = new PSName("sfnts", NATIVED);
	public static final PSName CIDMap = new PSName("CIDMap", NATIVED);
	public static final PSName GDBytes = new PSName("GDBytes", NATIVED);
	public static final PSName GlyphDictionary = new PSName("GlyphDictionary", NATIVED);
	public static final PSName MetricsCount = new PSName("MetricsCount", NATIVED);
	public static final PSName Registry = new PSName("Registry", NATIVED);
	public static final PSName Ordering = new PSName("Ordering", NATIVED);
	public static final PSName Supplement = new PSName("Supplement", NATIVED);
	public static final PSName CIDFontType = new PSName("CIDFontType", NATIVED);
	public static final PSName CIDFontName = new PSName("CIDFontName", NATIVED);
	public static final PSName CIDSystemInfo = new PSName("CIDSystemInfo", NATIVED);
	public static final PSName UIDBase = new PSName("UIDBase", NATIVED);
	public static final PSName _notdef = new PSName(".notdef", NATIVED);
	public static final PSName GlyphNames2Unicode = new PSName("GlyphNames2Unicode", NATIVED);
	public static final PSName CIDFont = new PSName("CIDFont", NATIVED);
	public static final PSName Identity_H = new PSName("Identity-H", NATIVED);
	public static final PSName CIDCount = new PSName("CIDCount", NATIVED);
	public static final PSName CIDMapOffset = new PSName("CIDMapOffset", NATIVED);
	public static final PSName FDBytes = new PSName("FDBytes", NATIVED);
	public static final PSName GlyphData = new PSName("GlyphData", NATIVED);
	public static final PSName FDArray = new PSName("FDArray", NATIVED);
	public static final PSName SubrCount = new PSName("SubrCount", NATIVED);
	public static final PSName SDBytes = new PSName("SDBytes", NATIVED);
	public static final PSName SubrMapOffset = new PSName("SubrMapOffset", NATIVED);
	public static final PSName GlyphDirectory = new PSName("GlyphDirectory", NATIVED);
	public static final PSName FSType = new PSName("FSType", NATIVED);
	public static final PSName Copyright = new PSName("Copyright", NATIVED);
	public static final PSName Subrs = new PSName("Subrs", NATIVED);
	public static final PSName OtherSubrs = new PSName("OtherSubr", NATIVED);
	
	private String name;
	private int flags;
	
	public PSName(String name) {
		assert(name != null);
		this.name = name;
		this.flags = 0;
	}
	
	public PSName(String name, boolean executable) {
		assert(name != null);
		this.name = name;
		if (executable) {
			this.flags = EXECUTABLE;
		}
		else {
			this.flags = 0;
		}
	}
	
	private PSName(String name, int flag) {
		assert(name != null);
		this.name  = name;
		this.flags = flag;
	}
		
	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (this == that) {
			return true;
		}
		if (!(that instanceof PSName)) {
			return false;
		}
		return this.name.equals(((PSName) that).name);
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int getType() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean isExecutable() {
		return (flags & EXECUTABLE) != 0;
	}
	
	@Override
	public void setExecutable(boolean value) {
		if ((flags & NATIVED) != 0) {
			// do nothing for native names
		}
		else {
			flags |= EXECUTABLE;
		}
	}
	
	@Override
	public String toString() {
		if (isExecutable()) {
			return name;
		}
		else {
			return "/" + name;
		}
	}

}
