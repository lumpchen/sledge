package com.docscience.pathfinder.font.driver.ttf;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * @author wxin
 * 
 */
public abstract class TTFEncoding {

	public static final int PLATFORM_UNICODE = 0;
	public static final int PLATFORM_MACINTOSH = 1;
	public static final int PLATFORM_ISO_DEPRECATED = 2; // funny thing.
	public static final int PLATFORM_MICROSOFT = 3;
	public static final int PLATFORM_CUSTOM = 4;

	public static final int UNICODE_ENCODING_10 = 0; // Unicode 1.0 semantics
	public static final int UNICODE_ENCODING_11 = 1; // Unicode 1.1 semantics
	public static final int UNICODE_ENCODING_ISO_10646 = 2; // ISO 10646:1993
															// semantics
	public static final int UNICODE_ENCODING_2_BMP = 3; // Unicode 2.0 and
														// onwards semantics,
														// Unicode BMP only.
	public static final int UNICODE_ENCODING_2_FULL = 4; // Unicode 2.0 and
															// onwards
															// semantics,
															// Unicode full
															// repertoire.

	public static final int MICROSOFT_ENCODING_SYMBOL = 0;
	public static final int MICROSOFT_ENCODING_UNICODE_BMP = 1;
	public static final int MICROSOFT_ENCODING_SHIFTJIS = 2;
	public static final int MICROSOFT_ENCODING_PRC = 3;
	public static final int MICROSOFT_ENCODING_BIG5 = 4;
	public static final int MICROSOFT_ENCODING_WANSUNG = 5;
	public static final int MICROSOFT_ENCODING_JOHAB = 6;
	public static final int MICROSOFT_ENCODING_UNICODE_FULL = 10; // Unicode
																	// full
																	// repertoire

	public static final int MACINTOSH_ENCODING_ROMAN = 0;
	public static final int MACINTOSH_ENCODING_JAPANESE = 1;
	public static final int MACINTOSH_ENCODING_CHINESE_TRADITIONAL = 2;
	public static final int MACINTOSH_ENCODING_KOREAN = 3;
	public static final int MACINTOSH_ENCODING_ARABIC = 4;
	public static final int MACINTOSH_ENCODING_HEBREW = 5;
	public static final int MACINTOSH_ENCODING_GREEK = 6;
	public static final int MACINTOSH_ENCODING_RUSSIAN = 7;
	public static final int MACINTOSH_ENCODING_RSYMBOL = 8;
	public static final int MACINTOSH_ENCODING_DEVANAGARI = 9;
	public static final int MACINTOSH_ENCODING_GURMUKHI = 10;
	public static final int MACINTOSH_ENCODING_GUJARATI = 11;
	public static final int MACINTOSH_ENCODING_ORIYA = 12;
	public static final int MACINTOSH_ENCODING_BENGALI = 13;
	public static final int MACINTOSH_ENCODING_TAMIL = 14;
	public static final int MACINTOSH_ENCODING_TELUGU = 15;
	public static final int MACINTOSH_ENCODING_KANNADA = 16;
	public static final int MACINTOSH_ENCODING_MALAYALAM = 17;
	public static final int MACINTOSH_ENCODING_SINHALESE = 18;
	public static final int MACINTOSH_ENCODING_BURMESE = 19;
	public static final int MACINTOSH_ENCODING_KHMER = 20;
	public static final int MACINTOSH_ENCODING_THAI = 21;
	public static final int MACINTOSH_ENCODING_LAOTIAN = 22;
	public static final int MACINTOSH_ENCODING_GEORGIAN = 23;
	public static final int MACINTOSH_ENCODING_ARMENIAN = 24;
	public static final int MACINTOSH_ENCODING_CHINESE_SIMPLIFIED = 25;
	public static final int MACINTOSH_ENCODING_TIBETAN = 26;
	public static final int MACINTOSH_ENCODING_MONGOLIAN = 27;
	public static final int MACINTOSH_ENCODING_GEEZ = 28;
	public static final int MACINTOSH_ENCODING_SLAVIC = 29;
	public static final int MACINTOSH_ENCODING_VIETNAMESE = 30;
	public static final int MACINTOSH_ENCODING_SINDHI = 31;
	public static final int MACINTOSH_ENCODING_UNINTERPRETED = 32;

	public static final int ISO_ENCODING_ASCII = 0;
	public static final int ISO_ENCODING_10646 = 1;
	public static final int ISO_ENCODING_8859_1 = 2;

