package com.awdean;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.junit.Test;

public class DisplayTest {

	@Test
	public void testGUI() {
		Display gui = new Display();
		assertNotNull(gui.getFrame());
		assertEquals(gui.getFrame().getTitle(), Display.TITLE);
		assertNotNull(gui.getTextArea());
	}
	
	@Test
	public void testGUIJFrame() {
		String title = "TEST";
		JFrame frame = new JFrame(title);
		Display gui = new Display(frame);
		assertSame(gui.getFrame(), frame);
		assertEquals(gui.getFrame().getTitle(), title);
		assertNotNull(gui.getTextArea());
	}
	
	@Test
	public void testGUIJTextArea() {
		JTextArea textArea = new JTextArea();
		Display gui = new Display(textArea);
		assertNotNull(gui.getFrame());
		assertEquals(gui.getFrame().getTitle(), Display.TITLE);
		assertSame(gui.getTextArea(), textArea);
	}

	@Test
	public void testGUIJFrameJTextArea() {
		String title = "TEST";
		JFrame frame = new JFrame(title);
		JTextArea textArea = new JTextArea();
		Display gui = new Display(frame, textArea);
		assertSame(gui.getFrame(), frame);
		assertEquals(gui.getFrame().getTitle(), title);
		assertSame(gui.getTextArea(), textArea);
	}
	
	@Test
	public void testGetInstance() {
		assertNotNull(Display.getInstance());
		assertSame(Display.getInstance(), Display.getInstance());
	}

	@Test
	public void testGetFrame() {
		assertSame(Display.getInstance().getFrame(), Display.getInstance().getFrame());
	}

	@Test
	public void testGetTextArea() {
		assertSame(Display.getInstance().getTextArea(), Display.getInstance().getTextArea());
	}

	@Test
	public void testInitFrame() {
		Display gui = new Display();
		gui.initFrame();
		assertEquals(gui.getFrame().getDefaultCloseOperation(), JFrame.EXIT_ON_CLOSE);
		assertNotNull(gui.getFrame().getLayout());
		assertThat(gui.getFrame().getLayout(), instanceOf(BorderLayout.class));
	}

	@Test
	public void testInitTextArea() {
		Display gui = new Display();
		gui.initFrame();
		assertNull(gui.getTextArea().getParent());
		gui.initTextArea();
		assertFalse(gui.getTextArea().isEditable());
		assertNotNull(gui.getTextArea().getParent());
	}

	@Test
	public void testUpdateText() {
		String text = "TEST";
		Display gui = new Display();
		assertEquals(gui.getTextArea().getText(), "");
		gui.updateText(text);
		assertEquals(gui.getTextArea().getText(), text);
	}

}
