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
package com.jtattoo.plaf.graphite;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.BaseRadioButtonUI;
import java.awt.*;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * @author Michael Hagen
 */
public class GraphiteRadioButtonUI extends BaseRadioButtonUI {

    private static GraphiteRadioButtonUI radioButtonUI = null;

    public static ComponentUI createUI(JComponent c) {
        if (radioButtonUI == null) {
            radioButtonUI = new GraphiteRadioButtonUI();
        }
        return radioButtonUI;
    }

    @Override
    protected void paintFocus(Graphics g, Rectangle t, Dimension d) {
        g.setColor(AbstractLookAndFeel.getFocusColor());
        BasicGraphicsUtils.drawDashedRect(g, t.x - 3, t.y - 1, t.width + 6, t.height + 2);
        BasicGraphicsUtils.drawDashedRect(g, t.x - 2, t.y, t.width + 4, t.height);
    }

} // end of class GraphiteRadioButtonUI