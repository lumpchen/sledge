package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.tree.DefaultMutableTreeNode;

import me.lumpchen.sledge.pdf.syntax.document.DocObject;

public class DocObjectTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	private DocObject docObj;
	
	public DocObjectTreeNode(String tag, DocObject docObj) {
		super(tag);
		this.docObj = docObj;
	}
	
	public DocObject getDocObject() {
		return this.docObj;
	}
}
