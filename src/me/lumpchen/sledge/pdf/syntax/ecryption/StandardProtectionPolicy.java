package me.lumpchen.sledge.pdf.syntax.ecryption;

public class StandardProtectionPolicy extends ProtectionPolicy {

	private AccessPermission permissions;

	private String ownerPassword = "";

	private String userPassword = "";

	public StandardProtectionPolicy(String ownerPass, String userPass, AccessPermission perms) {
		this.permissions = perms;
		this.userPassword = userPass;
		this.ownerPassword = ownerPass;
	}

	public AccessPermission getPermissions() {
		return permissions;
	}

	public void setPermissions(AccessPermission perms) {
		this.permissions = perms;
	}

	public String getOwnerPassword() {
		return ownerPassword;
	}

	public void setOwnerPassword(String ownerPass) {
		this.ownerPassword = ownerPass;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPass) {
		this.userPassword = userPass;
	}
}
