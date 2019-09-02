package pasa.cbentley.jtattoo;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.AbstractTheme;
import com.jtattoo.plaf.JTattooUtilities;

/**
 * Integrates Themes and JTattoo into a {@link JFrame} based application and its {@link JMenuBar}.
 * <br>
 * <p>
 * <b>Features</b>:
 * <li>Add Look and Feel Items to the {@link JMenu}.
 * <li>On demand loading when the user selects the Look and Feel Menu.
 * <li>Menu localization
 * <li>Favorite list: User can add or remove current look and feel in favorites. The list is saved in {@link Preferences}.
 * <li>Simple visual test method with {@link JavaSkinManager#main(String[])}
 * <br>
 * </p>
 * <br>
 * <p>
 * <b>How to use</b>:
 * <ol>
 * <li> Create a new instance with a {@link Preferences}
 * <li> Create a {@link JMenuBar}. <code> JMenuBar mb = new JMenuBar(); </code>. Add the menu bar to your {@link JFrame}
 * <li> Add the {@link JavaSkinManager#getRootMenu()} to the {@link JMenuBar}
 * <li> That's it!
 * </ol>
 * </p>
 * <p>
 * <b>String Localization</b>
 * <br>
 * By default, English Strings are hardcoded by default.<br>
 * A real application will call the {@link JavaSkinManager#guiUpdate(ResourceBundle)}.
 * <br>
 * <br>
 * The {@link ResourceBundle} must have the following keys
 * <li> {@link JavaSkinManager#BUNDLE_KEY_FAV}
 * <li> {@link JavaSkinManager#BUNDLE_KEY_FAV_ADD}
 * <li> {@link JavaSkinManager#BUNDLE_KEY_FAV_REMOVE}
 * <li> {@link JavaSkinManager#BUNDLE_KEY_MAIN_MENU}
 * <li> {@link JavaSkinManager#BUNDLE_KEY_OTHERS}
 * <li> {@link JavaSkinManager#BUNDLE_KEY_SYSTEM}
 * </p>
 * 
 * @author Charles Bentley
 * @version 1.0
 *
 */
public class JavaSkinManager implements IJavaSkinManager, ActionListener, MenuListener {

   /**
    * Current active Laf as an {@link Action}.
    * <br>
    * Will be initialized by {@link JavaSkinManager#populateLookAndFeelMenu(JMenu)}
    */
   private LafAction            currentAction;

   /**
    * Current action from the favorites. Null if current theme is not a favorite
    */
   private LafAction            currentActionFav;

   private String               currentLaf;

   /**
    * Metal;Hifi.Giant
    */
   private String               favoriteString;

   private Icon                 iconSelection;

   /**
    * Will keep the first look and feel from start.
    */
   private String               initLookClassName;

   private String               initThemeName;

   private JMenuItem            jmiFavAdd;

   private JMenuItem            jmiFavRemove;

   /**
    * 
    */
   private ButtonGroup          lafButtonGroup;

   private ButtonGroup          lafButtonGroupFav;

   private JMenu                menuFav;

   private JMenu                menuOthers;

   private JMenu                menuRoot;

   private JMenu                menuSystem;

   private ArrayList<LafAction> myLafActions = new ArrayList<>();

   private Preferences          prefsInit;

