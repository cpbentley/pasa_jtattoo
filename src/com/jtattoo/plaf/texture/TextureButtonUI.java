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
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * @author Michael Hagen
 */
public class TextureButtonUI extends BaseButtonUI {

    public static ComponentUI createUI(JComponent c) {
        return new TextureButtonUI();
    }

    @Override
    protected void paintBackground(Graphics g, AbstractButton b) {
        if (AbstractLookAndFeel.getTheme().doDrawSquareButtons()) {
            super.paintBackground(g, b);
        } else {
            Graphics2D g2D = (Graphics2D) g;
            Shape savedClip = g.getClip();
            if ((b.getBorder() != null) && b.isBorderPainted() && (b.getBorder() instanceof UIResource)) {
                int w = b.getWidth();
                int h = b.getHeight();
                Area clipArea = new Area(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 6, 6));
                clipArea.intersect(new Area(savedClip));
                g2D.setClip(clipArea);
            }
            super.paintBackground(g, b);
            g2D.setClip(savedClip);
        }
    }

    @Override
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
        AbstractButton b = (AbstractButton)c;
        Graphics2D g2D = (Graphics2D) g;
        Composite savedComposite = g2D.getComposite();
        if (!b.isContentAreaFilled()) {
            if (!b.isEnabled()) {
                AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
                g2D.setComposite(alpha);
            } else {
                AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f);
                g2D.setComposite(alpha);
            }
        }
        super.paintIcon(g, c, iconRect);
        g2D.setComposite(savedComposite);
    }

    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        Graphics2D g2D = (Graphics2D) g;
        Composite savedComposite = g2D.getComposite();
        ButtonModel model = b.getModel();
        FontMetrics fm = JTattooUtilities.getFontMetrics(b, g, b.getFont());
        int mnemIndex = b.getDisplayedMnemonicIndex();

        if (model.isEnabled()) {
            int offs = 0;
            if (model.isArmed() && model.isPressed()) {
                offs = 1;
            }
            Color foreground = b.getForeground();
            Color background = b.getBackground();
            if (background instanceof ColorUIResource) {
                if (model.isPressed() && model.isArmed()) {
                    foreground = AbstractLookAndFeel.getTheme().getPressedForegroundColor();
                } else if (model.isRollover()) {
                    foreground = AbstractLookAndFeel.getTheme().getRolloverForegroundColor();
                }
            }
            if (AbstractLookAndFeel.getTheme().isTextShadowOn() && ColorHelper.getGrayValue(foreground) > 164) {
                AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
                g2D.setComposite(alpha);
                g.setColor(Color.black);
                JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x + offs, textRect.y + offs + fm.getAscent() + 1);
                g2D.setComposite(savedComposite);
            } else {
                if (!(model.isPressed() && model.isArmed())) {
                    Object sc = b.getClientProperty("shadowColor");
                    if (sc instanceof Color) {
                        g.setColor((Color)sc);
                        JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x + 1, textRect.y + 1 + fm.getAscent());
                    }
                }
            }
            g.setColor(foreground);
            JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x + offs, textRect.y + offs + fm.getAscent());
        } else {
            AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
            g2D.setComposite(alpha);
            Color fc = b.getForeground();
            if (ColorHelper.getGrayValue(fc) > 164) {
                fc = ColorHelper.brighter(AbstractLookAndFeel.getDisabledForegroundColor(), 40);
                g.setColor(Color.black);
            } else {
                fc = AbstractLookAndFeel.getDisabledForegroundColor();
                g.setColor(Color.white);
            }
            JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x, textRect.y + 1 + fm.getAscent());
            g2D.setComposite(savedComposite);
            g.setColor(fc);
            JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent());
        }
    }

    @Override
    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        if (!AbstractLookAndFeel.getTheme().doShowFocusFrame()) {
            g.setColor(AbstractLookAndFeel.getFocusColor());
            BasicGraphicsUtils.drawDashedRect(g, 3, 2, b.getWidth() - 6, b.getHeight() - 5);
            BasicGraphicsUtils.drawDashedRect(g, 4, 3, b.getWidth() - 8, b.getHeight() - 7);
        }
    }

} // end of class TextureButtonUI


