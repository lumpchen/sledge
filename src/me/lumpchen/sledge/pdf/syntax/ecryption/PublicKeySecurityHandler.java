package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Iterator;

import me.lumpchen.sledge.pdf.syntax.lang.PArray;

import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.RecipientInformation;

public class PublicKeySecurityHandler extends SecurityHandler {

	public static final String FILTER = "Adobe.PubSec";

	private static final String SUBFILTER = "adbe.pkcs7.s4";

	private PublicKeyProtectionPolicy policy = null;

	public PublicKeySecurityHandler() {
	}

	public PublicKeySecurityHandler(PublicKeyProtectionPolicy p) {
		policy = p;
		this.keyLength = policy.getEncryptionKeyLength();
	}

	@Override
	public void prepareForDecryption(PDEncryptionDictionary encDictionary, PArray documentIDArray,
			DecryptionMaterial decryptionMaterial) throws CryptographyException {

		if (encDictionary.getLength() != 0) {
			this.keyLength = encDictionary.getLength();
		}

		if (!(decryptionMaterial instanceof PublicKeyDecryptionMaterial)) {
			throw new CryptographyException(
					"Provided decryption material is not compatible with the document");
		}
		PublicKeyDecryptionMaterial material = (PublicKeyDecryptionMaterial) decryptionMaterial;

		try {
			boolean foundRecipient = false;

			// the decrypted content of the enveloped data that match
			// the certificate in the decryption material provided
			byte[] envelopedData = null;

			// the bytes of each recipient in the recipients array
			byte[][] recipientFieldsBytes = new byte[encDictionary.getRecipientsLength()][];

			int recipientFieldsLength = 0;

			for (int i = 0; i < encDictionary.getRecipientsLength(); i++) {
				byte[] recipientBytes = encDictionary.getRecipientStringAt(i);
				CMSEnvelopedData data = new CMSEnvelopedData(recipientBytes);
				Iterator recipCertificatesIt = data.getRecipientInfos().getRecipients().iterator();
				while (recipCertificatesIt.hasNext()) {
					RecipientInformation ri = (RecipientInformation) recipCertificatesIt.next();
					// Impl: if a matching certificate was previously found it
					// is an error,
					// here we just don't care about it
					if (ri.getRID().match(material.getCertificate()) && !foundRecipient) {
						foundRecipient = true;
						envelopedData = ri.getContent(material.getPrivateKey(), "BC");
						break;
					}
				}
				recipientFieldsBytes[i] = recipientBytes;
				recipientFieldsLength += recipientBytes.length;
			}
			if (!foundRecipient || envelopedData == null) {
				throw new CryptographyException("The certificate matches no recipient entry");
			}
			if (envelopedData.length != 24) {
				throw new CryptographyException("The enveloped data does not contain 24 bytes");
			}
			// now envelopedData contains:
			// - the 20 bytes seed
			// - the 4 bytes of permission for the current user

			byte[] accessBytes = new byte[4];
			System.arraycopy(envelopedData, 20, accessBytes, 0, 4);

			currentAccessPermission = new AccessPermission(accessBytes);
			currentAccessPermission.setReadOnly();

			// what we will put in the SHA1 = the seed + each byte contained in
			// the recipients array
			byte[] sha1Input = new byte[recipientFieldsLength + 20];

			// put the seed in the sha1 input
			System.arraycopy(envelopedData, 0, sha1Input, 0, 20);

			// put each bytes of the recipients array in the sha1 input
			int sha1InputOffset = 20;
			for (int i = 0; i < recipientFieldsBytes.length; i++) {
				System.arraycopy(recipientFieldsBytes[i], 0, sha1Input, sha1InputOffset,
						recipientFieldsBytes[i].length);
				sha1InputOffset += recipientFieldsBytes[i].length;
			}

			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] mdResult = md.digest(sha1Input);

			// we have the encryption key ...
			encryptionKey = new byte[this.keyLength / 8];
			System.arraycopy(mdResult, 0, encryptionKey, 0, this.keyLength / 8);
		} catch (CMSException | KeyStoreException | NoSuchProviderException
				| NoSuchAlgorithmException e) {
			throw new CryptographyException(e);
		}
	}
}
