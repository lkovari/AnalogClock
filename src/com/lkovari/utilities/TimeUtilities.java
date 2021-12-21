/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.utilities
 * File: TimeUtilities.java
 * Created: Oct 24, 2005 11:58:42 AM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.utilities;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * 
 * @author lkovari
 * TimeUtilities
 */
public class TimeUtilities {
	public static final boolean LOCAL_HOTFIX = true;
	public static final int DAYLIGHT_SAVING_START_YEAR = 2200;
	
	public static final int[] dateElements = {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_YEAR};
	public static final int[] timeElements = {Calendar.HOUR_OF_DAY, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.AM_PM};

	public static final String TIME_MASK_12 = "hh:mm a";  
	public static final String TIME_MASK_24 = "HH:mm";  
	public static final String TIME_MASK_12_ZONE = "hh:mm a z";  
	public static final String TIME_MASK_24_ZONE = "HH:mm z";  
    public static final String LONG_DATE_MASK = "MM/dd/yyyy";  
    public static final String EXTENDED_DATE_MASK = "EEEE MMM dd,yyyy";  
    public static final String SHORT_DATE_MASK = "MM/dd/yy";  
	public static final String LONG_DATE_TIME_MASK_12 = "MM/dd/yyyy hh:mm a";  
	public static final String LONG_DATE_TIME_MASK_24 = "MM/dd/yyyy HH:mm";  
	public static final String LONG_DATE_TIME_MASK_12_ZONE = "MM/dd/yyyy hh:mm a z";  
	public static final String LONG_DATE_TIME_MASK_24_ZONE = "MM/dd/yyyy HH:mm z";  
    public static final String SHORT_DATE_TIME_MASK_12 = "MM/dd/yy hh:mm a";  
    public static final String SHORT_DATE_TIME_MASK_24 = "MM/dd/yy HH:mm";  
    public static final String SHORT_DATE_TIME_MASK_12_ZONE = "MM/dd/yy hh:mm a z";  
    public static final String SHORT_DATE_TIME_MASK_24_ZONE = "MM/dd/yy HH:mm z";  

	public static final String DATE_MASK_TO_FILENAME = "MM-dd-yyyy";
	public static final String DATE_MASK_TO_TIMESTAMP = "yyyy-MM-dd";
	
	/*
	private static SimpleTimeZone CreateSimpleTimeZone() {
		String[] ids = TimeZone.getAvailableIDs(1 * 60 * 60 * 1000);
		//create a Pacific Standard Time time zone
		//SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
		
		// this is a local
		SimpleTimeZone simpleTimeZone = new SimpleTimeZone(1 * 60 * 60 * 1000, ids[0]);

		// set up rules for daylight savings time
		simpleTimeZone.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		simpleTimeZone.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		return simpleTimeZone;
	}
	*/
	
	private static void assignDTElements(Calendar c0, Calendar c1, boolean date) {
		int[] elements = null;
		if (date) 
			elements = dateElements;
		else
			elements = timeElements;
		for (Integer e : elements) 
			c0.set(e, c1.get(e));
	}

	/*
	private static void assignDateElements(Calendar c0, Date d1) {
		c0.setTime(d1);
	}
	*/
	
	private static int compareDate(Calendar c0, Calendar c1) {
		int r = -1;
		r = c0.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		if (r == 0)
			r = c0.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		return r;
	}
	
	private static int compareTime(Calendar c0, Calendar c1) {
		int r = -1;
//		r = (c0.get(Calendar.HOUR_OF_DAY) < c1.get(Calendar.HOUR_OF_DAY) ? -1 : (c0.get(Calendar.HOUR_OF_DAY) == c1.get(Calendar.HOUR_OF_DAY) ? 0 : 1));
//		r = (r == 0 ? (c0.get(Calendar.MINUTE) < c1.get(Calendar.MINUTE) ? -1 : (c0.get(Calendar.MINUTE) == c1.get(Calendar.MINUTE) ? 0 : 1)) : r);
		
		r = (c0.get(Calendar.HOUR_OF_DAY) - c1.get(Calendar.HOUR_OF_DAY));
		if (r == 0)
			r = (c0.get(Calendar.MINUTE) - c1.get(Calendar.MINUTE));
		return r;
	}
	
