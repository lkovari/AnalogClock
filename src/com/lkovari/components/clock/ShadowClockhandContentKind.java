/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.clock
 * File: ShadowPointerContentKind.java
 * Created: Jan 5, 2006 3:23:32 PM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.components.clock;

/**
 * @author lkovari
 * ShadowPointerContentKind 
 */
public enum ShadowClockhandContentKind {
    // shadow clock hands represent the system time
    SYSTEM_TIME,
    // shadow clock hands represent the control time before show the clock as reference
    REFERENCE_TIME;
}
