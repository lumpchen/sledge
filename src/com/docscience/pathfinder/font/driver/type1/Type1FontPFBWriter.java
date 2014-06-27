package com.docscience.pathfinder.font.driver.type1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map.Entry;

import com.docscience.pathfinder.font.driver.ps.PSDictionary;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSObjectWriter;
import com.docscience.pathfinder.font.driver.ps.PSString;

/**
 * @author wxin
 *
 */
public class Type1FontPFBWriter {

	private static final byte IBMPC_HEADER = (byte) 0x80;
	private static final byte SEGMENT_1 = 1; // ASCII TEXT PORTION
	private static final byte SEGMENT_2 = 2; // BINARY DATA PORTION
	private static final byte SEGMENT_3 = 3; // END OF FILE

	private Type1FontDictionary type1FontDict;

	public Type1FontPFBWriter(Type1FontDictionary type1FontDict) {
		this.type1FontDict = type1FontDict;
	}

	public void write(OutputStream stream) throws IOException {
		byte[] plainText1 = getPlainTextPortion1();
		byte[] plainText2 = getPlainTextPortion2();
		byte[] binaryData = getEncryptedPortion();

		writeIBMPCMarker(stream, SEGMENT_1, plainText1.length);
		stream.write(plainText1);
		writeIBMPCMarker(stream, SEGMENT_2, binaryData.length);
		stream.write(binaryData);
		writeIBMPCMarker(stream, SEGMENT_1, plainText2.length);
		stream.write(plainText2);
		writeIBMPCMarker(stream, SEGMENT_3, 0);
	}

	private void writeIBMPCMarker(OutputStream stream, int type, int length) throws IOException {
		stream.write(IBMPC_HEADER);
		stream.write(type);
		if (type == SEGMENT_3) {
			return;
		}
		for (int i=0; i<4; ++i) {
			stream.write((length >> (i * 8)) & 0xff);
		}
	}

	public byte[] getPlainTextPortion1() throws IOException {
		ByteArrayOutputStream plainTextStream = new ByteArrayOutputStream();
		PSObjectWriter writer = new PSObjectWriter(plainTextStream);
		writer.setRemoveUnnecessarySpace(false);
		writer.setWellFormated(true);

		writer.common("!FontType1-1.0: "
				+ type1FontDict.getFontName() + " "
				+ type1FontDict.getFontInfo().getVersion());

		PSDictionary fontDict = (PSDictionary) type1FontDict.getPSObject();
		writer.integer(fontDict.size());
		writer.executeName("dict");
		writer.executeName("begin");
		writer.newLine();

		Iterator<Entry<PSObject, PSObject>> itr = fontDict.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<PSObject, PSObject> entry = itr.next();
			PSName key = (PSName) entry.getKey();
			PSObject value = entry.getValue();
			if (!key.equals(PSName.CharStrings) && !key.equals(PSName.Private)) {
				writer.psObject(key);
				writer.psObject(value);
				writer.executeName("def");
				writer.newLine();
			}
		}

		writer.executeName("currentdict");
		writer.executeName("end");
		writer.newLine();

		writer.executeName("currentfile");
		writer.executeName("eexec");
		writer.newLine();
		writer.flush();

		return plainTextStream.toByteArray();
	}

	public byte[] getPlainTextPortion2() throws IOException {
		ByteArrayOutputStream plainTextStream = new ByteArrayOutputStream();
		PSObjectWriter writer = new PSObjectWriter(plainTextStream);
		writer.setRemoveUnnecessarySpace(false);
		writer.setWellFormated(true);

		writer.setRemoveUnnecessarySpace(false);
		writer.setWellFormated(true);

		for (int r=0; r<8; ++r) {
			for (int c=0; c<64; ++c) {
				writer.write('0');
			}
			writer.lineBreak();
		}

		writer.executeName("cleartomark");
		writer.lineBreak();
		writer.flush();

		return plainTextStream.toByteArray();
	}

	public byte[] getEncryptedPortion() throws IOException {
		ByteArrayOutputStream plainTextStream = new ByteArrayOutputStream();
		PSObjectWriter writer = new PSObjectWriter(plainTextStream);
		writer.setRemoveUnnecessarySpace(false);
		writer.setWellFormated(true);

		PSDictionary fontDict = (PSDictionary) type1FontDict.getPSObject();
		PSDictionary privateDict = (PSDictionary) fontDict.get(PSName.Private);
		PSDictionary charStrings = (PSDictionary) fontDict.get(PSName.CharStrings);

		writer.executeName("dup");
		writer.literalName("Private");
		writer.integer(privateDict.size());
		writer.executeName("dict");
		writer.executeName("dup");
		writer.executeName("begin");
		writer.newLine();

		Iterator<Entry<PSObject, PSObject>> itr = null;
		
		// write private except subr
		itr = privateDict.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<PSObject, PSObject> entry = itr.next();
			PSName key = (PSName) entry.getKey();
			PSObject value = entry.getValue();
			if (!key.equals(PSName.Subrs)
					&& !key.equals(PSName.OtherSubrs)) {
				writer.psObject(key);
				writer.psObject(value);
				writer.executeName("def");
				writer.newLine();
			}
		}

		// write subr here
		// currently we don't support subr

		writer.integer(2);
		writer.executeName("index");
		writer.literalName("CharStrings");
		writer.integer(charStrings.size());
		writer.executeName("dict");
		writer.executeName("dup");
		writer.executeName("begin");
		writer.newLine();

		// write char strings
		itr = charStrings.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<PSObject, PSObject> entry = itr.next();
			PSName key = (PSName) entry.getKey();
			PSString value = (PSString) entry.getValue();
			writer.psObject(key);
			writer.integer(value.length());
			writer.write(' ');
			writer.write('R');
			writer.write('D');
			writer.write(' ');
			writer.write(value.getBytes());
			writer.write(' ');
			writer.write('N');
			writer.write('D');
			writer.newLine();
		}

		writer.executeName("end");
		writer.executeName("end");
		writer.newLine();
		writer.executeName("readonly");
		writer.executeName("put");
		writer.newLine();
		writer.executeName("noaccess");
		writer.executeName("put");
		writer.newLine();
		writer.executeName("dup");
		writer.literalName("FontName");
		writer.executeName("get");
		writer.executeName("exch");
		writer.executeName("definefont");
		writer.executeName("pop");
		writer.executeName("mark");
		writer.executeName("currentfile");
		writer.executeName("closefile");
		writer.newLine();                 // needed for AFP outline fonts
		writer.flush();

		byte[] plainTextBytes = plainTextStream.toByteArray();
		byte[] encryptedBytes = new byte[plainTextBytes.length + 4];
		Type1Encryption.encryptExec(plainTextBytes, encryptedBytes, new byte[] {0, 0, 0, 0});

		return encryptedBytes;
	}

}
