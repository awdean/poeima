package com.awdean;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ClipMon extends Thread {

    public static final int DELTA_SLEEP = 20;
    public static final int MAX_SLEEP = 2560;
    public static final int MIN_SLEEP = 100;
    
    private static final ClipMon INSTANCE = new ClipMon();
    
    private Clipboard _clipboard;
    private String _contents;
    private int _delta;
    
    public ClipMon() {
        this(Toolkit.getDefaultToolkit().getSystemClipboard());
    }
    
    public ClipMon(Clipboard clipboard) {
        _clipboard = clipboard;
        _contents = null;
        _delta = MIN_SLEEP;
    }
    
    public static ClipMon getInstance() {
        return INSTANCE;
    }
    
    public Clipboard getClipboard() {
        return _clipboard;
    }
    
    public String getContents() {
        return _contents;
    }
    
    public int getDelta() {
        return _delta;
    }
    
    public void setContents(String contents) {
        _contents = contents;
    }
    
    public void setDelta(int delta) {
        _delta = Math.max(MIN_SLEEP, Math.min(MAX_SLEEP, delta));
    }
    
    @Override
    public void run() {
        int delay = MIN_SLEEP;
        while (true) {
            try {
                sleep(delay);
                if (updateContents()) {
                    Display.getInstance().updateText(getContents());
                    delay = MIN_SLEEP;
                } else {
                    delay = Math.min(MAX_SLEEP, delay + DELTA_SLEEP);
                }
            } catch (InterruptedException ex) {
                // FIXME do nothing
            }
        }
    }
    
    public boolean hasStringFlavor(List<DataFlavor> flavors) {
        if (flavors.contains(DataFlavor.imageFlavor)
                || flavors.contains(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        return flavors.contains(DataFlavor.stringFlavor);
    }
    
    public boolean updateContents() {
        if (! hasStringFlavor(Arrays.asList(getClipboard().getAvailableDataFlavors()))) {
            return false;
        }
        
        Transferable data = getClipboard().getContents(null);
        
        try {
            String contents = (String) data.getTransferData(DataFlavor.stringFlavor);
            if (contents.equals(getContents())) {
                return false;
            }
        
            setContents(contents);
            return true;
        } catch (IOException ex) {
            return false;
        } catch (UnsupportedFlavorException ex) {
            return false;
        }
    }
    
}
