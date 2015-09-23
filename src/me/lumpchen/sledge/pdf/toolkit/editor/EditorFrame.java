package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.lumpchen.sledge.pdf.reader.PDFAuthenticationFailureException;
import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.PDFFile;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class EditorFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private int width;
	private int height;

	private JScrollPane leftScrollPane;
	private JScrollPane rightScrollPane;

	private XRefTable xRefTable;
	private JTextArea textarea;
	
	private JButton prevBtn;
	private JButton nextBtn;
	
	private PDFFile openPDF;
	
	public EditorFrame() {
		super();

		this.width = 960;
		this.height = 600;
		this.setSize(this.width, this.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.leftScrollPane = new JScrollPane();
		leftScrollPane.setMinimumSize(new Dimension(this.width / 3, this.height));

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		this.rightScrollPane = new JScrollPane();
		rightScrollPane.setPreferredSize(new Dimension(this.width / 3,
				this.height));
		rightPanel.add(this.rightScrollPane, BorderLayout.CENTER);

		JScrollPane rightBottomScrollPane = new JScrollPane();
		rightBottomScrollPane.setPreferredSize(new Dimension(this.width / 3,
				this.height / 2));
		rightPanel.add(rightBottomScrollPane, BorderLayout.SOUTH);
		this.textarea = new JTextArea();
		rightBottomScrollPane.setViewportView(this.textarea);

		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftScrollPane, rightPanel);
		getContentPane().add(sp, BorderLayout.CENTER);

		this.setJMenuBar(this.createMenu());
		this.getContentPane().add(this.createToolBar(), BorderLayout.NORTH);
	}

	private JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		return menuBar;
	}

	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		
		toolBar.add(this.createFileOpenButton());
		toolBar.add(this.createFileCloseButton());
		
		toolBar.add(this.createPrevButton());
		toolBar.add(this.createNextButton());
		return toolBar;
	}

	private JButton createFileOpenButton() {
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
					openDocument(file);
					setTitle(file.getName());
				}
			}
		});
		return open;
	}
	
	private JButton createFileCloseButton() {
		JButton open = new JButton("close");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeDocument();
				setTitle("");
			}
		});
		return open;
	}
	
	private JButton createPrevButton() {
		this.prevBtn = new JButton("<<<<");
		this.prevBtn.setToolTipText("back");
		
		if (this.xRefTable == null) {
			prevBtn.setEnabled(false);
		}
		
		prevBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				xRefTable.gotoPrev();
			}
		});
		return prevBtn;
	}
	
	private JButton createNextButton() {
		this.nextBtn = new JButton(">>>>");
		this.nextBtn.setToolTipText("forward");
		
		if (this.xRefTable == null) {
			nextBtn.setEnabled(false);
		}
		
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				xRefTable.gotoNext();
			}
		});
		return nextBtn;
	}
	
	public void openDocument(File f) {
		
		if (!f.exists()) {
			JOptionPane.showMessageDialog(this, "File not found.");
		}
		try {
			this.openPDF = new PDFFile(f);
			this.openPDFFile();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
			closeDocument();
		} catch (PDFAuthenticationFailureException e) {
			this.showPasswordDlg();
		}
	}
	
	private void openPDFFile() throws IOException, PDFAuthenticationFailureException {
		PDFReader reader = new PDFReader();
		PDFDocument doc = reader.read(this.openPDF);
		this.createXRefTable(doc);
	}
	
	private void showPasswordDlg() {
		PasswordDlg dlg = new PasswordDlg(this, true);
		dlg.setVisible(true);
		if (dlg.isOk()) {
			try {
				char[] pwd = dlg.getPassword();
				this.openPDF.setPassword(new String(pwd));
				this.openPDFFile();				
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage());
				closeDocument();
			} catch (PDFAuthenticationFailureException e) {
				JOptionPane.showMessageDialog(this, "The password is incorrect.", 
						"Password", JOptionPane.WARNING_MESSAGE, null);
				this.showPasswordDlg();
			}
		} else {
			this.closeDocument();
		}
	}

	public void closeDocument() {
		if (this.openPDF != null) {
			this.openPDF.close();
			
			if (this.xRefTable != null) {
				this.xRefTable.clearAllRows();
			}
			if (this.textarea != null) {
				this.textarea.setText("");				
			}
		}
	}
	
	private void createXRefTable(PDFDocument doc) {
		XRefTableModel tableModel = new XRefTableModel(doc);

		PropertyTableModel propTableModel = new PropertyTableModel(doc);

		this.xRefTable = new XRefTable(tableModel, propTableModel, this.textarea);
		this.leftScrollPane.setViewportView(xRefTable);
		this.xRefTable.setPrevButton(this.prevBtn);
		this.xRefTable.setNextBtn(this.nextBtn);

		PropertyTable propTable = new PropertyTable(propTableModel, xRefTable);
		this.rightScrollPane.setViewportView(propTable);
		this.textarea.setText("");
	}

}
