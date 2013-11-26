package me.lumpchen.sledge.pdf.syntax.basic;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.InvalidNameException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class PName extends PObject {

	public static final byte BEGIN = '/';

	protected String name;

	public static final String PARENT = "Parent";
	public static final PName parent = new PName(PARENT);
	
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
	
	private static Map<String, PName> nameMap = new HashMap<String, PName>();
	static {
		nameMap.put(PARENT, parent);
		nameMap.put(ID, id);
		nameMap.put(INFO, info);
		nameMap.put(ROOT, root);
		nameMap.put(SIZE, size);

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
	}

	private PName(byte[] name) {
		this.name = new String(name, Charset.defaultCharset());
	}

	private PName(String name) {
		this.name = name;
	}

	public static PName instance(byte[] name) {
		String key = new String(name, Charset.defaultCharset());
		if (nameMap.containsKey(key)) {
			return nameMap.get(key);
		}
		throw new InvalidNameException(key);
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
	public void read(ObjectReader reader) {
		// read directly, do nothing here
	}

	public String toString() {
		return "/" + this.name;
	}

	@Override
	protected void readBeginTag(ObjectReader reader) {
	}

	@Override
	protected void readBody(ObjectReader reader) {
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
	}
}
