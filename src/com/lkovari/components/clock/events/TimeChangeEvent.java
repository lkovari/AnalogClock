/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.events
 * File: TimeChangeEvent.java
 * Created: Oct 24, 2005 11:58:42 AM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.components.clock.events;


/**
 * 
 * TimeChangeEvent
 * @author László Kővári
 * Description:
 *  Event for change the time
 */
public class TimeChangeEvent extends java.util.EventObject {
    /**
     * Field <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1234320543604583903L;
    
    TimeChangeContainer timeChangeContainer = null;
    
    /**
     * 
     * Method: getTimeChangeContainer 
     * @return TimeChangeContainer - contain the values, descriptions of changes
     */
    public TimeChangeContainer getTimeChangeContainer() {
        // return value of timeChangeContainer
        return timeChangeContainer;
    }

    /**
     * 
     * Constructor TimeChangeEvent
     * @param src Object - source of event
     * @param timeChangeContainer TimeChangeContainer - contain the values, descriptions of changes
     */
    public TimeChangeEvent(Object src, TimeChangeContainer timeChangeContainer) {
        super(src);
        this.timeChangeContainer = timeChangeContainer;
    }


}