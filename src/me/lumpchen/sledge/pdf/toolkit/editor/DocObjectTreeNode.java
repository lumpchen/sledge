package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.tree.DefaultMutableTreeNode;

import me.lumpchen.sledge.pdf.syntax.document.DocObject;

public class DocObjectTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	public DocObjectTreeNode(DocObject docObj) {
		super(docObj);
	}
	
	public DocObject getDocObject() {
		return (DocObject) this.getUserObject();
	}
}
