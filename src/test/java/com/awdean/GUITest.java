package com.awdean;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.junit.Test;

public class GUITest {

	@Test
	public void testGUI() {
		GUI gui = new GUI();
		assertNotNull(gui.getFrame());
		assertEquals(gui.getFrame().getTitle(), GUI.TITLE);
		assertNotNull(gui.getTextArea());
	}
	
	@Test
	public void testGUIJFrame() {
		String title = "TEST";
		JFrame frame = new JFrame(title);
		GUI gui = new GUI(frame);
		assertSame(gui.getFrame(), frame);
		assertEquals(gui.getFrame().getTitle(), title);
		assertNotNull(gui.getTextArea());
	}
	
	@Test
	public void testGUIJTextArea() {
		JTextArea textArea = new JTextArea();
		GUI gui = new GUI(textArea);
		assertNotNull(gui.getFrame());
		assertEquals(gui.getFrame().getTitle(), GUI.TITLE);
		assertSame(gui.getTextArea(), textArea);
	}

	@Test
	public void testGUIJFrameJTextArea() {
		String title = "TEST";
		JFrame frame = new JFrame(title);
		JTextArea textArea = new JTextArea();
		GUI gui = new GUI(frame, textArea);
		assertSame(gui.getFrame(), frame);
		assertEquals(gui.getFrame().getTitle(), title);
		assertSame(gui.getTextArea(), textArea);
	}
	
	@Test
	public void testGetInstance() {
		assertNotNull(GUI.getInstance());
		assertSame(GUI.getInstance(), GUI.getInstance());
	}

	@Test
	public void testGetFrame() {
		assertSame(GUI.getInstance().getFrame(), GUI.getInstance().getFrame());
	}

	@Test
	public void testGetTextArea() {
		assertSame(GUI.getInstance().getTextArea(), GUI.getInstance().getTextArea());
	}

	@Test
	public void testInitFrame() {
		GUI gui = new GUI();
		gui.initFrame();
		assertEquals(gui.getFrame().getDefaultCloseOperation(), JFrame.EXIT_ON_CLOSE);
		assertNotNull(gui.getFrame().getLayout());
		assertThat(gui.getFrame().getLayout(), instanceOf(BorderLayout.class));
	}

	@Test
	public void testInitTextArea() {
		GUI gui = new GUI();
		gui.initFrame();
		assertNull(gui.getTextArea().getParent());
		gui.initTextArea();
		assertNotNull(gui.getTextArea().getParent());
	}

	@Test
	public void testUpdateText() {
		String text = "TEST";
		GUI gui = new GUI();
		assertEquals(gui.getTextArea().getText(), "");
		gui.updateText(text);
		assertEquals(gui.getTextArea().getText(), text);
	}

}
