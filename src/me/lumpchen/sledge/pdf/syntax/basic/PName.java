package me.lumpchen.sledge.pdf.syntax.basic;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.PObjectReader;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

public class PName extends PObject {

	public static final byte BEGIN = '/';

	protected String name;

	public static final String PARENT = "Parent";
	public static final PName parent = new PName(PARENT);
	
	public static final String PREV = "Prev";
	public static final PName prev = new PName(PREV);
	
	public static final String SIZE = "Size";
	public static final PName size = new PName(SIZE);
	
	public static final String ROOT = "Root";
	public static final PName root = new PName(ROOT);
	
	public static final String ID = "ID";
	public static final PName id = new PName(ID);
	
	public static final String INFO = "Info";
	public static final PName info = new PName(INFO);
	
	// document infomation
	public static final String TITLE = "Title";
	public static final PName title = new PName(TITLE);
	
	public static final String AUTHOR = "Author";
	public static final PName author = new PName(AUTHOR);
	
	public static final String SUBJECT = "Subject";
	public static final PName subject = new PName(SUBJECT);
	
	public static final String KEYWORDS = "Keywords";
	public static final PName keywords = new PName(KEYWORDS);
	
	public static final String CREATOR = "Creator";
	public static final PName creator = new PName(CREATOR);
	
	public static final String PRODUCER = "Producer";
	public static final PName producer = new PName(PRODUCER);
	
	public static final String CREATIONDATA = "CreationDate";
	public static final PName creationDate = new PName(CREATIONDATA);
	
	public static final String MODDATE = "ModDate";
	public static final PName modDate = new PName(MODDATE);
	
	public static final String TRAPPED = "Trapped";
	public static final PName trapped = new PName(TRAPPED);
	
	public static final String TYPE = "Type";
	public static final PName type = new PName(TYPE);
	
	public static final String CATALOG = "Catalog";
	public static final PName catalog = new PName(CATALOG);
	
	public static final String PAGES = "Pages";
	public static final PName pages = new PName(PAGES);
	
	public static final String PAGE = "Page";
	public static final PName page = new PName(PAGE);
	
	public static final String COUNT = "Count";
	public static final PName count = new PName(COUNT);
	
	public static final String KIDS = "Kids";
	public static final PName kids = new PName(KIDS);
	
	public static final String CONTENTS = "Contents";
	public static final PName contents = new PName(CONTENTS);
	public static final String RESOURCES = "Resources";
	public static final PName resources = new PName(RESOURCES);
	public static final String PROCSET = "ProcSet";
	public static final PName procset = new PName(PROCSET);
	public static final String PDF = "PDF";
	public static final PName pdf = new PName(PDF);
	
	public static final String TEXT = "Text";
	public static final PName text = new PName(TEXT);
	public static final String IMAGEB = "ImageB";
	public static final PName imageb = new PName(IMAGEB);
	public static final String IMAGEC = "ImageC";
	public static final PName imagec = new PName(IMAGEC);
	public static final String IMAGEI = "ImageI";
	public static final PName imagei = new PName(IMAGEI);
	public static final String FONT = "Font";
	public static final PName font = new PName(FONT);
	public static final String F1 = "F1";
	public static final PName f1 = new PName(F1);
	public static final String F2 = "F2";
	public static final PName f2 = new PName(F2);
	public static final String MEDIABOX = "MediaBox";
	public static final PName mediabox = new PName(MEDIABOX);
	
	// filter
	public static final String ASCII85DECODE = "ASCII85Decode";
	public static final PName ASCII85Decode = new PName(ASCII85DECODE);
	public static final String ASCIIHEXDECODE = "ASCIIHexDecode";
	public static final PName ASCIIHexDecode = new PName(ASCIIHEXDECODE);
	public static final String LZWDECODE = "LZWDecode";
	public static final PName LZWDecode = new PName(LZWDECODE);
	public static final String FLATEDECODE = "FlateDecode";
	public static final PName FlateDecode = new PName(FLATEDECODE);
	public static final String RUNLENGTHDECODE = "RunLengthDecode";
	public static final PName RunLengthDecode = new PName(RUNLENGTHDECODE);
	public static final String CCITTFAXDECODE = "CCITTFaxDecode";
	public static final PName CCITTFaxDecode = new PName(CCITTFAXDECODE);
	public static final String JBIG2DECODE = "JBIG2Decode";
	public static final PName JBIG2Decode = new PName(JBIG2DECODE);
	public static final String DCTDECODE = "DCTDecode";
	public static final PName DCTDecode = new PName(DCTDECODE);
	public static final String JPXDECODE = "JPXDecode";
	public static final PName JPXDecode = new PName(JPXDECODE);
	public static final String CRYPT = "Crypt";
	public static final PName Crypt = new PName(CRYPT);
	
	
	public static final String LENGTH = "Length";
	public static final PName Length = new PName(LENGTH);
	
