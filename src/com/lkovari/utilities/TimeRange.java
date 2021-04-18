/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.utilities
 * File: TimeRange.java
 * Created: Oct 24, 2005 11:58:42 AM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.utilities;

import java.util.Calendar;
import java.util.Date;

public class TimeRange {
    private Calendar start = null;
    private Calendar end = null;
    
    public Calendar getStart() {
        return this.start;
    }
    
    public Calendar getEnd() {
        return this.end;
    }
    
    public void setEnd(Calendar end) {
        this.end = end;
    }
    
    public void setStart(Calendar start) {
        this.start = start;
    }

    public TimeRange() {
        this(null, null);
    }
    
    public TimeRange(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;
    }

    public static TimeRange Dates2TimeRange(TimeRange timeRange, Date startDate, Date endDate, Date startTime, Date endTime) {
        // to calendar
        Calendar startDt = TimeUtilities.CreateCalendarLocalInstanceFromDate(startDate);
        Calendar endDt = TimeUtilities.CreateCalendarLocalInstanceFromDate(endDate);
        Calendar startTm = TimeUtilities.CreateCalendarLocalInstanceFromDate(startTime);
        Calendar endTm = TimeUtilities.CreateCalendarLocalInstanceFromDate(endTime);
        Calendar startTimeC = Calendar.getInstance();
        Calendar endTimeC = Calendar.getInstance();
        // merge
        TimeUtilities.MergeDateAndTime(startTimeC, startDt, startTm);
        TimeUtilities.MergeDateAndTime(endTimeC, endDt, endTm);
        // to time range class
        timeRange.setStart(startTimeC);
        timeRange.setEnd(endTimeC);
        return timeRange;
    }
    
    
    public static TimeRange AssignTimeRange(Date startDate, Date endDate, Date startTime, Date endTime) {
        TimeRange timeRange = new TimeRange();
        return Dates2TimeRange(timeRange, startDate, endDate, startTime, endTime);
    }
}
