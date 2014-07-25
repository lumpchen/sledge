package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;

public class PublicKeyProtectionPolicy extends ProtectionPolicy {

	private ArrayList<PublicKeyRecipient> recipients = null;

	private X509Certificate decryptionCertificate;

	public PublicKeyProtectionPolicy() {
		recipients = new ArrayList<PublicKeyRecipient>();
	}

	public void addRecipient(PublicKeyRecipient r) {
		recipients.add(r);
	}

	public boolean removeRecipient(PublicKeyRecipient r) {
		return recipients.remove(r);
	}

	public Iterator<PublicKeyRecipient> getRecipientsIterator() {
		return recipients.iterator();
	}

	public X509Certificate getDecryptionCertificate() {
		return decryptionCertificate;
	}

	public void setDecryptionCertificate(X509Certificate aDecryptionCertificate) {
		this.decryptionCertificate = aDecryptionCertificate;
	}

	public int getRecipientsNumber() {
		return recipients.size();
	}
}
