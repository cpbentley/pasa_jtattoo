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
 
package com.jtattoo.plaf.texture;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.XPScrollBarUI;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author  Michael Hagen
 */
public class TextureScrollBarUI extends XPScrollBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new TextureScrollBarUI();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        if (AbstractLookAndFeel.getTheme().isMacStyleScrollBarOn()) {
            return super.createDecreaseButton(orientation);
        } else {
            return new TextureScrollButton(orientation, scrollBarWidth);
        }
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        if (AbstractLookAndFeel.getTheme().isMacStyleScrollBarOn()) {
            return super.createIncreaseButton(orientation);
        } else {
            return new TextureScrollButton(orientation, scrollBarWidth);
        }
    }

} // end of class TextureRootPaneUI
