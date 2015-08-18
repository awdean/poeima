package com.awdean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

import org.junit.Test;

public class ClipMonTest {

	@Test
	public void testClipMon() {
		ClipMon cm = new ClipMon();
		assertNotNull(cm.getClipboard());
		assertNull(cm.getContents());
	}

	@Test
	public void testClipMonClipboard() {
		Clipboard clipboard = new Clipboard("TEST");
		ClipMon cm = new ClipMon(clipboard);
		assertNotNull(cm.getClipboard());
		assertNull(cm.getContents());
	}

	@Test
	public void testGetInstance() {
		assertNotNull(ClipMon.getInstance());
		assertSame(ClipMon.getInstance(), ClipMon.getInstance());
		assertSame(ClipMon.getInstance().getClipboard(), Toolkit
				.getDefaultToolkit().getSystemClipboard());
	}

	@Test
	public void testGetClipboard() {
		Clipboard clipboard = new Clipboard("TEST");
		ClipMon cm = new ClipMon(clipboard);
		assertSame(ClipMon.getInstance().getClipboard(), Toolkit
				.getDefaultToolkit().getSystemClipboard());
		assertSame(cm.getClipboard(), clipboard);
	}

	@Test
	public void testGetContents() {
		assertNull(ClipMon.getInstance().getContents());
	}

	@Test
	public void testGetDelta() {
		assertTrue(ClipMon.getInstance().getDelta() >= ClipMon.MIN_SLEEP);
		assertTrue(ClipMon.getInstance().getDelta() <= ClipMon.MAX_SLEEP);
	}

	@Test
	public void testSetContents() {
		Clipboard clipboard = new Clipboard("TEST");
		ClipMon cm = new ClipMon(clipboard);
		assertNull(cm.getContents());
		String test = "TEST_STRING";
		clipboard.setContents(new StringSelection(test), null);
		assertTrue(cm.updateContents());
		assertEquals(cm.getContents(), test);
	}

	@Test
	public void testSetDelta() {
		ClipMon cm = new ClipMon();
		cm.setDelta(ClipMon.MIN_SLEEP);
		assertEquals(cm.getDelta(), ClipMon.MIN_SLEEP);
		cm.setDelta(ClipMon.MIN_SLEEP - 1);
		assertEquals(cm.getDelta(), ClipMon.MIN_SLEEP);
		cm.setDelta(ClipMon.MAX_SLEEP + 1);
		assertEquals(cm.getDelta(), ClipMon.MAX_SLEEP);
	}

	@Test
	public void testHasStringFlavor() {
		ClipMon cm = new ClipMon();
		assertFalse(cm.hasStringFlavor(Arrays.asList(DataFlavor.imageFlavor)));
		assertFalse(cm.hasStringFlavor(Arrays
				.asList(DataFlavor.javaFileListFlavor)));
		assertTrue(cm.hasStringFlavor(Arrays.asList(DataFlavor.stringFlavor)));
		assertFalse(cm.hasStringFlavor(Arrays.asList(DataFlavor.imageFlavor,
				DataFlavor.stringFlavor)));
		assertFalse(cm.hasStringFlavor(Arrays.asList(
				DataFlavor.javaFileListFlavor, DataFlavor.stringFlavor)));
	}

	@Test
	public void testUpdateContents() {
		Clipboard clipboard = new Clipboard("TEST");
		ClipMon cm = new ClipMon(clipboard);
		assertFalse(cm.updateContents());
		// this is missing an image or filelist data flavor negative test
		String test = "TEST_STRING";
		clipboard.setContents(new StringSelection(test), null);
		assertNotEquals(cm.getContents(), test);
		assertTrue(cm.updateContents());
		assertEquals(cm.getContents(), test);
		assertFalse(cm.updateContents());
		assertEquals(cm.getContents(), test);
	}

}