	public static final int MICROSOFT_LANGUAGE_ENGLISH = 9;
	public static final int MICROSOFT_LANGUAGE_ARABIC_SAUDI_ARABIA = 1025;
	public static final int MICROSOFT_LANGUAGE_BULGARIAN = 1026;
	public static final int MICROSOFT_LANGUAGE_CATALAN = 1027;
	public static final int MICROSOFT_LANGUAGE_CHINESE_TAIWAN = 1028;
	public static final int MICROSOFT_LANGUAGE_CZECH = 1029;
	public static final int MICROSOFT_LANGUAGE_DANISH = 1030;
	public static final int MICROSOFT_LANGUAGE_GERMAN_GERMANY = 1031;
	public static final int MICROSOFT_LANGUAGE_GREEK = 1032;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_UNITED_STATES = 1033;
	public static final int MICROSOFT_LANGUAGE_SPANISH_SPAIN_TRADITIONAL = 1034;
	public static final int MICROSOFT_LANGUAGE_FINNISH = 1035;
	public static final int MICROSOFT_LANGUAGE_FRENCH_FRANCE = 1036;
	public static final int MICROSOFT_LANGUAGE_HEBREW = 1037;
	public static final int MICROSOFT_LANGUAGE_HUNGARIAN = 1038;
	public static final int MICROSOFT_LANGUAGE_ICELANDIC = 1039;
	public static final int MICROSOFT_LANGUAGE_ITALIAN_ITALY = 1040;
	public static final int MICROSOFT_LANGUAGE_JAPANESE = 1041;
	public static final int MICROSOFT_LANGUAGE_KOREAN = 1042;
	public static final int MICROSOFT_LANGUAGE_DUTCH_NETHERLANDS = 1043;
	public static final int MICROSOFT_LANGUAGE_NORWEGIAN_BOKML = 1044;
	public static final int MICROSOFT_LANGUAGE_POLISH = 1045;
	public static final int MICROSOFT_LANGUAGE_PORTUGUESE_BRAZIL = 1046;
	public static final int MICROSOFT_LANGUAGE_RAETO_ROMANCE = 1047;
	public static final int MICROSOFT_LANGUAGE_ROMANIAN_ROMANIA = 1048;
	public static final int MICROSOFT_LANGUAGE_RUSSIAN = 1049;
	public static final int MICROSOFT_LANGUAGE_CROATIAN = 1050;
	public static final int MICROSOFT_LANGUAGE_SLOVAK = 1051;
	public static final int MICROSOFT_LANGUAGE_ALBANIAN = 1052;
	public static final int MICROSOFT_LANGUAGE_SWEDISH_SWEDEN = 1053;
	public static final int MICROSOFT_LANGUAGE_THAI = 1054;
	public static final int MICROSOFT_LANGUAGE_TURKISH = 1055;
	public static final int MICROSOFT_LANGUAGE_URDU = 1056;
	public static final int MICROSOFT_LANGUAGE_INDONESIAN = 1057;
	public static final int MICROSOFT_LANGUAGE_UKRAINIAN = 1058;
	public static final int MICROSOFT_LANGUAGE_BELARUSIAN = 1059;
	public static final int MICROSOFT_LANGUAGE_SLOVENIAN = 1060;
	public static final int MICROSOFT_LANGUAGE_ESTONIAN = 1061;
	public static final int MICROSOFT_LANGUAGE_LATVIAN = 1062;
	public static final int MICROSOFT_LANGUAGE_LITHUANIAN = 1063;
	public static final int MICROSOFT_LANGUAGE_TAJIK = 1064;
	public static final int MICROSOFT_LANGUAGE_FARSI_PERSIAN = 1065;
	public static final int MICROSOFT_LANGUAGE_VIETNAMESE = 1066;
	public static final int MICROSOFT_LANGUAGE_ARMENIAN = 1067;
	public static final int MICROSOFT_LANGUAGE_AZERI_LATIN = 1068;
	public static final int MICROSOFT_LANGUAGE_BASQUE = 1069;
	public static final int MICROSOFT_LANGUAGE_SORBIAN = 1070;
	public static final int MICROSOFT_LANGUAGE_FYRO_MACEDONIA = 1071;
	public static final int MICROSOFT_LANGUAGE_SESOTHO_SUTU = 1072;
	public static final int MICROSOFT_LANGUAGE_TSONGA = 1073;
	public static final int MICROSOFT_LANGUAGE_SETSUANA = 1074;
	public static final int MICROSOFT_LANGUAGE_VENDA = 1075;
	public static final int MICROSOFT_LANGUAGE_XHOSA = 1076;
	public static final int MICROSOFT_LANGUAGE_ZULU = 1077;
	public static final int MICROSOFT_LANGUAGE_AFRIKAANS = 1078;
	public static final int MICROSOFT_LANGUAGE_GEORGIAN = 1079;
	public static final int MICROSOFT_LANGUAGE_FAROESE = 1080;
	public static final int MICROSOFT_LANGUAGE_HINDI = 1081;
	public static final int MICROSOFT_LANGUAGE_MALTESE = 1082;
	public static final int MICROSOFT_LANGUAGE_SAMI_LAPPISH = 1083;
	public static final int MICROSOFT_LANGUAGE_GAELIC_SCOTLAND = 1084;
	public static final int MICROSOFT_LANGUAGE_YIDDISH = 1085;
	public static final int MICROSOFT_LANGUAGE_MALAY_MALAYSIA = 1086;
	public static final int MICROSOFT_LANGUAGE_KAZAKH = 1087;
	public static final int MICROSOFT_LANGUAGE_KYRGYZ_CYRILLIC = 1088;
	public static final int MICROSOFT_LANGUAGE_SWAHILI = 1089;
	public static final int MICROSOFT_LANGUAGE_TURKMEN = 1090;
	public static final int MICROSOFT_LANGUAGE_UZBEK_LATIN = 1091;
	public static final int MICROSOFT_LANGUAGE_TATAR = 1092;
	public static final int MICROSOFT_LANGUAGE_BENGALI_INDIA = 1093;
	public static final int MICROSOFT_LANGUAGE_PUNJABI = 1094;
	public static final int MICROSOFT_LANGUAGE_GUJARATI = 1095;
	public static final int MICROSOFT_LANGUAGE_ORIYA = 1096;
	public static final int MICROSOFT_LANGUAGE_TAMIL = 1097;
	public static final int MICROSOFT_LANGUAGE_TELUGU = 1098;
	public static final int MICROSOFT_LANGUAGE_KANNADA = 1099;
	public static final int MICROSOFT_LANGUAGE_MALAYALAM = 1100;
	public static final int MICROSOFT_LANGUAGE_ASSAMESE = 1101;
	public static final int MICROSOFT_LANGUAGE_MARATHI = 1102;
	public static final int MICROSOFT_LANGUAGE_SANSKRIT = 1103;
	public static final int MICROSOFT_LANGUAGE_MONGOLIAN = 1104;
	public static final int MICROSOFT_LANGUAGE_TIBETAN = 1105;
	public static final int MICROSOFT_LANGUAGE_WELSH = 1106;
	public static final int MICROSOFT_LANGUAGE_KHMER = 1107;
	public static final int MICROSOFT_LANGUAGE_LAO = 1108;
	public static final int MICROSOFT_LANGUAGE_BURMESE = 1109;
	public static final int MICROSOFT_LANGUAGE_GALICIAN = 1110;
	public static final int MICROSOFT_LANGUAGE_KONKANI = 1111;
	public static final int MICROSOFT_LANGUAGE_MANIPURI = 1112;
	public static final int MICROSOFT_LANGUAGE_SINDHI = 1113;
	public static final int MICROSOFT_LANGUAGE_SYRIAC = 1114;
	public static final int MICROSOFT_LANGUAGE_SINHALA_SINHALESE = 1115;
	public static final int MICROSOFT_LANGUAGE_AMHARIC = 1118;
	public static final int MICROSOFT_LANGUAGE_KASHMIRI = 1120;
	public static final int MICROSOFT_LANGUAGE_NEPALI = 1121;
	public static final int MICROSOFT_LANGUAGE_FRISIAN_NETHERLANDS = 1122;
	public static final int MICROSOFT_LANGUAGE_FILIPINO = 1124;
	public static final int MICROSOFT_LANGUAGE_DIVEHI_DHIVEHI_MALDIVIAN = 1125;
	public static final int MICROSOFT_LANGUAGE_EDO = 1126;
	public static final int MICROSOFT_LANGUAGE_IGBO_NIGERIA = 1136;
	public static final int MICROSOFT_LANGUAGE_GUARANI_PARAGUAY = 1140;
	public static final int MICROSOFT_LANGUAGE_LATIN = 1142;
	public static final int MICROSOFT_LANGUAGE_SOMALI = 1143;
	public static final int MICROSOFT_LANGUAGE_MAORI = 1153;
	public static final int MICROSOFT_LANGUAGE_HID_HUMAN_INTERFACE_DEVICE = 1279;
	public static final int MICROSOFT_LANGUAGE_ARABIC_IRAQ = 2049;
	public static final int MICROSOFT_LANGUAGE_CHINESE_CHINA = 2052;
	public static final int MICROSOFT_LANGUAGE_GERMAN_SWITZERLAND = 2055;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_GREAT_BRITAIN = 2057;
	public static final int MICROSOFT_LANGUAGE_SPANISH_MEXICO = 2058;
	public static final int MICROSOFT_LANGUAGE_FRENCH_BELGIUM = 2060;
	public static final int MICROSOFT_LANGUAGE_ITALIAN_SWITZERLAND = 2064;
	public static final int MICROSOFT_LANGUAGE_KOREAN_JOHAB = 2066;
	public static final int MICROSOFT_LANGUAGE_DUTCH_BELGIUM = 2067;
	public static final int MICROSOFT_LANGUAGE_NORWEGIAN_NYNORSK = 2068;
	public static final int MICROSOFT_LANGUAGE_PORTUGUESE_PORTUGAL = 2070;
	public static final int MICROSOFT_LANGUAGE_ROMANIAN_MOLDOVA = 2072;
	public static final int MICROSOFT_LANGUAGE_RUSSIAN_MOLDOVA = 2073;
	public static final int MICROSOFT_LANGUAGE_SERBIAN_LATIN = 2074;
	public static final int MICROSOFT_LANGUAGE_SWEDISH_FINLAND = 2077;
	public static final int MICROSOFT_LANGUAGE_AZERI_CYRILLIC = 2092;
	public static final int MICROSOFT_LANGUAGE_GAELIC_IRELAND = 2108;
	public static final int MICROSOFT_LANGUAGE_MALAY_BRUNEI = 2110;
	public static final int MICROSOFT_LANGUAGE_UZBEK_CYRILLIC = 2115;
	public static final int MICROSOFT_LANGUAGE_BENGALI_BANGLADESH = 2117;
	public static final int MICROSOFT_LANGUAGE_MONGOLIAN_2 = 2128;
	public static final int MICROSOFT_LANGUAGE_ARABIC_EGYPT = 3073;
	public static final int MICROSOFT_LANGUAGE_CHINESE_HONG_KONG_SAR = 3076;
	public static final int MICROSOFT_LANGUAGE_GERMAN_AUSTRIA = 3079;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_AUSTRALIA = 3081;
	public static final int MICROSOFT_LANGUAGE_SPANISH_SPAIN_MODERN_SORT = 3082;
	public static final int MICROSOFT_LANGUAGE_FRENCH_CANADA = 3084;
	public static final int MICROSOFT_LANGUAGE_SERBIAN_CYRILLIC = 3098;
	public static final int MICROSOFT_LANGUAGE_ARABIC_LIBYA = 4097;
	public static final int MICROSOFT_LANGUAGE_CHINESE_SINGAPORE = 4100;
	public static final int MICROSOFT_LANGUAGE_GERMAN_LUXEMBOURG = 4103;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_CANADA = 4105;
	public static final int MICROSOFT_LANGUAGE_SPANISH_GUATEMALA = 4106;
	public static final int MICROSOFT_LANGUAGE_FRENCH_SWITZERLAND = 4108;
	public static final int MICROSOFT_LANGUAGE_ARABIC_ALGERIA = 5121;
	public static final int MICROSOFT_LANGUAGE_CHINESE_MACAU_SAR = 5124;
	public static final int MICROSOFT_LANGUAGE_GERMAN_LIECHTENSTEIN = 5127;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_NEW_ZEALAND = 5129;
	public static final int MICROSOFT_LANGUAGE_SPANISH_COSTA_RICA = 5130;
	public static final int MICROSOFT_LANGUAGE_FRENCH_LUXEMBOURG = 5132;
	public static final int MICROSOFT_LANGUAGE_BOSNIAN = 5146;
	public static final int MICROSOFT_LANGUAGE_ARABIC_MOROCCO = 6145;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_IRELAND = 6153;
	public static final int MICROSOFT_LANGUAGE_SPANISH_PANAMA = 6154;
	public static final int MICROSOFT_LANGUAGE_FRENCH_MONACO = 6156;
	public static final int MICROSOFT_LANGUAGE_ARABIC_TUNISIA = 7169;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_SOUTHERN_AFRICA = 7177;
	public static final int MICROSOFT_LANGUAGE_SPANISH_DOMINICAN_REPUBLIC = 7178;
	public static final int MICROSOFT_LANGUAGE_FRENCH_WEST_INDIES = 7180;
	public static final int MICROSOFT_LANGUAGE_ARABIC_OMAN = 8193;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_JAMAICA = 8201;
	public static final int MICROSOFT_LANGUAGE_SPANISH_VENEZUELA = 8202;
	public static final int MICROSOFT_LANGUAGE_ARABIC_YEMEN = 9217;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_CARIBBEAN = 9225;
	public static final int MICROSOFT_LANGUAGE_SPANISH_COLOMBIA = 9226;
	public static final int MICROSOFT_LANGUAGE_FRENCH_CONGO = 9228;
	public static final int MICROSOFT_LANGUAGE_ARABIC_SYRIA = 10241;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_BELIZE = 10249;
	public static final int MICROSOFT_LANGUAGE_SPANISH_PERU = 10250;
	public static final int MICROSOFT_LANGUAGE_FRENCH_SENEGAL = 10252;
	public static final int MICROSOFT_LANGUAGE_ARABIC_JORDAN = 11265;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_TRINIDAD = 11273;
	public static final int MICROSOFT_LANGUAGE_SPANISH_ARGENTINA = 11274;
	public static final int MICROSOFT_LANGUAGE_FRENCH_CAMEROON = 11276;
	public static final int MICROSOFT_LANGUAGE_ARABIC_LEBANON = 12289;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_ZIMBABWE = 12297;
	public static final int MICROSOFT_LANGUAGE_SPANISH_ECUADOR = 12298;
	public static final int MICROSOFT_LANGUAGE_FRENCH_COTE_DIVOIRE = 12300;
	public static final int MICROSOFT_LANGUAGE_ARABIC_KUWAIT = 13313;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_PHILLIPPINES = 13321;
	public static final int MICROSOFT_LANGUAGE_SPANISH_CHILE = 13322;
	public static final int MICROSOFT_LANGUAGE_FRENCH_MALI = 13324;
	public static final int MICROSOFT_LANGUAGE_ARABIC_UNITED_ARAB_EMIRATES = 14337;
	public static final int MICROSOFT_LANGUAGE_SPANISH_URUGUAY = 14346;
	public static final int MICROSOFT_LANGUAGE_FRENCH_MOROCCO = 14348;
	public static final int MICROSOFT_LANGUAGE_ARABIC_BAHRAIN = 15361;
	public static final int MICROSOFT_LANGUAGE_SPANISH_PARAGUAY = 15370;
	public static final int MICROSOFT_LANGUAGE_ARABIC_QATAR = 16385;
	public static final int MICROSOFT_LANGUAGE_ENGLISH_INDIA = 16393;
	public static final int MICROSOFT_LANGUAGE_SPANISH_BOLIVIA = 16394;
	public static final int MICROSOFT_LANGUAGE_SPANISH_EL_SALVADOR = 17418;
	public static final int MICROSOFT_LANGUAGE_SPANISH_HONDURAS = 18442;
	public static final int MICROSOFT_LANGUAGE_SPANISH_NICARAGUA = 19466;
	public static final int MICROSOFT_LANGUAGE_SPANISH_PUERTO_RICO = 20490;

