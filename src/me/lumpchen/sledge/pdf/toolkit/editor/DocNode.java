package me.lumpchen.sledge.pdf.toolkit.editor;

import me.lumpchen.sledge.pdf.syntax.document.DocObject;

public class DocNode {

	private DocObject docObject;
	
	public DocNode(DocObject docObject) {
		this.docObject = docObject;
	}
	
	public String toString() {
		return this.docObject.toString();
	}
}
