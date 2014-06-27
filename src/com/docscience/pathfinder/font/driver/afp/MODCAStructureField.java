package com.docscience.pathfinder.font.driver.afp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author wxin
 *
 */
public class MODCAStructureField {
	
	// structure field defined in modca
	public static final int ID_MFC = 0xD3A088; // Medium Finishing Control
	public static final int ID_TLE = 0xD3A090; // Tag Logical Element
	public static final int ID_MCC = 0xD3A288; // Medium Copy Count
	public static final int ID_OBD = 0xD3A66B; // Object Area Descriptor
	public static final int ID_IID = 0xD3A67B; // IM Image Input Descriptor (C)
	public static final int ID_MDD = 0xD3A688; // Medium Descriptor
	public static final int ID_CDD = 0xD3A692; // Container Data Descriptor
	public static final int ID_PTD_1 = 0xD3A69B; // Presentation Text Descriptor Format-1 (C)
	public static final int ID_PGD = 0xD3A6AF; // Page Descriptor
	public static final int ID_GDD = 0xD3A6BB; // Graphics Data Descriptor
	public static final int ID_FGD = 0xD3A6C5; // Form Environment Group Descriptor (O)
	public static final int ID_BDD = 0xD3A6EB; // Bar Code Data Descriptor
	public static final int ID_IDD = 0xD3A6FB; // Image Data Descriptor
	public static final int ID_IOC = 0xD3A77B; // IM Image Output Control (C)
	public static final int ID_MMC = 0xD3A788; // Medium Modification Control
	public static final int ID_CTC = 0xD3A79B; // Composed Text Control (O)
	public static final int ID_PEC = 0xD3A7A8; // Presentation Environment Control
	public static final int ID_PMC = 0xD3A7AF; // Page Modification Control
	public static final int ID_BPS = 0xD3A85F; // Begin Page Segment
	public static final int ID_BCA = 0xD3A877; // Begin Color Attribute Table
	public static final int ID_BII = 0xD3A87B; // Begin IM Image (C)
	public static final int ID_BOC = 0xD3A892; // Begin Object Container
	public static final int ID_BPT = 0xD3A89B; // Begin Presentation Text Object
	public static final int ID_BDI = 0xD3A8A7; // Begin Document Index
	public static final int ID_BDT = 0xD3A8A8; // Begin Document
	public static final int ID_BNG = 0xD3A8AD; // Begin Named Page Group
	public static final int ID_BPG = 0xD3A8AF; // Begin Page
	public static final int ID_BGR = 0xD3A8BB; // Begin Graphics Object
	public static final int ID_BDG = 0xD3A8C4; // Begin Document Environment Group
	public static final int ID_BFG = 0xD3A8C5; // Begin Form Environment Group (O)
	public static final int ID_BRG = 0xD3A8C6; // Begin Resource Group
	public static final int ID_BOG = 0xD3A8C7; // Begin Object Environment Group
	public static final int ID_BAG = 0xD3A8C9; // Begin Active Environment Group
	public static final int ID_BMM = 0xD3A8CC; // Begin Medium Map
	public static final int ID_BFM = 0xD3A8CD; // Begin Form Map
	public static final int ID_BRS = 0xD3A8CE; // Begin Resource
	public static final int ID_BSG = 0xD3A8D9; // Begin Resource Environment Group
	public static final int ID_BMO = 0xD3A8DF; // Begin Overlay
	public static final int ID_BBC = 0xD3A8EB; // Begin Bar Code Object
	public static final int ID_BIM = 0xD3A8FB; // Begin Image Object
	public static final int ID_EPS = 0xD3A95F; // End Page Segment
	public static final int ID_ECA = 0xD3A977; // End Color Attribute Table
	public static final int ID_EII = 0xD3A97B; // End IM Image (C)
	public static final int ID_EOC = 0xD3A992; // End Object Container
	public static final int ID_EPT = 0xD3A99B; // End Presentation Text Object
	public static final int ID_EDI = 0xD3A9A7; // End Document Index
	public static final int ID_EDT = 0xD3A9A8; // End Document
	public static final int ID_ENG = 0xD3A9AD; // End Named Page Group
	public static final int ID_EPG = 0xD3A9AF; // End Page
	public static final int ID_EGR = 0xD3A9BB; // End Graphics Object
	public static final int ID_EDG = 0xD3A9C4; // End Document Environment Group
	public static final int ID_EFG = 0xD3A9C5; // End Form Environment Group (O)
	public static final int ID_ERG = 0xD3A9C6; // End Resource Group
	public static final int ID_EOG = 0xD3A9C7; // End Object Environment Group
	public static final int ID_EAG = 0xD3A9C9; // End Active Environment Group
	public static final int ID_EMM = 0xD3A9CC; // End Medium Map
	public static final int ID_EFM = 0xD3A9CD; // End Form Map
	public static final int ID_ERS = 0xD3A9CE; // End Resource
	public static final int ID_ESG = 0xD3A9D9; // End Resource Environment Group
	public static final int ID_EMO = 0xD3A9DF; // End Overlay
	public static final int ID_EBC = 0xD3A9EB; // End Bar Code Object
	public static final int ID_EIM = 0xD3A9FB; // End Image Object
	public static final int ID_MCA = 0xD3AB77; // Map Color Attribute Table
	public static final int ID_MMT = 0xD3AB88; // Map Media Type
	public static final int ID_MCF = 0xD3AB8A; // Map Coded Font
	public static final int ID_MCD = 0xD3AB92; // Map Container Data
	public static final int ID_MPG = 0xD3ABAF; // Map Page
	public static final int ID_MGO = 0xD3ABBB; // Map Graphics Object
	public static final int ID_MDR = 0xD3ABC3; // Map Data Resource
	public static final int ID_IMM = 0xD3ABCC; // Invoke Medium Map
	public static final int ID_MPO = 0xD3ABD8; // Map Page Overlay
	public static final int ID_MSU = 0xD3ABEA; // Map Suppression
	public static final int ID_MBC = 0xD3ABEB; // Map Bar Code Object
	public static final int ID_MIO = 0xD3ABFB; // Map Image Object
	public static final int ID_OBP = 0xD3AC6B; // Object Area Position
	public static final int ID_ICP = 0xD3AC7B; // IM Image Cell Position (C)
	public static final int ID_PGP_1 = 0xD3ACAF; // Page Position Format-1 (C)
	public static final int ID_PPO = 0xD3ADC3; // Preprocess Presentation Object
	public static final int ID_IPS = 0xD3AF5F; // Include Page Segment
	public static final int ID_IPG = 0xD3AFAF; // Include Page
	public static final int ID_IOB = 0xD3AFC3; // Include Object
	public static final int ID_IPO = 0xD3AFD8; // Include Page Overlay
	public static final int ID_CAT = 0xD3B077; // Color Attribute Table
	public static final int ID_MPS = 0xD3B15F; // Map Page Segment
	public static final int ID_MCF_1 = 0xD3B18A; // Map Coded Font Format-1 (C)
	public static final int ID_PTD = 0xD3B19B; // Presentation Text Data Descriptor
	public static final int ID_PGP = 0xD3B1AF; // Page Position
	public static final int ID_MMO = 0xD3B1DF; // Map Medium Overlay
	public static final int ID_PFC = 0xD3B288; // Presentation Fidelity Control
	public static final int ID_IEL = 0xD3B2A7; // Index Element
	public static final int ID_LLE = 0xD3B490; // Link Logical Element
	public static final int ID_IRD = 0xD3EE7B; // IM Image Raster Data (C)
	public static final int ID_OCD = 0xD3EE92; // Object Container Data
	public static final int ID_PTX = 0xD3EE9B; // Presentation Text Data
	public static final int ID_GAD = 0xD3EEBB; // Graphics Data
	public static final int ID_BDA = 0xD3EEEB; // Bar Code Data
	public static final int ID_NOP = 0xD3EEEE; // No Operation
	public static final int ID_IPD = 0xD3EEFB; // Image Picture Data

