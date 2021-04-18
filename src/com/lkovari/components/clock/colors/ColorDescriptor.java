/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.colorsk
 * File: ColorDescriptor.java
 * Created: Jan 5, 2006 3:23:32 PM
 * 
 * Description:
 * 
 * 
 */
package com.lkovari.components.clock.colors;

import java.awt.Color;

public class ColorDescriptor {
    public static final String PROP_HEADER_BG_COLOR = "headerBackgroundColor";
    public static final String PROP_HEADER_COLOR = "headerColor";
    public static final String PROP_BOODY_BG_COLOR = "boodyBackgroundColor";
    public static final String PROP_BOODY_COLOR = "boodyColor";
    public static final String PROP_FOOTER_BG_COLOR = "footerBackgroundColor";
    public static final String PROP_FOOTER_COLOR = "footerColor";
    public static final String PROP_CLOCKFACE_BG_COLOR = "clockFaceColorBackgroundColor";
    public static final String PROP_CLOCKFACE_FG_COLOR = "clockFaceColorForegroundColor";
    public static final String PROP_BORDER_COLOR = "borderColor";
    public static final String PROP_CLOCKFACENUMBERS_COLOR = "clockFaceNumbersColor";
    public static final String PROP_CLOCKHOURCLOCKHAND_COLOR = "hourClockhandColor";
    public static final String PROP_CLOCKMINUTECLOCKHAND_COLOR = "minuteClockhandColor";
    public static final String PROP_CLOCKSECUNDUMCLOCKHAND_COLOR = "secundumClockhandColor";
    public static final String PROP_CLOCKSHADOWCLOCKHAND_COLOR = "shadowClockhandColor";
    public static final String PROP_LOGO_COLOR = "logoColor";
    public static final String PROP_AMPM_COLOR = "ampmColor";
    public static final String PROP_SELECTED_COLOR = "selectedColor";
    public static final String PROP_NAVIGATOR_COLOR = "navigatorColor";
    public static final String PROP_TODAY_COLOR = "todayColor";
    public static final String PROP_WEAK_BG_COLOR = "weekBackgroundColor";
    public static final String PROP_ORIGIN_COLOR = "originColor";
    
    private Color headerBackgroundColor = new Color(0, 0, 160);
    private Color headerColor = Color.white;
    private Color boodyBackgroundColor = new Color(0, 0, 160);
    private Color boodyColor = Color.white;
    private Color footerBackgroundColor = new Color(0, 0, 160);
    private Color footerColor = Color.white;
    private Color clockFaceColorBackgroundColor = Color.black;
    private Color clockFaceColorForegroundColor = Color.white;
    private Color borderColor = Color.blue;
    private Color clockFaceNumbersColor = Color.cyan;
    private Color hourClockhandColor = Color.white;
    private Color minuteClockhandColor = Color.white;
    private Color secundumClockhandColor = Color.red;
    private Color shadowClockhandColor = new Color(168, 168, 255);
    private Color logoColor = new Color(0, 128, 128);
    private Color ampmColor = Color.yellow;
    private Color selectedColor = new Color(168, 168, 255);
    private Color navigatorColor = new Color(66, 66, 255);
    private Color todayColor = Color.blue;
    private Color weekBackgroundColor = new Color(0, 0, 160);
    private Color originColor = new Color(238, 238, 238);
    
    private static ColorDescriptor colorDescriptorInstance = null;
    
    public ColorDescriptor() {
    }
    
    public Color getOriginColor() {
        return this.originColor;
    }
    
    public void setOriginColor(Color originColor) {
        this.originColor = originColor;
    }
    
    public Color getWeekBackgroundColor() {
        return this.weekBackgroundColor;
    }
    
    public void setWeekBackgroundColor(Color value) {
        this.weekBackgroundColor = value;
    }
    
    public Color getTodayColor() {
        return this.todayColor;
    }
    
