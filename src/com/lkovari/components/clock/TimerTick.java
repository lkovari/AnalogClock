/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.clock
 * File: TimerTick.java
 * Created: Jan 5, 2006 3:23:32 PM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.components.clock;


public class TimerTick extends Thread {
    private AnalogClock analogClock = null;
    private volatile boolean isThreadRun = true;
    
    /**
     * 
     * Method: setIsThreadRun 
     * @param isThreadRun if false stop the thread
     */
    public void setIsThreadRun(boolean isThreadRun) {
        // parameter of this.isThreadRun
        this.isThreadRun = isThreadRun;
        //System.out.println("State  is " + this.isThreadRun);
    }
    
    /**
     * 
     * Method: getIsThreadRun 
     * @return boolean - if true the thread is run else not
     */
    public boolean isThreadRun() {
        return this.isThreadRun;
    }
    
    @Override
    public void run() {
        while (this.isThreadRun) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // catch block for InterruptedException
                e.printStackTrace();
            }
            if (!this.isThreadRun)
                this.interrupt();
            else {
                this.analogClock.repaint();
            }    
            super.run();
        }
    }    
    
    /**
     * 
     * Constructor TimerTick
     * @param analogClock AnalogClock - the analog clock component
     */
    public TimerTick(AnalogClock analogClock) {
        this.analogClock = analogClock;
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.analogClock = null;
        this.isThreadRun = false;
    }
}