	public static final int NAME_COPYRIGHT_NOTICE = 0;
	public static final int NAME_FONT_FAMILY = 1;
	public static final int NAME_FONT_SUBFAMILY = 2;
	public static final int NAME_FONT_IDENTIFIER = 3;
	public static final int NAME_FONT_FULL_NAME = 4;
	public static final int NAME_VERSION_STRING = 5;
	public static final int NAME_POSTSCRIPT_FONT_NAME = 6;
	public static final int NAME_TRADEMARK = 7;
	public static final int NAME_MANUFACTURER = 8;
	public static final int NAME_DESIGNER = 9;
	public static final int NAME_DESCRIPTION = 10;
	public static final int NAME_URL_VENDOR = 11;
	public static final int NAME_URL_DESIGNER = 12;
	public static final int NAME_LICENSE_DESCRIPTION = 13;
	public static final int NAME_LICENSE_INFO_URL = 14;
	public static final int NAME_RESERVED = 15;
	public static final int NAME_PREFERRED_FAMILY = 16;
	public static final int NAME_PREFERRED_SUBFAMILY = 17;
	public static final int NAME_COMPATIBLE_FULL = 18;
	public static final int NAME_SAMPLE_TEXT = 19;
	public static final int NAME_POSTSCRIPT_CID_FINDFONT_NAME = 20;

