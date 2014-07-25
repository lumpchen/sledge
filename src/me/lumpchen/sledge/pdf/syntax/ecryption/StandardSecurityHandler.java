package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

public class StandardSecurityHandler extends SecurityHandler {

	public static final String FILTER = "Standard";

	private static final int DEFAULT_VERSION = 1;

	private static final int DEFAULT_REVISION = 3;

	private StandardProtectionPolicy policy;
	protected AccessPermission currentAccessPermission = null;

	public static final byte[] ENCRYPT_PADDING = { (byte) 0x28, (byte) 0xBF, (byte) 0x4E,
			(byte) 0x5E, (byte) 0x4E, (byte) 0x75, (byte) 0x8A, (byte) 0x41, (byte) 0x64,
			(byte) 0x00, (byte) 0x4E, (byte) 0x56, (byte) 0xFF, (byte) 0xFA, (byte) 0x01,
			(byte) 0x08, (byte) 0x2E, (byte) 0x2E, (byte) 0x00, (byte) 0xB6, (byte) 0xD0,
			(byte) 0x68, (byte) 0x3E, (byte) 0x80, (byte) 0x2F, (byte) 0x0C, (byte) 0xA9,
			(byte) 0xFE, (byte) 0x64, (byte) 0x53, (byte) 0x69, (byte) 0x7A };

	public StandardSecurityHandler() {
	}

	public StandardSecurityHandler(StandardProtectionPolicy p) {
		policy = p;
		keyLength = policy.getEncryptionKeyLength();
	}

	private int computeVersionNumber() {
		if (keyLength == 40) {
			return DEFAULT_VERSION;
		}
		return 2;
	}

	private int computeRevisionNumber() {
		if (version < 2 && !policy.getPermissions().hasAnyRevision3PermissionSet()) {
			return 2;
		}
		if (version == 2 || version == 3 || policy.getPermissions().hasAnyRevision3PermissionSet()) {
			return 3;
		}
		return 4;
	}

	@Override
	public void prepareForDecryption(PDEncryptionDictionary encryptDict, PArray documentIDArray,
			DecryptionMaterial decryptionMaterial) throws CryptographyException, IOException {
		if (!(decryptionMaterial instanceof StandardDecryptionMaterial)) {
			throw new CryptographyException(
					"Provided decryption material is not compatible with the document");
		}

		StandardDecryptionMaterial material = (StandardDecryptionMaterial) decryptionMaterial;

		byte[] password = material.getPassword();
		if (password == null) {
			password = new byte[0];
		}

		int dicPermissions = encryptDict.getPermissions();
		int dicRevision = encryptDict.getRevision();
		int dicLength = encryptDict.getLength() / 8;

		byte[] documentIDBytes = null;
		if (documentIDArray != null && documentIDArray.size() >= 1) {
			PObject id = documentIDArray.get(0);
			if (id != null && id instanceof PString) {
				documentIDBytes = ((PString) id).getBytes();
			}
		} else {
			documentIDBytes = new byte[0];
		}

		// we need to know whether the meta data was encrypted for password
		// calculation
		boolean encryptMetadata = encryptDict.isEncryptMetaData();

		byte[] u = encryptDict.getUserKey();
		byte[] o = encryptDict.getOwnerKey();

		boolean isUserPassword = isUserPassword(password, u, o, dicPermissions, documentIDBytes,
				dicRevision, dicLength, encryptMetadata);
		boolean isOwnerPassword = isOwnerPassword(password, u, o, dicPermissions, documentIDBytes,
				dicRevision, dicLength, encryptMetadata);

		if (isUserPassword) {
			currentAccessPermission = new AccessPermission(dicPermissions);
			encryptionKey = computeEncryptedKey(password, o, dicPermissions, documentIDBytes,
					dicRevision, dicLength, encryptMetadata);
		} else if (isOwnerPassword) {
			currentAccessPermission = AccessPermission.getOwnerAccessPermission();
			byte[] computedUserPassword = getUserPassword(password, o, dicRevision, dicLength);
			encryptionKey = computeEncryptedKey(computedUserPassword, o, dicPermissions,
					documentIDBytes, dicRevision, dicLength, encryptMetadata);
		} else {
			throw new CryptographyException(
					"Error: The supplied password does not match either the owner or user password in the document.",
					CryptographyException.CausedBy.wrongPassword);
		}

		// detect whether AES encryption is used. This assumes that the
		// encryption algo is stored in the PDCryptFilterDictionary
		PDCryptFilterDictionary stdCryptFilterDictionary = encryptDict
				.getStdCryptFilterDictionary();

		if (stdCryptFilterDictionary != null) {
			String cryptFilterMethod = stdCryptFilterDictionary.getCryptFilterMethod();
			if (cryptFilterMethod != null) {
				setAES("AESV2".equalsIgnoreCase(cryptFilterMethod));
			}
		}
	}

