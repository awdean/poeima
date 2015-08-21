package com.awdean;



/**
 * Hello world!
 * 
 */
public class Main {
    public static void main(String[] args) {
        Display gui = Display.getInstance();
        gui.init();

        gui.updateText("Hello World!\nAnd the same to you!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            // do nothing
        }

        gui.updateText("I've altered your text.\nPray I do not alter it further.");
        
        ClipMon.getInstance().start();
    }

}
