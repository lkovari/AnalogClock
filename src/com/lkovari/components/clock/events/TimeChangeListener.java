/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.events
 * File: TimeChangeListener.java
 * Created: Oct 24, 2005 11:58:42 AM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.components.clock.events;

import java.util.EventListener;

/**
 * 
 * TimeChangeListener
 * @author László Kővári
 * Description:
 *  Listener for change the time values
 */
public interface TimeChangeListener extends EventListener {
    void timeChanged(TimeChangeEvent timeChangeEvent);
}