   /**
    * Upon creation, module looks for installed/accessible look and feels.
    * 
    */
   public JavaSkinManager(Preferences prefs) {
      //the very first thing we need to do is read the preference and set the look and feel
      prefsInit(prefs);

      //the initialization of menu must be done after the lookandfeel preference has been loaded
      //otherwise the first look for those menu will be metal
      menuRoot = new JMenu("Skins");
      menuRoot.addMenuListener(this); //if menu activated with the keyboard
      //when using the most common case.. use a thread to smooth out user experience
      menuRoot.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            //do this check in a SwingWorker
            checkPopulateRootMenu();
         }
      });

      //first build the menu for the system look and feels
      menuSystem = new JMenu("System");
      menuOthers = new JMenu("Others");
      setMenuFavorite(new JMenu("Favorites"));

      jmiFavRemove = new JMenuItem("Remove current skin from favorites");
      jmiFavRemove.addActionListener(this);

      jmiFavAdd = new JMenuItem("Add current skin to favorites");
      jmiFavAdd.addActionListener(this);

      getMenuFavorite().add(jmiFavAdd);
      getMenuFavorite().add(jmiFavRemove);
      getMenuFavorite().addSeparator();
      //list favorite here
      installSome();
   }

   /**
    * The LookAndFeel changes are processed by {@link LafAction#actionPerformed(ActionEvent)}
    * <br>
    */
   public void actionPerformed(ActionEvent e) {
      LafAction currentActionNow = getCurrentAction();
      if (e.getSource() == jmiFavAdd) {
         //get current
         if (currentActionNow != null) {
            //check if already in the list
            JMenuItem item = getActionFavMatch(currentActionNow);
            if (item == null) {
               //create a new lafaction with menufav as root menu
               LafAction la = new LafAction(this, currentActionNow.getLookAndFeelInfo(), currentActionNow.getTheme(), currentActionNow.getAbstractLookAndFeel(), getMenuFavorite());
               //not in the list
               JRadioButtonMenuItem rbm = new JRadioButtonMenuItem(la);
               getLafButtonGroupFav().add(rbm);
               getMenuFavorite().add(rbm);
               getMenuFavorite().setIcon(getIconSelection());
               //refresh
               getLafButtonGroupFav().clearSelection();
               rbm.setSelected(true);
               getMenuFavorite().repaint();
            }
         }
      } else if (e.getSource() == jmiFavRemove) {
         JMenuItem item = getActionFavMatch(currentActionNow);
         if (item != null) {
            getLafButtonGroupFav().remove(item);
            getLafButtonGroupFav().clearSelection();
            getMenuFavorite().remove(item);
            getMenuFavorite().setIcon(null);
            getMenuFavorite().repaint();
            //update the favorite string
         }
      }
   }

   /**
    * Create the {@link JRadioButtonMenuItem} for the LookAndFeel action.
    * <br>
    * @param lookAndFeelMenu
    * @param laf
    * @param lafClassName 
    */
   private void addRadioButton(UIManager.LookAndFeelInfo laf, String lafClassName) {
      String name = laf.getName();
      LafAction action = getAction(name, null);
      //remove this check in production
      if (action == null)
         throw new RuntimeException("Could not find Action for " + name);

      myLafActions.add(action);
      JRadioButtonMenuItem buttonLaf = new JRadioButtonMenuItem(action);
      if (lafClassName.equals(initLookClassName) && initThemeName == null) {
         action.getActionRootMenu().setIcon(getIconSelection());
         buttonLaf.setSelected(true);
         setCurrentAction(action);
      }
      lafButtonGroup.add(buttonLaf);
      action.getMenu().add(buttonLaf);
   }

   private void addSeparator(JMenu menu, String lafClassName, String lastLafClassName) {
      //insert the separator before adding the new laf
      int indexPoint = lafClassName.indexOf('.');
      if (indexPoint != -1) {
         indexPoint = lafClassName.indexOf('.', indexPoint + 1);
         if (indexPoint != -1) {
            String startOfNewLaf = lafClassName.substring(0, indexPoint);
            //System.out.println("lafClassName=" + lafClassName + " lastLafClassName=" + lastLafClassName + " startOfNewLaf=" + startOfNewLaf);
            if (lastLafClassName != null && !lastLafClassName.startsWith(startOfNewLaf)) {
               //add a separator for changing families of LAF
               menu.addSeparator();
            }
         }
      }
   }

   private void checkPopulateRootMenu() {
      if (menuRoot.getItemCount() == 0) {
         populateLookAndFeelMenu(menuRoot);
      }
   }

   /**
    * 
    * @param lafsInstalled
    */
   private void createActions(UIManager.LookAndFeelInfo[] lafsInstalled) {
      for (int i = 0; i < lafsInstalled.length; i++) {
         UIManager.LookAndFeelInfo laf = lafsInstalled[i];
         String lafClassName = laf.getClassName();
         if (lafClassName.startsWith("com.jtattoo.plaf")) {
            try {
               Class c = Class.forName(lafClassName);
               //create new instance
               AbstractLookAndFeel alf = (AbstractLookAndFeel) c.newInstance();
               List listThemes = alf.getMyThemes();
               JMenu menu = new JMenu(laf.getName());
               for (Iterator iterator = listThemes.iterator(); iterator.hasNext();) {
                  String theme = (String) iterator.next();
                  LafAction action = new LafAction(this, laf, theme, alf, menu);
                  myLafActions.add(action);
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         } else {
            JMenu menu = null;
            if (lafClassName.startsWith("javax.swing.plaf") || lafClassName.startsWith("com.sun.java")) {
               menu = menuSystem;
            } else {
               menu = menuOthers;
            }
            LafAction action = new LafAction(this, laf, menu);
            myLafActions.add(action);
         }
      }

   }

   private LafAction getAction(String lafName, String theme) {
      for (Iterator iterator = myLafActions.iterator(); iterator.hasNext();) {
         LafAction lafAction = (LafAction) iterator.next();
         if (lafAction.isMatch(lafName, theme)) {
            return lafAction;
         }
      }
      return null;
   }

   private JMenuItem getActionFavMatch(LafAction ac) {
      int num = getMenuFavorite().getItemCount();
      for (int i = 0; i < num; i++) {
         JMenuItem ji = getMenuFavorite().getItem(i);
         if (ji instanceof JRadioButtonMenuItem) {
            LafAction action = (LafAction) ji.getAction();
            if (ac.isMatch(action)) {
               return ji;
            }
         }
      }
      return null;
   }

   LafAction getCurrentAction() {
      return currentAction;
   }

   LafAction getCurrentActionFav() {
      return currentActionFav;
   }

   /**
    * Returns the current laf
    * @return
    */
   public String getCurrentLaf() {
      return currentLaf;
   }

   Icon getIconSelection() {
      return iconSelection;
   }

   ButtonGroup getLafButtonGroupFav() {
      return lafButtonGroupFav;
   }

   JMenu getMenuFavorite() {
      return menuFav;
   }

   /**
    * Returns the {@link JMenu} to be added to the {@link JMenuBar}.
    * <br>
    * There is nothing to do. The {@link MenuListener} is already working
    * @return
    */
   public JMenu getRootMenu() {
      return menuRoot;
   }

   /**
    * Optional. By default, all widgets have been initialized with English strings.
    * <br>
    * Valid bundle with the following keys
    * <li> {@link JavaSkinManager#BUNDLE_KEY_FAV}
    * <li> {@link JavaSkinManager#BUNDLE_KEY_FAV_ADD}
    * <li> {@link JavaSkinManager#BUNDLE_KEY_FAV_REMOVE}
    * <li> {@link JavaSkinManager#BUNDLE_KEY_MAIN_MENU}
    * <li> {@link JavaSkinManager#BUNDLE_KEY_OTHERS}
    * <li> {@link JavaSkinManager#BUNDLE_KEY_SYSTEM}
    * 
    * @param resBundle
    */
   public void guiUpdate(ResourceBundle resBundle) {
      menuRoot.setText(resBundle.getString(BUNDLE_KEY_MAIN_MENU));
      menuOthers.setText(resBundle.getString(BUNDLE_KEY_OTHERS));
      menuSystem.setText(resBundle.getString(BUNDLE_KEY_SYSTEM));
      getMenuFavorite().setText(resBundle.getString(BUNDLE_KEY_FAV));
      jmiFavAdd.setText(resBundle.getString(BUNDLE_KEY_FAV_ADD));
      jmiFavRemove.setText(resBundle.getString(BUNDLE_KEY_FAV_REMOVE));

   }

   /**
    * Put the LnF that work well here
    */
   private void installSome() {
      //jtattoos
      installTry("com.jtattoo.plaf.acryl.AcrylLookAndFeel", "Acryl");
      installTry("com.jtattoo.plaf.aero.AeroLookAndFeel", "Aero");
      installTry("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel", "Aluminium");
      installTry("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", "Bernstein");
      installTry("com.jtattoo.plaf.fast.FastLookAndFeel", "Fast");
      installTry("com.jtattoo.plaf.graphite.GraphiteLookAndFeel", "Graphite");
      installTry("com.jtattoo.plaf.hifi.HiFiLookAndFeel", "HiFi");
      installTry("com.jtattoo.plaf.luna.LunaLookAndFeel", "Luna");
      installTry("com.jtattoo.plaf.mcwin.McWinLookAndFeel", "McWin");
      installTry("com.jtattoo.plaf.mint.MintLookAndFeel", "Mint");
      installTry("com.jtattoo.plaf.noire.NoireLookAndFeel", "Noire");
      installTry("com.jtattoo.plaf.smart.SmartLookAndFeel", "Smart");
      installTry("com.jtattoo.plaf.texture.TextureLookAndFeel", "Texture");
   }

   private void installTry(String classPath, String title) {
      try {
         Class.forName(classPath);
         UIManager.installLookAndFeel(title, classPath);
      } catch (ClassNotFoundException e) {
      }
   }

   /**
    * Called last when populating the Skin root menu
    * <br>
    * Read favorites from preference string
    */
   private void loadFavorites() {
      if (favoriteString != null && !favoriteString.equals("")) {
         String[] str = favoriteString.split(";");
         if (str != null) {
            for (int i = 0; i < str.length; i++) {
               String lookTheme = str[i];
               LafAction actionFav = null;
               int indexDot = lookTheme.indexOf('.');
               if (indexDot == -1) {
                  LafAction lag = getAction(lookTheme, null);
                  //look up the existing
                  if (lag != null) {
                     actionFav = new LafAction(this, lag.getLookAndFeelInfo(), getMenuFavorite());
                  }
               } else {
                  //split theme
                  String laf = lookTheme.substring(0, indexDot);
                  String theme = lookTheme.substring(indexDot + 1, lookTheme.length());
                  LafAction actionRegular = getAction(laf, theme);
                  //look up the existing
                  if (actionRegular != null) {
                     actionFav = new LafAction(this, actionRegular.getLookAndFeelInfo(), theme, actionRegular.getAbstractLookAndFeel(), getMenuFavorite());
                  }
                  //check if init theme and the set cu
                  String className = actionRegular.getLookAndFeelInfo().getClassName();
                  if (className.equals(initLookClassName) && theme.equals(initThemeName)) {
                     setCurrentActionFav(actionFav);
                     getCurrentActionFav().getActionRootMenu().setIcon(getIconSelection());
                  }
               }
               JRadioButtonMenuItem rad = new JRadioButtonMenuItem(actionFav);
               if (getCurrentActionFav() == actionFav) {
                  rad.setSelected(true);
               }
               getLafButtonGroupFav().add(rad);
               getMenuFavorite().add(rad);
               //check if already
            }
         }
      }
   }

   public void menuCanceled(MenuEvent e) {
   }

   public void menuDeselected(MenuEvent e) {
   }

   public void menuSelected(MenuEvent e) {
      if (e.getSource() == menuRoot) {
         checkPopulateRootMenu();
      }
   }

   /**
    * A {@link JMenuBar} will list a JMenu and call this method only when the user selects
    * the menu. Use {@link MenuListener#menuSelected(MenuEvent)}
    * @param lookAndFeelMenu
    */
   private void populateLookAndFeelMenu(JMenu lookAndFeelMenu) {
      UIManager.LookAndFeelInfo[] lafsInstalled = UIManager.getInstalledLookAndFeels();

      createActions(lafsInstalled);

      //add the favorite menu
      lookAndFeelMenu.add(getMenuFavorite());
      lookAndFeelMenu.add(menuSystem);

      lafButtonGroup = new ButtonGroup(); //only 1 LAF button can be selected at a time
      setLafButtonGroupFav(new ButtonGroup());

      String lastLafClassName = null;
      for (int i = 0; i < lafsInstalled.length; i++) {
         UIManager.LookAndFeelInfo laf = lafsInstalled[i];
         String lafClassName = laf.getClassName();
         String lafName = laf.getName();

         addSeparator(lookAndFeelMenu, lafClassName, lastLafClassName);
         //System.out.println("LAF Class=" + lafClassName + " = " + lafName);

         //load jtatto themes
         if (lafClassName.startsWith("com.jtattoo.plaf")) {
            try {
               Class c = Class.forName(lafClassName);
               AbstractLookAndFeel alf = (AbstractLookAndFeel) c.newInstance();
               List listThemes = alf.getMyThemes();
               JMenu menu = null;
               int count = 0;
               //all jtattoo themes have several themes
               for (Iterator iterator = listThemes.iterator(); iterator.hasNext();) {
                  String theme = (String) iterator.next();
                  JRadioButtonMenuItem buttonTheme = new JRadioButtonMenuItem(theme);
                  lafButtonGroup.add(buttonTheme);

                  //get the action
                  LafAction action = getAction(lafName, theme);
                  menu = action.getActionRootMenu();
                  if (lafClassName.equals(initLookClassName) && theme.equals(initThemeName)) {
                     menu.setIcon(getIconSelection());
                     buttonTheme.setSelected(true);
                     setCurrentAction(action);
                  }
                  menu.add(buttonTheme);
                  buttonTheme.setAction(action);
                  myLafActions.add(action);
                  //add a separator every 4
                  count++;
                  if (count % 4 == 0) {
                     menu.addSeparator();
                  }
               }
               lookAndFeelMenu.add(menu);
            } catch (ClassNotFoundException e) {
               e.printStackTrace();
            } catch (InstantiationException e) {
               e.printStackTrace();
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            }
         } else {
            addRadioButton(laf, lafClassName);
         }

         lastLafClassName = lafClassName;
      }

      if (menuOthers.getItemCount() != 0) {
         lookAndFeelMenu.add(menuOthers);
      }
      loadFavorites();

   }

   private void prefsInit(Preferences prefs) {
      if (prefs == null)
         return;
      prefsInit = prefs;
      String lookFeelClassName = prefs.get(PREF_LOOKANDFEEL, "");
      String lookFeelTheme = prefs.get(PREF_LOOKANDFEEL_THEME, "");
      favoriteString = prefs.get(PREF_LOOKANDFEEL_FAV, "");

      if (!lookFeelClassName.equals("")) {
         try {
            if (lookFeelClassName.startsWith("com.jtattoo.plaf")) {
               if (!lookFeelTheme.equals("")) {
                  Class c = Class.forName(lookFeelClassName);
                  AbstractLookAndFeel alf = (AbstractLookAndFeel) c.newInstance();
                  List listThemes = alf.getMyThemes();
                  for (Iterator iterator = listThemes.iterator(); iterator.hasNext();) {
                     String theme = (String) iterator.next();
                     if (theme.equals(lookFeelTheme)) {
                        initThemeName = lookFeelTheme;
                        System.out.println("Restoring Theme " + lookFeelTheme);
                        alf.setMyTheme(lookFeelTheme);
                        break;
                     }
                  }
               }
            }
            initLookClassName = lookFeelClassName;
            System.out.println("Restoring Look and Feel " + lookFeelClassName);
            UIManager.setLookAndFeel(lookFeelClassName);

            //if success here create the current Laf
            LookAndFeel laf = UIManager.getLookAndFeel();
            UIManager.LookAndFeelInfo lafi = new UIManager.LookAndFeelInfo(laf.getName(), lookFeelClassName);
            setCurrentAction(new LafAction(this, lafi, lookFeelTheme, null, null));
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   /**
    * Save LnF current state using {@link Preferences} object loaded at start.
    */
   public void prefsSave() {
      prefsSave(prefsInit);
   }

   public void prefsSave(Preferences prefs) {
      if (prefs == null) {
         return;
      }
      String value = UIManager.getLookAndFeel().getClass().getName();
      LookAndFeel laf = UIManager.getLookAndFeel();
      prefs.put(PREF_LOOKANDFEEL, value);
      if (laf instanceof AbstractLookAndFeel) {
         ////////// WARNING TO READER! NEVER USE STATIC VARIABLES. Its a mistake that bites in the future.
         AbstractLookAndFeel alf = (AbstractLookAndFeel) laf;
         AbstractTheme at = alf.getTheme(); //must be called on instance. ignore IDE warning
         String name = at.getInternalName(); //must be called on instance. ignore IDE warning
         ////////// All this ugly code because of static variables
         System.out.println("Saving Look and Feel " + value + " " + name);
         prefs.put(PREF_LOOKANDFEEL_THEME, name);
      } else {
         prefs.put(PREF_LOOKANDFEEL_THEME, "");
      }
      //do the favorites
      int num = getMenuFavorite().getItemCount();
      StringBuilder sb = new StringBuilder(100);
      for (int i = 0; i < num; i++) {
         JMenuItem ji = getMenuFavorite().getItem(i);
         if (ji instanceof JRadioButtonMenuItem) {
            LafAction action = (LafAction) ji.getAction();
            sb.append(action.getLookAndFeelInfo().getName());
            if (action.getTheme() != null) {
               sb.append('.');
               sb.append(action.getTheme());
            }
            sb.append(';');
         }
      }
      String favString = sb.toString();
      System.out.println("Saving Favorites as " + favString);
      prefs.put(PREF_LOOKANDFEEL_FAV, favString);
   }

   /**
    * Tries to set the new look and feel.
    * 
    * Method will update the Component Tree.
    * 
    * @param className could be null
    */
   public void setApplicationLookAndFeel(String className) {
      if (className != null) {
         try {
            LookAndFeel oldLAF = UIManager.getLookAndFeel();
            boolean oldDecorated = false;
            if (oldLAF instanceof MetalLookAndFeel) {
               oldDecorated = true;
            }
            if (oldLAF instanceof AbstractLookAndFeel) {
               oldDecorated = AbstractLookAndFeel.getTheme().isWindowDecorationOn();
            }
            JFrame.setDefaultLookAndFeelDecorated(false);
            JDialog.setDefaultLookAndFeelDecorated(false);
            UIManager.setLookAndFeel(className);
            currentLaf = className;

            LookAndFeel newLAF = UIManager.getLookAndFeel();
            boolean newDecorated = false;
            if (newLAF instanceof MetalLookAndFeel) {
               newDecorated = true;
            }
            if (newLAF instanceof AbstractLookAndFeel) {
               newDecorated = AbstractLookAndFeel.getTheme().isWindowDecorationOn();
            }
            if (oldDecorated != newDecorated) {
               // Reboot the application
               System.out.println("Reboot the application " + oldLAF.getName() + " " + oldDecorated + " new " + newLAF.getName() + " " + newDecorated);
            }
            updateComponentTree();
         } catch (Exception e) {
            e.printStackTrace();
         }

         updateComponentTree();
      }
   }

   void setCurrentAction(LafAction currentAction) {
      this.currentAction = currentAction;
   }

   void setCurrentActionFav(LafAction currentActionFav) {
      this.currentActionFav = currentActionFav;
   }

   /**
    * Sets the icon that will be displayed next to the selected look and feel menu.
    * <br>
    * @param icon
    */
   public void setIconSelected(Icon icon) {
      this.setIconSelection(icon);
   }

   void setIconSelection(Icon iconSelection) {
      this.iconSelection = iconSelection;
   }

   void setLafButtonGroupFav(ButtonGroup lafButtonGroupFav) {
      this.lafButtonGroupFav = lafButtonGroupFav;
   }

   void setMenuFavorite(JMenu menuFav) {
      this.menuFav = menuFav;
   }

   /**
    * Called when a regular Skin is changed. Look up the favorites
    * and if its inside.. select it in the group
    */
   public void syncFav(LafAction currentAction) {
      int num = getMenuFavorite().getItemCount();
      for (int i = 0; i < num; i++) {
         JMenuItem ji = getMenuFavorite().getItem(i);
         //a simple reference check is not sufficient since fav skins have their own action.
         if (ji instanceof JRadioButtonMenuItem) {
            LafAction actionFav = (LafAction) ji.getAction();
            if (currentAction.isMatch(actionFav)) {
               ji.setSelected(true);
               setCurrentActionFav(actionFav);
               actionFav.getActionRootMenu().setIcon(getIconSelection());
               break;
            }
         }

      }
   }

   /**
    * Called when a fav item has been selected
    */
   public void syncReal(LafAction favAction) {
      int countI = menuRoot.getItemCount();
      for (int i = 0; i < countI; i++) {
         JMenuItem ji = menuRoot.getItem(i);
         if (ji != getMenuFavorite()) {
            if (ji instanceof JMenu) {
               JMenu m = ((JMenu) ji);
               int countK = m.getItemCount();
               boolean isSelected = false;
               for (int k = 0; k < countK; k++) {
                  JMenuItem jik = m.getItem(k);
                  if (jik != null) {
                     Action ac = jik.getAction();
                     if (ac != null && ac instanceof LafAction) {
                        LafAction actionReg = (LafAction) ac;
                        if (favAction.isMatch(actionReg)) {
                           setCurrentAction(actionReg);
                           jik.setSelected(true);
                           m.setIcon(getIconSelection());
                           isSelected = true;
                        }
                     }
                  }
               }
               //remove the others
               if (!isSelected && m.getIcon() != null) {
                  m.setIcon(null);
               }
            }
         }
      }
   }

   /**
    * 
    */
   private void updateComponentTree() {
      // Update the application
      if (JTattooUtilities.getJavaVersion() >= 1.6) {
         Window windows[] = Window.getWindows();
         for (int i = 0; i < windows.length; i++) {
            if (windows[i].isDisplayable()) {
               SwingUtilities.updateComponentTreeUI(windows[i]);
            }
         }
      } else {
         Frame frames[] = Frame.getFrames();
         for (int i = 0; i < frames.length; i++) {
            if (frames[i].isDisplayable()) {
               SwingUtilities.updateComponentTreeUI(frames[i]);
            }
         }
      }
   }

}
