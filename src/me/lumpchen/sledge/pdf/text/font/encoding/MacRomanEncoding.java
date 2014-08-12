package me.lumpchen.sledge.pdf.text.font.encoding;

import java.util.HashMap;
import java.util.Map;

public class MacRomanEncoding  implements Encoding {
	
	private static Map<Integer, String> code2name = new HashMap<Integer, String>();
	private static Map<String, Integer> name2code = new HashMap<String, Integer>();
	static {
		addCharacterEncoding(65, "A" );
		addCharacterEncoding(174, "AE" );
		addCharacterEncoding(231, "Aacute" );
		addCharacterEncoding(229, "Acircumflex" );
		addCharacterEncoding(128, "Adieresis" );
		addCharacterEncoding(203, "Agrave" );
		addCharacterEncoding(129, "Aring" );
		addCharacterEncoding(204, "Atilde" );
		addCharacterEncoding(66, "B" );
		addCharacterEncoding(67, "C" );
		addCharacterEncoding(130, "Ccedilla" );
		addCharacterEncoding(68, "D" );
		addCharacterEncoding(69, "E" );
		addCharacterEncoding(131, "Eacute" );
		addCharacterEncoding(230, "Ecircumflex" );
		addCharacterEncoding(232, "Edieresis" );
		addCharacterEncoding(233, "Egrave" );
		addCharacterEncoding(70, "F" );
		addCharacterEncoding(71, "G" );
		addCharacterEncoding(72, "H" );
		addCharacterEncoding(73, "I" );
		addCharacterEncoding(234, "Iacute" );
		addCharacterEncoding(235, "Icircumflex" );
		addCharacterEncoding(236, "Idieresis" );
		addCharacterEncoding(237, "Igrave" );
		addCharacterEncoding(74, "J" );
		addCharacterEncoding(75, "K" );
		addCharacterEncoding(76, "L" );
		addCharacterEncoding(77, "M" );
		addCharacterEncoding(78, "N" );
		addCharacterEncoding(132, "Ntilde" );
		addCharacterEncoding(79, "O" );
		addCharacterEncoding(206, "OE" );
		addCharacterEncoding(238, "Oacute" );
		addCharacterEncoding(239, "Ocircumflex" );
		addCharacterEncoding(133, "Odieresis" );
		addCharacterEncoding(241, "Ograve" );
		addCharacterEncoding(175, "Oslash" );
		addCharacterEncoding(205, "Otilde" );
		addCharacterEncoding(80, "P" );
		addCharacterEncoding(81, "Q" );
		addCharacterEncoding(82, "R" );
		addCharacterEncoding(83, "S" );
		addCharacterEncoding(84, "T" );
		addCharacterEncoding(85, "U" );
		addCharacterEncoding(242, "Uacute" );
		addCharacterEncoding(243, "Ucircumflex" );
		addCharacterEncoding(134, "Udieresis" );
		addCharacterEncoding(244, "Ugrave" );
		addCharacterEncoding(86, "V" );
		addCharacterEncoding(87, "W" );
		addCharacterEncoding(88, "X" );
		addCharacterEncoding(89, "Y" );
		addCharacterEncoding(217, "Ydieresis" );
		addCharacterEncoding(90, "Z" );
		addCharacterEncoding(97, "a" );
		addCharacterEncoding(135, "aacute" );
		addCharacterEncoding(137, "acircumflex" );
		addCharacterEncoding(171, "acute" );
		addCharacterEncoding(138, "adieresis" );
		addCharacterEncoding(190, "ae" );
		addCharacterEncoding(136, "agrave" );
		addCharacterEncoding(38, "ampersand" );
		addCharacterEncoding(140, "aring" );
		addCharacterEncoding(94, "asciicircum" );
		addCharacterEncoding(126, "asciitilde" );
		addCharacterEncoding(42, "asterisk" );
		addCharacterEncoding(64, "at" );
		addCharacterEncoding(139, "atilde" );
		addCharacterEncoding(98, "b" );
		addCharacterEncoding(92, "backslash" );
		addCharacterEncoding(124, "bar" );
		addCharacterEncoding(123, "braceleft" );
		addCharacterEncoding(125, "braceright" );
		addCharacterEncoding(91, "bracketleft" );
		addCharacterEncoding(93, "bracketright" );
		addCharacterEncoding(249, "breve" );
		addCharacterEncoding(165, "bullet" );
		addCharacterEncoding(99, "c" );
		addCharacterEncoding(255, "caron" );
		addCharacterEncoding(141, "ccedilla" );
		addCharacterEncoding(252, "cedilla" );
		addCharacterEncoding(162, "cent" );
		addCharacterEncoding(246, "circumflex" );
		addCharacterEncoding(58, "colon" );
		addCharacterEncoding(44, "comma" );
		addCharacterEncoding(169, "copyright" );
		addCharacterEncoding(219, "currency" );
		addCharacterEncoding(100, "d" );
		addCharacterEncoding(160, "dagger" );
		addCharacterEncoding(224, "daggerdbl" );
		addCharacterEncoding(161, "degree" );
		addCharacterEncoding(172, "dieresis" );
		addCharacterEncoding(214, "divide" );
		addCharacterEncoding(36, "dollar" );
		addCharacterEncoding(250, "dotaccent" );
		addCharacterEncoding(245, "dotlessi" );
		addCharacterEncoding(101, "e" );
		addCharacterEncoding(142, "eacute" );
		addCharacterEncoding(144, "ecircumflex" );
		addCharacterEncoding(145, "edieresis" );
		addCharacterEncoding(143, "egrave" );
		addCharacterEncoding(56, "eight" );
		addCharacterEncoding(201, "ellipsis" );
		addCharacterEncoding(209, "emdash" );
		addCharacterEncoding(208, "endash" );
		addCharacterEncoding(61, "equal" );
		addCharacterEncoding(33, "exclam" );
		addCharacterEncoding(193, "exclamdown" );
		addCharacterEncoding(102, "f" );
		addCharacterEncoding(222, "fi" );
		addCharacterEncoding(53, "five" );
		addCharacterEncoding(223, "fl" );
		addCharacterEncoding(196, "florin" );
		addCharacterEncoding(52, "four" );
		addCharacterEncoding(218, "fraction" );
		addCharacterEncoding(103, "g" );
		addCharacterEncoding(167, "germandbls" );
		addCharacterEncoding(96, "grave" );
		addCharacterEncoding(62, "greater" );
		addCharacterEncoding(199, "guillemotleft" );
		addCharacterEncoding(200, "guillemotright" );
		addCharacterEncoding(220, "guilsinglleft" );
		addCharacterEncoding(221, "guilsinglright" );
		addCharacterEncoding(104, "h" );
		addCharacterEncoding(253, "hungarumlaut" );
		addCharacterEncoding(45, "hyphen" );
		addCharacterEncoding(105, "i" );
		addCharacterEncoding(146, "iacute" );
		addCharacterEncoding(148, "icircumflex" );
		addCharacterEncoding(149, "idieresis" );
		addCharacterEncoding(147, "igrave" );
		addCharacterEncoding(106, "j" );
		addCharacterEncoding(107, "k" );
		addCharacterEncoding(108, "l" );
		addCharacterEncoding(60, "less" );
		addCharacterEncoding(194, "logicalnot" );
		addCharacterEncoding(109, "m" );
		addCharacterEncoding(248, "macron" );
		addCharacterEncoding(181, "mu" );
		addCharacterEncoding(110, "n" );
		addCharacterEncoding(57, "nine" );
		addCharacterEncoding(150, "ntilde" );
		addCharacterEncoding(35, "numbersign" );
		addCharacterEncoding(111, "o" );
		addCharacterEncoding(151, "oacute" );
		addCharacterEncoding(153, "ocircumflex" );
		addCharacterEncoding(154, "odieresis" );
		addCharacterEncoding(207, "oe" );
		addCharacterEncoding(254, "ogonek" );
		addCharacterEncoding(152, "ograve" );
		addCharacterEncoding(49, "one" );
		addCharacterEncoding(187, "ordfeminine" );
		addCharacterEncoding(188, "ordmasculine" );
		addCharacterEncoding(191, "oslash" );
		addCharacterEncoding(155, "otilde" );
		addCharacterEncoding(112, "p" );
		addCharacterEncoding(166, "paragraph" );
		addCharacterEncoding(40, "parenleft" );
		addCharacterEncoding(41, "parenright" );
		addCharacterEncoding(37, "percent" );
		addCharacterEncoding(46, "period" );
		addCharacterEncoding(225, "periodcentered" );
		addCharacterEncoding(228, "perthousand" );
		addCharacterEncoding(43, "plus" );
		addCharacterEncoding(177, "plusminus" );
		addCharacterEncoding(113, "q" );
		addCharacterEncoding(63, "question" );
		addCharacterEncoding(192, "questiondown" );
		addCharacterEncoding(34, "quotedbl" );
		addCharacterEncoding(227, "quotedblbase" );
		addCharacterEncoding(210, "quotedblleft" );
		addCharacterEncoding(211, "quotedblright" );
		addCharacterEncoding(212, "quoteleft" );
		addCharacterEncoding(213, "quoteright" );
		addCharacterEncoding(226, "quotesinglbase" );
		addCharacterEncoding(39, "quotesingle" );
		addCharacterEncoding(114, "r" );
		addCharacterEncoding(168, "registered" );
		addCharacterEncoding(251, "ring" );
		addCharacterEncoding(115, "s" );
		addCharacterEncoding(164, "section" );
		addCharacterEncoding(59, "semicolon" );
		addCharacterEncoding(55, "seven" );
		addCharacterEncoding(54, "six" );
		addCharacterEncoding(47, "slash" );
		addCharacterEncoding(32, "space" );
		addCharacterEncoding(163, "sterling" );
		addCharacterEncoding(116, "t" );
		addCharacterEncoding(51, "three" );
		addCharacterEncoding(247, "tilde" );
		addCharacterEncoding(170, "trademark" );
		addCharacterEncoding(50, "two" );
		addCharacterEncoding(117, "u" );
		addCharacterEncoding(156, "uacute" );
		addCharacterEncoding(158, "ucircumflex" );
		addCharacterEncoding(159, "udieresis" );
		addCharacterEncoding(157, "ugrave" );
		addCharacterEncoding(95, "underscore" );
		addCharacterEncoding(118, "v" );
		addCharacterEncoding(119, "w" );
		addCharacterEncoding(120, "x" );
		addCharacterEncoding(121, "y" );
		addCharacterEncoding(216, "ydieresis" );
		addCharacterEncoding(180, "yen" );
		addCharacterEncoding(122, "z" );
		addCharacterEncoding(48, "zero" );
	}
	
	public MacRomanEncoding() {
	}
	
	static void addCharacterEncoding(int code, String name) {
		code2name.put(code, name);
		name2code.put(name, code);
	}

	@Override
	public String getCharacterName(int code) {
		return code2name.get(code);
	}

	@Override
	public int getCharacterCode(String name) {
		return name2code.get(name);
	}
	
}