	/**
	 * 
	 * @param timeSlotDuration
	 * @param dt Date - The time
	 * @return int - The difference from the nearest time value which rounded to the nearest whole of time slot duration 
	 */
	public static int DifferenceFromTheNearestTimeSlotDurationValue(int timeSlotDuration, Date dt) {
		Calendar calendar = null;
		//calendar = TimeUtilities.CreateCalendar(calendar);
		calendar = TimeUtilities.CreateCalendarUTCInstance();
		int diff = 0;
		try {
			calendar.setTime(dt);
			int minutes = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE);
			int df = 0;
			int modulusMin = (minutes % timeSlotDuration);
			if (modulusMin != 0) {
				if (modulusMin <= (timeSlotDuration / 2)) {
					// to the lowest whole of time slot duration /prev/
					diff = (modulusMin * -1);
				}	
				else {
					// to the highest whole of time slot duration /next/
					df = (timeSlotDuration - modulusMin);
					diff = df;
				}	
			}
		}
		finally {
			calendar = null;
		}
		return diff;
	}

	public static Calendar ArrangeToTheNearestTimeSlotDurationValue(int tmSlotDuration, Calendar cal) {
		cal.setTime(ArrangeToTheNearestTimeSlotDurationValue(tmSlotDuration, cal.getTime()));
		return cal;
	}
	
	/**
	 * 
	 * @param tmSlotDuration
	 * @param cal Calendar - The time value
	 * @return Calendar - The time value which rounded to the nearest whole of time slot duration
	 */
	public static Date ArrangeToTheNearestTimeSlotDurationValue(int tmSlotDuration, Date dt) {
		Calendar calendar = null;
		//calendar = TimeUtilities.CreateCalendar(calendar);
		calendar = TimeUtilities.CreateCalendarUTCInstance();
		try {
			// get the difference from the nearest value which round to the whole multiple of time slot duration 
			int diff = DifferenceFromTheNearestTimeSlotDurationValue(tmSlotDuration, dt);
			calendar.setTime(dt);
			calendar.add(Calendar.MINUTE, diff);
			dt.setTime(calendar.getTimeInMillis());
		}
		finally {
			calendar = null;
		}
		//System.out.println("R " + dt.getHours() + ":" + dt.getMinutes());
		return dt;
	}

	/*
	public static Calendar CreateCalendar(Calendar value) {
		//value = new GregorianCalendar(CreateSimpleTimeZone());
		value = Calendar.getInstance();
		value.setTimeZone(CreateSimpleTimeZone());
		return value;
	}
	*/
	
	/* add timeZoneOffsetInMils to current time before display
	TimeZone tz = TimeZone.getDefault();
	long timeZoneOffsetInMils = tz.getRawOffset();
	*/

	/*
	public static Calendar AdjustTimeZone(Calendar value) {
		if (value != null)  
			value.setTimeZone(CreateSimpleTimeZone());
		return value;
	}
	*/
	
	public static void AssignDateValues(Calendar c0, Calendar c1) {
		assignDTElements(c0, c1, true);
	}

	public static void AssignTimeValues(Calendar c0, Calendar c1) {
		assignDTElements(c0, c1, false);
		c0.set(Calendar.SECOND, 0);
		c0.set(Calendar.MILLISECOND, 0);
	}
	
	public static void AssignDateAndTimeValues(Calendar c0, Calendar c1) {
		//c0.setTimeInMillis(c1.getTimeInMillis());
		assignDTElements(c0, c1, true);
		assignDTElements(c0, c1, false);
		c0.set(Calendar.SECOND, 0);
		c0.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * 
	 * Method: Compare 
	 * @param c0
	 * @param c1
	 * @param kind
	 * @return
	 * 
     * Description: Compare two calendar value
     *	 0 - equal
     *  -1 - this < argument /c0 < c1/
     *   1 - this > argument /c0 > c1/
	 */
    public static int Compare(Calendar c0, Calendar c1, TimePartKind timePartKind) {
		int r = -1;
		switch (timePartKind) {
			case DATE : {
				r = compareDate(c0, c1);
				break;
			}
			case TIME : {
				r = compareTime(c0, c1);
				break;
			}
			case DATEANDTIME : {
				ResetTime(c0, TimePartKind.SECMILS);
				ResetTime(c1, TimePartKind.SECMILS);
				r = c0.compareTo(c1);
				break;
			}
		default:
			break;
			
		}	
		return r;
    }
    
    /**
     * 
     * Method: IsGreaterThan 
     * @param c0
     * @param c1
     * @param kind
     * @return boolean - true if c0 > c1
     */
    public static boolean IsGreaterThan(Calendar c0, Calendar c1, TimePartKind kind) {
    	return (Compare(c0, c1, kind) > 0);
    }

    /**
     * 
     * Method: IsLessThan 
     * @param c0 Calendar - 
     * @param c1
     * @param kind
     * @return boolean - true if c0 < c1
     */
    public static boolean IsLessThan(Calendar c0, Calendar c1, TimePartKind kind) {
    	return (Compare(c0, c1, kind) < 0);
    }
    
    /**
     * 
     * Method: IsEqualWith 
     * @param c0
     * @param c1
     * @param kind
     * @return
     */
    public static boolean IsEqualWith(Calendar c0, Calendar c1, TimePartKind kind) {
    	boolean res = (Compare(c0, c1, kind) == 0); 
    	return res;
    }
	
    
    //public static final String DATEFORMAT = "MM/dd/yyyy";
    //public static final String TIMEFORMAT = "HH:mm a";
    //public static final String DATETIMEFORMAT = "MM/dd/yyyy HH:mm a";
    
    public static String Time2Text(Calendar c, String mask) {
    	if (c == null)
    		return "";
    	String timeAsText = null;
    	DateFormat dateFormat = null;
    	/*
    	switch (part) {
    		case DATE : {
    			dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    			break;
    		}
    		case TIME : {
    			dateFormat = new SimpleDateFormat("HH:mm a z");
    			break;
    		}
    		case BOTH : {
    			dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a z");
    			break;
    		}
    	}
    	*/
		dateFormat = new SimpleDateFormat(mask);
		dateFormat.setTimeZone(c.getTimeZone());
		//System.out.println(" " + dateFormat.getTimeZone().getDisplayName() + " " + dateFormat.getTimeZone().getID());
		timeAsText = dateFormat.format(c.getTime());
		dateFormat = null;
		return timeAsText;   
    }
    
    public static String Time2FormatedText(Calendar c, TimeFormatKind timeFormatKind) {
    	String timeAsText = null;
    	switch (timeFormatKind) {
    	case FORMAT_OF_LONG_DATE : {
    		timeAsText = Time2Text(c, TimeUtilities.LONG_DATE_MASK);
    		break;
    	}
        case FORMAT_OF_SHORT_DATE : {
            timeAsText = Time2Text(c, TimeUtilities.SHORT_DATE_MASK);
            break;
        }
    	case FORMAT_OF_LONG_DATE_TIME_12 : {
    		timeAsText = Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_12);
    		break;
    	}
        case FORMAT_OF_SHORT_DATE_TIME_12 : {
            timeAsText = Time2Text(c, TimeUtilities.SHORT_DATE_TIME_MASK_12);
            break;
        }
        case FORMAT_OF_LONG_DATE_TIME_12_ZONE : {
    		timeAsText = Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_12_ZONE);
    		break;
    	}
        case FORMAT_OF_SHORT_DATE_TIME_12_ZONE : {
            timeAsText = Time2Text(c, TimeUtilities.SHORT_DATE_TIME_MASK_12_ZONE);
            break;
        }
    	case FORMAT_OF_LONG_DATE_TIME_24 : {
    		timeAsText = Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24);
    		break;
    	}
        case FORMAT_OF_SHORT_DATE_TIME_24 : {
            timeAsText = Time2Text(c, TimeUtilities.SHORT_DATE_TIME_MASK_24);
            break;
        }
    	case FORMAT_OF_LONG_DATE_TIME_24_ZONE : {
    		timeAsText = Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE);
    		break;
    	}
        case FORMAT_OF_SHORT_DATE_TIME_24_ZONE : {
            timeAsText = Time2Text(c, TimeUtilities.SHORT_DATE_TIME_MASK_24_ZONE);
            break;
        }
    	case FORMAT_OF_TIME_12 : {
    		timeAsText = Time2Text(c, TimeUtilities.TIME_MASK_12);
    		break;
    	}
    	case FORMAT_OF_TIME_12_ZONE : {
    		timeAsText = Time2Text(c, TimeUtilities.TIME_MASK_12_ZONE);
    		break;
    	}
    	case FORMAT_OF_TIME_24 : {
    		timeAsText = Time2Text(c, TimeUtilities.TIME_MASK_24);
    		break;
    	}
    	case FORMAT_OF_TIME_24_ZONE : {
    		timeAsText = Time2Text(c, TimeUtilities.TIME_MASK_24_ZONE);
    		break;
    	}
    	}
    	return timeAsText;
    }
    
    public static String Time2FormatedTextByTimeFormat(Calendar c, int timeFormat, boolean withDate, boolean withTimeZone, boolean isDateLong) {
    	String timeAsText = null;
    	if (timeFormat == 12) {
    		// 12 hours
    		if (withDate) {
                if (isDateLong)
                    timeAsText = (withTimeZone ? Time2FormatedText(c, TimeFormatKind.FORMAT_OF_LONG_DATE_TIME_12_ZONE) : Time2FormatedText(c, TimeFormatKind.FORMAT_OF_LONG_DATE_TIME_12));
                else
                    timeAsText = (withTimeZone ? Time2FormatedText(c, TimeFormatKind.FORMAT_OF_SHORT_DATE_TIME_12_ZONE) : Time2FormatedText(c, TimeFormatKind.FORMAT_OF_SHORT_DATE_TIME_12));
            }    
    		else
    			timeAsText = (withTimeZone ? Time2FormatedText(c, TimeFormatKind.FORMAT_OF_TIME_12_ZONE) : Time2FormatedText(c, TimeFormatKind.FORMAT_OF_TIME_12));
    	}
    	else {
    		// 24 hours
    		if (withDate) {
                if (isDateLong)
                    timeAsText = (withTimeZone ? Time2FormatedText(c, TimeFormatKind.FORMAT_OF_LONG_DATE_TIME_24_ZONE) : Time2FormatedText(c, TimeFormatKind.FORMAT_OF_LONG_DATE_TIME_24));
                else
                    timeAsText = (withTimeZone ? Time2FormatedText(c, TimeFormatKind.FORMAT_OF_SHORT_DATE_TIME_24_ZONE) : Time2FormatedText(c, TimeFormatKind.FORMAT_OF_SHORT_DATE_TIME_24));
            }    
    		else
    			timeAsText = (withTimeZone ? Time2FormatedText(c, TimeFormatKind.FORMAT_OF_TIME_24_ZONE) : Time2FormatedText(c, TimeFormatKind.FORMAT_OF_TIME_24));
    	}
    	return timeAsText;
    }
    
    public final static TimeZone UTC = TimeZone.getTimeZone("UTC");
    public final static TimeZone LOCAL = TimeZone.getDefault();

    private static Calendar InitializeSecAndMils(Calendar c) {
    	c.set(Calendar.SECOND, 0);
    	c.set(Calendar.MILLISECOND, 0);
    	return c;
    }
    
    public static Calendar CreateCalendarUTCInstance() {
    	return InitializeSecAndMils(Calendar.getInstance(UTC));
    }
    
    public static Calendar CreateCalendarInstanceByCustomTimeZone(String zone) {
    	TimeZone timeZone;
   		timeZone = TimeZone.getTimeZone(zone);
    	//System.out.println(timeZone.getDisplayName() + " " + timeZone.getID());
    	return Calendar.getInstance(timeZone);
    }

    public static Calendar CreateCalendarInstanceByCustomTimeZone(TimeZone timeZone) {
   		return Calendar.getInstance(timeZone);
    }
    
    public static Calendar CreateCalendarUTCInstanceFromDate(Date d) {
    	Calendar c = Calendar.getInstance(UTC);
    	c.setTime(d);
    	return InitializeSecAndMils(c);
    }

    public static Calendar CreateCalendarLocalInstance() {
    	return InitializeSecAndMils(Calendar.getInstance());
    }

    public static Calendar CreateCalendarLocalInstanceFromDate(Date d) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(d);
    	return InitializeSecAndMils(c);
    }

    public static void UTCTime2Local(Calendar c) {
    	c.setTimeZone(TimeZone.getDefault());
    }

    public static void LocalTime2UTC(Calendar c) {
    	c.setTimeZone(UTC);
    }
    
    public static void Time2CustomZone(Calendar c0, Calendar c1, TimeZone timeZone) {
        c0.setTimeZone(timeZone);
        c0.setTimeInMillis(c1.getTimeInMillis());
    }
    
    public static void LocalTime2Custom(Calendar c0, Calendar c1, TimeZone timeZone) {
        Time2CustomZone(c0, c1, timeZone);
    }
    
    public static void CustomTime2Local(Calendar c0, Calendar c1) {
        Time2CustomZone(c0, c1, TimeZone.getDefault());
    }
    
    public static void UTCTime2Custom(Calendar c0, Calendar c1, TimeZone timeZone) {
        Time2CustomZone(c0, c1, timeZone);
    }

    public static void CustomTime2UTC(Calendar c0, Calendar c1) {
        Time2CustomZone(c0, c1, TimeZone.getTimeZone("UTC"));
    }
    
    public static void LocalTime2UTC(Calendar c0, Calendar c1) {
        CustomTime2UTC(c0, c1);
    }
    
    public static void UTCTime2Local(Calendar c0, Calendar c1) {
        UTCTime2Custom(c0, c1, TimeZone.getDefault());
    }
    
    public static Calendar AdjustDaylightSaving(Calendar c) {
    	int dstOffset = 0; 
    	TimeZone localTimeZone = TimeZone.getDefault();
    	// this zone use daylight saving?
		if (localTimeZone.useDaylightTime())
			// this date is in daylight saving range?
			if (localTimeZone.inDaylightTime(c.getTime())) {
				// get DST offset
				dstOffset = localTimeZone.getDSTSavings();
				// add DST offset -> get the real UTC time
				c.add(Calendar.MILLISECOND, dstOffset);
			}	
		return c;
    }

    
    public static void ResetTime(Calendar c, TimePartKind timePartKind) {
    	switch (timePartKind) {
    	case DATEANDTIME : {
    		c.clear();
    		break;
    	}
    	case DATE : {
        	c.clear(Calendar.YEAR);
        	c.clear(Calendar.MONTH);
        	c.clear(Calendar.DATE);
        	c.clear(Calendar.DAY_OF_MONTH);
        	c.clear(Calendar.WEEK_OF_MONTH);
        	c.clear(Calendar.DAY_OF_WEEK_IN_MONTH);
        	c.clear(Calendar.DAY_OF_WEEK);
        	c.clear(Calendar.WEEK_OF_YEAR);
        	c.clear(Calendar.DAY_OF_YEAR);
    		break;
    	}
    	case TIME : {
        	c.clear(Calendar.AM_PM);
        	c.clear(Calendar.HOUR);
        	c.clear(Calendar.HOUR_OF_DAY);
        	c.clear(Calendar.MINUTE);
        	c.clear(Calendar.SECOND);
        	c.clear(Calendar.MILLISECOND);
    		break;
    	}
    	case SECMILS : {
        	c.clear(Calendar.SECOND);
        	c.clear(Calendar.MILLISECOND);
    		break;
    	}
    	}
    }
     
    /**
     * 
     * Method: MergeDateAndTime 
     * @param out Calendar - The merged calendar in UTC format
     * @param date Calendar - Contain the date
     * @param time Calendar - Contain the time in Local format
     */
    public static void MergeDateAndTime(Calendar out, Calendar date, Calendar time) {
        //System.out.println("TimeUtilities.MergeDateAndTime");
        //System.out.println(" Date " + TimeUtilities.Time2Text(date, "MM/dd/yyyy HH:mm a z")); 
        //System.out.println(" Time " + TimeUtilities.Time2Text(time, "MM/dd/yyyy HH:mm a z")); 
        //Calendar c = TimeUtilities.CreateCalendarUTCInstance();
        //2006.10.20. DIFF_FIX Calendar c = TimeUtilities.CreateCalendarLocalInstance();
    	Calendar c = TimeUtilities.CreateCalendarLocalInstanceWoDlse();
        // set date
        TimeUtilities.AssignDateValues(c, date);
        //System.out.println(" C.Date = Date " + TimeUtilities.Time2Text(c, "MM/dd/yyyy HH:mm a z"));
        // set 0:00 AM
        TimeUtilities.ResetTime(c, TimePartKind.TIME);
        //System.out.println(" C.Time = 0 " + TimeUtilities.Time2Text(c, "MM/dd/yyyy HH:mm a z"));
        // get minutes of time since 0:00 AM
        int mins = (time.get(Calendar.HOUR_OF_DAY) * 60) + time.get(Calendar.MINUTE);
        // add minutes
        //2006.10.18. ADD_FIX c.add(Calendar.MINUTE, mins);
        TimeUtilities.AddTime(c, Calendar.MINUTE, mins);
        //System.out.println(" C.Time = Time " + TimeUtilities.Time2Text(c, "MM/dd/yyyy HH:mm a z"));
        // convert to UTC
        TimeUtilities.LocalTime2UTC(out, c);
        //System.out.println(" UTC C = " + TimeUtilities.Time2Text(out, "MM/dd/yyyy HH:mm a z"));
        // assign to out
        c = null;
    }
    
    /**
     * 
     * Method: fromCalendar 
     * @param calendar in UTC 
     * @param local
     * @return
     */
    public static Date fromCalendar(Calendar calendar, TimeZone local) {
    	/*
    	int offset = local.getRawOffset();
    	if (local.inDaylightTime(calendar.getTime()))
    		offset += local.getDSTSavings();
    	*/
		return new Date(calendar.getTimeInMillis());
    }
    
    /**
     * 
     * Method: fromCalendar 
     * @param calendar
     * @return
     */
    public static Date fromCalendar(Calendar calendar) {
		return fromCalendar(calendar, TimeZone.getDefault());
    }
    
    /**
     * 
     * Method: DifferenceInMinutes 
     * @param c0
     * @param c1
     * @return
     */
    public static int DifferenceInMinutes(Calendar c0, Calendar c1, boolean isAbs) {
//    	int differenceInMinutes = 0;
//    	long inMils0 = c0.getTimeInMillis();
//    	long inMils1 = c1.getTimeInMillis();
//    	int inMinutes0 = (int)(inMils0 / 60000);
//    	int inMinutes1 = (int)(inMils1 / 60000);
//    	differenceInMinutes = Math.abs(inMinutes1 - inMinutes0);
//    	return differenceInMinutes;
    	long inMinutes0 = Calendar2Minutes(c0);
    	long inMinutes1 = Calendar2Minutes(c1);
    	int diff = (int) (inMinutes1 - inMinutes0);
    	// is need absolute value or not
    	return (isAbs ? (int) Math.abs(diff) : diff);
    }
    
    public static void setCalendarToUTCAndTimeBack(Calendar c) {
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		TimeUtilities.ResetTime(c, TimePartKind.TIME);
		TimeUtilities.LocalTime2UTC(c);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
    }
    
    public static void MergeTimeWithDate(Calendar datetime, Calendar date, Calendar time) {
    	// reset time of date
    	TimeUtilities.ResetTime(date, TimePartKind.TIME);
    	// synchronize time zones
    	datetime.setTimeZone(time.getTimeZone());
    	
    	// assign date
    	TimeUtilities.AssignDateValues(datetime, date);
    	System.out.println("datetime " + TimeUtilities.Time2Text(datetime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
    	// assign time
    	TimeUtilities.AssignTimeValues(datetime, time);
    	System.out.println("datetime " + TimeUtilities.Time2Text(datetime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
    	/*
    	// store origin zones
    	String outTimeZone1 = datetime1.getTimeZone().getID();
    	String outTimeZone2 = datetime1.getTimeZone().getID();
    	String dateTimeZone = date.getTimeZone().getID();
    	String timeTimeZone1 = time1.getTimeZone().getID();
    	String timeTimeZone2 = time2.getTimeZone().getID();
    	
    	// set all zone to UTC
    	datetime1.setTimeZone(TimeZone.getTimeZone("UTC"));
    	System.out.println("datetime1 " + TimeUtilities.Time2Text(datetime1, TimeUtilities.DATE_TIME_MASK_24_ZONE));
    	datetime2.setTimeZone(TimeZone.getTimeZone("UTC"));
    	System.out.println("datetime2 " + TimeUtilities.Time2Text(datetime2, TimeUtilities.DATE_TIME_MASK_24_ZONE));
    	date.setTimeZone(TimeZone.getTimeZone("UTC"));
    	System.out.println("date " + TimeUtilities.Time2Text(date, TimeUtilities.DATE_TIME_MASK_24_ZONE));
    	time1.setTimeZone(TimeZone.getTimeZone("UTC"));
    	System.out.println("time1 " + TimeUtilities.Time2Text(time1, TimeUtilities.DATE_TIME_MASK_24_ZONE));
    	time2.setTimeZone(TimeZone.getTimeZone("UTC"));
    	System.out.println("time1 " + TimeUtilities.Time2Text(time2, TimeUtilities.DATE_TIME_MASK_24_ZONE));
    	
    	// merging the begin time with the date
    	Calendar out = TimeUtilities.CreateCalendarUTCInstance();
    	TimeUtilities.AssignDateValues(out, date);
    	// calculate the begin time in minutes
    	long beginRange = time1.getTimeInMillis();
    	long beginRangeInMin = (beginRange / 60000); 
    	// calculate the end time in minutes
    	long endRange = time2.getTimeInMillis();
    	long endRangeInMin = (endRange / 60000);
    	// calculate time range length
    	int timeRangeLenght = (int) (endRangeInMin - beginRangeInMin);
    	// assign the time
    	TimeUtilities.AssignTimeValues(out, time1);
    	// copy to output
    	datetime1.setTimeInMillis(out.getTimeInMillis());
    	// merging the end time with the date
    	TimeUtilities.AssignDateValues(out, date);
    	// assign time 1 newly to the date
    	TimeUtilities.AssignTimeValues(out, time1);
    	// add to the first time the time range length in minutes
    	out.add(Calendar.MINUTE, timeRangeLenght);
    	// copy to out
    	datetime2.setTimeInMillis(out.getTimeInMillis());
    	// restore origin time zones
    	datetime1.setTimeZone(TimeZone.getTimeZone(outTimeZone1));
    	datetime2.setTimeZone(TimeZone.getTimeZone(outTimeZone2));
    	date.setTimeZone(TimeZone.getTimeZone(dateTimeZone));
    	time1.setTimeZone(TimeZone.getTimeZone(timeTimeZone1));
    	time2.setTimeZone(TimeZone.getTimeZone(timeTimeZone2));
    	*/
    	System.out.println("datetime " + TimeUtilities.Time2Text(datetime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
    	//System.out.println("datetime2 " + TimeUtilities.Time2Text(datetime, TimeUtilities.DATE_TIME_MASK_24_ZONE));
    }
    public static void checkAndArrangeWorkingTimes(Calendar startOfWorkingTime, Calendar endOfWorkingTime, int timeSlotDuration) {
        // check working hour start and end minutes
        //int min = selectedStartOfWorkHoursTime.get(Calendar.MINUTE);
        //2006.05.25.int min = selectedStartOfWorkHoursTimeLocal.get(Calendar.MINUTE);
    	int min = startOfWorkingTime.get(Calendar.MINUTE);
        if ((min % timeSlotDuration) != 0) {
            //TimeUtilities.ArrangeToTheNearestTimeSlotDurationValue(timeSlotDuration, selectedStartOfWorkHoursTime);
            //2006.05.25.TimeUtilities.ArrangeToTheNearestTimeSlotDurationValue(timeSlotDuration, selectedStartOfWorkHoursTimeLocal);
        	TimeUtilities.ArrangeToTheNearestTimeSlotDurationValue(timeSlotDuration, startOfWorkingTime);
            //startOfWorkHoursTimePicker.setStepInMinutes(timeSlotDuration);
            //!!!!!!!workingHoursStartCalendarCombo
        }
        //min = selectedEndOfWorkHoursTime.get(Calendar.MINUTE);
        //2006.05.25.min = selectedEndOfWorkHoursTimeLocal.get(Calendar.MINUTE);
        min = endOfWorkingTime.get(Calendar.MINUTE);
        if ((min % timeSlotDuration) != 0) {
            //TimeUtilities.ArrangeToTheNearestTimeSlotDurationValue(timeSlotDuration, selectedEndOfWorkHoursTime);
            //2006.05.25.TimeUtilities.ArrangeToTheNearestTimeSlotDurationValue(timeSlotDuration, selectedEndOfWorkHoursTimeLocal);
        	TimeUtilities.ArrangeToTheNearestTimeSlotDurationValue(timeSlotDuration, endOfWorkingTime);
            //endOfWorkHoursTimePicker.setStepInMinutes(timeSlotDuration);
            //workingHoursEndCalendarCombo
        }
    }
    
    public static boolean IsTimeMidnight(Calendar c) {
    	return ((c.get(Calendar.HOUR_OF_DAY) == 0) && (c.get(Calendar.MINUTE) == 0));
    }
    
    public static boolean IsRangeFromMidnightToMidnight(Calendar start, Calendar end) {
    	// check if the time on a day is midnight or not
        boolean isHourAndMinuteZero = IsTimeMidnight(start);
        isHourAndMinuteZero &= IsTimeMidnight(end);
        // check if the day difference is one day
        boolean isDayDifferenceOne = ((end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR)) == 1);
        return (isHourAndMinuteZero && isDayDifferenceOne); 
    }
    

    /**
     * 
     * Method: ExtendTimeRangeWithDate 
     * @param rangeStart Calendar - start
     * @param rangeEnd Calendar - end
     * @param date Calendar - date
     * @return TimeRange - real time range
     * 
     * 1970.01.01 07:00 - 1970.01.01 17:00 = 07 - 17
     * 1970.01.01 07:00 - 1970.01.02 00:00 = 07 - 24
     * 
     */
    public static TimeRange ExtendTimeRangeWithDate(Calendar rangeStart, Calendar rangeEnd, Calendar date) {
    	TimeRange timeRange = null;
    	if ((date != null) && (rangeStart != null) && (rangeEnd != null)) {
    		//2006.10.20. DIFF_FIX Calendar startTime = Calendar.getInstance();
    		//2006.10.20. DIFF_FIX Calendar endTime = Calendar.getInstance();
    		Calendar startTime = TimeUtilities.CreateCalendarLocalInstanceWoDlse();
    		Calendar endTime = TimeUtilities.CreateCalendarLocalInstanceWoDlse();
    		// copy values
    		TimeUtilities.AssignDateAndTimeValues(startTime, rangeStart);
    		TimeUtilities.AssignDateAndTimeValues(endTime, rangeEnd);
        	PrintToConsole("ExtendTimeRangeWithDate", "origin range " + TimeUtilities.Time2Text(startTime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE) + " - " + TimeUtilities.Time2Text(endTime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE), false, true);

    		// assign dates
    		TimeUtilities.AssignDateValues(startTime, date);
            TimeUtilities.AssignDateValues(endTime, date);
        	PrintToConsole("ExtendTimeRangeWithDate", "range with date " + TimeUtilities.Time2Text(startTime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE) + " - " + TimeUtilities.Time2Text(endTime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE), false, true);
        	
        	// end time is midnight?
        	if (TimeUtilities.IsTimeMidnight(endTime)) {
        		//extend to real 24 hour interval with date
        		endTime.add(Calendar.DAY_OF_MONTH, 1);
            	PrintToConsole("ExtendTimeRangeWithDate", "extend to 24 " + TimeUtilities.Time2Text(startTime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE) + " - " + TimeUtilities.Time2Text(endTime, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE), false, true);
        	}
        	timeRange = new TimeRange(startTime, endTime);
    	}
    	return timeRange;
    }

    public static long Calendar2Minutes(Calendar c) {
    	Calendar temp = TimeUtilities.CreateCalendarLocalInstanceWoDlse();
    	TimeUtilities.AssignDateAndTimeValues(temp, c);
    	long inMils = temp.getTimeInMillis();
    	long inMinutes = (inMils / 60000);
    	temp = null;
    	return inMinutes; 
    }
    
    public static long Calendar2Hours(Calendar c) {
    	return (Calendar2Minutes(c) / 60);
    }

    public static long Calendar2Days(Calendar c) {
    	return (Calendar2Hours(c) / 24);
    }
    
    public static boolean isRangeGreaterThan24Hours(Calendar start, Calendar end) {
    	long startHours = Calendar2Hours(start);
    	long endHours = Calendar2Hours(end);
    	return (endHours - startHours) > 24;
    }

    public static boolean isRangeEqual24Hours(Calendar start, Calendar end) {
    	long startHours = Calendar2Hours(start);
    	long endHours = Calendar2Hours(end);
    	return (endHours - startHours) == 24;
    }
    
    
    /**
     * 
     * Method: MergeDateWithTimeRange 
     * @param date Calendar - the date
     * @param rangeStart Calendar - range start
     * @param rangeEnd Calendar - range end
     * @return TimeRange - represent the merget time range with the date
     */
//    public static TimeRange MergeTimeRangeWithDate(Calendar date, Calendar rangeStart, Calendar rangeEnd) {
//    	TimeRange timeRange = null;
//    	if ((date != null) && (rangeStart != null) && (rangeEnd != null)) {
//    		// copy
//    		Calendar startTime = (Calendar) rangeStart.clone();
//    		Calendar endTime = (Calendar) rangeEnd.clone();
//        	PrintToConsole("mergeDateWithTimeRange", "origin range " + TimeUtilities.Time2Text(startTime, TimeUtilities.DATE_TIME_MASK_24_ZONE) + " - " + TimeUtilities.Time2Text(endTime, TimeUtilities.DATE_TIME_MASK_24_ZONE), false, true);
//
//    		// assign dates
//    		TimeUtilities.AssignDateValues(startTime, date);
//            TimeUtilities.AssignDateValues(endTime, date);
//        	PrintToConsole("mergeDateWithTimeRange", "range with date " + TimeUtilities.Time2Text(startTime, TimeUtilities.DATE_TIME_MASK_24_ZONE) + " - " + TimeUtilities.Time2Text(endTime, TimeUtilities.DATE_TIME_MASK_24_ZONE), false, true);
//        	
//        	// date is equal?
//        	if (!TimeUtilities.IsEqualWith(rangeStart, rangeEnd, TimePartKind.DATE)) {
//        		// get the difference in days between start and end date
//            	long diffInDays = ((rangeEnd.getTimeInMillis() / 60000) - (rangeStart.getTimeInMillis() / 60000)) / (60 * 24);
//            	// if need add day difference
//            	if (diffInDays > 0)
//            		endTime.add(Calendar.DAY_OF_MONTH, (int) diffInDays);
//
//        	}
//        	PrintToConsole("mergeDateWithTimeRange", "merged range " + TimeUtilities.Time2Text(startTime, TimeUtilities.DATE_TIME_MASK_24_ZONE) + " - " + TimeUtilities.Time2Text(endTime, TimeUtilities.DATE_TIME_MASK_24_ZONE), false, true);
//        	timeRange = new TimeRange(startTime, endTime);
//        	
//    	}
//    	return timeRange;
//    }
    
    public static void PrintToConsole(String where, String text, boolean error, boolean lf) {
    	String mess = "sc \t";
    	mess += where + " " + text;
    	if (error)
    		if (lf)
    			System.err.println(mess);
    		else
    			System.err.print(mess);
    	else
    		if (lf)
    			System.out.println(mess);
    		else
    			System.out.print(mess);
    }

    public static void AddTime(Calendar to, int calendarField, int value) {
    	Calendar tmpCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    	//Calendar tmpCalendar = TimeUtilities.CreateCalendarLocalInstanceWoDlse();
    	TimeUtilities.AssignDateAndTimeValues(tmpCalendar, to);
    	tmpCalendar.add(calendarField, value);
    	TimeUtilities.AssignDateAndTimeValues(to, tmpCalendar);
    }


    public static void AddHourAndMinute(Calendar to, int h, int m) {
    	Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    	TimeUtilities.AssignDateAndTimeValues(utcCalendar, to);
    	utcCalendar.add(Calendar.HOUR_OF_DAY, h);
    	utcCalendar.add(Calendar.MINUTE, m);
    	TimeUtilities.AssignDateAndTimeValues(to, utcCalendar);
    }
    
    public static void SynchronizeDaylightSavingOffset(Calendar c0, Calendar c1) {
    	  // get offsets
    	  int dstOffset0 = c0.get(Calendar.DST_OFFSET);
    	  int dstOffset1 = c1.get(Calendar.DST_OFFSET);
    	  // determinate is need synchronize or not, 
    	  // if 0 both or not 0 both and equal then not need
    	  boolean isNotNeedSynch = ((dstOffset0 == 0) && (dstOffset1 == 0)); 
    	  isNotNeedSynch |= (((dstOffset0 != 0) && (dstOffset1 != 0)) && (dstOffset0 == dstOffset1));
    	  if (!isNotNeedSynch) {
    		  TimeUtilities.PrintToConsole("TimeUtilities.SynchronizeDaylightSavingOffset", "SYNC NEED " + c0.getTime() + " ~ " + c1.getTime(), false, true);
    		  // is need to set standard summer time
    		  if (dstOffset0 > dstOffset1)
        		  c1.set(Calendar.DST_OFFSET, c0.get(Calendar.DST_OFFSET));
    		  else
        		  c0.set(Calendar.DST_OFFSET, c1.get(Calendar.DST_OFFSET));
    	  }
    	  else
    		  TimeUtilities.PrintToConsole("TimeUtilities.SynchronizeDaylightSavingOffset", "SYNC NOT NEED " + c0.getTime() + " ~ " + c1.getTime(), false, true);
    		  
      }
      
      /**
       * 
       * Method: SwitchOffDaykightSaving 
       * @param c
       */
      public static Calendar SwitchOffDaykightSaving(Calendar c) {
    	  SimpleTimeZone stz = new SimpleTimeZone(c.getTimeZone().getRawOffset(), c.getTimeZone().getID());
    	  // can specify the year when the daylight saving time schedule starts in effect
//    	  stz.setStartYear(DAYLIGHT_SAVING_START_YEAR);
//    	  stz.setStartRule(Calendar.JANUARY, 1, 1000);
//    	  stz.setEndRule(Calendar.JANUARY, 1, 1000);
    	  c.setTimeZone(stz);
    	  return c;
      }
      
      /**
       * 
       * Method: CreateCalendarLocalInstanceWoDlse 
       * @return Calendar - without daylight saving effect
       */
      public static Calendar CreateCalendarLocalInstanceWoDlse() {
    	  return SwitchOffDaykightSaving(Calendar.getInstance());
      }

      private static Calendar SetTime(Calendar c, int ye, int mo, int da, int ho, int mi) {
        c.set(Calendar.YEAR, ye);
        c.set(Calendar.MONTH, mo);
        c.set(Calendar.DAY_OF_MONTH, da);
        c.set(Calendar.HOUR_OF_DAY, ho);
        c.set(Calendar.MINUTE, mi);
        return c;
      }
      
      public static Date getEndOfDay(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
      }
      
      
      public static void main(String[] args) {
        Calendar c = null;
        // before switch
        c = Calendar.getInstance();
        TimeUtilities.SetTime(c, 2006, Calendar.MARCH, 25, 8, 0);
        System.out.println("Origin " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        TimeUtilities.AdjustDaylightSaving(c);
        System.out.println("Adjusted " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        System.out.println("");

        // after
        c = Calendar.getInstance();
        TimeUtilities.SetTime(c, 2006, Calendar.MARCH, 26, 0, 0);
        System.out.println("Origin " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        TimeUtilities.AdjustDaylightSaving(c);
        System.out.println("Adjusted " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        System.out.println("");
        
        // before switch
        c = Calendar.getInstance();
        TimeUtilities.SetTime(c, 2006, Calendar.MARCH, 26, 2, 59);
        System.out.println("Origin " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        TimeUtilities.AdjustDaylightSaving(c);
        System.out.println("Adjusted " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        System.out.println("");

        // today
        c = Calendar.getInstance();
        TimeUtilities.SetTime(c, 2006, Calendar.OCTOBER, 6, 8, 0);
        System.out.println("Origin " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        TimeUtilities.AdjustDaylightSaving(c);
        System.out.println("Adjusted " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        System.out.println("");

        // before switch
        c = Calendar.getInstance();
        TimeUtilities.SetTime(c, 2006, Calendar.OCTOBER, 28, 23, 59);
        System.out.println("Origin " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        TimeUtilities.AdjustDaylightSaving(c);
        System.out.println("Adjusted " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        System.out.println("");
        
        // after switch
        c = Calendar.getInstance();
        TimeUtilities.SetTime(c, 2006, Calendar.OCTOBER, 29, 8, 0);
        System.out.println("Origin " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        TimeUtilities.AdjustDaylightSaving(c);
        System.out.println("Adjusted " + TimeUtilities.Time2Text(c, TimeUtilities.LONG_DATE_TIME_MASK_24_ZONE));
        System.out.println("");


        
        
      }
    
}