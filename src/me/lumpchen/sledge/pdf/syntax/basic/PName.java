package me.lumpchen.sledge.pdf.syntax.basic;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class PName extends PObject {

	public static final byte BEGIN = '/';

	protected String name;

	public static final PName Parent = new PName("Parent");
	public static final PName XRefStm = new PName("XRefStm");
	public static final PName Prev = new PName("Prev");
	public static final PName Encrypt = new PName("Encrypt");
	public static final PName Size = new PName("Size");
	public static final PName Root = new PName("Root");
	public static final PName ID = new PName("ID");
	public static final PName Info = new PName("Info");
	public static final PName Title = new PName("Title");
	public static final PName Author = new PName("Author");
	public static final PName Subject = new PName("Subject");
	public static final PName Keywords = new PName("Keywords");
	public static final PName Creator = new PName("Creator");
	public static final PName Producer = new PName("Producer");
	public static final PName CreationDate = new PName("CreationDate");
	public static final PName ModDate = new PName("ModDate");
	public static final PName Trapped = new PName("Trapped");
	public static final PName Type = new PName("Type");
	public static final PName Catalog = new PName("Catalog");
	public static final PName Pages = new PName("Pages");
	public static final PName Page = new PName("Page");
	public static final PName Count = new PName("Count");
	public static final PName Kids = new PName("Kids");
	public static final PName Contents = new PName("Contents");
	public static final PName Resources = new PName("Resources");
	public static final PName ProcSet = new PName("ProcSet");
	public static final PName PDF = new PName("PDF");
	public static final PName Text = new PName("Text");
	public static final PName ImageB = new PName("ImageB");
	public static final PName ImageC = new PName("ImageC");
	public static final PName ImageI = new PName("ImageI");
	public static final PName Font = new PName("Font");
	public static final PName F1 = new PName("F1");
	public static final PName F2 = new PName("F2");
	public static final PName MediaBox = new PName("MediaBox");
	
	// filter
	public static final PName Filter = new PName("Filter");
	public static final PName ASCII85Decode = new PName("ASCII85Decode");
	public static final PName ASCIIHexDecode = new PName("ASCIIHexDecode");
	public static final PName LZWDecode = new PName("LZWDecode");
	public static final PName FlateDecode = new PName("FlateDecode");
	public static final PName RunLengthDecode = new PName("RunLengthDecode");
	public static final PName CCITTFaxDecode = new PName("CCITTFaxDecode");
	public static final PName JBIG2Decode = new PName("JBIG2Decode");
	public static final PName DCTDecode = new PName("DCTDecode");
	public static final PName JPXDecode = new PName("JPXDecode");
	public static final PName Crypt = new PName("Crypt");
	
	public static final PName Standard = new PName("Standard");
	public static final PName V = new PName("V");
	public static final PName R = new PName("R");
	public static final PName O = new PName("O");
	public static final PName U = new PName("U");
	public static final PName P = new PName("P");
	public static final PName EncryptMetadata = new PName("EncryptMetadata");
	public static final PName CF = new PName("CF");
	public static final PName CFM = new PName("CFM");
	public static final PName StmF = new PName("StmF");
	public static final PName StrF = new PName("StrF");
	public static final PName DecodeParms = new PName("DecodeParms");
	public static final PName Columns = new PName("Columns");
	public static final PName Predictor = new PName("Predictor");
	public static final PName Length = new PName("Length");
	
	// font
	public static final PName BaseFont = new PName("BaseFont");
	public static final PName Subtype = new PName("Subtype");
	public static final PName FirstChar = new PName("FirstChar");
	public static final PName LastChar = new PName("LastChar");
	public static final PName Widths = new PName("Widths");
	public static final PName FontDescriptor = new PName("FontDescriptor");
	public static final PName Encoding = new PName("Encoding");
	public static final PName ToUnicode = new PName("ToUnicode");
	public static final PName DescendantFonts = new PName("DescendantFonts");
	public static final PName CMap = new PName("CMap");
	public static final PName MacRomanEncoding = new PName("MacRomanEncoding");
	public static final PName MacExpertEncoding = new PName("MacExpertEncoding");
	public static final PName WinAnsiEncoding = new PName("WinAnsiEncoding");
	public static final PName BaseEncoding = new PName("BaseEncoding");
	public static final PName Differences = new PName("Differences");
	
	
	public static final PName XRef = new PName("XRef");
	public static final PName Index = new PName("Index");
	public static final PName W = new PName("W");
	
	public static final PName ObjStm = new PName("ObjStm");
	public static final PName First = new PName("First");
	public static final PName N = new PName("N");
	public static final PName Extends = new PName("Extends");
	
	private static Map<String, PName> nameMap = new HashMap<String, PName>();
	
	static {
		nameMap.put("Parent", Parent);
		nameMap.put("XRefStm", XRefStm);
		nameMap.put("Prev", Prev);
		nameMap.put("Encrypt", Encrypt);
		nameMap.put("Size", Size);
		nameMap.put("Root", Root);
		nameMap.put("ID", ID);
		nameMap.put("Info", Info);
		nameMap.put("Title", Title);
		nameMap.put("Author", Author);
		nameMap.put("Subject", Subject);
		nameMap.put("Keywords", Keywords);
		nameMap.put("Creator", Creator);
		nameMap.put("Producer", Producer);
		nameMap.put("CreationDate", CreationDate);
		nameMap.put("ModDate", ModDate);
		nameMap.put("Trapped", Trapped);
		nameMap.put("Type", Type);
		nameMap.put("Catalog", Catalog);
		nameMap.put("Pages", Pages);
		nameMap.put("Page", Page);
		nameMap.put("Count", Count);
		nameMap.put("Kids", Kids);
		nameMap.put("Contents", Contents);
		nameMap.put("Resources", Resources);
		nameMap.put("ProcSet", ProcSet);
		nameMap.put("PDF", PDF);
		nameMap.put("Text", Text);
		nameMap.put("ImageB", ImageB);
		nameMap.put("ImageC", ImageC);
		nameMap.put("ImageI", ImageI);
		nameMap.put("Font", Font);
		nameMap.put("F1", F1);
		nameMap.put("F2", F2);
		nameMap.put("MediaBox", MediaBox);
		nameMap.put("Filter", Filter);
		nameMap.put("ASCII85Decode", ASCII85Decode);
		nameMap.put("ASCIIHexDecode", ASCIIHexDecode);
		nameMap.put("LZWDecode", LZWDecode);
		nameMap.put("FlateDecode", FlateDecode);
		nameMap.put("RunLengthDecode", RunLengthDecode);
		nameMap.put("CCITTFaxDecode", CCITTFaxDecode);
		nameMap.put("JBIG2Decode", JBIG2Decode);
		nameMap.put("DCTDecode", DCTDecode);
		nameMap.put("JPXDecode", JPXDecode);
		nameMap.put("Crypt", Crypt);
		nameMap.put("Standard", Standard);
		nameMap.put("V", V);
		nameMap.put("R", R);
		nameMap.put("O", O);
		nameMap.put("U", U);
		nameMap.put("P", P);
		nameMap.put("EncryptMetadata", EncryptMetadata);
		nameMap.put("CF", CF);
		nameMap.put("CFM", CFM);
		nameMap.put("StmF", StmF);
		nameMap.put("StrF", StrF);
		nameMap.put("DecodeParms", DecodeParms);
		nameMap.put("Columns", Columns);
		nameMap.put("Predictor", Predictor);
		nameMap.put("Length", Length);
		nameMap.put("BaseFont", BaseFont);
		nameMap.put("Subtype", Subtype);
		nameMap.put("FirstChar", FirstChar);
		nameMap.put("LastChar", LastChar);
		nameMap.put("Widths", Widths);
		nameMap.put("FontDescriptor", FontDescriptor);
		nameMap.put("Encoding", Encoding);
		nameMap.put("ToUnicode", ToUnicode);
		nameMap.put("DescendantFonts", DescendantFonts);
		nameMap.put("MacRomanEncoding", MacRomanEncoding);
		nameMap.put("CMap", CMap);
		nameMap.put("MacExpertEncoding", MacExpertEncoding);
		nameMap.put("WinAnsiEncoding", WinAnsiEncoding);
		nameMap.put("BaseEncoding", BaseEncoding);
		nameMap.put("Differences", Differences);
		nameMap.put("XRef", XRef);
		nameMap.put("Index", Index);
		nameMap.put("W", W);
		nameMap.put("ObjStm", ObjStm);
		nameMap.put("First", First);
		nameMap.put("N", N);
		nameMap.put("Extends", Extends);
		nameMap.put("Parent", Parent);
		nameMap.put("Parent", Parent);
		nameMap.put("Parent", Parent);
	}

	private PName(byte[] name) {
		super.type = TYPE.Name;
		this.name = new String(name, Charset.defaultCharset());
	}

	private PName(String name) {
		super.type = TYPE.Name;
		this.name = name;
	}

	public static PName instance(byte[] name) {
		String key = new String(name, Charset.defaultCharset());
		return PName.instance(key);
	}
	
	public static PName instance(String name) {
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		if (nameMap.containsKey(name)) {
			return nameMap.get(name);
		} else {
			PName unDef = new PName(name);
			nameMap.put(name, unDef);
			return unDef;
		}
	}

	public String getName() {
		return this.name;
	}
	
	public int hashCode() {
		return super.hashCode() + this.name.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PName)) {
			return false;
		}
		if (!(this.name.equals(((PName) obj).name))) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "/" + this.name;
	}
}
