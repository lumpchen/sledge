package me.lumpchen.sledge.pdf.syntax.decrypt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PBoolean;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class PDFDecrypterFactory {

    /** The name of the standard Identity CryptFilter */
    public static final String CF_IDENTITY = "Identity";
    
    /** Default key length for versions where key length is optional */
    private static final int DEFAULT_KEY_LENGTH = 40;

	public static PDFDecrypter createDecrypter(IndirectObject encryptObj, PArray doucmentID, PDFPassword password)
			throws EncryptionUnsupportedByProductException, EncryptionUnsupportedByPlatformException, 
			IOException, PDFDecryptException {

		if (password == null) {
			return null;
		}

		PDictionary encryptDict = encryptObj.getDict();

		if (encryptDict == null) {
			return IdentityDecrypter.getInstance();
		}

		PName filter = encryptDict.getValueAsName(PName.Filter);

		if (filter == null) {
			throw new SyntaxException("No Filter specified in Encrypt dictionary");
		}

		if (filter.equals(PName.Standard)) {
			PNumber num = encryptDict.getValueAsNumber(PName.V);
			int v = num != null ? num.intValue() : 0;
			if (v == 1 || v == 2) {
				num = encryptDict.getValueAsNumber(PName.Length);
				int length = num != null ? num.intValue() : DEFAULT_KEY_LENGTH;
				return createStandardDecrypter(encryptDict, doucmentID, password,
						length, false, StandardDecrypter.EncryptionAlgorithm.RC4);
			} else if (v == 4) {
                return createCryptFilterDecrypter(encryptDict, doucmentID, password, v);
            } else {
                throw new EncryptionUnsupportedByPlatformException("Unsupported encryption version: " + v);
            }
		}

		return null;
	}

    private static PDFDecrypter createCryptFilterDecrypter(PDictionary encryptDict, 
    		PArray documentID, PDFPassword password, int v)
    				throws
    				IOException,
    				EncryptionUnsupportedByPlatformException,
    				EncryptionUnsupportedByProductException, 
    				PDFDecryptException {
        assert v >= 4 : "crypt filter decrypter not supported for " +
                "standard encryption prior to version 4";
        
        // encryptMetadata is true if not present. Note that we don't actually
        // use this to change our reading of metadata streams (that's all done
        // internally by the document specifying a Crypt filter of None if
        // appropriate), but it does affect the encryption key.
        boolean encryptMetadata = true;
        
        PBoolean b = encryptDict.getValueAsBool(PName.EncryptMetadata);
        encryptMetadata = b.getValue();
        
        // Assemble decrypters for each filter in the
        // crypt filter (CF) dictionary
        final Map<String, PDFDecrypter> cfDecrypters = new HashMap<String, PDFDecrypter>();
        final PDictionary cfDict = encryptDict.getValueAsDict(PName.CF);
        if (cfDict == null) {
            throw new SyntaxException("No CF value present in Encrypt dict for V4 encryption");
        }
        
        Iterator<Entry<PName, PObject>> it = cfDict.entryIterator();
        while (it.hasNext()) {
        	Entry<PName, PObject> next = it.next();
        	PName key = next.getKey();
        	String cfName = key.getName();
        	
        	PDFDecrypter cfDecrypter = null;
        	
        	PObject value = next.getValue();
        	if (value == null || !(value instanceof PDictionary)) {
        		PDictionary cryptFilter = (PDictionary) value;
        		
                // The Errata for PDF 1.7 explains that the value of
                // Length in CF dictionaries is in bytes
        		PNumber lengthObj = cryptFilter.getValueAsNumber(PName.Length);
                int length = lengthObj != null ? lengthObj.intValue() * 8 : null;
                
                // CFM is the crypt filter method, describing whether RC4,
                // AES, or None (i.e., identity) is the encryption mechanism
                // used for the name crypt filter
                PString cfmObj = cryptFilter.getValueAsString(PName.CFM);
                String cfm = cfmObj != null ? cfmObj.toJavaString() : "None";
                
                if ("None".equals(cfm)) {
                    cfDecrypter = IdentityDecrypter.getInstance();
                } else if ("V2".equals(cfm)) {
                    cfDecrypter = createStandardDecrypter(
                            encryptDict, documentID, password, length, 
                            encryptMetadata, StandardDecrypter.EncryptionAlgorithm.RC4);
                } else if ("AESV2".equals(cfm)) {
                    cfDecrypter = createStandardDecrypter(
                            encryptDict, documentID, password, length,
                            encryptMetadata, StandardDecrypter.EncryptionAlgorithm.AESV2);
                } else {
                    throw new UnsupportedOperationException(
                            "Unknown CryptFilter method: " + cfm);
                }
        	}
        	cfDecrypters.put(cfName, cfDecrypter);
        }
        
        // always put Identity in last so that it will override any
        // Identity filter sneakily declared in the CF entry
        cfDecrypters.put(CF_IDENTITY, IdentityDecrypter.getInstance());

        PString stmFObj = encryptDict.getValueAsString(PName.StmF);
        String defaultStreamFilter = stmFObj != null ? stmFObj.toJavaString() : CF_IDENTITY;

        PString strFObj = encryptDict.getValueAsString(PName.StrF);
        String defaultStringFilter = strFObj != null ? stmFObj.toJavaString() : CF_IDENTITY;

//        return new CryptFilterDecrypter(cfDecrypters, defaultStreamFilter, defaultStringFilter);
        return null;
    }
    
	private static PDFDecrypter createStandardDecrypter(PDictionary encryptDict, 
			PArray documentID, PDFPassword password, int keyLength, boolean encryptMetadata,
			StandardDecrypter.EncryptionAlgorithm encryptionAlgorithm) 
					throws 
					EncryptionUnsupportedByProductException, 
					EncryptionUnsupportedByPlatformException, 
					IOException, 
					PDFDecryptException {
		
        // R describes the revision of the security handler
		PNumber num = encryptDict.getValueAsNumber(PName.R);
        if (num == null) {
            throw new SyntaxException("No R entry present in Encrypt dictionary");
        }
        int revision = num.intValue();
        
        // O describes validation details for the owner key
        PString s = encryptDict.getValueAsString(PName.O);
        if (s == null) {
        	throw new SyntaxException("No O entry present in Encrypt dictionary");
        }
        final byte[] o = s.asBytes();
        if (o.length != 32) {
            throw new SyntaxException("Expected owner key O " + "value of 32 bytes; found " + o.length);
        }
        
        // U describes validation details for the user key
        s = encryptDict.getValueAsString(PName.U);
        if (s == null) {
        	throw new SyntaxException("No U entry present in Encrypt dictionary");
        }
        final byte[] u = s.asBytes();
        if (o.length != 32) {
            throw new SyntaxException("Expected user key U value of 32 bytes; found " + o.length);
        }
        
		num = encryptDict.getValueAsNumber(PName.P);
        if (num == null) {
            throw new SyntaxException("No P entry present in Encrypt dictionary");
        }
        int p = num.intValue();
        
        return new StandardDecrypter(encryptionAlgorithm, documentID, keyLength,
                revision, o, u, p, encryptMetadata, password);
	}
}