	public static String getNameDesc(int nameID) {
		switch (nameID) {
		case NAME_COPYRIGHT_NOTICE:
			return "copyright notice";
		case NAME_FONT_FAMILY:
			return "font family";
		case NAME_FONT_SUBFAMILY:
			return "font subfamily";
		case NAME_FONT_IDENTIFIER:
			return "font identifier";
		case NAME_FONT_FULL_NAME:
			return "font full name";
		case NAME_VERSION_STRING:
			return "version string";
		case NAME_POSTSCRIPT_FONT_NAME:
			return "font postscript name";
		case NAME_TRADEMARK:
			return "trademark";
		case NAME_MANUFACTURER:
			return "manufacturer";
		case NAME_DESIGNER:
			return "designer";
		case NAME_DESCRIPTION:
			return "description";
		case NAME_URL_VENDOR:
			return "url vendor";
		case NAME_URL_DESIGNER:
			return "url designer";
		case NAME_LICENSE_DESCRIPTION:
			return "license description";
		case NAME_LICENSE_INFO_URL:
			return "license info url";
		case NAME_RESERVED:
			return "reserved";
		case NAME_PREFERRED_FAMILY:
			return "preferred family";
		case NAME_PREFERRED_SUBFAMILY:
			return "preferred subfamily";
		case NAME_COMPATIBLE_FULL:
			return "compatible full";
		case NAME_SAMPLE_TEXT:
			return "sample text";
		case NAME_POSTSCRIPT_CID_FINDFONT_NAME:
			return "postscript cid findfont name";
		default:
			return "unknown";
		}
	}

