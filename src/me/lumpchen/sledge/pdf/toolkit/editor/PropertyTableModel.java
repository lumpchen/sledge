package me.lumpchen.sledge.pdf.toolkit.editor;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.document.DocObject;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class PropertyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMNS = new String[] { "Key", "Type", "Value" };

	private List<RowClass> classEntryList;
	
	public static class RowClass {
		public PObject key;
		public PObject.Type type;
		public PObject value;
	}
	
	public PropertyTableModel(PDFDocument pdfDocument) {
		super(COLUMNS, 0);
		this.classEntryList = new LinkedList<RowClass>(); 
	}

	public void removeAllRows() {
		int n = this.getRowCount();
		for (int i = n - 1; i >= 0; i--) {
			this.removeRow(i);
		}
		this.classEntryList.clear();
	}
	
	public RowClass getRowClass(int row) {
		return this.classEntryList.get(row);
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
				RowClass classEntry = new RowClass();
				classEntry.key = key;
				
				PObject value = dict.get(key);
				
				String c0 = key.toString();
				String c1 = "", c2 = "";
				if (value != null) {
					PObject.Type type = value.getType();
					c1 = type.toString();
					c2 = value.toString();
					
					classEntry.type = type;
					classEntry.value = value;
				}
				
				this.classEntryList.add(classEntry);
				this.addRow(new String[]{c0, c1, c2});
			}
		}
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
