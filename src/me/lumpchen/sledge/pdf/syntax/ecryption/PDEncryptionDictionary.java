package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.io.IOException;

import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PBoolean;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

public class PDEncryptionDictionary {

	public static final int VERSION0_UNDOCUMENTED_UNSUPPORTED = 0;
	public static final int VERSION1_40_BIT_ALGORITHM = 1;
	public static final int VERSION2_VARIABLE_LENGTH_ALGORITHM = 2;
	public static final int VERSION3_UNPUBLISHED_ALGORITHM = 3;
	public static final int VERSION4_SECURITY_HANDLER = 4;

	public static final String DEFAULT_NAME = "Standard";
	public static final int DEFAULT_LENGTH = 40;

	public static final int DEFAULT_VERSION = VERSION0_UNDOCUMENTED_UNSUPPORTED;

	private PDictionary dict;

	public PDEncryptionDictionary(PDictionary dict) {
		this.dict = dict;
	}

	public String getFilter() {
		return this.dict.getValueAsName(PName.Filter).getName();
	}
	
	public boolean isStandard() {
		return DEFAULT_NAME.equals(this.getFilter());
	}

	public String getSubFilter() {
		return this.dict.getValueAsString(PName.SubFilter).toJavaString();
	}

	public int getVersion() {
		return this.dict.getValueAsInt(PName.V, 0);
	}

	public int getLength() {
		return this.dict.getValueAsInt(PName.Length, 40);
	}

	public int getRevision() {
		return this.dict.getValueAsInt(PName.R, DEFAULT_VERSION);
	}

	public byte[] getOwnerKey() throws IOException {
		byte[] o = null;
		PString owner = this.dict.getValueAsString(PName.O);
		if (owner != null) {
			o = owner.getBytes();
		}
		return o;
	}

	public byte[] getUserKey() throws IOException {
		byte[] u = null;
		PString user = this.dict.getValueAsString(PName.U);
		if (user != null) {
			u = user.getBytes();
		}
		return u;
	}

	public int getPermissions() {
		return this.dict.getValueAsInt(PName.P, 0);
	}

	public boolean isEncryptMetaData() {
		boolean encryptMetaData = true;

		PBoolean value = this.dict.getValueAsBool(PName.EncryptMetadata);

		if (value != null) {
			encryptMetaData = value.getValue();
		}

		return encryptMetaData;
	}

	public int getRecipientsLength() {
		PArray array = this.dict.getValueAsArray(PName.instance("Recipients"));
		if (array != null) {
			return array.size();
		}
		return 0;
	}

	public byte[] getRecipientStringAt(int i) {
		PArray array = this.dict.getValueAsArray(PName.instance("Recipients"));
		if (array != null) {
			return ((PString) array.get(i)).getBytes();
		}
		return null;
	}

	public PDCryptFilterDictionary getStdCryptFilterDictionary() {
		PDictionary cfDict = this.dict.getValueAsDict(PName.CF);
		if (cfDict == null) {
			return null;
		}

		return new PDCryptFilterDictionary(cfDict);
	}

	public String getStreamFilterName() {
		PString stmF = this.dict.getValueAsString(PName.StmF);
		if (stmF == null) {
			return "Identity";
		}
		return stmF.toJavaString();
	}

	public String getStringFilterName() {
		PString strF = this.dict.getValueAsString(PName.StrF);
		if (strF == null) {
			return "Identity";
		}
		return strF.toJavaString();
	}
}
