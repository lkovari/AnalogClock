package com.lkovari.components.clock;

import java.util.stream.Stream;

public enum TimeFaceKind {
	HOUR12(12),
	HOUR24(24);
	
    private int timeFaceKind;
	 
    TimeFaceKind(int value) {
        this.timeFaceKind = value;
    }
	
    // standard getters and setters 
     public static Stream<TimeFaceKind> stream() {
        return Stream.of(TimeFaceKind.values()); 
    }		
     
    public int getValue() {
    	return timeFaceKind;
    }
}
