/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.clock.test
 * File: AnalogClockTest.java
 * Created: Aug 22, 2020 5:11 PM
 * 
 * Description:
 *  Test of AnalogClock component
 * 
 */
package com.lkovari.components.clock;

import java.util.stream.Stream;

public enum TimeRunKind {
	DIGITAL("Digital"),
	ANALOG("Analog");
	
    private String kindOfRun;
	 
    TimeRunKind(String kindOfRun) {
        this.kindOfRun = kindOfRun;
    }
	
    // standard getters and setters 
 
    public static Stream<TimeRunKind> stream() {
        return Stream.of(TimeRunKind.values()); 
    }	
}
