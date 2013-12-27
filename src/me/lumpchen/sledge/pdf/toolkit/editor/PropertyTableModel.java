package me.lumpchen.sledge.pdf.toolkit.editor;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.document.DocObject;

public class PropertyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMNS = new String[] { "Key", "Type",
			"Value" };

	public PropertyTableModel() {
		super(COLUMNS, 0);
	}

	public void removeAllRows() {
		int n = this.getRowCount();
		for (int i = n - 1; i >= 0; i--) {
			this.removeRow(i);
		}
	}
	
	public void addDocObject(DocObject obj) {
		if (null  == obj) {
			return;
		}

		IndirectObject inside = obj.insideObject();
		if (null == inside) {
			return;
		}
		if (inside.insideObj() instanceof PDictionary) {
			PDictionary dict = (PDictionary) inside.insideObj();
			List<PName> keyList = dict.keyList();
			for (PName key : keyList) {
				PObject value = dict.get(key);
				
				String c0 = key.toString();
				String c1 = "", c2 = "";
				if (value != null) {
					PObject.Type type = value.getType();
					c1 = type.toString();
					c2 = value.toString();
				}
				
				this.addRow(new String[]{c0, c1, c2});
			}
		}
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}
}
