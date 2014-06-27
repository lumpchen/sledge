package com.docscience.pathfinder.font.shared;

public enum FontTechnology {

	OPEN_TYPE_TT("Open Type (TT)"), OPEN_TYPE_PS("Open Type (PS)"), TRUE_TYPE(
			"True Type"), TYPE_1("Type 1");

	private final String description;

	private FontTechnology(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
