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
import java.awt.Graphics;
import javax.swing.JRootPane;

/**
 * @author Michael Hagen
 */
public class HiFiTitlePane extends BaseTitlePane {

    public HiFiTitlePane(JRootPane root, BaseRootPaneUI ui) {
        super(root, ui);
    }

    @Override
    protected boolean centerButtons() {
        return false;
    }

    @Override
    public void paintText(Graphics g, int x, int y, String title) {
        g.setColor(Color.black);
        JTattooUtilities.drawString(rootPane, g, title, x + 1, y + 1);
        if (isActive()) {
            g.setColor(AbstractLookAndFeel.getWindowTitleForegroundColor());
        } else {
            g.setColor(AbstractLookAndFeel.getWindowInactiveTitleForegroundColor());
        }
        JTattooUtilities.drawString(rootPane, g, title, x, y);
    }

    @Override
    protected void paintBorder(Graphics g) {
    }

} // end of class HiFiTitlePane
