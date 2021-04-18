/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.utilities
 * File: TimeFormatKind.java
 * Created: Oct 24, 2005 11:58:42 AM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.utilities;

/**
 * TimeFormatKind
 * @author László Kővári
 * Description:
 *
 */
public enum TimeFormatKind {
	// MM/dd/yyyy
	FORMAT_OF_LONG_DATE,
    // MM/dd/yy
    FORMAT_OF_SHORT_DATE,
	// hh:mm a
	FORMAT_OF_TIME_12,  
	// HH:mm
	FORMAT_OF_TIME_24,  
	// hh:mm a z
	FORMAT_OF_TIME_12_ZONE,
	// HH:mm z
	FORMAT_OF_TIME_24_ZONE,
	// MM/dd/yyyy HH:mm
	FORMAT_OF_LONG_DATE_TIME_12,
	// MM/dd/yyyy hh:mm a
	FORMAT_OF_LONG_DATE_TIME_24,
	// MM/dd/yyyy hh:mm a z
	FORMAT_OF_LONG_DATE_TIME_12_ZONE,
	// MM/dd/yyyy HH:mm z
	FORMAT_OF_LONG_DATE_TIME_24_ZONE,  
    // MM/dd/yy HH:mm
    FORMAT_OF_SHORT_DATE_TIME_12,
    // MM/dd/yy hh:mm a
    FORMAT_OF_SHORT_DATE_TIME_24,
    // MM/dd/yy hh:mm a z
    FORMAT_OF_SHORT_DATE_TIME_12_ZONE,
    // MM/dd/yy HH:mm z
    FORMAT_OF_SHORT_DATE_TIME_24_ZONE;  
}
