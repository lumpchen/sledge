package com.docscience.pathfinder.font.shared;

public final class FontDescriptor {

	private final String fontFamily;
	private final String fontStyle;
	private final FontTechnology technology;

	public FontDescriptor(String fontFamily, String fontStyle, FontTechnology technology) {
		this.fontFamily = fontFamily;
		this.fontStyle = fontStyle;
		this.technology = technology;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public FontTechnology getTechnoloy() {
		return technology;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fontFamily == null) ? 0 : fontFamily.hashCode());
		result = prime * result + ((fontStyle == null) ? 0 : fontStyle.hashCode());
		result = prime * result + ((technology == null) ? 0 : technology.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontDescriptor other = (FontDescriptor) obj;
		if (fontFamily == null) {
			if (other.fontFamily != null)
				return false;
		} else if (!fontFamily.equals(other.fontFamily))
			return false;
		if (fontStyle == null) {
			if (other.fontStyle != null)
				return false;
		} else if (!fontStyle.equals(other.fontStyle))
			return false;
		if (technology != other.technology)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FontDescriptor [fontFamily=" + fontFamily + ", fontStyle=" + fontStyle
				+ ", technology=" + technology + "]";
	}

}
