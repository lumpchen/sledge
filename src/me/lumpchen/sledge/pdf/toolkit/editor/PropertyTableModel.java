package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.table.DefaultTableModel;

import me.lumpchen.sledge.pdf.syntax.document.DocObject;

public class PropertyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMNS = new String[]{"Key", "Type", "Value"}; 
	
	public PropertyTableModel() {
		super(COLUMNS, 0);
	}

	public void addDocObject(DocObject obj) {
		this.addRow(new String[]{"1", "2", "3"});
	}
}
