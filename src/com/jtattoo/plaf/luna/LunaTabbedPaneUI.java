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
package com.jtattoo.plaf.luna;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.BaseTabbedPaneUI;
import java.awt.*;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

/**
 * author Michael Hagen
 */
public class LunaTabbedPaneUI extends BaseTabbedPaneUI {

    private static final Color[] SELECTED_TAB_COLOR = new Color[]{AbstractLookAndFeel.getBackgroundColor()};
    private static final Color[] SEP_COLOR = new Color[]{AbstractLookAndFeel.getControlDarkShadow()};

    public static ComponentUI createUI(JComponent c) {
        return new LunaTabbedPaneUI();
    }

    @Override
    public void installDefaults() {
        super.installDefaults();
        tabAreaInsets = new Insets(2, 6, 2, 6);
        contentBorderInsets = new Insets(0, 0, 0, 0);
    }

    @Override
    protected void installComponents() {
        simpleButtonBorder = true;
        super.installComponents();
    }

    @Override
    protected Font getTabFont(boolean isSelected) {
        if (isSelected) {
            return super.getTabFont(isSelected).deriveFont(Font.BOLD);
        } else {
            return super.getTabFont(isSelected);
        }
    }

    @Override
    protected Color[] getTabColors(int tabIndex, boolean isSelected, boolean isRollover) {
        if (isSelected && (tabPane.getBackgroundAt(tabIndex) instanceof UIResource)) {
            return SELECTED_TAB_COLOR;
        } else {
            return super.getTabColors(tabIndex, isSelected, isRollover);
        }
    }

    @Override
    protected Color[] getContentBorderColors(int tabPlacement) {
        return SEP_COLOR;
    }

    @Override
    protected boolean hasInnerBorder() {
        return true;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Color backColor = tabPane.getBackgroundAt(tabIndex);
        if (isSelected && (backColor instanceof UIResource)) {
            if (tabPane.getBackgroundAt(tabIndex) instanceof UIResource) {
                g.setColor(AbstractLookAndFeel.getBackgroundColor());
            } else {
                g.setColor(tabPane.getBackgroundAt(tabIndex));
            }
            switch (tabPlacement) {
                case TOP:
                    g.fillRect(x + 1, y + 1, w - 1, h + 2);
                    break;
                case LEFT:
                    g.fillRect(x + 1, y + 1, w + 2, h - 1);
                    break;
                case BOTTOM:
                    g.fillRect(x + 1, y - 2, w - 1, h + 2);
                    break;
                default:
                    g.fillRect(x - 2, y + 1, w + 2, h - 1);
                    break;
            }
        } else {
            super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            if (!isSelected && tabIndex == rolloverIndex && tabPane.isEnabledAt(tabIndex)) {
                g.setColor(AbstractLookAndFeel.getFocusColor());
                switch (tabPlacement) {
                    case TOP:
                        g.fillRect(x + 2, y + 1, w - 3, 2);
                        break;
                    case LEFT:
                        g.fillRect(x, y + 1, w - 1, 2);
                        break;
                    case BOTTOM:
                        g.fillRect(x + 2, y + h - 3, w - 3, 2);
                        break;
                    default:
                        g.fillRect(x, y + 1, w - 1, 2);
                        break;
                }
            }
        }
    }

} // end of class LunaTabbedPaneUI
