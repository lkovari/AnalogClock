/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.events
 * File: TimeChangeContainer.java
 * Created: Oct 24, 2005 11:58:42 AM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.components.clock.events;

import java.util.Calendar;

public class TimeChangeContainer {
    private Object source = null;
    private TimeChangeKind timeChnageKind = null;
    private int value = 0;
    private Calendar time = null;
    
    /**
     * 
     * Method: getSource 
     * @return Object - get the event source
     */
    public Object getSource() {
        return source;
    }
    
    /**
     * 
     * Method: getTimeChnageKind 
     * @return TimeChangeKind - Enumerated HOUR = changed the hour, MINUTE changed the minute
     */
    public TimeChangeKind getTimeChnageKind() {
        return timeChnageKind;
    }
    
    /**
     * 
     * Method: getValue 
     * @return int - the value of changes
     */
    public int getValue() {
        return value;
    }    
    
    /**
     * 
     * Method: getTime 
     * @return Calendar - the time which changed
     */
    public Calendar getTime() {
        return time;
    }
    
    /**
     * 
     * Constructor TimeChangeContainer
     * @param source Object - the event source
     * @param timeChnageKind TimeChangeKind - Enumerated HOUR = changed the hour, MINUTE changed the minute 
     * @param value int - the value of changes
     * @param time Calendar - the time which changed
     */
    public TimeChangeContainer(Object source, TimeChangeKind timeChnageKind, int value, Calendar time) {
        this.source = source;
        this.timeChnageKind = timeChnageKind;
        this.value = value;
        this.time = time;
    }
}
