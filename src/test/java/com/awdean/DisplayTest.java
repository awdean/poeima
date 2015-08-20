package com.awdean;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.junit.Test;

public class DisplayTest {

    @Test
    public void testGUI() {
        Display gui = new Display();
        
        assertThat(gui.getFrame(), is(notNullValue()));
        
        assertThat(gui.getFrame().getTitle(), is(Display.TITLE));
        
        assertThat(gui.getTextArea(), is(notNullValue()));
    }
    
    @Test
    public void testGUIJFrame() {
        String title = "TEST";
        JFrame frame = new JFrame(title);
        Display gui = new Display(frame);

        assertThat(gui.getFrame(), is(notNullValue()));
        assertThat(gui.getFrame(), is(sameInstance(frame)));
        
        assertThat(gui.getFrame().getTitle(), is(title));
        
        assertThat(gui.getTextArea(), is(notNullValue()));
    }
    
    @Test
    public void testGUIJTextArea() {
        JTextArea textArea = new JTextArea();
        Display gui = new Display(textArea);

        
        assertThat(gui.getFrame(), is(notNullValue()));
        
        assertThat(gui.getFrame().getTitle(), is(Display.TITLE));
        
        assertThat(gui.getTextArea(), is(notNullValue()));
        assertThat(gui.getTextArea(), is(sameInstance(textArea)));
    }

    @Test
    public void testGUIJFrameJTextArea() {
        String title = "TEST";
        JFrame frame = new JFrame(title);
        JTextArea textArea = new JTextArea();
        Display gui = new Display(frame, textArea);
        
        assertThat(gui.getFrame(), is(notNullValue()));
        assertThat(gui.getFrame(), is(sameInstance(frame)));
        
        assertThat(gui.getFrame().getTitle(), is(title));

        assertThat(gui.getTextArea(), is(notNullValue()));
        assertThat(gui.getTextArea(), is(sameInstance(textArea)));
    }
    
    @Test
    public void testGetInstance() {
        assertThat(Display.getInstance(), is(notNullValue()));
        assertThat(Display.getInstance(), is(sameInstance(Display.getInstance())));
    }

    @Test
    public void testGetFrame() {
        Display di = Display.getInstance();
        
        assertThat(di.getFrame(), is(notNullValue()));
        assertThat(di.getFrame(), is(sameInstance(di.getFrame())));
    }

    @Test
    public void testGetTextArea() {
        Display di = Display.getInstance();
        
        assertThat(di.getTextArea(), is(notNullValue()));
        assertThat(di.getTextArea(), is(sameInstance(di.getTextArea())));
    }

    @Test
    public void testInitFrame() {
        Display gui = new Display();
        gui.initFrame();
        
        assertThat(gui.getFrame().getDefaultCloseOperation(), is(JFrame.EXIT_ON_CLOSE));
        
        assertThat(gui.getFrame().getLayout(), is(notNullValue()));
        assertThat(gui.getFrame().getLayout(), is(instanceOf(BorderLayout.class)));
    }

    @Test
    public void testInitTextArea() {
        Display gui = new Display();
        gui.initFrame();
        
        assertThat(gui.getTextArea().getParent(), is(nullValue()));
        
        gui.initTextArea();
        
        assertFalse(gui.getTextArea().isEditable());
        assertThat(gui.getTextArea().getParent(), is(notNullValue()));
    }

    @Test
    public void testUpdateText() {
        String text = "TEST";
        Display gui = new Display();
        
        assertThat(gui.getTextArea().getText(), isEmptyOrNullString());
        
        gui.updateText(text);
        
        assertThat(gui.getTextArea().getText(), is(text));
    }

}