    public void setTodayColor(Color today) {
        this.todayColor = today;
    }
    
    public Color getBoodyColor() {
        return this.boodyColor;
    }
    
    public Color getAmpmColor() {
        return this.ampmColor;
    }
    
    public Color getBoodyBackgroundColor() {
        return this.boodyBackgroundColor;
    }
    
    public Color getBorderColor() {
        return this.borderColor;
    }
    
    public Color getClockFaceColorBackgroundColor() {
        return this.clockFaceColorBackgroundColor;
    }
    
    public Color getClockFaceColorForegroundColor() {
        return this.clockFaceColorForegroundColor;
    }
    
    public Color getClockFaceNumbersColor() {
        return this.clockFaceNumbersColor;
    }
    
    public Color getFooterColor() {
        return this.footerColor;
    }
    
    public Color getFooterBackgroundColor() {
        return this.footerBackgroundColor;
    }
    
    public Color getHeaderColor() {
        return this.headerColor;
    }
    
    public Color getHeaderBackgroundColor() {
        return this.headerBackgroundColor;
    }
    
    public Color getHourClockhandColor() {
        return this.hourClockhandColor;
    }
    
    public Color getLogoColor() {
        return this.logoColor;
    }
    
    public Color getMinuteClockhandColor() {
        return this.minuteClockhandColor;
    }
    
    public Color getNavigatorColor() {
        return this.navigatorColor;
    }
    
    public Color getSecundumClockhandColor() {
        return this.secundumClockhandColor;
    }
    
    public Color getSelectionColor() {
        return this.selectedColor;
    }
    
    public Color getShadowClockhandColor() {
        return this.shadowClockhandColor;
    }
    
    public void setAmpmColor(Color ampmColor) {
        this.ampmColor = ampmColor;
    }
    
    public void setBoodyColor(Color boody) {
        this.boodyColor = boody;
    }
    
    public void setBoodyBackgroundColor(Color value) {
        this.boodyBackgroundColor = value;
    }
    
    public void setBorderColor(Color value) {
        this.borderColor = value;
    }
    
    public void setClockFaceColorBackgroundColor(Color value) {
        this.clockFaceColorBackgroundColor = value;
    }
    
    public void setClockFaceColorForegroundColor(Color value) {
        this.clockFaceColorForegroundColor = value;
    }
    
    public void setClockFaceNumbersColor(Color clockFaceNumbersColor) {
        this.clockFaceNumbersColor = clockFaceNumbersColor;
    }

    public void setFooterColor(Color footer) {
        this.footerColor = footer;
    }
    
    public void setFooterBackgroundColor(Color footerBg) {
        this.footerBackgroundColor = footerBg;
    }
    
    public void setHeaderColor(Color header) {
        this.headerColor = header;
    }
    
    public void setHeaderBackgroundColor(Color headerBg) {
        this.headerBackgroundColor = headerBg;
    }
    
    public void setHourClockhandColor(Color hourClockhandColor) {
        this.hourClockhandColor = hourClockhandColor;
    }
    
    public void setLogoColor(Color logoColor) {
        this.logoColor = logoColor;
    }
    
    public void setMinuteClockhandColor(Color minuteClockhandColor) {
        this.minuteClockhandColor = minuteClockhandColor;
    }
    
    public void setNavigatorColor(Color navigator) {
        this.navigatorColor = navigator;
    }
    
    public void setSecundumClockhandColor(Color secundumClockhandColor) {
        this.secundumClockhandColor = secundumClockhandColor;
    }
    
    public void setSelectionColor(Color selected) {
        this.selectedColor = selected;
    }
    
    public void setShadowClockhandColor(Color shadowClockhandColor) {
        this.shadowClockhandColor = shadowClockhandColor;
    }

    public static ColorDescriptor getInstance() {
        if (colorDescriptorInstance == null)
            colorDescriptorInstance = new ColorDescriptor();
        return colorDescriptorInstance;
    }

}
