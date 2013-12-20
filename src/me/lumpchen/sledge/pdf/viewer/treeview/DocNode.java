package me.lumpchen.sledge.pdf.viewer.treeview;

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
