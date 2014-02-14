package me.lumpchen.sledge.pdf.syntax.decrypt;

import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class CryptFilterDecrypter implements PDFDecrypter {

	@Override
	public ByteBuffer decryptBuffer(String cryptFilterName,
			IndirectObject streamObj, PStream stream)
			throws PDFDecryptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decryptString(int objNum, int objGen, String inputBasicString)
			throws PDFDecryptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOwnerAuthorised() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEncryptionPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEncryptionPresent(String cryptFilterName) {
		// TODO Auto-generated method stub
		return false;
	}

}
