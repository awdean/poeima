package com.awdean;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class GUI {

	public static final String TITLE = "poeima";

	private static final GUI instance = new GUI();
	
	public static GUI getInstance() {
		return instance;
	}
	
	public GUI() {
		this(new JFrame(TITLE), new JTextArea());
	}
	
	public GUI(JFrame frame) {
		this(frame, new JTextArea());
	}
	
	public GUI(JTextArea textArea) {
		this(new JFrame(TITLE), textArea);
	}
	
	public GUI(JFrame frame, JTextArea textArea) {
		this._frame = frame;
		this._textArea = textArea;
	}
	
	private JFrame _frame;
	private JTextArea _textArea;

	public JFrame getFrame() {
		return _frame;
	}
	
	public JTextArea getTextArea() {
		return _textArea;
	}
	
	public void init() {
		initFrame();
		initTextArea();
		initVisible();
	}
	
	public void initFrame() {
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	getFrame().setLayout(new BorderLayout());
	}
	
	public void initTextArea() {
		getTextArea().setEditable(false);
		getFrame().add(getTextArea(), BorderLayout.CENTER);
		getFrame().pack();
	}
	
	public void initVisible() {
		getFrame().setLocationRelativeTo(null);
		getFrame().setVisible(true);
	}
	
	public void updateText(String text) {
		getTextArea().setText(text);
		getFrame().pack();
	}
	
}
