/*
 * 
 * Copyright (c) 2005-2020 by László Kővári
 * Email laszlo.kovary@gmail.com
 * Author László Kővári
 * 
 * Project: AnalogClock
 * Package: com.lkovari.components.clock.test
 * File: AnalogClockTest.java
 * Created: Jan 7, 2006 1:11:18 PM
 * 
 * Description:
 *  Test of AnalogClock component
 * 
 */
package com.lkovari.components.clock.test;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import com.lkovari.components.clock.AnalogClock;
import com.lkovari.components.clock.ShadowClockhandContentKind;
import com.lkovari.components.clock.TimeRunKind;
import com.lkovari.components.clock.colors.ColorDescriptor;
import com.lkovari.components.clock.events.TimeChangeEvent;
import com.lkovari.components.clock.events.TimeChangeListener;
import com.lkovari.utilities.TimeUtilities;

public class AnalogClockTest extends JPanel implements TimeChangeListener, WindowListener {
    /**
     * Field <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6240240790335071607L;
    private AnalogClock analogClock = null;
    private Calendar dataModel = Calendar.getInstance();
    private Calendar selectedTime = Calendar.getInstance();
    private JLabel timeLabel = new JLabel("-");
    private boolean clockworking = true;
    
    public AnalogClockTest() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ColorDescriptor colorDescriptor = ColorDescriptor.getInstance();
        /*
         * Calendar model, 
         * int stepInMin, 
         * boolean timeIsRun, 
         * boolean showShadowTime, 
         * boolean keepOriginDate, 
         * boolean enableClockhandMoving, 
         * boolean captureBackground, 
         * ShadowClockhandContentKind contentKind, 
         * String logoText, 
         */
        analogClock = new AnalogClock(
                // model
                dataModel,
                // stepInMin
                1, 
                // timeIsRun
                clockworking,
                // showShadowTime
                false, 
                // keepOriginDate
                false, 
                // enableClockhandMoving
                true,
                // captureBackground
                false, 
                // contentKind
                ShadowClockhandContentKind.REFERENCE_TIME,
                // logoText
                "by L.Kővári",
                // Color descriptor
                colorDescriptor);
        //analogClock = new AnalogClock(dataModel, 1, clockworking, true, false, true, true, ShadowClockhandContentKind.SYSTEM_TIME, "Monxla", new Color(00, 141, 127), null, null, null, null, null, null, null, null);
        //analogClock = new AnalogClock();
        analogClock.addTimeChangeListener(this);
        //analogClock.setSize(new Dimension(400, 425));
        analogClock.setSize(new Dimension(140, 165));
        this.add(timeLabel, BorderLayout.NORTH);
        this.add(analogClock, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        JLabel label = new JLabel("Movement:");
        bottomPanel.add(label, BorderLayout.CENTER);
        ButtonGroup buttonGroup = new ButtonGroup();
        Arrays.asList(TimeRunKind.values()).forEach(timeRunKind -> {
            JRadioButton jRadioButton = new JRadioButton(timeRunKind.name());
            jRadioButton.setActionCommand(timeRunKind.name());
            if (timeRunKind.name() == "DIGITAL") {
            	jRadioButton.setSelected(true);	
            }
            jRadioButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	if (e.getActionCommand() == "DIGITAL") {
                		analogClock.setTimeRunKind(TimeRunKind.DIGITAL);
                	} else if (e.getActionCommand() == "ANALOG") {
                		analogClock.setTimeRunKind(TimeRunKind.ANALOG);
                	}
                }
            });
            buttonGroup.add(jRadioButton);
            bottomPanel.add(jRadioButton, BorderLayout.CENTER);
        });        
        this.add(bottomPanel, BorderLayout.PAGE_END);            
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        AnalogClockTest analogClockTest = new AnalogClockTest();
        mainFrame.addWindowListener(analogClockTest);
        mainFrame.getContentPane().add(analogClockTest);
        mainFrame.setPreferredSize(new Dimension(200, 200));
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setTitle("Analog clock test");
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    
    public void timeChanged(TimeChangeEvent timeChangeEvent) {
        if (timeChangeEvent.getSource() instanceof AnalogClock) {
            /*
            switch (timeChangeEvent.getTimeChangeContainer().getTimeChnageKind()) {
                case HOUR : {
                    selectedTime.set(Calendar.HOUR_OF_DAY, timeChangeEvent.getTimeChangeContainer().getValue());
                    break;
                }
                case MINUTE : {
                    selectedTime.set(Calendar.MINUTE, timeChangeEvent.getTimeChangeContainer().getValue());
                    break;
                }
            }
            */
            TimeUtilities.AssignDateAndTimeValues(selectedTime, analogClock.getDataModel());
            timeLabel.setText(TimeUtilities.Time2Text(selectedTime, "MM/dd/yyyy HH:mm a z"));
        }
    }
    

    public void windowOpened(WindowEvent e) {
        // method stub 
        
    }

    public void windowClosing(WindowEvent e) {
        analogClock.stop();
    }

    public void windowClosed(WindowEvent e) {
        // method stub 
        
    }

    public void windowIconified(WindowEvent e) {
        // method stub 
        
    }

    public void windowDeiconified(WindowEvent e) {
        // method stub 
        
    }

    public void windowActivated(WindowEvent e) {
        // method stub 
        
    }

    public void windowDeactivated(WindowEvent e) {
        // method stub 
        
    }
    
}