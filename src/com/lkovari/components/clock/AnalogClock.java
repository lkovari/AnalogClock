/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.clock
 * File: AnalogClock.java
 * Created: Jan 5, 2006 3:23:32 PM
 * 
 * Description:
 * 
 * 
 */

package com.lkovari.components.clock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import com.lkovari.components.clock.colors.ColorDescriptor;
import com.lkovari.components.clock.events.TimeChangeContainer;
import com.lkovari.components.clock.events.TimeChangeEvent;
import com.lkovari.components.clock.events.TimeChangeKind;
import com.lkovari.components.clock.events.TimeChangeListener;



/**
 * 
 * @author László Kővári
 * TODO List
 * - replace long constructor parameter with a parameter class
 */
public class AnalogClock extends JPanel implements MouseMotionListener, MouseListener {
    /**
     * Field <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3914010935604952311L;	
    private String logoText = "by L.Kővári";
    public static final String COMP_NAME = "AnalogClock";
    
    public static final String PROP_DATAMODEL = "dataModel";
    public static final String PROP_STEP_IN_MINUTE = "stepInMinute";
    public static final String PROP_TIME_IS_RUN = "timeRun";
    public static final String PROP_IS_SHOW_SHADOW_TIME = "showShadowTime";
    public static final String PROP_IS_KEEP_ORIGIN_DATE = "keepOriginDate";
    public static final String PROP_ENABLE_CLOCKHAND_MOVING = "enableClockhandMoving";
    public static final String PROP_SHADOW_CLOCKHAND_CONTENT_KIND = "shadowClockhandKind";
    public static final String PROP_LOGO_TEXT = "logoText";
    public static final String PROP_CAPTURE_BACKGROUND = "captureBackground";
    public static final String PROP_COLORDESCRIPTOR = "colorDescriptor";
    private transient RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // the engine for drive the pointer of secundum
    private transient volatile TimerTick timerTick;
    // model
    private Calendar dataModel = null;
    private Calendar originDate = null;
    private Calendar shadowDataModel = null;
    // step in minutes
    private int stepInMinutes = 1;
    private ShadowClockhandContentKind shadowClockhandKind;
    //private boolean initialized = false;
    private Point centerOfClock = new Point();;
    //
    private int radius;
    // current time values
    private float currentHour = 0;
    private float currentMin = 0;
    private float currentSec = 0;
    private float currentMils = 0;
    private int hour = -1;
    private int minute = -1;
    private int secundum = -1;
    private int referenceHour = 0;
    private int referenceMin = 0;
    
    private int initHour = -1;
    private int initMin = -1;
    
    // colors
    private ColorDescriptor colorDescriptor = new ColorDescriptor();
    // length of pointers
    private double lengthOfSecPointer = 0f;
    private double lengthOfMinPointer = 0f;
    private double lengthOfHourPointer = 0f;
    // angle of pointer of secundum
    @SuppressWarnings("unused")
	private double baseAngleOfMouse = -1;
    // minute pointer for sign if a mouse is in the minute pointer area
    private Polygon minutePointerAsPolygon = new Polygon();
    // hour pointer for sign if a mouse is in the minute pointer area
    private Polygon hourPointerAsPolygon = new Polygon();
    private Polygon secPointerAsPolygon = new Polygon();
    private boolean isMousePointerInMotion = false;
    // cursor shapes
    private Cursor catchCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private boolean isCatchClockPointer = false;
    private boolean isTimeRun = false;
    private boolean isShowShadowTime = false;
    private boolean keepOriginDate = false;
    private boolean enableClockhandMoving = false;
    private boolean backgroundStored = false;
    private boolean captureBackground = false;
    private transient Image background;
    private TimeRunKind timeRunKind = TimeRunKind.DIGITAL;

    public void setTimeRunKind(TimeRunKind v) {
    	this.timeRunKind = v;
    }
    
    public TimeRunKind getTimeRunKind() {
    	return this.timeRunKind;
    }
    // set the default TimeFaceKind 
    private  TimeFaceKind timeFaceKind = TimeFaceKind.HOUR12;
    public void setTimeFaceKind(TimeFaceKind v) {
    	this.timeFaceKind = v;
    }
    
    public TimeFaceKind getTimeFaceKind() {
    	return this.timeFaceKind;
    }
    
    private int hourMarkerInDegree = 30;    
    private int hourIncrementInDegree = 6;

    
    protected int fontSize = 8;
    //protected Font clockFont = new Font("Arial", Font.PLAIN, fontSize);
    
    // java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());
    
    protected EventListenerList listenerList = new EventListenerList();
    
    /**
     *
     * Method: storeBackground
     */
    private void storeBackground() {
        try {
            Robot robot = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            this.background = robot.createScreenCapture(new Rectangle(0,0,(int)dim.getWidth(), (int)dim.getHeight()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     *
     * Method: restoreBackground
     * @param g
     */
    private void restoreBackground(Graphics g) {
        Point pos = this.getLocationOnScreen( );
        Point offset = new Point(-pos.x,-pos.y);
        g.drawImage(this.background, offset.x, offset.y, null);
    }
    
    /**
     * Adjust the brightness of the color
     * @param originColor Color
     * @param index double
     * @param darkest boolean
     * @return Color
     */
    private Color adjustBrightness(Color originColor, double index, boolean darkest) {
        float[] hsbModel = null;
        Color darkestColor = null;
        float hue = 0;
        float saturation = 0;
        float brightness = 0;
        try {
            hsbModel = new float[3];
            int red = originColor.getRed();
            int green = originColor.getGreen();
            int blue = originColor.getBlue();
            Color.RGBtoHSB(red, green, blue, hsbModel);
            // color "schema"
            hue = hsbModel[0];
            saturation = hsbModel[1];
            brightness = hsbModel[2];
            //Color.getHSBColor(hue, saturation, brightness);
            // adjust brightness
            if (darkest)
                brightness -= index;
            else
                brightness += index;
            // create adjusted color
            darkestColor = Color.getHSBColor(hue, saturation, brightness);
        } finally {
            // 2005.08.27.
            hsbModel = null;
            // 2006.01.07.
            originColor = null;
        }
        return darkestColor;
    }
    
    
    /**
     *
     * Method: xPolarToRectangular
     * @param centerX
     * @param dist
     * @param angle
     * @return
     */
    private int xPolarToRectangular(int centerX, double dist, double angle) {
        return centerX + (int) Math.round(dist * Math.sin(angle));
    }
    
    /**
     *
     * Method: yPolarToRectangular
     * @param centerY
     * @param dist
     * @param angle
     * @return
     */
    private int yPolarToRectangular(int centerY, double dist, double angle) {
        return centerY - (int) Math.round(dist * Math.cos(angle));
    }
    
    
    /**
     *
     * Method: radian2Degree
     * @param r double - radian
     * @return double - degree
     */
    private double radian2Degree(double r) {
        return ((180.0 / Math.PI) * r);
    }
    
    /**
     *
     * Method: degree2Radian
     * @param d double - degree
     * @return double - radian
     */
    private double degree2Radian(double d) {
        return ((Math.PI / 180.0) * d);
    }
    
    /**
     *
     * Method: coordinate2Degree
     * @param x int - coordinate x
     * @param y int - coordinate y
     * @return double - the degree of a line between orrigo to x and y positions
     *
    private double coordinate2Degree(int x, int y) {
        return radian2Degree(Math.atan2(y, x));
    }
    */
    
    /**
     *
     * Method: calculateCircleCoordinates
     * @param center Point - the center of the circle
     * @param radius Double - radius of circle
     * @param fi double - angle
     * @param p Point - container structure for output
     * @return Point - one point of circle
     *
    private Point calculateCircleCoordinates(Point center, double radius, double fi, Point p) {
        double circleX = radius * Math.sin(fi);
        double circleY = radius * Math.cos(fi);
        int toX = (int) (center.x + circleX);
        int toY = (int) (center.y + circleY);
        p.x = toX;
        p.y = toY;
        return p;
    }
    */
    
    /**
     *  Assign a time part of calendar to an other
     *
    private void assignTime(Calendar c0, Calendar c1) {
        c0.set(Calendar.AM_PM, c1.get(Calendar.AM_PM));
        c0.set(Calendar.HOUR, c1.get(Calendar.HOUR));
        c0.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
        c0.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
        c0.set(Calendar.SECOND, c1.get(Calendar.SECOND));
        c0.set(Calendar.MILLISECOND, c1.get(Calendar.MILLISECOND));
    }
    */

    /**
     *
     * Method: drawClockhand
     * @param g
     * @param handPolygon
     * @param handColor
     * @param length
     * @param width
     * @param backLength
     * @param angle
     */
    private void drawClockhand(Graphics g, Polygon handPolygon, Color handColor, int length, int width, int backLength, double angle) {
        Color backupColor = g.getColor();
        g.setColor(handColor);
        // add calculated points
        handPolygon.addPoint(xPolarToRectangular(this.centerOfClock.x, -backLength, angle), yPolarToRectangular(this.centerOfClock.y, -backLength, angle));
        handPolygon.addPoint(xPolarToRectangular(this.centerOfClock.x, width, angle + Math.PI / 2), yPolarToRectangular(this.centerOfClock.y, width, angle + Math.PI / 2));
        handPolygon.addPoint(xPolarToRectangular(this.centerOfClock.x, length, angle), yPolarToRectangular(this.centerOfClock.y, length, angle));
        handPolygon.addPoint(xPolarToRectangular(this.centerOfClock.x, width, angle - Math.PI / 2), yPolarToRectangular(this.centerOfClock.y, width, angle - Math.PI / 2));
        // draw clock hand
        g.drawPolygon(handPolygon);
        // fill clock hand
        g.fillPolygon(handPolygon);
        // draw border of clock hand
        g.setColor(adjustBrightness(handColor, 0.3, true));
        g.drawPolygon(handPolygon);
        g.setColor(backupColor);
    }
    
    /**
     *
     * Method: drawHand
     * @param g Graphics
     * @param degree double - degree
     * @param center Point - Center of clock
     * @param pointerLength double - length of pointer
     * @param colorOfPointer Color - color of pointer
     * @param minute boolean - true if minute pointer else false
     */
    private void drawHand(Graphics g, Polygon clockHandPolygon, Color handColor, int length, int width, int backLength, double degree, Point center) {
        double alfa = degree2Radian(degree);
        clockHandPolygon.reset();
        drawClockhand(g, clockHandPolygon, handColor, length, width, backLength, alfa);
    }
    
    /**
     *
     * Method: calculateSizeOfPointers
     */
    private void calculateSizeOfPointers() {
        this.lengthOfSecPointer = (radius / 100F) * 93F;
        this.lengthOfMinPointer = (radius / 100F) * 83F;
        this.lengthOfHourPointer = (radius / 100F) * 55F;
    }
    
    /**
     *
     * Method: calculatePositionOfPointers
     * @param center Point - center of clock
     */
    private void calculatePositionOfPointers(Point center) {
    	Calendar currentTime = Calendar.getInstance();
        // adjust
        if (this.isTimeRun) {
            if (this.initHour != currentTime.get(Calendar.HOUR_OF_DAY)) {
                this.initHour = currentTime.get(Calendar.HOUR_OF_DAY);
                this.dataModel.add(Calendar.HOUR_OF_DAY , 1);
                this.currentHour++;
                fireHourChanged(this.dataModel.get(Calendar.HOUR_OF_DAY));
            }
            
            if (this.initMin != currentTime.get(Calendar.MINUTE)) {
                this.initMin = currentTime.get(Calendar.MINUTE);
                this.dataModel.add(Calendar.MINUTE , 1);
                this.currentMin++;
                fireMinuteChanged(this.dataModel.get(Calendar.MINUTE));
            }
        	switch (this.timeRunKind) {
        	case DIGITAL : {
                this.currentHour = this.dataModel.get(Calendar.HOUR);
                this.currentMin = this.dataModel.get(Calendar.MINUTE);
                this.currentSec = currentTime.get(Calendar.SECOND);
                this.currentMils = currentTime.get(Calendar.MILLISECOND);
        		// logger.info("Digital Hours " + this.currentHour + " Mins " + this.currentMin + " Secs " + this.currentSec + " Mils " + this.currentMils);
        		break;
        	}
        	case ANALOG : {
                this.currentHour = this.dataModel.get(Calendar.HOUR);
                
                this.currentMils = currentTime.get(Calendar.MILLISECOND);
        		float mils = this.currentMils / 1000;
        		this.currentSec = currentTime.get(Calendar.SECOND) + mils;
        		this.currentMin = this.dataModel.get(Calendar.MINUTE) + (currentSec / 60);
        		// logger.info("ANALOG Hours " + this.currentHour + " Mins " + this.currentMin + " Secs " + this.currentSec + " Mils " + this.currentMils);
        		break;
        	}
        	}
        } else {
            this.currentHour = this.dataModel.get(Calendar.HOUR);
            this.currentMin = this.dataModel.get(Calendar.MINUTE);
            this.currentSec = this.dataModel.get(Calendar.SECOND);
        }
        
        // get the real hour and minute
        if (this.shadowClockhandKind.ordinal() == ShadowClockhandContentKind.SYSTEM_TIME.ordinal()) {
            this.referenceHour = currentTime.get(Calendar.HOUR);
            this.referenceMin = currentTime.get(Calendar.MINUTE);
        } else {
            this.referenceHour = this.shadowDataModel.get(Calendar.HOUR);
            this.referenceMin = this.shadowDataModel.get(Calendar.MINUTE);
        }
        
        currentTime = null;
        
        calculateSizeOfPointers();
    }
    
    /**
     *
     * Method: calculatePointerAngle
     * @param x double - x coordinate
     * @param y double - y coordinate
     * @return double - the angle of pointer
     */
    private double calculatePointerAngle(double x, double y) {
        double degree = radian2Degree(Math.atan2((y - this.centerOfClock.y), (x - this.centerOfClock.x)) + (Math.PI / 2));
        if (degree < 0)
            degree += 360f;
        return degree;
    }
    
    /**
     *
     * Method: drawHourhand
     * @param g Graphics
     */
    private void drawHourhand(Graphics g, float hr, float min, Color pointerColor) {
    	drawHand(g, this.hourPointerAsPolygon, pointerColor, (int)this.lengthOfHourPointer, (int)(0.075 * this.radius), (int)(0.1 * this.radius), (hr * this.hourMarkerInDegree + min / 2), this.centerOfClock);
    }
    
    /**
     *
     * Method: drawMinuteHand
     * @param g Graphics
     */
    private void drawMinuteHand(Graphics g, float min, Color pointerColor) {
        //drawPointer(g, (minute * 6), centerOfClock, lengthOfHourPointer, pointerColor, true);
    	drawHand(g, this.minutePointerAsPolygon, pointerColor, (int)this.lengthOfMinPointer, (int)(0.075 * this.radius), (int)(0.15 * this.radius), (min * 6), this.centerOfClock);
    }
    
    /**
     *
     * Method: drawSecundumHand
     * @param g
     * @param sec
     * @param pointerColor
     */
    private void drawSecundumHand(Graphics g, float sec, Color pointerColor) {
    	drawHand(g, this.secPointerAsPolygon, pointerColor, (int)this.lengthOfSecPointer, (int)(0.025 * this.radius), (int)(0.15 * this.radius), (sec * 6), this.centerOfClock);
    }
    
    /**
     *
     * Method: drawClockHands
     * @param g Graphics
     * @param center Point - center of clock
     */
    private void drawClockHands(Graphics g, Point center) {
        calculatePositionOfPointers(center);
        // draw the real time
        if (this.isShowShadowTime) {
        	drawHourhand(g, this.referenceHour, this.referenceMin, this.colorDescriptor.getShadowClockhandColor());
            drawMinuteHand(g, this.referenceMin, this.colorDescriptor.getShadowClockhandColor());
        }
        // draw the datamodel time
        drawHourhand(g, this.currentHour, this.currentMin, this.colorDescriptor.getHourClockhandColor());
        drawMinuteHand(g, this.currentMin, this.colorDescriptor.getHourClockhandColor());
        if (this.isTimeRun)
        	drawSecundumHand(g, this.currentSec, this.colorDescriptor.getSecundumClockhandColor());
    }
    
    /**
     *
     * Method: drawCircle
     * @param g Graphics
     * @param radius int - the radius of circle
     * @param center Point - center of circle
     *
    private void drawCircle(Graphics g, int radius, Point center) {
        Color backupColor = g.getColor();
        
        Point p = new Point();
        double startFi = 0;
        double stepFi = 0.05;
        p = calculateCircleCoordinates(center, radius, startFi, p);
        startFi += stepFi;
        int fromX = p.x;
        int fromY = p.y;
        int toX = 0;
        int toY = 0;
        for (double fi = startFi; fi < ((2 * Math.PI)); fi += stepFi) {
            g.setColor(this.colorDescriptor.getClockFaceColorForegroundColor());
            p = calculateCircleCoordinates(center, radius, fi, p);
            toX = p.x;
            toY = p.y;
            g.drawLine(fromX, fromY, p.x, p.y);
            // save prev pos
            fromX = p.x;
            fromY = p.y;
        }
        
        g.setColor(backupColor);
    }
    */
    
    /**
     *
     * Method: calculateVectorLength
     * @param beginx int - x coordinate the begin of vector
     * @param beginy int - y coordinate the begin of vector
     * @param endx int - x coordinate the end of vector
     * @param endy int - y coordinate the end of vector
     * @return double - the length of vector
     *
    private double calculateVectorLength(int beginx, int beginy, int endx, int endy) {
        double xb = beginx;
        double xe = endx;
        double yb = beginy;
        double ye = endy;
        return Math.sqrt(Math.pow((xe - xb), 2) + Math.pow((ye - yb), 2));
    }
    */
    /**
     *
     * Method: drawClockFaceNumber
     * @param g
     * @param text String - the number as text
     * @param x int - X coordinate of number
     * @param y int - Y coordinate of number
     */
    private void drawClockFaceNumber(Graphics g, String text, int x, int y) {
        Color backupColor = g.getColor();
        g.setColor(this.colorDescriptor.getClockFaceNumbersColor());
        FontMetrics fontMetrics = g.getFontMetrics();
        // y + distance from the baseline - (font height / 2)
        int locationY = y + fontMetrics.getAscent() - fontMetrics.getHeight() / 2;
        int locationX = x - fontMetrics.stringWidth(text) / 2;
        
        g.drawString(text, locationX, locationY);
        g.setColor(backupColor);
    }
    
    /**
     *
     * Method: drawLogo
     * @param g Graphics -
     * @param logo String - text of logo
     * @param x int - X coordinate of logo
     * @param y int - Y coordinate of logo
     */
    private void drawLogo(Graphics g, String logo, int x, int y) {
        Color backupColor = g.getColor();
        g.setColor(this.colorDescriptor.getLogoColor());
        FontMetrics fontMetrics = g.getFontMetrics();
        
        int locationY = y + fontMetrics.getAscent() - fontMetrics.getHeight() / 2;
        int locationX = x - fontMetrics.stringWidth(logo) / 2;
        
        g.drawString(logo, locationX, locationY);
        g.setColor(backupColor);
    }
    
    /**
     *
     * Method: drawAmPm
     * @param g Graphics -
     * @param ampm String - AM/PM text
     * @param x int - X coordinate of AM/PM sign
     * @param y int - Y coordinate of AM/PM sign
     */
    private void drawAmPm(Graphics g, String ampm, int x, int y) {
        Color backupColor = g.getColor();
        g.setColor(this.colorDescriptor.getAmpmColor());
        FontMetrics fontMetrics = g.getFontMetrics();
        //
        int locationY = y + fontMetrics.getAscent() - fontMetrics.getHeight() / 2;
        // center horizontally
        int locationX = x - fontMetrics.stringWidth(ampm) / 2;
        
        g.drawString(ampm, locationX, locationY);
        g.setColor(backupColor);
    }
    
    
    /**
     *
     * Method: drawClockFace
     * @param g Graphics
     * @param center Point - the center of clock
     */
    private void drawClockFace(Graphics g, Point center) {
        Graphics2D g2 = (Graphics2D)g;
        BasicStroke basicStroke = null;
        BasicStroke backupBasicStroke = null;
        double strokeValue = 1.0;
        
        boolean setStroke = false;
        
        Color backupColor = g.getColor();
        
        g.setColor(this.colorDescriptor.getClockFaceColorForegroundColor());
        
        int fontsize = this.radius / 7;
        
        if (fontsize < 1)
            fontsize = 1;
        g.setFont(new Font("SansSerif", Font.PLAIN, fontsize));
        
        // step in minutes
        for (int l = 0; l < 360; l += this.hourIncrementInDegree) {
            double radian = l * Math.PI / 180;
            
            backupBasicStroke = (BasicStroke) g2.getStroke();
            
            if (l % hourMarkerInDegree == 0) {
                if ((l == 0) || (l == 90) || (l == 180) || (l == 270)) {
                    strokeValue = 3.0;
                    setStroke = true;
                } else {
                    strokeValue = 1.5;
                    setStroke = false;
                }
                
                
                basicStroke = new BasicStroke((float) strokeValue);
                g2.setStroke(basicStroke);
                
                // every five minutes
                g.drawLine(xPolarToRectangular(this.centerOfClock.x, this.radius, radian), yPolarToRectangular(this.centerOfClock.y, this.radius, radian),  xPolarToRectangular(this.centerOfClock.x, 0.925 * this.radius, radian), yPolarToRectangular(this.centerOfClock.y, 0.925 * this.radius, radian));
                
                if (setStroke) {
                    basicStroke = new BasicStroke(1.0f);
                    g2.setStroke(basicStroke);
                }
                
                int currentHour = l / hourMarkerInDegree;
                if (l == 0) {
                	currentHour = timeFaceKind.getValue();
                }
                
                // draw a number
                drawClockFaceNumber(g, Integer.toString(currentHour), xPolarToRectangular(this.centerOfClock.x, 0.8 * this.radius, radian), yPolarToRectangular(this.centerOfClock.y, 0.8 * this.radius, radian));
                basicStroke = null;
            } else {
                basicStroke = new BasicStroke(0.5f);
                g2.setStroke(basicStroke);
                // every minutes
                g.drawLine(xPolarToRectangular(this.centerOfClock.x, this.radius, radian), yPolarToRectangular(centerOfClock.y, radius, radian), xPolarToRectangular(this.centerOfClock.x, 0.975 * this.radius, radian), yPolarToRectangular(this.centerOfClock.y, 0.975 * this.radius, radian));
                basicStroke = null;
            }
            g2.setStroke(backupBasicStroke);
        }
        double logoRad = 0 * Math.PI / 180;
        drawLogo(g, this.logoText, xPolarToRectangular(this.centerOfClock.x, 0.8 * this.radius, logoRad), yPolarToRectangular(this.centerOfClock.y, 0.8 * this.radius, logoRad) + (this.radius / 3));
        String ampm = null;
        if (this.dataModel.get(Calendar.AM_PM) == Calendar.AM)
            ampm = "AM";
        else
            ampm = "PM";
        drawAmPm(g, ampm, xPolarToRectangular(this.centerOfClock.x, 0.8 * this.radius, logoRad), yPolarToRectangular(this.centerOfClock.y, 0.8 * this.radius, logoRad) + (this.radius + (this.radius / 3)));
        g.setColor(backupColor);
    }
    
    /**
     *
     * Method: drawClockCenter
     * @param g Graphics -
     * @param center Point - Center of the clock
     */
    private void drawClockCenter(Graphics g, Point center) {
        Color backupColor = g.getColor();
        g.setColor(this.colorDescriptor.getClockFaceColorForegroundColor());
        // draw center
        int w = this.radius / (this.radius / 5);
        g.drawOval(center.x - (w / 2), center.y - (w / 2), w, w);
        g.setColor(backupColor);
    }
    
    /**
     *
     * Method: drawClockPlate
     * @param g Graphics
     * @param center Point - the center of clock
     */
    private void drawClockPlate(Graphics g, Point center) {
        Graphics2D g2 = (Graphics2D)g;
        BasicStroke basicStroke = null;
        BasicStroke backupBasicStroke = (BasicStroke) g2.getStroke();
        
        Color backupColor = g.getColor();
        g.setColor(this.colorDescriptor.getClockFaceColorForegroundColor());
        //drawCircle(g, 2, center);
        
        // draw border
        g.setColor(this.colorDescriptor.getBorderColor());
        basicStroke = new BasicStroke(5.0f);
        g2.setStroke(basicStroke);
        g.drawOval(center.x - this.radius, center.y - this.radius, (this.radius - 1) * 2, (this.radius - 1) * 2);
        basicStroke = null;
        g2.setStroke(backupBasicStroke);
        
        //fill the clock face with color
        g.setColor(this.colorDescriptor.getClockFaceColorBackgroundColor());
        g.fillOval(center.x - this.radius, center.y - this.radius, (this.radius - 1) * 2, (this.radius - 1) * 2);
        // draw numbers minute signs etc.
        drawClockFace(g, center);
        g.setColor(backupColor);
    }
    
    /**
     *
     * Method: calculateCenter
     */
    private void calculateCenter() {
        this.centerOfClock.x = this.getWidth() / 2;
        this.centerOfClock.y = this.getHeight() / 2;
    }
    
    /**
     *
     * Method: calculateRadius
     * @return
     */
    private int calculateRadius() {
        int r = 0;
        r = Math.min(this.getWidth(), this.getHeight());
        r /= 2;
        //r -= 7;
        return r;
    }
    
    /**
     *
     * Method: fireHourChanged
     * @param h int - hour
     */
    private void fireHourChanged(int h) {
        TimeChangeContainer timeChangeContainer = new TimeChangeContainer(this, TimeChangeKind.HOUR, h, getDataModel());
        TimeChangeEvent timeChangeEvent = new TimeChangeEvent(this, timeChangeContainer);
        fireTimeChanged(timeChangeEvent);
    }
    
    /**
     *
     * Method: fireMinuteChanged
     * @param min int - minute
     */
    private void fireMinuteChanged(int min) {
        TimeChangeContainer timeChangeContainer = new TimeChangeContainer(this, TimeChangeKind.MINUTE, min, getDataModel());
        TimeChangeEvent timeChangeEvent = new TimeChangeEvent(this, timeChangeContainer);
        fireTimeChanged(timeChangeEvent);
    }
    
    /**
     *
     * Method: fireTimeChanged
     * @param e TimeChangeEvent
     */
    private synchronized void fireTimeChanged(TimeChangeEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = this.listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == TimeChangeListener.class)
                ((TimeChangeListener)listeners[i + 1]).timeChanged(e);
        }
    }
    
    /**
     *
     * Method: setupParams
     * @param model Calendar - model
     * @param border Color - border color /clock face/
     * @param hourPointer Color - color of hour pointer
     * @param minPointer Color - color of minute pointer
     * @param secPointer Color - color of secundum pointer
     */
    private void setupParams(Calendar model, int stepInMin, boolean timeRun, boolean showShadowTime, boolean keepOriginDate, boolean enableClockhandMoving, boolean captureBackground, ShadowClockhandContentKind shadowClockhandKind, String logoText, ColorDescriptor colorDescr) {
        this.setName(COMP_NAME);
        if (model != null)
            this.dataModel = model;
        else
            this.dataModel = Calendar.getInstance();
        
        this.setFocusable(true);

        this.setSize(new Dimension(140, 165));
        this.setMinimumSize(new Dimension(140, 165));
        this.setPreferredSize(new Dimension(140, 165));
        this.setMaximumSize(new Dimension(1280, 1024));
        
        this.shadowDataModel = (Calendar)this.dataModel.clone();
        
        this.isTimeRun = timeRun;
        this.isShowShadowTime = showShadowTime;
        this.keepOriginDate = keepOriginDate;
        this.enableClockhandMoving = enableClockhandMoving;
        this.shadowClockhandKind = shadowClockhandKind;
        this.captureBackground = captureBackground;

        if (colorDescr == null)
            this.colorDescriptor =  ColorDescriptor.getInstance();
        else
            this.colorDescriptor = colorDescr;
        
        if (logoText != null)
            this.logoText = logoText;
        
        this.setOpaque(true);
        //this.setBackground(clockFaceColorBg);
        
        if (this.keepOriginDate) {
            this.originDate = (Calendar)this.dataModel.clone();
        }
        this.stepInMinutes = stepInMin;
        this.shadowClockhandKind = shadowClockhandKind;
        //initHour = dataModel.get(Calendar.HOUR_OF_DAY);
        //initMin = dataModel.get(Calendar.MINUTE);
        Calendar c = Calendar.getInstance();
        this.initHour = c.get(Calendar.HOUR_OF_DAY);
        this.initMin = c.get(Calendar.MINUTE);
        c = null;
        
        if (this.isTimeRun) {
            timerTick = new TimerTick(this);
            timerTick.start();
        }
        
        String helpText = "<html><head></head>";
        helpText += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"><style type=\"text/css\"><!--.styleL {font-size: 5px}--></style>";
        helpText += "<body>";
        
        helpText = "<html><head><title>Monxla's Analog Clock</title></head>";
        helpText += "<body bgcolor=\"F0FFF0\"><p><strong>Set the time</strong></p><blockquote>";
        helpText += "<p> -move clockwise/counterclockwise the minute clock hand this<br>";
        helpText += "<p>  increase/decrease the position of minute clock hand<br>";
        helpText += "<p>  /Copyright (C) 2005-2007 by Lï¿½szlï¿½ Kï¿½vï¿½ri laszlo.kovari@mail.tvnet.hu/<br>";
        helpText += "</p></blockquote></body></html>";
        
        setToolTipText(helpText);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        this.setFocusable(true);

        //initialized = true;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setupClockFace();
    }

    private void setupClockFace() {
        switch (this.timeFaceKind) {
        	case HOUR12: {
        		this.hourMarkerInDegree = 30;
        		this.hourIncrementInDegree = 6;
        		break;
        	}
        	case HOUR24: {
        		this.hourMarkerInDegree = 15;
        		this.hourIncrementInDegree = 3;
        		break;
        	}
        }    	
    }
    
    /**
     *
     * Method: isMinutesIsWholeMultiplierOfStep
     * @param currentTime
     * @param incrementInMin
     * @param stepInMins
     * @return
     */
    private boolean isMinutesIsWholeMultiplierOfStep(Calendar currentTime, int incrementInMin, int stepInMins) {
        long mils = currentTime.getTimeInMillis();
        long mins = (mils / 60000);
        mins += incrementInMin;
        return ((mins % stepInMins) == 0);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        /*
        arg0.setFont(clockFont);
        ((Graphics2D)arg0).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         */
        Graphics2D g2 = (Graphics2D) g;
        this.renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(this.renderingHints);
        
        super.paintComponent(g);
        calculateCenter();
        // need to capture background?
        if (this.captureBackground) {
            if (!this.backgroundStored) {
                storeBackground();
                this.backgroundStored = true;
            }
            restoreBackground(g);
        }
        this.radius = calculateRadius();
        drawClockPlate(g, this.centerOfClock);
        drawClockHands(g, this.centerOfClock);
        drawClockCenter(g, this.centerOfClock);
    }
    
    
    /**
     *
     * Overriden method
     * @see java.lang.Object#finalize()
     *
     */
    @Override
    protected void finalize() throws Throwable {
        this.removeMouseListener(this);
        this.removeMouseMotionListener(this);
        this.listenerList = null;
        // thread started?
        if (this.timerTick.isThreadRun())
            this.timerTick.setIsThreadRun(false);
        this.timerTick = null;
        this.centerOfClock = null;
        this.colorDescriptor = null;
        this.originDate = null;
        this.minutePointerAsPolygon = null;
        this.shadowDataModel = null;
    }
    
    public void resetShadowTime() {
        this.shadowDataModel.setTimeInMillis(this.dataModel.getTimeInMillis());
    }
    
    
    /**
     *
     * Method: getCaptureBackground
     * @return boolean - if true will capture the background and set it as clock background
     */
    public boolean getCaptureBackground() {
        return this.captureBackground;
    }
    
    /**
     *
     * Method: setCaptureBackground
     * @param captureBackground boolean - if true then capture the background and set it as clock background "pseudo transparent"
     */
    public void setCaptureBackground(boolean captureBackground) {
        this.captureBackground = captureBackground;
        if (this.captureBackground)
            this.backgroundStored = false;
    }
    
    /**
     *
     * Method: getIsMousePointerInMotion
     * @return boolean - true if the mouse is in motion
     */
    public boolean getIsMousePointerInMotion() {
        return this.isMousePointerInMotion;
    }
    
    /**
     *
     * Method: setTime
     * @param time Calendar - represent the time
     */
    public void setTime(Calendar time) {
        calculatePositionOfPointers(this.centerOfClock);
    }
    
    /**
     *
     * Method: addTimeChangeListener
     * @param l TimeChangeListener -
     */
    public synchronized void addTimeChangeListener(TimeChangeListener l) {
        this.listenerList.add(TimeChangeListener.class, l);
    }
    
    /**
     *
     * Method: removeTimeChangeListener
     * @param l TimeChangeListener -
     */
    public synchronized void removeTimeChangeListener(TimeChangeListener l) {
        this.listenerList.remove(TimeChangeListener.class, l);
    }
    
    /**
     *
     * Method: setThreadIsRun
     * @param threadIsRun boolean - if true is run else stop
     */
    public void setThreadIsRun(boolean isThreadRun) {
        if (isThreadRun) {
            // want to start
            if (!this.isTimeRun) {
                this.timerTick = new TimerTick(this);
                this.timerTick.run();
            }
            
        } else {
            // want to stop
            if (this.isTimeRun) {
                this.timerTick.setIsThreadRun(isThreadRun);
                this.timerTick = null;
            }
        }
    }
    
    /**
     *
     * Method: stop
     */
    public void stop() {
        if (this.timerTick != null) {
            this.timerTick.setIsThreadRun(false);
            this.timerTick = null;
        }
    }
    
    
    /**
     *
     * Method: getIsClockOperating
     * @return boolean - true if the clock is working /sec clock hand is showed and moving/
     */
    public boolean getIsClockOperating() {
        return this.timerTick.isThreadRun();
    }
    
    /**
     *
     * Method: getDataModel
     * @return Calendar - the data model
     */
    public Calendar getDataModel() {
        if (this.keepOriginDate) {
            this.dataModel.set(Calendar.YEAR, this.originDate.get(Calendar.YEAR));
            this.dataModel.set(Calendar.MONTH, this.originDate.get(Calendar.MONTH));
            this.dataModel.set(Calendar.DAY_OF_MONTH, this.originDate.get(Calendar.DAY_OF_MONTH));
        }
        return this.dataModel;
    }
    
    /**
     *
     * Method: setDataModel
     * @param dataModel Calendar - the data model
     */
    public void setDataModel(Calendar dataModel) {
        if (this.dataModel == null)
            this.dataModel = dataModel;
        else
            this.dataModel.setTimeInMillis(dataModel.getTimeInMillis());
        
        calculatePositionOfPointers(this.centerOfClock);
        this.repaint();
    }
    
    /**
     *
     * Method: getStepInMinutes
     * @return int - increase or decrease the minutes by this value
     */
    public int getStepInMinutes() {
        return this.stepInMinutes;
    }
    
    /**
     *
     * Method: setStepInMinutes
     * @param stepInMinutes int - increase or decrease the minutes by this value
     */
    public void setStepInMinutes(int stepInMinutes) {
        this.stepInMinutes = stepInMinutes;
        this.repaint();
    }
    
    /**
     * 
     * @return ColorDescriptor
     */
    public ColorDescriptor getColorDescriptor() {
        return this.colorDescriptor;
    }
    
    /**
     *
     * Method: setShadowClockhandKind
     * @param shadowClockhandKind ShadowClockhandContentKind - Enumerated, if set SYSTEM_TIME then the shadow clock hands represent the current /real time/. If set REFERENCE_TIME then the shadow clock hands represent the time before you move the clockhand of minute
     */
    public void setShadowClockhandKind(ShadowClockhandContentKind shadowClockhandKind) {
        this.shadowClockhandKind = shadowClockhandKind;
        this.repaint();
    }
    
    /**
     * 
     * @param value ColorDescriptor
     */
    public void setColorDescriptor(ColorDescriptor value) {
        this.colorDescriptor = value;
        this.repaint();
    }
    
    
    /**
     *
     * Method: isTimeRun
     * @return boolean - if true the the clockhand of secundum is moving, the clock is working else not
     */
    public boolean isTimeRun() {
        return this.isTimeRun;
    }
    
    /**
     *
     * Method: setIsTimeRun
     * @param timeRun boolean - if true the clockhand of secundum is moving, the clock is working else not
     */
    public void setTimeRun(boolean timeRun) {
        if (timeRun) {
            // to start
            if (!this.isTimeRun) {
                // not running -> start it
                this.timerTick = new TimerTick(this);
                this.timerTick.start();
            }
        } else {
            // to stop
            if (this.isTimeRun) {
                // running -> stop it
                this.timerTick.setIsThreadRun(timeRun);
                this.timerTick = null;
            }
        }
        this.isTimeRun = timeRun;
    }
    
    /**
     *
     * Method: isShowShadowTime
     * @return boolean - if true show the shadow pointers else not show
     */
    public boolean isShowShadowTime() {
        return this.isShowShadowTime;
    }
    
    /**
     *
     * Method: setIsShowShadowTime
     * @param showShadowTime boolean - if true show the shadow pointers which can represent the current time or the time before modify the time
     * it is depend from the ShadowClockhandContentKind
     */
    public void setShowShadowTime(boolean showShadowTime) {
        this.isShowShadowTime = showShadowTime;
        this.repaint();
    }
    
    /**
     *
     * Method: isKeepOriginDate
     * @return boolean - true if keep the origin date, else not keep
     */
    public boolean isKeepOriginDate() {
        return this.keepOriginDate;
    }
    
    /**
     *
     * Method: setIsKeepOriginDate
     * @param keepOriginDate boolean - keep the date of the data model when the set over 24 hours
     */
    public void setKeepOriginDate(boolean keepOriginDate) {
        this.keepOriginDate = keepOriginDate;
    }
    
    public boolean isEnableClockhandMoving() {
        return this.enableClockhandMoving;
    }
    
    public void setEnableClockhandMoving(boolean enableClockhandMoving) {
        this.enableClockhandMoving = enableClockhandMoving;
    }
    
    /**
     *
     * Method: getLogoText
     * @return String - text of logo
     */
    public String getLogoText() {
        return this.logoText;
    }
    
    /**
     *
     * Method: setLogoText
     * @param logoText String - text of logo
     */
    public void setLogoText(String logoText) {
        this.logoText = logoText;
        this.repaint();
    }
    
    /**
     *
     * Method: getHour
     * @return int - hour
     */
    public int getHour() {
        this.hour = this.dataModel.get(Calendar.HOUR_OF_DAY);
        return this.hour;
    }
    
    /**
     *
     * Method: setHour
     * @param hour int - hour
     */
    public void setHour(int hour) {
        this.hour = hour;
        this.dataModel.set(Calendar.HOUR_OF_DAY, this.hour);
    }
    
    /**
     *
     * Method: getMinute
     * @return int - minute
     */
    public int getMinute() {
        this.minute = this.dataModel.get(Calendar.MINUTE);
        return this.minute;
    }
    
    /**
     *
     * Method: setMinute
     * @param minute int - minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
        this.dataModel.set(Calendar.MINUTE, this.minute);
    }
    
    /**
     *
     * Method: getSecundum
     * @return int - secundum
     */
    public int getSecundum() {
        this.secundum = this.dataModel.get(Calendar.SECOND);
        return this.secundum;
    }
    
    /**
     *
     * Method: setSecundum
     * @param secundum - int secundum
     */
    public void setSecundum(int secundum) {
        this.secundum = secundum;
        this.dataModel.set(Calendar.SECOND, this.secundum);
    }
    
    public AnalogClock() {
        super();
        setupParams(null, 1, false, true, false, true, false, ShadowClockhandContentKind.REFERENCE_TIME, null, null);
    }
    
    public AnalogClock(Calendar model) {
        super();
        setupParams(model, 1, false, true, false, true, false, ShadowClockhandContentKind.REFERENCE_TIME, "by L.Kővári", null);
    }

    /**
     * 
     * @param model Calendar - data model
     * @param stepInMin int - increment or decrement the minute with this value
     * @param timeIsRunn boolean - if true then the clock functioning as an analog clock /moving the clock hand of secundum/
     * @param showShadowTime boolean - if true then show the shadow time, else not /shadow time is the 2nd. hour and minute clock hand/
     * @param keepOriginDate boolean - if true then keep the origin date if the time is over 24 hour, else increment/or decrement/ the day depend from the direction of modification
     * @param enableClockhandMoving boolean - if true then enable the catch of minute clock hand, and can moving it else not
     * @param captureBackground boolean - if true then enable the capture of background under the clock and copy to the clock background /pseudo transparent/ else not allow this
     * @param contentKind ShadowClockhandContentKind - SYSTEM_TIME = the shadow clock hands represent the real time, REFERENCE_TIME = the shadow clock hands represent the time before modify the time
     * @param logoText String - the text of logo
     * @param colorDescr ColorDescriptor - parameter class
     */
    public AnalogClock(Calendar model, int stepInMin, boolean timeIsRun, boolean showShadowTime, boolean keepOriginDate, boolean enableClockhandMoving, boolean captureBackground, ShadowClockhandContentKind contentKind, String logoText, ColorDescriptor colorDescr) {
        super();
        setupParams(model, stepInMin, timeIsRun, showShadowTime, keepOriginDate, enableClockhandMoving, captureBackground, contentKind, logoText, colorDescr);
    }
    
    /**
     *
     * Constructor AnalogClock
     * @param model Calendar - data model
     * @param stepInMin int - increment or decrement the minute with this value
     * @param timeIsRun boolean - if true then the clock functioning as an analog clock /moving the clock hand of secundum/
     * @param showShadowTime boolean - if true then show the shadow time, else not /shadow time is the 2nd. hour and minute clock hand/
     * @param keepOriginDate boolean - if true then keep the origin date if the time is over 24 hour, else increment/or decrement/ the day depend from the direction of modification
     * @param enableClockhandMoving boolean - if true then enable the catch of minute clock hand, and can moving it else not
     * @param captureBackground boolean - if true then enable the capture of background under the clock and copy to the clock background /pseudo transparent/ else not allow this
     * @param contentKind ShadowClockhandContentKind - SYSTEM_TIME = the shadow clock hands represent the real time, REFERENCE_TIME = the shadow clock hands represent the time before modify the time
     * @param logoText String - the text of logo
     * @param logoColor Color - color of logo text
     * @param ampmColor Color - color of AM/PM sign
     * @param shadowClockhandColor Color - color of shadow clock hands
     * @param faceNumbersColor Color - color of numbers on the clock face
     * @param clockFaceColorBg Color - color of background of clock face
     * @param clockFaceColorFg Color - color of foreground of clock face
     * @param hourClockhandColor Color - color of clockhand of hour
     * @param minuteClockhandColor Color - color of clockhand of minute
     * @param secClockhandColor Color - color of clockhand of secundum
     */
    public AnalogClock(Calendar model, int stepInMin, boolean timeIsRun, boolean showShadowTime, boolean keepOriginDate, boolean enableClockhandMoving, boolean captureBackground, ShadowClockhandContentKind contentKind, String logoText, ColorDescriptor colorDescr, LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        setupParams(model, stepInMin, timeIsRun, showShadowTime, keepOriginDate, enableClockhandMoving, captureBackground, contentKind, logoText, colorDescr);
    }
    
    /**
     *
     * Constructor AnalogClock
     * @param model Calendar - data model
     * @param stepInMin int - increment or decrement the minute with this value
     * @param timeIsRun boolean - if true then the clock functioning as an analog clock /moving the clock hand of secundum/
     * @param showShadowTime boolean - if true then show the shadow time, else not /shadow time is the 2nd. hour and minute clock hand/
     * @param keepOriginDate boolean - if true then keep the origin date if the time is over 24 hour, else increment/or decrement/ the day depend from the direction of modification
     * @param enableClockhandMoving boolean - if true then enable the catch of minute clock hand, and can moving it else not
     * @param captureBackground boolean - if true then enable the capture of background under the clock and copy to the clock background /pseudo transparent/ else not allow this
     * @param contentKind ShadowClockhandContentKind - SYSTEM_TIME = the shadow clock hands represent the real time, REFERENCE_TIME = the shadow clock hands represent the time before modify the time
     * @param logoText String - the text of logo
     * @param logoColor Color - color of logo text
     * @param ampmColor Color - color of AM/PM sign
     * @param shadowClockhandColor Color - color of shadow clock hands
     * @param faceNumbersColor Color - color of numbers on the clock face
     * @param clockFaceColorBg Color - color of background of clock face
     * @param clockFaceColorFg Color - color of foreground of clock face
     * @param hourClockhandColor Color - color of clockhand of hour
     * @param minuteClockhandColor Color - color of clockhand of minute
     * @param secClockhandColor Color - color of clockhand of secundum
     * @param layout LayoutManager -
     */
    public AnalogClock(Calendar model, int stepInMin, boolean timeIsRun, boolean showShadowTime, boolean keepOriginDate, boolean enableClockhandMoving, boolean captureBackground, ShadowClockhandContentKind contentKind, String logoText, ColorDescriptor colorDescr, LayoutManager layout) {
        super(layout, true);
        setupParams(model, stepInMin, timeIsRun, showShadowTime, keepOriginDate, enableClockhandMoving, captureBackground, contentKind, logoText, colorDescr);
    }
    
    /**
     *
     * Constructor AnalogClock
     * @param model Calendar - data model
     * @param stepInMin int - increment or decrement the minute with this value
     * @param timeIsRun boolean - if true then the clock functioning as an analog clock /moving the clock hand of secundum/
     * @param showShadowTime boolean - if true then show the shadow time, else not /shadow time is the 2nd. hour and minute clock hand/
     * @param keepOriginDate boolean - if true then keep the origin date if the time is over 24 hour, else increment/or decrement/ the day depend from the direction of modification
     * @param enableClockhandMoving boolean - if true then enable the catch of minute clock hand, and can moving it else not
     * @param captureBackground boolean - if true then enable the capture of background under the clock and copy to the clock background /pseudo transparent/ else not allow this
     * @param contentKind ShadowClockhandContentKind - SYSTEM_TIME = the shadow clock hands represent the real time, REFERENCE_TIME = the shadow clock hands represent the time before modify the time
     * @param logoText String - the text of logo
     * @param logoColor Color - color of logo text
     * @param ampmColor Color - color of AM/PM sign
     * @param shadowClockhandColor Color - color of shadow clock hands
     * @param faceNumbersColor Color - color of numbers on the clock face
     * @param clockFaceColorBg Color - color of background of clock face
     * @param clockFaceColorFg Color - color of foreground of clock face
     * @param hourClockhandColor Color - color of clockhand of hour
     * @param minuteClockhandColor Color - color of clockhand of minute
     * @param secClockhandColor Color - color of clockhand of secundum
     * @param isDoubleBuffered boolean -
     */
    public AnalogClock(Calendar model, int stepInMin, boolean timeIsRun, boolean showShadowTime, boolean keepOriginDate, boolean enableClockhandMoving, boolean captureBackground, ShadowClockhandContentKind contentKind, String logoText, ColorDescriptor colorDescr, boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setupParams(model, stepInMin, timeIsRun, showShadowTime, keepOriginDate, enableClockhandMoving, captureBackground, contentKind, logoText, colorDescr);
    }
    
    public void generateMouseEvent() {
        this.processMouseEvent(new MouseEvent(this, 500, Calendar.getInstance().getTimeInMillis(), 16, 1, 1, 1, false, 1));
    }
    
    /*
     *
     *
     *
     *  Events
     *
     *
     *
     */
    
    
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        if (!isTimeRun())
            this.repaint();
        
        if (this.enableClockhandMoving)
            if ((this.isMousePointerInMotion) && (this.isCatchClockPointer)) {
            double nearestMinute = 0;
            int minDelta = 0;
            Calendar currentTime = null;
            
            double angleOfMouse = Math.round(calculatePointerAngle(x, y));
            /*
            boolean clockwise = false;
            // for detect rotate direction
            if (angleOfMouse > baseAngleOfMouse)
                clockwise = true;
            else {
                clockwise = false;
             
                if (minutePointerAsPolygon.contains(x, y)) {
                    if (this.getCursor().getType() != Cursor.HAND_CURSOR) {
                        this.setCursor(catchCursor);
                        this.repaint();
                        catchClockPointer = true;
                    }
                }
                else {
                    this.setCursor(defaultCursor);
                    catchClockPointer = false;
                    isMousePointerInMotion = false;
                }
             
            }
             */
            
            currentTime = getDataModel();
            int currentMinute = currentTime.get(Calendar.MINUTE);
            if ((angleOfMouse % 6) == 0) {
                nearestMinute = (angleOfMouse / 6f);
                currentMinute = currentTime.get(Calendar.MINUTE);
                // which quater contain the current time?
                boolean firstQuaterCurrent = ((currentMinute >=0) && (currentMinute <= 15));
                boolean firstQuaterNearest = ((nearestMinute >=0) && (nearestMinute <= 15));
                boolean fourthQuaterCurrent = ((currentMinute >=45) && (currentMinute < 60));
                boolean fourthQuaterNearest = ((nearestMinute >=45) && (nearestMinute < 60));
                
                
                if (currentMinute == nearestMinute){
                    minDelta = 0;
                } else {
                    if ((currentMinute != 0) && (nearestMinute != 0)) {
                        if (fourthQuaterCurrent && firstQuaterNearest) {
                            minDelta = (int) ((60 - currentMinute) + nearestMinute);
                        } else
                            if (fourthQuaterNearest && firstQuaterCurrent) {
                            minDelta = (int) ((60 - nearestMinute) + currentMinute) * -1;
                            } else
                                if ((nearestMinute < currentMinute))
                                    minDelta = (int) ((currentMinute - nearestMinute) * -1);
                                else
                                    minDelta = (int) (nearestMinute - currentMinute);
                    } else {
                        if ((currentMinute == 0) || (nearestMinute == 0)) {
                            if ((fourthQuaterCurrent) && (nearestMinute == 0))
                                minDelta = (60 - currentMinute);
                            if ((fourthQuaterNearest) && (currentMinute == 0))
                                minDelta = (int) (60 - nearestMinute) * -1;
                            if ((firstQuaterNearest) && (currentMinute == 0))
                                minDelta = (int) (nearestMinute - currentMinute);
                            if ((firstQuaterCurrent) && (nearestMinute == 0))
                                minDelta = (int) (currentMinute - nearestMinute) * -1;
                        } else {
                            System.err.println("Mindelta " + minDelta + " currentMinute " + currentMinute + " nearestMinute " + nearestMinute);
                        }
                    }
                }
                /*
                if (nearestMinute < currentMinute)
                    minDelta = (int) ((60 - currentMinute) + nearestMinute);
                else
                    minDelta = (int) (nearestMinute - currentTime.get(Calendar.MINUTE));
                 */
                //System.out.println("Mindelta " + minDelta + " currentMinute " + currentMinute + " nearestMinute " + nearestMinute);
                //System.out.println("Angle " + angleOfMouse + " nearestMinute " + nearestMinute + " minDelta " + minDelta + " - " + currentTime.get(Calendar.HOUR_OF_DAY) + ":" + currentTime.get(Calendar.MINUTE));
                if (minDelta != 0) {
                    //
                    if (isMinutesIsWholeMultiplierOfStep(currentTime, minDelta, this.stepInMinutes)) {
                        currentTime.add(Calendar.MINUTE, minDelta);
                        //System.out.println(currentTime.get(Calendar.HOUR_OF_DAY) + ":" + currentTime.get(Calendar.MINUTE));
                        this.setTime(currentTime);
                        fireHourChanged(currentTime.get(Calendar.HOUR_OF_DAY));
                        fireMinuteChanged(currentTime.get(Calendar.MINUTE));
                    }
                }
            }
        }
    }
    
    /**
     *
     * Overriden method
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     *
     */
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // the pointer angle
        this.baseAngleOfMouse = Math.round(calculatePointerAngle(x, y));
        if (this.minutePointerAsPolygon.contains(x, y)) {
            if (this.enableClockhandMoving)
                if (this.getCursor().getType() != Cursor.HAND_CURSOR) {
                this.setCursor(this.catchCursor);
                this.repaint();
                this.isCatchClockPointer = true;
                }
        } else {
            this.setCursor(this.defaultCursor);
            this.isCatchClockPointer = false;
            this.isMousePointerInMotion = false;
        }
    }
    
    /**
     *
     * Overriden method
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     *
     */
    public void mouseClicked(MouseEvent e) {
        this.requestFocus();
    }
    
    /**
     *
     * Overriden method
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     *
     */
    public void mousePressed(MouseEvent e) {
        this.isMousePointerInMotion = true;
    }
    
    /**
     *
     * Overriden method
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     *
     */
    public void mouseReleased(MouseEvent e) {
        this.isMousePointerInMotion = false;
    }
    
    /**
     *
     * Overriden method
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     *
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     *
     * Overriden method
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     *
     */
    public void mouseExited(MouseEvent e) {
    }

}
