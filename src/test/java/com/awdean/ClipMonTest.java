package com.awdean;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
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
        
        assertThat(cm.getClipboard(), is(notNullValue()));
        
        assertThat(cm.getContents(), is(nullValue()));
    }

    @Test
    public void testClipMonClipboard() {
        Clipboard clipboard = new Clipboard("TEST");
        ClipMon cm = new ClipMon(clipboard);
        
        assertThat(cm.getClipboard(), is(notNullValue()));
        assertThat(cm.getClipboard(), is(sameInstance(clipboard)));
        
        assertThat(cm.getContents(), is(nullValue()));
    }

    @Test
    public void testGetInstance() {
        assertThat(ClipMon.getInstance(), is(notNullValue()));
        
        assertThat(ClipMon.getInstance(), is(sameInstance(ClipMon.getInstance())));
        
        Clipboard cmiClip = ClipMon.getInstance().getClipboard();
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        assertThat(cmiClip, is(sameInstance(sysClip)));
    }

    @Test
    public void testGetClipboard() {
        Clipboard clipboard = new Clipboard("TEST");
        ClipMon cm = new ClipMon(clipboard);
        
        assertThat(cm.getClipboard(), is(sameInstance(clipboard)));
        
        Clipboard cmiClip = ClipMon.getInstance().getClipboard();
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        assertThat(cmiClip, is(sameInstance(sysClip)));
    }

    @Test
    public void testGetContents() {
        assertThat(ClipMon.getInstance().getContents(), is(nullValue()));
    }

    @Test
    public void testGetDelta() {
        assertThat(ClipMon.getInstance().getDelta(), greaterThanOrEqualTo(ClipMon.MIN_SLEEP));
        assertThat(ClipMon.getInstance().getDelta(), lessThanOrEqualTo(ClipMon.MAX_SLEEP));
    }

    @Test
    public void testSetContents() {
        Clipboard clipboard = new Clipboard("TEST");
        ClipMon cm = new ClipMon(clipboard);
        assertThat(cm.getContents(), is(nullValue()));
        
        String test = "TEST_STRING";
        clipboard.setContents(new StringSelection(test), null);
        
        assertTrue(cm.updateContents());
        assertThat(cm.getContents(), is(test));
    }

    @Test
    public void testSetDelta() {
        ClipMon cm = new ClipMon();
        
        cm.setDelta(ClipMon.MIN_SLEEP);
        assertThat(cm.getDelta(), is(ClipMon.MIN_SLEEP));
        
        cm.setDelta(ClipMon.MIN_SLEEP - 1);
        assertThat(cm.getDelta(), is(ClipMon.MIN_SLEEP));
        
        cm.setDelta(ClipMon.MAX_SLEEP + 1);
        assertThat(cm.getDelta(), is(ClipMon.MAX_SLEEP));
        
        cm.setDelta(ClipMon.MAX_SLEEP);
        assertThat(cm.getDelta(), is(ClipMon.MAX_SLEEP));
        
        int midpoint = ClipMon.MIN_SLEEP + (ClipMon.MAX_SLEEP - ClipMon.MIN_SLEEP) / 2;
        cm.setDelta(midpoint);
        assertThat(cm.getDelta(), is(midpoint));
    }

    @Test
    public void testHasStringFlavor() {
        ClipMon cm = new ClipMon();
        assertFalse(cm.hasStringFlavor(Arrays.asList(DataFlavor.imageFlavor)));
        assertFalse(cm.hasStringFlavor(Arrays.asList(DataFlavor.javaFileListFlavor)));
        assertTrue(cm.hasStringFlavor(Arrays.asList(DataFlavor.stringFlavor)));
        assertFalse(cm.hasStringFlavor(Arrays.asList(DataFlavor.imageFlavor, DataFlavor.stringFlavor)));
        assertFalse(cm.hasStringFlavor(Arrays.asList(DataFlavor.javaFileListFlavor, DataFlavor.stringFlavor)));
    }

    @Test
    public void testUpdateContents() {
        Clipboard clipboard = new Clipboard("TEST");
        ClipMon cm = new ClipMon(clipboard);
        assertFalse(cm.updateContents());
        
        // this is missing an image or filelist data flavor negative test
        String test = "TEST_STRING";
        clipboard.setContents(new StringSelection(test), null);
        
        assertThat(cm.getContents(), is(not(test)));
        
        assertTrue(cm.updateContents());
        assertThat(cm.getContents(), is(test));
        
        assertFalse(cm.updateContents());
        assertThat(cm.getContents(), is(test));
    }

}