	// structure field defined in foca
	public static final int ID_BCF = 0xD3A88A; // Begin Coded Font
	public static final int ID_BCP = 0xD3A887; // Begin Code Page
	public static final int ID_BFN = 0xD3A889; // Begin Font
	public static final int ID_CFC = 0xD3A78A; // Coded Font Control
	public static final int ID_CFI = 0xD38C8A; // Coded Font Index
	public static final int ID_CPC = 0xD3A787; // Code Page Control
	public static final int ID_CPD = 0xD3A687; // Code Page Descriptor
	public static final int ID_CPI = 0xD38C87; // Code Page Index
	public static final int ID_ECF = 0xD3A98A; // End Coded Font
	public static final int ID_ECP = 0xD3A987; // End Code Page
	public static final int ID_EFN = 0xD3A989; // End Font
	public static final int ID_FNC = 0xD3A789; // Font Control
	public static final int ID_FND = 0xD3A689; // Font Descriptor
	public static final int ID_FNG = 0xD3EE89; // Font Patterns
	public static final int ID_FNI = 0xD38C89; // Font Index
	public static final int ID_FNM = 0xD3A289; // Font Patterns Map
	public static final int ID_FNN = 0xD3AB89; // Font Names (Outline Fonts Only)
	public static final int ID_FNO = 0xD3AE89; // Font Orientation
	public static final int ID_FNP = 0xD3AC89; // Font Position

