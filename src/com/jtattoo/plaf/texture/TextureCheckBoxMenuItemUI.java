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

import com.jtattoo.plaf.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

/**
 * @author Michael Hagen
 */
public class TextureCheckBoxMenuItemUI extends BaseCheckBoxMenuItemUI {

    public static ComponentUI createUI(JComponent c) {
        return new TextureCheckBoxMenuItemUI();
    }

    @Override
    protected void paintBackground(Graphics g, JComponent c, int x, int y, int w, int h) {
        JMenuItem mi = (JMenuItem) c;
        ButtonModel model = mi.getModel();
        if (model.isArmed() || (c instanceof JMenu && model.isSelected())) {
            TextureUtils.fillComponent(g, c, TextureUtils.ROLLOVER_TEXTURE_TYPE);
        } else {
            if (!(mi.getBackground() instanceof ColorUIResource)) {
                super.paintBackground(g, c, x, y, w, h);
            } else {
                TextureUtils.fillComponent(g, c, TextureUtils.MENUBAR_TEXTURE_TYPE);
            }
        }
    }

    @Override
    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        if (!AbstractLookAndFeel.getTheme().isDarkTexture()) {
            super.paintText(g, menuItem, textRect, text);
            return;
        }
        ButtonModel model = menuItem.getModel();
        FontMetrics fm = JTattooUtilities.getFontMetrics(menuItem, g, menuItem.getFont());
        int mnemIndex = menuItem.getDisplayedMnemonicIndex();
        if (!menuItem.isArmed()) {
            g.setColor(Color.black);
            JTattooUtilities.drawStringUnderlineCharAt(menuItem, g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent() - 1);
        }
        if (!model.isEnabled()) {
            // *** paint the text disabled
            g.setColor(ColorHelper.brighter(AbstractLookAndFeel.getDisabledForegroundColor(), 40));
        } else {
            // *** paint the text normally
            if (menuItem.isArmed()) {
                g.setColor(AbstractLookAndFeel.getMenuSelectionForegroundColor());
            } else {
                Color foreColor = menuItem.getForeground();
                if (foreColor instanceof UIResource) {
                    foreColor = AbstractLookAndFeel.getMenuForegroundColor();
                }
                g.setColor(foreColor);
            }
        }
        JTattooUtilities.drawStringUnderlineCharAt(menuItem, g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent());
    }
    
} // end of class TextureCheckBoxMenuItemUI
