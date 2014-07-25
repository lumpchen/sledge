package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class PublicKeyDecryptionMaterial implements DecryptionMaterial {

	private String password = null;
	private KeyStore keyStore = null;
	private String alias = null;

	public PublicKeyDecryptionMaterial(KeyStore keystore, String a, String pwd) {
		keyStore = keystore;
		alias = a;
		password = pwd;
	}

	public X509Certificate getCertificate() throws KeyStoreException {
		if (keyStore.size() == 1) {
			Enumeration<String> aliases = keyStore.aliases();
			String keyStoreAlias = (String) aliases.nextElement();
			return (X509Certificate) keyStore.getCertificate(keyStoreAlias);
		} else {
			if (keyStore.containsAlias(alias)) {
				return (X509Certificate) keyStore.getCertificate(alias);
			}
			throw new KeyStoreException("the keystore does not contain the given alias");
		}
	}

	public String getPassword() {
		return password;
	}

	public Key getPrivateKey() throws KeyStoreException {
		try {
			if (keyStore.size() == 1) {
				Enumeration<String> aliases = keyStore.aliases();
				String keyStoreAlias = (String) aliases.nextElement();
				return keyStore.getKey(keyStoreAlias, password.toCharArray());
			} else {
				if (keyStore.containsAlias(alias)) {
					return keyStore.getKey(alias, password.toCharArray());
				}
				throw new KeyStoreException("the keystore does not contain the given alias");
			}
		} catch (UnrecoverableKeyException ex) {
			throw new KeyStoreException("the private key is not recoverable");
		} catch (NoSuchAlgorithmException ex) {
			throw new KeyStoreException(
					"the algorithm necessary to recover the key is not available");
		}
	}
}