	public static final int FLAG_ALL       = -1;
	public static final int FLAG_EXTENSION = 0x80;
	public static final int FLAG_SEGMENTED = 0x10;
	public static final int FLAG_PADDING   = 0x04;
	
	private static final int SFI_BASE_LENGTH = 8;
	
	private static final int SFLENGTH_OFFSET = 0;
	private static final int SFID_OFFSET = 2;
	private static final int SFFLAGS_OFFSET = 5;
	private static final int SFSEQUENCE_NUMBER_OFFSET = 6;
	private static final int SFIEXTENSION_OFFSET = 8;
	
//	private static final int MAX_BYTES_IN_EXTENSION = 254;

	protected int sfID;
	protected int sfFlags;
	protected int sfSequenceNumber;
	protected MODCAByteBuffer sfiExtensionData;
	protected MODCAByteBuffer sfParameterData;
	protected MODCAByteBuffer sfPaddingData;
	protected int sfPaddingTail = 1;
	
	public int getSFLength() {
		return getSFIntroducerLength() + getSFParameterLength() + getSFPaddingLength();
	}

	public int getSFID() {
		return sfID;
	}
	
	public void setSFID(int id) {
		sfID = id;
	}
	
	public int getSFFlags() {
		return sfFlags;
	}
	
	public void setSFFlags(int flags, boolean b) {
		if (b) {
			sfFlags |= flags;
		}
		else {
			sfFlags &= ~flags;
		}
	}
		
	public int getSFSequenceNumber() {
		return sfSequenceNumber;
	}
	
	public void setSFSequenceNumber(int sequence) {
		this.sfSequenceNumber = sequence;
	}
	
	public int getSFIntroducerLength() {
		if (sfiExtensionData == null || sfiExtensionData.size() == 0) {
			return SFI_BASE_LENGTH;
		}
		else {
			return SFI_BASE_LENGTH + sfiExtensionData.size() + 1;
		}
	}
		
	public int getSFParameterLength() {
		if (sfParameterData == null || sfParameterData.size() == 0) {
			return 0;
		}
		else {
			return sfParameterData.size();
		}
	}
		
	public int getSFPaddingLength() {
		if (sfPaddingData == null || sfPaddingData.size() == 0) {
			return 0;
		}
		else {
			if (sfPaddingData.size() > 255) {
				assert(sfPaddingTail == 3);
				return sfPaddingData.size() + sfPaddingTail;
			}
			else {
				assert(sfPaddingTail == 3 || sfPaddingTail == 1);
				return sfPaddingData.size() + sfPaddingTail;
			}
		}
	}

	public MODCAByteBuffer getSFIExtensionData() {
		if (sfiExtensionData == null) {
			sfiExtensionData = new MODCAByteBuffer();
		}
		return sfiExtensionData;
	}
	
	public MODCAByteBuffer getSFParameterData() {
		if (sfParameterData == null) {
			sfParameterData = new MODCAByteBuffer();
		}
		return sfParameterData;
	}
		