	@Deprecated
	public static String getEncoding(int platformID, int encodingID) {
		switch (platformID) {
		case PLATFORM_UNICODE:
			return "UTF-16";
		case PLATFORM_MACINTOSH:
			switch (encodingID) {
			case MACINTOSH_ENCODING_ROMAN:
				return "MacRoman";
			case MACINTOSH_ENCODING_JAPANESE:
				return "SJIS";
			case MACINTOSH_ENCODING_CHINESE_TRADITIONAL:
				return "Big5";
			case MACINTOSH_ENCODING_CHINESE_SIMPLIFIED:
				return "GBK";
			case MACINTOSH_ENCODING_ARABIC:
				return "MacArabic";
			case MACINTOSH_ENCODING_HEBREW:
				return "MacHebrew";
			case MACINTOSH_ENCODING_THAI:
				return "MacThai";
			case MACINTOSH_ENCODING_KOREAN:
				return "EUC_KR";
			case MACINTOSH_ENCODING_GREEK:
				return "MacGreek";
			case MACINTOSH_ENCODING_RUSSIAN:
				return "KOI8_R";
			case MACINTOSH_ENCODING_RSYMBOL:
				return "MacSymbol";
			default:
				return "ASCII";
			}
		case PLATFORM_ISO_DEPRECATED:
			switch (encodingID) {
			case ISO_ENCODING_ASCII:
				return "ASCII";
			case ISO_ENCODING_10646:
				return "UTF-16";
			case ISO_ENCODING_8859_1:
				return "ISO8859_1";
			default:
				return "ASCII";
			}
		case PLATFORM_MICROSOFT:
			switch (encodingID) {
			case MICROSOFT_ENCODING_SYMBOL:
				return "UTF-16";
			case MICROSOFT_ENCODING_UNICODE_BMP:
				return "UTF-16";
			case MICROSOFT_ENCODING_SHIFTJIS:
				return "SJIS";
			case MICROSOFT_ENCODING_PRC:
				return "GBK";
			case MICROSOFT_ENCODING_BIG5:
				return "Big5";
			case MICROSOFT_ENCODING_UNICODE_FULL:
				return "UTF-16";
			case MICROSOFT_ENCODING_WANSUNG:
				return "EUC_KR";
			case MICROSOFT_ENCODING_JOHAB:
				return "Johab";
			default:
				return "ASCII";
			}
		case PLATFORM_CUSTOM:
		default:
			return "ASCII";
		}
	}

	public static Charset getCharset(int platformID, int encodingID) {
		String encoding = getEncoding(platformID, encodingID);
		if (Charset.isSupported(encoding)) {
			return Charset.forName(encoding);
		}
		return Charset.forName("ISO-8859-1");
	}

	public static Locale getLocale(int platformID, int languageID) {
		switch (platformID) {
		case PLATFORM_UNICODE:
		case PLATFORM_ISO_DEPRECATED:
		case PLATFORM_CUSTOM:
			return Locale.US;
		case PLATFORM_MACINTOSH:
			return getMacintoshLocale(languageID);
		case PLATFORM_MICROSOFT:
			return getMicrosoftLocale(languageID);
		default:
			throw new IllegalArgumentException();
		}
	}

	private static Map<Integer, Locale> microsoftLocaleCache = new HashMap<Integer, Locale>();

	private static Locale getMicrosoftLocale(int languageID) {
		Locale locale = microsoftLocaleCache.get(languageID);
		if (locale == null) {
			locale = getMicrosoftLocaleReal(languageID);
			if (locale == null) {
				return Locale.US;
			}
			microsoftLocaleCache.put(languageID, locale);
		}
		return locale;
	}