	public final boolean isUserPassword(byte[] password, byte[] u, byte[] o, int permissions,
			byte[] id, int encRevision, int length, boolean encryptMetadata)
			throws CryptographyException, IOException {
		boolean matches = false;
		// STEP 1
		byte[] computedValue = computeUserPassword(password, o, permissions, id, encRevision,
				length, encryptMetadata);
		if (encRevision == 2) {
			// STEP 2
			matches = Arrays.equals(u, computedValue);
		} else if (encRevision == 3 || encRevision == 4) {
			// STEP 2
			matches = arraysEqual(u, computedValue, 16);
		} else {
			throw new IOException("Unknown Encryption Revision " + encRevision);
		}
		return matches;
	}

	public final boolean isOwnerPassword(byte[] ownerPassword, byte[] u, byte[] o, int permissions,
			byte[] id, int encRevision, int length, boolean encryptMetadata)
			throws CryptographyException, IOException {
		byte[] userPassword = getUserPassword(ownerPassword, o, encRevision, length);
		return isUserPassword(userPassword, u, o, permissions, id, encRevision, length,
				encryptMetadata);
	}

	public final byte[] getUserPassword(byte[] ownerPassword, byte[] o, int encRevision, long length)
			throws CryptographyException, IOException {
		try {
			ByteArrayOutputStream result = new ByteArrayOutputStream();

			// 3.3 STEP 1
			byte[] ownerPadded = truncateOrPad(ownerPassword);

			// 3.3 STEP 2
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(ownerPadded);
			byte[] digest = md.digest();

			// 3.3 STEP 3
			if (encRevision == 3 || encRevision == 4) {
				for (int i = 0; i < 50; i++) {
					md.reset();
					md.update(digest);
					digest = md.digest();
				}
			}
			if (encRevision == 2 && length != 5) {
				throw new CryptographyException("Error: Expected length=5 actual=" + length);
			}

			// 3.3 STEP 4
			byte[] rc4Key = new byte[(int) length];
			System.arraycopy(digest, 0, rc4Key, 0, (int) length);

			// 3.7 step 2
			if (encRevision == 2) {
				rc4.setKey(rc4Key);
				rc4.write(o, result);
			} else if (encRevision == 3 || encRevision == 4) {
				byte[] iterationKey = new byte[rc4Key.length];
				byte[] otemp = new byte[o.length]; // sm
				System.arraycopy(o, 0, otemp, 0, o.length); // sm
				rc4.write(o, result);// sm

				for (int i = 19; i >= 0; i--) {
					System.arraycopy(rc4Key, 0, iterationKey, 0, rc4Key.length);
					for (int j = 0; j < iterationKey.length; j++) {
						iterationKey[j] = (byte) (iterationKey[j] ^ (byte) i);
					}
					rc4.setKey(iterationKey);
					result.reset(); // sm
					rc4.write(otemp, result); // sm
					otemp = result.toByteArray(); // sm
				}
			}
			return result.toByteArray();
		} catch (NoSuchAlgorithmException e) {
			throw new CryptographyException(e);
		}
	}

