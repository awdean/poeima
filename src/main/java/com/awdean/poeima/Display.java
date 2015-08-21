package com.awdean;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Display {

    public static final String TITLE = "poeima";

    private static final Display INSTANCE = new Display();
    
    public static Display getInstance() {
        return INSTANCE;
    }
    
    public Display() {
        this(new JFrame(TITLE), new JTextArea());
    }
    
    public Display(JFrame frame) {
        this(frame, new JTextArea());
    }
    
    public Display(JTextArea textArea) {
        this(new JFrame(TITLE), textArea);
    }
    
    public Display(JFrame frame, JTextArea textArea) {
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
        getFrame().setMinimumSize(new Dimension(233, 377));
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
        synchronized (this) {
            getTextArea().setText(text);
            getFrame().pack();
            getFrame().toFront();
        }
    }
    
}
