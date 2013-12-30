package me.lumpchen.sledge.pdf.toolkit.editor;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.XRef;
import me.lumpchen.sledge.pdf.syntax.XRef.XRefEntry;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class XRefTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMNS = new String[] { "Obj Num", "Gen Num", "Free", "Offset" };
	
	private List<XRefEntry> entryList;
	private PDFDocument pdf;
	
	public XRefTableModel(PDFDocument pdf) {
		super(COLUMNS, 0);
		this.pdf = pdf;
		this.init();
	}
	
	private void init() {
		XRef xref = this.pdf.getXRef();
		this.entryList = xref.getEntryList();
		
		for (int i = 0, n = entryList.size(); i < n; i++) {
			XRefEntry entry = entryList.get(i);
			this.addRow(entry);
		}
	}
	
	private void addRow(XRefEntry entry) {
		int iobj = entry.objNum;
		int igen = entry.genNum;
		boolean free = entry.free;
		long offset = entry.offset;
		this.addRow(new Object[]{iobj, igen, free, offset});
	}
	
	public IndirectObject getRowObject(int row) {
		XRefEntry entry = this.entryList.get(row);
		IndirectObject obj = pdf.getObject(new IndirectRef(entry.objNum, entry.genNum));
		return obj;
	}
	
	public String getRowString(int row) {
		IndirectObject obj = this.getRowObject(row);
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
