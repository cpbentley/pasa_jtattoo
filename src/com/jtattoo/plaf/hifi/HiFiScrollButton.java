/*
* Copyright (c) 2002 and later by MH Software-Entwicklung. All Rights Reserved.
*  
* JTattoo is multiple licensed. If your are an open source developer you can use
* it under the terms and conditions of the GNU General Public License version 2.0
* or later as published by the Free Software Foundation.
*  
* see: gpl-2.0.txt
* 
* If you pay for a license you will become a registered user who could use the
* software under the terms and conditions of the GNU Lesser General Public License
* version 2.0 or later with classpath exception as published by the Free Software
* Foundation.
* 
* see: lgpl-2.0.txt
* see: classpath-exception.txt
* 
* Registered users could also use JTattoo under the terms and conditions of the 
* Apache License, Version 2.0 as published by the Apache Software Foundation.
*  
* see: APACHE-LICENSE-2.0.txt
 */
package com.jtattoo.plaf.hifi;

import com.jtattoo.plaf.*;
import java.awt.Color;

/**
 * @author Michael Hagen
 */
public class HiFiScrollButton extends XPScrollButton {

    public HiFiScrollButton(int direction, int width) {
        super(direction, width);
    }

    @Override
    public Color getFrameColor() {
        Color frameColor = ColorHelper.brighter(AbstractLookAndFeel.getTheme().getButtonBackgroundColor(), 8);
        if (getModel().isPressed()) {
            return ColorHelper.darker(frameColor, 8);
        } else if (getModel().isRollover()) {
            return ColorHelper.brighter(frameColor, 16);
        } else {
            return frameColor;
        }
    }

} // end of class HiFiScrollButton
