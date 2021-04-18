/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.events
 * File: TimeChangeKind.java
 * Created: Oct 24, 2005 11:58:42 AM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.components.clock.events;

/**
 * 
 * TimeChangeKind
 * @author László Kővári
 * Description:
 *  Different between time parts focused by change
 */
public enum TimeChangeKind {
    DATE,
    HOUR        ,
    MINUTE      ;
    
    TimeChangeKind() {
    }

}