	private static Locale getMicrosoftLocaleReal(int languageID) {
		switch (languageID) {
		case MICROSOFT_LANGUAGE_ENGLISH:
			return new Locale("en");
		case MICROSOFT_LANGUAGE_ARABIC_SAUDI_ARABIA:
			return new Locale("ar", "SA");
		case MICROSOFT_LANGUAGE_BULGARIAN:
			return new Locale("bg");
		case MICROSOFT_LANGUAGE_CATALAN:
			return new Locale("ca");
		case MICROSOFT_LANGUAGE_CHINESE_TAIWAN:
			return new Locale("zh", "TW");
		case MICROSOFT_LANGUAGE_CZECH:
			return new Locale("cs");
		case MICROSOFT_LANGUAGE_DANISH:
			return new Locale("da");
		case MICROSOFT_LANGUAGE_GERMAN_GERMANY:
			return new Locale("de", "DE");
		case MICROSOFT_LANGUAGE_GREEK:
			return new Locale("el");
		case MICROSOFT_LANGUAGE_ENGLISH_UNITED_STATES:
			return new Locale("en", "US");
		case MICROSOFT_LANGUAGE_SPANISH_SPAIN_TRADITIONAL:
			return new Locale("es", "ES");
		case MICROSOFT_LANGUAGE_FINNISH:
			return new Locale("fi");
		case MICROSOFT_LANGUAGE_FRENCH_FRANCE:
			return new Locale("fr", "FR");
		case MICROSOFT_LANGUAGE_HEBREW:
			return new Locale("he");
		case MICROSOFT_LANGUAGE_HUNGARIAN:
			return new Locale("hu");
		case MICROSOFT_LANGUAGE_ICELANDIC:
			return new Locale("is");
		case MICROSOFT_LANGUAGE_ITALIAN_ITALY:
			return new Locale("it", "IT");
		case MICROSOFT_LANGUAGE_JAPANESE:
			return new Locale("ja");
		case MICROSOFT_LANGUAGE_KOREAN:
			return new Locale("ko");
		case MICROSOFT_LANGUAGE_DUTCH_NETHERLANDS:
			return new Locale("nl", "NL");
		case MICROSOFT_LANGUAGE_NORWEGIAN_BOKML:
			return new Locale("no", "NO");
		case MICROSOFT_LANGUAGE_POLISH:
			return new Locale("pl");
		case MICROSOFT_LANGUAGE_PORTUGUESE_BRAZIL:
			return new Locale("pt", "BR");
		case MICROSOFT_LANGUAGE_RAETO_ROMANCE:
			return new Locale("rm");
		case MICROSOFT_LANGUAGE_ROMANIAN_ROMANIA:
			return new Locale("ro");
		case MICROSOFT_LANGUAGE_RUSSIAN:
			return new Locale("ru");
		case MICROSOFT_LANGUAGE_CROATIAN:
			return new Locale("hr");
		case MICROSOFT_LANGUAGE_SLOVAK:
			return new Locale("sk");
		case MICROSOFT_LANGUAGE_ALBANIAN:
			return new Locale("sq");
		case MICROSOFT_LANGUAGE_SWEDISH_SWEDEN:
			return new Locale("sv", "SE");
		case MICROSOFT_LANGUAGE_THAI:
			return new Locale("th");
		case MICROSOFT_LANGUAGE_TURKISH:
			return new Locale("tr");
		case MICROSOFT_LANGUAGE_URDU:
			return new Locale("ur");
		case MICROSOFT_LANGUAGE_INDONESIAN:
			return new Locale("id");
		case MICROSOFT_LANGUAGE_UKRAINIAN:
			return new Locale("uk");
		case MICROSOFT_LANGUAGE_BELARUSIAN:
			return new Locale("be");
		case MICROSOFT_LANGUAGE_SLOVENIAN:
			return new Locale("sl");
		case MICROSOFT_LANGUAGE_ESTONIAN:
			return new Locale("et");
		case MICROSOFT_LANGUAGE_LATVIAN:
			return new Locale("lv");
		case MICROSOFT_LANGUAGE_LITHUANIAN:
			return new Locale("lt");
		case MICROSOFT_LANGUAGE_TAJIK:
			return new Locale("tg");
		case MICROSOFT_LANGUAGE_FARSI_PERSIAN:
			return new Locale("fa");
		case MICROSOFT_LANGUAGE_VIETNAMESE:
			return new Locale("vi");
		case MICROSOFT_LANGUAGE_ARMENIAN:
			return new Locale("hy");
		case MICROSOFT_LANGUAGE_AZERI_LATIN:
			return new Locale("az", "AZ");
		case MICROSOFT_LANGUAGE_BASQUE:
			return new Locale("eu");
		case MICROSOFT_LANGUAGE_SORBIAN:
			return new Locale("sb");
		case MICROSOFT_LANGUAGE_FYRO_MACEDONIA:
			return new Locale("mk");
		case MICROSOFT_LANGUAGE_SESOTHO_SUTU:
			return new Locale("sx");
		case MICROSOFT_LANGUAGE_TSONGA:
			return new Locale("ts");
		case MICROSOFT_LANGUAGE_SETSUANA:
			return new Locale("tn");
		case MICROSOFT_LANGUAGE_VENDA:
			return new Locale("ve");
		case MICROSOFT_LANGUAGE_XHOSA:
			return new Locale("xh");
		case MICROSOFT_LANGUAGE_ZULU:
			return new Locale("zu");
		case MICROSOFT_LANGUAGE_AFRIKAANS:
			return new Locale("af");
		case MICROSOFT_LANGUAGE_FAROESE:
			return new Locale("fo");
		case MICROSOFT_LANGUAGE_HINDI:
			return new Locale("hi");
		case MICROSOFT_LANGUAGE_MALTESE:
			return new Locale("mt");
		case MICROSOFT_LANGUAGE_SAMI_LAPPISH:
			return new Locale("sz");
		case MICROSOFT_LANGUAGE_GAELIC_SCOTLAND:
			return new Locale("gd");
		case MICROSOFT_LANGUAGE_YIDDISH:
			return new Locale("yi");
		case MICROSOFT_LANGUAGE_MALAY_MALAYSIA:
			return new Locale("ms", "MY");
		case MICROSOFT_LANGUAGE_KAZAKH:
			return new Locale("kk");
		case MICROSOFT_LANGUAGE_SWAHILI:
			return new Locale("sw");
		case MICROSOFT_LANGUAGE_TURKMEN:
			return new Locale("tk");
		case MICROSOFT_LANGUAGE_UZBEK_LATIN:
			return new Locale("uz", "UZ");
		case MICROSOFT_LANGUAGE_TATAR:
			return new Locale("tt");
		case MICROSOFT_LANGUAGE_BENGALI_INDIA:
			return new Locale("bn");
		case MICROSOFT_LANGUAGE_PUNJABI:
			return new Locale("pa");
		case MICROSOFT_LANGUAGE_GUJARATI:
			return new Locale("gu");
		case MICROSOFT_LANGUAGE_ORIYA:
			return new Locale("or");
		case MICROSOFT_LANGUAGE_TAMIL:
			return new Locale("ta");
		case MICROSOFT_LANGUAGE_TELUGU:
			return new Locale("te");
		case MICROSOFT_LANGUAGE_KANNADA:
			return new Locale("kn");
		case MICROSOFT_LANGUAGE_MALAYALAM:
			return new Locale("ml");
		case MICROSOFT_LANGUAGE_ASSAMESE:
			return new Locale("as");
		case MICROSOFT_LANGUAGE_MARATHI:
			return new Locale("mr");
		case MICROSOFT_LANGUAGE_SANSKRIT:
			return new Locale("sa");
		case MICROSOFT_LANGUAGE_TIBETAN:
			return new Locale("bo");
		case MICROSOFT_LANGUAGE_WELSH:
			return new Locale("cy");
		case MICROSOFT_LANGUAGE_KHMER:
			return new Locale("km");
		case MICROSOFT_LANGUAGE_LAO:
			return new Locale("lo");
		case MICROSOFT_LANGUAGE_BURMESE:
			return new Locale("my");
		case MICROSOFT_LANGUAGE_SINDHI:
			return new Locale("sd");
		case MICROSOFT_LANGUAGE_SINHALA_SINHALESE:
			return new Locale("si");
		case MICROSOFT_LANGUAGE_AMHARIC:
			return new Locale("am");
		case MICROSOFT_LANGUAGE_KASHMIRI:
			return new Locale("ks");
		case MICROSOFT_LANGUAGE_NEPALI:
			return new Locale("ne");
		case MICROSOFT_LANGUAGE_DIVEHI_DHIVEHI_MALDIVIAN:
			return new Locale("dv");
		case MICROSOFT_LANGUAGE_GUARANI_PARAGUAY:
			return new Locale("gn");
		case MICROSOFT_LANGUAGE_LATIN:
			return new Locale("la");
		case MICROSOFT_LANGUAGE_SOMALI:
			return new Locale("so");
		case MICROSOFT_LANGUAGE_MAORI:
			return new Locale("mi");
		case MICROSOFT_LANGUAGE_ARABIC_IRAQ:
			return new Locale("ar", "IQ");
		case MICROSOFT_LANGUAGE_CHINESE_CHINA:
			return new Locale("zh", "CN");
		case MICROSOFT_LANGUAGE_GERMAN_SWITZERLAND:
			return new Locale("de", "CH");
		case MICROSOFT_LANGUAGE_ENGLISH_GREAT_BRITAIN:
			return new Locale("en", "GB");
		case MICROSOFT_LANGUAGE_SPANISH_MEXICO:
			return new Locale("es", "MX");
		case MICROSOFT_LANGUAGE_FRENCH_BELGIUM:
			return new Locale("fr", "BE");
		case MICROSOFT_LANGUAGE_ITALIAN_SWITZERLAND:
			return new Locale("it", "CH");
		case MICROSOFT_LANGUAGE_KOREAN_JOHAB:
			return new Locale("ko");
		case MICROSOFT_LANGUAGE_DUTCH_BELGIUM:
			return new Locale("nl", "BE");
		case MICROSOFT_LANGUAGE_NORWEGIAN_NYNORSK:
			return new Locale("no", "NO");
		case MICROSOFT_LANGUAGE_PORTUGUESE_PORTUGAL:
			return new Locale("pt", "PT");
		case MICROSOFT_LANGUAGE_ROMANIAN_MOLDOVA:
			return new Locale("ro", "MO");
		case MICROSOFT_LANGUAGE_RUSSIAN_MOLDOVA:
			return new Locale("ru", "MO");
		case MICROSOFT_LANGUAGE_SERBIAN_LATIN:
			return new Locale("sr", "SP");
		case MICROSOFT_LANGUAGE_SWEDISH_FINLAND:
			return new Locale("sv", "FI");
		case MICROSOFT_LANGUAGE_AZERI_CYRILLIC:
			return new Locale("az", "AZ");
		case MICROSOFT_LANGUAGE_GAELIC_IRELAND:
			return new Locale("gd", "IE");
		case MICROSOFT_LANGUAGE_MALAY_BRUNEI:
			return new Locale("ms", "BN");
		case MICROSOFT_LANGUAGE_UZBEK_CYRILLIC:
			return new Locale("uz", "UZ");
		case MICROSOFT_LANGUAGE_BENGALI_BANGLADESH:
			return new Locale("bn");
		case MICROSOFT_LANGUAGE_MONGOLIAN:
			return new Locale("mn");
		case MICROSOFT_LANGUAGE_ARABIC_EGYPT:
			return new Locale("ar", "EG");
		case MICROSOFT_LANGUAGE_CHINESE_HONG_KONG_SAR:
			return new Locale("zh", "HK");
		case MICROSOFT_LANGUAGE_GERMAN_AUSTRIA:
			return new Locale("de", "AT");
		case MICROSOFT_LANGUAGE_ENGLISH_AUSTRALIA:
			return new Locale("en", "AU");
		case MICROSOFT_LANGUAGE_SPANISH_SPAIN_MODERN_SORT:
			return new Locale("es");
		case MICROSOFT_LANGUAGE_FRENCH_CANADA:
			return new Locale("fr", "CA");
		case MICROSOFT_LANGUAGE_SERBIAN_CYRILLIC:
			return new Locale("sr", "SP");
		case MICROSOFT_LANGUAGE_ARABIC_LIBYA:
			return new Locale("ar", "LY");
		case MICROSOFT_LANGUAGE_CHINESE_SINGAPORE:
			return new Locale("zh", "SG");
		case MICROSOFT_LANGUAGE_GERMAN_LUXEMBOURG:
			return new Locale("de", "LU");
		case MICROSOFT_LANGUAGE_ENGLISH_CANADA:
			return new Locale("en", "CA");
		case MICROSOFT_LANGUAGE_SPANISH_GUATEMALA:
			return new Locale("es", "GT");
		case MICROSOFT_LANGUAGE_FRENCH_SWITZERLAND:
			return new Locale("fr", "CH");
		case MICROSOFT_LANGUAGE_ARABIC_ALGERIA:
			return new Locale("ar", "DZ");
		case MICROSOFT_LANGUAGE_CHINESE_MACAU_SAR:
			return new Locale("zh", "MO");
		case MICROSOFT_LANGUAGE_GERMAN_LIECHTENSTEIN:
			return new Locale("de", "LI");
		case MICROSOFT_LANGUAGE_ENGLISH_NEW_ZEALAND:
			return new Locale("en", "NZ");
		case MICROSOFT_LANGUAGE_SPANISH_COSTA_RICA:
			return new Locale("es", "CR");
		case MICROSOFT_LANGUAGE_FRENCH_LUXEMBOURG:
			return new Locale("fr", "LU");
		case MICROSOFT_LANGUAGE_BOSNIAN:
			return new Locale("bs");
		case MICROSOFT_LANGUAGE_ARABIC_MOROCCO:
			return new Locale("ar", "MA");
		case MICROSOFT_LANGUAGE_ENGLISH_IRELAND:
			return new Locale("en", "IE");
		case MICROSOFT_LANGUAGE_SPANISH_PANAMA:
			return new Locale("es", "PA");
		case MICROSOFT_LANGUAGE_ARABIC_TUNISIA:
			return new Locale("ar", "TN");
		case MICROSOFT_LANGUAGE_ENGLISH_SOUTHERN_AFRICA:
			return new Locale("en", "ZA");
		case MICROSOFT_LANGUAGE_SPANISH_DOMINICAN_REPUBLIC:
			return new Locale("es", "DO");
		case MICROSOFT_LANGUAGE_ARABIC_OMAN:
			return new Locale("ar", "OM");
		case MICROSOFT_LANGUAGE_ENGLISH_JAMAICA:
			return new Locale("en", "JM");
		case MICROSOFT_LANGUAGE_SPANISH_VENEZUELA:
			return new Locale("es", "VE");
		case MICROSOFT_LANGUAGE_ARABIC_YEMEN:
			return new Locale("ar", "YE");
		case MICROSOFT_LANGUAGE_ENGLISH_CARIBBEAN:
			return new Locale("en", "CB");
		case MICROSOFT_LANGUAGE_SPANISH_COLOMBIA:
			return new Locale("es", "CO");
		case MICROSOFT_LANGUAGE_ARABIC_SYRIA:
			return new Locale("ar", "SY");
		case MICROSOFT_LANGUAGE_ENGLISH_BELIZE:
			return new Locale("en", "BZ");
		case MICROSOFT_LANGUAGE_SPANISH_PERU:
			return new Locale("es", "PE");
		case MICROSOFT_LANGUAGE_ARABIC_JORDAN:
			return new Locale("ar", "JO");
		case MICROSOFT_LANGUAGE_ENGLISH_TRINIDAD:
			return new Locale("en", "TT");
		case MICROSOFT_LANGUAGE_SPANISH_ARGENTINA:
			return new Locale("es", "AR");
		case MICROSOFT_LANGUAGE_ARABIC_LEBANON:
			return new Locale("ar", "LB");
		case MICROSOFT_LANGUAGE_SPANISH_ECUADOR:
			return new Locale("es", "EC");
		case MICROSOFT_LANGUAGE_ARABIC_KUWAIT:
			return new Locale("ar", "KW");
		case MICROSOFT_LANGUAGE_ENGLISH_PHILLIPPINES:
			return new Locale("en", "PH");
		case MICROSOFT_LANGUAGE_SPANISH_CHILE:
			return new Locale("es", "CL");
		case MICROSOFT_LANGUAGE_ARABIC_UNITED_ARAB_EMIRATES:
			return new Locale("ar", "AE");
		case MICROSOFT_LANGUAGE_SPANISH_URUGUAY:
			return new Locale("es", "UY");
		case MICROSOFT_LANGUAGE_ARABIC_BAHRAIN:
			return new Locale("ar", "BH");
		case MICROSOFT_LANGUAGE_SPANISH_PARAGUAY:
			return new Locale("es", "PY");
		case MICROSOFT_LANGUAGE_ARABIC_QATAR:
			return new Locale("ar", "QA");
		case MICROSOFT_LANGUAGE_ENGLISH_INDIA:
			return new Locale("en", "IN");
		case MICROSOFT_LANGUAGE_SPANISH_BOLIVIA:
			return new Locale("es", "BO");
		case MICROSOFT_LANGUAGE_SPANISH_EL_SALVADOR:
			return new Locale("es", "SV");
		case MICROSOFT_LANGUAGE_SPANISH_HONDURAS:
			return new Locale("es", "HN");
		case MICROSOFT_LANGUAGE_SPANISH_NICARAGUA:
			return new Locale("es", "NI");
		case MICROSOFT_LANGUAGE_SPANISH_PUERTO_RICO:
			return new Locale("es", "PR");
		default:
			return null;
		}
	}

	private static Locale getMacintoshLocale(int languageID) {
		return Locale.US;
	}

}