	public final byte[] computeUserPassword(byte[] password, byte[] o, int permissions, byte[] id,
			int encRevision, int length, boolean encryptMetadata) throws CryptographyException,
			IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		// STEP 1
		byte[] encryptionKey = computeEncryptedKey(password, o, permissions, id, encRevision,
				length, encryptMetadata);

		if (encRevision == 2) {
			// STEP 2
			rc4.setKey(encryptionKey);
			rc4.write(ENCRYPT_PADDING, result);
		} else if (encRevision == 3 || encRevision == 4) {
			try {
				// STEP 2
				MessageDigest md = MessageDigest.getInstance("MD5");
				// md.update( truncateOrPad( password ) );
				md.update(ENCRYPT_PADDING);

				// STEP 3
				md.update(id);
				result.write(md.digest());

				// STEP 4 and 5
				byte[] iterationKey = new byte[encryptionKey.length];
				for (int i = 0; i < 20; i++) {
					System.arraycopy(encryptionKey, 0, iterationKey, 0, iterationKey.length);
					for (int j = 0; j < iterationKey.length; j++) {
						iterationKey[j] = (byte) (iterationKey[j] ^ i);
					}
					rc4.setKey(iterationKey);
					ByteArrayInputStream input = new ByteArrayInputStream(result.toByteArray());
					result.reset();
					rc4.write(input, result);
				}

				// step 6
				byte[] finalResult = new byte[32];
				System.arraycopy(result.toByteArray(), 0, finalResult, 0, 16);
				System.arraycopy(ENCRYPT_PADDING, 0, finalResult, 16, 16);
				result.reset();
				result.write(finalResult);
			} catch (NoSuchAlgorithmException e) {
				throw new CryptographyException(e);
			}
		}
		return result.toByteArray();
	}

	public final byte[] computeEncryptedKey(byte[] password, byte[] o, int permissions, byte[] id,
			int encRevision, int length, boolean encryptMetadata) throws CryptographyException {
		byte[] result = new byte[length];
		try {
			// PDFReference 1.4 pg 78
			// step1
			byte[] padded = truncateOrPad(password);

			// step 2
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(padded);

			// step 3
			md.update(o);

			// step 4
			byte zero = (byte) (permissions >>> 0);
			byte one = (byte) (permissions >>> 8);
			byte two = (byte) (permissions >>> 16);
			byte three = (byte) (permissions >>> 24);

			md.update(zero);
			md.update(one);
			md.update(two);
			md.update(three);

			// step 5
			md.update(id);

			// (Security handlers of revision 4 or greater) If document metadata
			// is not being encrypted,
			// pass 4 bytes with the value 0xFFFFFFFF to the MD5 hash function.
			// see 7.6.3.3 Algorithm 2 Step f of PDF 32000-1:2008
			if (encRevision == 4 && !encryptMetadata) {
				md.update(new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff });
			}

			byte[] digest = md.digest();

			// step 6
			if (encRevision == 3 || encRevision == 4) {
				for (int i = 0; i < 50; i++) {
					md.reset();
					md.update(digest, 0, length);
					digest = md.digest();
				}
			}

			// step 7
			if (encRevision == 2 && length != 5) {
				throw new CryptographyException(
						"Error: length should be 5 when revision is two actual=" + length);
			}
			System.arraycopy(digest, 0, result, 0, length);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptographyException(e);
		}
		return result;
	}

	private final byte[] truncateOrPad(byte[] password) {
		byte[] padded = new byte[ENCRYPT_PADDING.length];
		int bytesBeforePad = Math.min(password.length, padded.length);
		System.arraycopy(password, 0, padded, 0, bytesBeforePad);
		System.arraycopy(ENCRYPT_PADDING, 0, padded, bytesBeforePad, ENCRYPT_PADDING.length
				- bytesBeforePad);
		return padded;
	}

	private static final boolean arraysEqual(byte[] first, byte[] second, int count) {
		// both arrays have to have a minimum length of count
		if (first.length < count || second.length < count) {
			return false;
		}
		for (int i = 0; i < count; i++) {
			if (first[i] != second[i]) {
				return false;
			}
		}
		return true;
	}

}
