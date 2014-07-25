package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.security.cert.X509Certificate;

public class PublicKeyRecipient {
	private X509Certificate x509;

	private AccessPermission permission;

	public X509Certificate getX509() {
		return x509;
	}

	public void setX509(X509Certificate aX509) {
		this.x509 = aX509;
	}

	public AccessPermission getPermission() {
		return permission;
	}

	public void setPermission(AccessPermission permissions) {
		this.permission = permissions;
	}
}