	public MODCAByteBuffer getSFPaddingData() {
		if (sfPaddingData == null) {
			sfPaddingData = new MODCAByteBuffer();
		}
		return sfPaddingData;
	}
		
	public void load(MODCAByteBuffer buffer, int offset) {
		int length = buffer.getUBIN2(offset + SFLENGTH_OFFSET);
		int introducerLength = SFI_BASE_LENGTH;
		int paddingLength = 0;
		sfID = buffer.getUBIN3(offset + SFID_OFFSET);
		sfFlags = buffer.getUBIN1(offset + SFFLAGS_OFFSET);
		sfSequenceNumber = buffer.getUBIN2(offset + SFSEQUENCE_NUMBER_OFFSET);
		if ((getSFFlags() & FLAG_EXTENSION) != 0) {
			int extLength = buffer.getUBIN1(offset + SFIEXTENSION_OFFSET);
			byte[] extBytes = buffer.getBytes(offset + SFIEXTENSION_OFFSET, extLength - 1);
			sfiExtensionData = new MODCAByteBuffer(extBytes);
			introducerLength += extLength;
		}
		if ((getSFFlags() & FLAG_PADDING) != 0) {
			paddingLength = buffer.getUBIN1(offset + length - 1);
			if (paddingLength == 0) {
				sfPaddingTail = 3;
				paddingLength = buffer.getUBIN2(offset + length - 3);
			}
			else {
				sfPaddingTail = 1;
			}
			byte[] paddingBytes = buffer.getBytes(offset + length - paddingLength, paddingLength - sfPaddingTail);
			sfPaddingData = new MODCAByteBuffer(paddingBytes);
		}
		int parameterLength = length - introducerLength - paddingLength;
		if (parameterLength > 0) {
			byte[] parameterBytes = buffer.getBytes(offset + introducerLength, parameterLength);
			sfParameterData = new MODCAByteBuffer(parameterBytes);
		}
	}
	
	public void save(MODCAByteBuffer buffer) {
		buffer.putUBIN2(getSFLength());
		buffer.putUBIN3(getSFID());
		buffer.putUBIN1(getSFFlags());
		buffer.putUBIN2(getSFSequenceNumber());
		if ((getSFFlags() & FLAG_EXTENSION) != 0) {
			if (sfiExtensionData == null || sfiExtensionData.size() == 0) {
				throw new IllegalStateException("sfiExtensionData is empty");
			}
			if (sfiExtensionData.size() > 254) {
				throw new IllegalStateException("sfiExtensionData out of range");
			}
			buffer.putUBIN1(sfiExtensionData.size() + 1);
			buffer.putBytes(sfiExtensionData.getBytes());
		}
		if (getSFParameterLength() != 0) {
			buffer.putBytes(sfParameterData.getBytes());
		}
		if ((getSFFlags() & FLAG_PADDING) != 0) {
			if (sfPaddingData == null || sfPaddingData.size() == 0) {
				throw new IllegalStateException("sfPaddingData is empty");
			}
			buffer.putBytes(sfPaddingData.getBytes());
			if (sfPaddingTail == 1) {
				if (sfPaddingData.size() > 255) {
					throw new IllegalStateException("improper sfPaddingTail");
				}
				buffer.putUBIN1(sfPaddingData.size() + 1);
			}
			else if (sfPaddingTail == 3) {
				buffer.putUBIN2(sfPaddingData.size() + 3);
				buffer.putUBIN1(0);
			}
			else {
				assert(false) : "never goes here";
			}
		}
	}

	private static String getSFIDName(int id) {
		Field[] fields = MODCAStructureField.class.getDeclaredFields();
		for (int i=0; i<fields.length; ++i) {
			try {
				if (Modifier.isStatic(fields[i].getModifiers()) 
						&& fields[i].getName().startsWith("ID_")
						&& fields[i].getInt(MODCAStructureField.class) == id) {
					return fields[i].getName().substring(3);
				}
			} catch (IllegalArgumentException e) {
				assert(false) : "never goes here";
			} catch (IllegalAccessException e) {
				assert(false) : "never goes here";
			}
		}
		return "NOT_DEFINED_ID";
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("structure field " + getSFIDName(getSFID()) + ":\n");
		sb.append("");
		return sb.toString();
	}
}
