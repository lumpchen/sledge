package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.table.DefaultTableModel;

import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.document.DocObject;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class PropertyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMNS = new String[] { "Key", "Type",
			"Value" };

	public PropertyTableModel() {
		super(COLUMNS, 0);
	}

	public void removeAllRows() {
		int n = this.getRowCount();
		for (int i = 0; i < n; i++) {
			this.removeRow(i);
		}
	}
	
	public void addDocObject(DocObject obj) {
		if (PName.page.equals(obj.getType())) {
			Page page = (Page) obj;
		}
		
		this.addRow(new String[] { "1", "2", "3" });
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}
}
