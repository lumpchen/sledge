package me.lumpchen.sledge.pdf.text.font.encoding;

public interface Encoding {

	public String getCharacterName(int code);

	public int getCharacterCode(String name);
}
