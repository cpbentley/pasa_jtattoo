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
package com.jtattoo.plaf.aluminium;

import com.jtattoo.plaf.*;
import java.util.*;
import javax.swing.UIDefaults;

/**
 * @author Michael Hagen
 */
public class AluminiumLookAndFeel extends AbstractLookAndFeel {

    private static AluminiumDefaultTheme myTheme = null;

    private static final ArrayList themesList = new ArrayList();
    private static final HashMap themesMap = new HashMap();
    private static final Properties defaultProps = new Properties();
    private static final Properties smallFontProps = new Properties();
    private static final Properties largeFontProps = new Properties();
    private static final Properties giantFontProps = new Properties();

    static {
        smallFontProps.setProperty("controlTextFont", "Dialog 10");
        smallFontProps.setProperty("systemTextFont", "Dialog 10");
        smallFontProps.setProperty("userTextFont", "Dialog 10");
        smallFontProps.setProperty("menuTextFont", "Dialog 10");
        smallFontProps.setProperty("windowTitleFont", "Dialog bold 10");
        smallFontProps.setProperty("subTextFont", "Dialog 8");

        largeFontProps.setProperty("controlTextFont", "Dialog 14");
        largeFontProps.setProperty("systemTextFont", "Dialog 14");
        largeFontProps.setProperty("userTextFont", "Dialog 14");
        largeFontProps.setProperty("menuTextFont", "Dialog 14");
        largeFontProps.setProperty("windowTitleFont", "Dialog bold 14");
        largeFontProps.setProperty("subTextFont", "Dialog 12");

        giantFontProps.setProperty("controlTextFont", "Dialog 18");
        giantFontProps.setProperty("systemTextFont", "Dialog 18");
        giantFontProps.setProperty("userTextFont", "Dialog 18");
        giantFontProps.setProperty("menuTextFont", "Dialog 18");
        giantFontProps.setProperty("windowTitleFont", "Dialog 18");
        giantFontProps.setProperty("subTextFont", "Dialog 16");

        themesList.add("Default");
        themesList.add("Small-Font");
        themesList.add("Large-Font");
        themesList.add("Giant-Font");

        themesMap.put("Default", defaultProps);
        themesMap.put("Small-Font", smallFontProps);
        themesMap.put("Large-Font", largeFontProps);
        themesMap.put("Giant-Font", giantFontProps);
    }

    public static java.util.List getThemes() {
        return themesList;
    }

    public static Properties getThemeProperties(String name) {
        return ((Properties) themesMap.get(name));
    }

    public static void setTheme(String name) {
        setTheme((Properties) themesMap.get(name));
        if (myTheme != null) {
            AbstractTheme.setInternalName(name);
        }
    }

    public static void setTheme(String name, String licenseKey, String logoString) {
        Properties props = (Properties) themesMap.get(name);
        if (props != null) {
            props.put("licenseKey", licenseKey);
            props.put("logoString", logoString);
            setTheme(props);
            if (myTheme != null) {
                AbstractTheme.setInternalName(name);
            }
        }
    }

    public static void setTheme(Properties themesProps) {
        currentThemeName = "aluminiumTheme";
        if (myTheme == null) {
            myTheme = new AluminiumDefaultTheme();
        }
        if ((myTheme != null) && (themesProps != null)) {
            myTheme.setUpColor();
            myTheme.setProperties(themesProps);
            myTheme.setUpColorArrs();
            AbstractLookAndFeel.setTheme(myTheme);
        }
    }

    public static void setCurrentTheme(Properties themesProps) {
        setTheme(themesProps);
    }

    @Override
    public String getName() {
        return "Aluminium";
    }

    @Override
    public String getID() {
        return "Aluminium";
    }

    @Override
    public String getDescription() {
        return "The Aluminium Look and Feel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override
    public AbstractBorderFactory getBorderFactory() {
        return AluminiumBorderFactory.getInstance();
    }

    @Override
    public AbstractIconFactory getIconFactory() {
        return AluminiumIconFactory.getInstance();
    }

    @Override
    protected void createDefaultTheme() {
        if (myTheme == null) {
            myTheme = new AluminiumDefaultTheme();
        }
        setTheme(myTheme);
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        if (!"aluminiumTheme".equals(currentThemeName)) {
            setTheme("Default");
        }
        super.initClassDefaults(table);
        Object[] uiDefaults = {
            // BaseLookAndFeel classes
            "LabelUI", BaseLabelUI.class.getName(),
            "SeparatorUI", BaseSeparatorUI.class.getName(),
            "TextFieldUI", BaseTextFieldUI.class.getName(),
            "TextAreaUI", BaseTextAreaUI.class.getName(),
            "EditorPaneUI", BaseEditorPaneUI.class.getName(),
            "PasswordFieldUI", BasePasswordFieldUI.class.getName(),
            "ComboBoxUI", BaseComboBoxUI.class.getName(),
            "ToolTipUI", BaseToolTipUI.class.getName(),
            "TreeUI", BaseTreeUI.class.getName(),
            "TableUI", BaseTableUI.class.getName(),
            "TableHeaderUI", BaseTableHeaderUI.class.getName(),
            "ProgressBarUI", BaseProgressBarUI.class.getName(),
            "ScrollBarUI", BaseScrollBarUI.class.getName(),
            "FileChooserUI", BaseFileChooserUI.class.getName(),
            "MenuUI", BaseMenuUI.class.getName(),
            "PopupMenuUI", BasePopupMenuUI.class.getName(),
            "MenuItemUI", BaseMenuItemUI.class.getName(),
            "CheckBoxMenuItemUI", BaseCheckBoxMenuItemUI.class.getName(),
            "RadioButtonMenuItemUI", BaseRadioButtonMenuItemUI.class.getName(),
            // AluminiumLookAndFeel classes
            "CheckBoxUI", AluminiumCheckBoxUI.class.getName(),
            "RadioButtonUI", AluminiumRadioButtonUI.class.getName(),
            "ButtonUI", AluminiumButtonUI.class.getName(),
            "ToggleButtonUI", AluminiumToggleButtonUI.class.getName(),
            "SliderUI", AluminiumSliderUI.class.getName(),
            "PanelUI", AluminiumPanelUI.class.getName(),
            "ScrollPaneUI", AluminiumScrollPaneUI.class.getName(),
            "TabbedPaneUI", AluminiumTabbedPaneUI.class.getName(),
            "SplitPaneUI", AluminiumSplitPaneUI.class.getName(),
            "ToolBarUI", AluminiumToolBarUI.class.getName(),
            "MenuBarUI", AluminiumMenuBarUI.class.getName(),
            "PopupMenuSeparatorUI", AluminiumPopupMenuSeparatorUI.class.getName(),
            "InternalFrameUI", AluminiumInternalFrameUI.class.getName(),
            "RootPaneUI", AluminiumRootPaneUI.class.getName(),
            "DesktopPaneUI", AluminiumDesktopPaneUI.class.getName(),};
        table.putDefaults(uiDefaults);
        table.put("FormattedTextFieldUI", BaseFormattedTextFieldUI.class.getName());
        table.put("SpinnerUI", BaseSpinnerUI.class.getName());
    }
    
    @Override
    public List getMyThemes() {
       return themesList;
    }

    @Override
    public void setMyTheme(String theme) {
       setTheme(theme);
    }

} // end of class AluminiumLookAndFeel
