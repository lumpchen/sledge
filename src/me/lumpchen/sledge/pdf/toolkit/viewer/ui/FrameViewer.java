package me.lumpchen.sledge.pdf.toolkit.viewer.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.PDFFile;
import me.lumpchen.sledge.pdf.syntax.decrypt.PDFAuthenticationFailureException;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class FrameViewer extends JFrame {

	private static final long serialVersionUID = -7681692658609592158L;

	private PDFFile openPDF;
	private PDFDocument pdf;
	private int selectedPage = 1;
	private JComboBox<String> pageList = new JComboBox<String>();
	private PageCanvas pageCanvas = new PageCanvas();

	public FrameViewer() {
		super();

		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().add(this.createToolBar(), BorderLayout.NORTH);
		this.add(pageCanvas, BorderLayout.CENTER);
	}

	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		JButton open = new JButton("open");
		open.addActionListener(new ActionListener() {
			private File lastDirectory;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser();
				if (null == this.lastDirectory) {
					fileopen.setCurrentDirectory(new File("/"));
				} else {
					fileopen.setCurrentDirectory(this.lastDirectory);
				}
				FileFilter filter = new FileNameExtensionFilter(".pdf files", "pdf");
				fileopen.setFileFilter(filter);
				fileopen.setAcceptAllFileFilterUsed(true);
				int ret = fileopen.showDialog(null, "Open file");

				if (ret == JFileChooser.APPROVE_OPTION) {
					File file = fileopen.getSelectedFile();
					this.lastDirectory = file.getParentFile();
					PDFReader reader = new PDFReader();
					try {
						openPDF = new PDFFile(file);
						pdf = reader.read(openPDF);
						updatePageList();
						setPageCanvas(1);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (PDFAuthenticationFailureException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		toolBar.add(open);
		
		this.pageList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) pageList.getSelectedItem();
				int sel = Integer.parseInt(s);
				if (sel != selectedPage) {
					setPageCanvas(sel);
				}
			} 
			
		});
		
		toolBar.add(this.pageList);
		
		return toolBar;
	}

	public void closeDocument() {
		if (this.openPDF != null) {
			this.openPDF.close();
		}
	}
	
	private void updatePageList() {
		if (this.pdf == null) {
			return;
		}
		this.pageList.removeAllItems();

		int count = this.pdf.getPageCount();
		for (int i = 1; i <= count; i++) {
			this.pageList.addItem(i + "");
		}
	}

	public void setPageCanvas(int pageNo) {
		this.selectedPage = pageNo;
		Page page = this.pdf.getPage(pageNo);
		this.pageCanvas.setPage(page);
	}
}
