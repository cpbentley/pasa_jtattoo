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

package com.jtattoo.plaf;

import java.awt.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Michael Hagen
 */
public class LazyImageIcon implements Icon {

    private String name = null;
    private ImageIcon icon = null;

    public LazyImageIcon(String name) {
        this.name = name;
    }

    private Icon getIcon() {
        if (icon == null) {
            try {
                icon = new ImageIcon(LazyImageIcon.class.getResource(name));
            } catch (Throwable t) {
                System.out.println("ERROR: loading image " + name + " failed!");
            }
        }
        return icon;
    }

    public Image getImage() {
        if (getIcon() != null) {
            return icon.getImage();
        }
        return null;
    }
    
    @Override
    public int getIconHeight() {
        if (getIcon() != null) {
            return icon.getIconHeight();
        } else {
            return 16;
        }
    }

    @Override
    public int getIconWidth() {
        if (getIcon() != null) {
            return icon.getIconWidth();
        } else {
            return 16;
        }
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (getIcon() != null) {
            icon.paintIcon(c, g, x, y);
        } else {
            g.setColor(Color.red);
            g.fillRect(x, y, 16, 16);
            g.setColor(Color.white);
            g.drawLine(x, y, x + 15, y + 15);
            g.drawLine(x + 15, y, x, y + 15);
        }
    }

} // end of class LazyImageIcon