	// font
	public static final String BASEFONT = "BaseFont";
	public static final PName BaseFont = new PName(BASEFONT);
	public static final String SUBTYPE = "Subtype";
	public static final PName Subtype = new PName(SUBTYPE);
	public static final String FIRSTCHAR = "FirstChar";
	public static final PName FirstChar = new PName(FIRSTCHAR);
	public static final String LASTCHAR = "LastChar";
	public static final PName LastChar = new PName(LASTCHAR);
	public static final String WIDTHS = "Widths";
	public static final PName Widths = new PName(WIDTHS);
	public static final String FONTDESCRIPTOR = "FontDescriptor";
	public static final PName FontDescriptor = new PName(FONTDESCRIPTOR);
	public static final String ENCODING = "Encoding";
	public static final PName Encoding = new PName(ENCODING);
	public static final String TOUNICODE = "ToUnicode";
	public static final PName ToUnicode = new PName(TOUNICODE);
	
	
	private static Map<String, PName> nameMap = new HashMap<String, PName>();
	static {
		nameMap.put(PARENT, parent);
		nameMap.put(ID, id);
		nameMap.put(INFO, info);
		nameMap.put(ROOT, root);
		nameMap.put(SIZE, size);
		nameMap.put(PREV, prev);

		nameMap.put(TITLE, title);
		nameMap.put(AUTHOR, author);
		nameMap.put(SUBJECT, subject);
		nameMap.put(KEYWORDS, keywords);
		nameMap.put(CREATOR, creator);
		nameMap.put(PRODUCER, producer);
		nameMap.put(CREATIONDATA, creationDate);
		nameMap.put(MODDATE, modDate);
		nameMap.put(TRAPPED, trapped);
		
		nameMap.put(TYPE, type);
		nameMap.put(CATALOG, catalog);
		nameMap.put(PAGES, pages);
		nameMap.put(PAGE, page);
		
		nameMap.put(COUNT, count);
		nameMap.put(KIDS, kids);
		
		nameMap.put(CONTENTS, contents);
		nameMap.put(RESOURCES, resources);
		nameMap.put(PROCSET, procset);
		nameMap.put(PDF, pdf);
		
		nameMap.put(TEXT, text);
		nameMap.put(IMAGEB, imageb);
		nameMap.put(IMAGEC, imagec);
		nameMap.put(IMAGEI, imagei);
		nameMap.put(FONT, font);
		nameMap.put(F1, f1);
		nameMap.put(F2, f2);
		nameMap.put(MEDIABOX, mediabox);
		
		nameMap.put(ASCII85DECODE, ASCII85Decode);
		nameMap.put(ASCIIHEXDECODE, ASCIIHexDecode);
		nameMap.put(LZWDECODE, LZWDecode);
		nameMap.put(FLATEDECODE, FlateDecode);
		nameMap.put(RUNLENGTHDECODE, RunLengthDecode);
		nameMap.put(CCITTFAXDECODE, CCITTFaxDecode);
		nameMap.put(JBIG2DECODE, JBIG2Decode);
		nameMap.put(DCTDECODE, DCTDecode);
		nameMap.put(JPXDECODE, JPXDecode);
		nameMap.put(CRYPT, Crypt);
		
		nameMap.put(LENGTH, Length);
		
		nameMap.put(BASEFONT, BaseFont);
		nameMap.put(FONTDESCRIPTOR, FontDescriptor);
		nameMap.put(SUBTYPE, Subtype);
		nameMap.put(FIRSTCHAR, FirstChar);
		nameMap.put(LASTCHAR, LastChar);
		nameMap.put(WIDTHS, Widths);
		nameMap.put(ENCODING, Encoding);
		nameMap.put(TOUNICODE, ToUnicode);
	}

	private PName(byte[] name) {
		super.type = Type.Name;
		this.name = new String(name, Charset.defaultCharset());
	}

	private PName(String name) {
		super.type = Type.Name;
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

	@Override
	public void read(PObjectReader reader) {
		// read directly, do nothing here
	}

	public String toString() {
		return "/" + this.name;
	}

	@Override
	protected void readBeginTag(PObjectReader reader) {
	}

	@Override
	protected void readBody(PObjectReader reader) {
	}

	@Override
	protected void readEndTag(PObjectReader reader) {
	}

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
		writer.writeString("/" + this.name);
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
	}
}
