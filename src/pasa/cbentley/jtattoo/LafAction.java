package pasa.cbentley.jtattoo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.UIManager;

import com.jtattoo.plaf.AbstractLookAndFeel;

/**
 * Each Theme menu items will be represented by a {@link LafAction}.
 * 
 * @author Charles Bentley
 *
 */
public class LafAction extends AbstractAction {

   /**
    * 
    */
   private static final long         serialVersionUID = -2891206240367644231L;

   private AbstractLookAndFeel       alf;

   /**
    * 
    */
   private final JavaSkinManager     javaSkinManager;

   private UIManager.LookAndFeelInfo laf;

   private JMenu                     menu;

   private String                    theme;

   /**
    * 
    * @param laf
    * @param menu the {@link JMenu} that will hold this action
    * @param javaSkinManager The owner of this {@link LafAction}
    * @throws NullPointerException if laf is null
    */
   public LafAction(JavaSkinManager javaSkinManager, UIManager.LookAndFeelInfo laf, JMenu menu) {
      super(laf.getName());
      this.javaSkinManager = javaSkinManager;
      this.setLookAndFeelInfo(laf);
      this.setMenu(menu);
   }

   /**
    * 
    * @param laf
    * @param theme theme String
    * @param alf the {@link AbstractLookAndFeel}
    * @param menu the {@link JMenu} that will hold this action
    * @param javaSkinManager The owner of this {@link LafAction}
    * @throws NullPointerException if laf is null
    */
   public LafAction(JavaSkinManager javaSkinManager, UIManager.LookAndFeelInfo laf, String theme, AbstractLookAndFeel alf, JMenu menu) {
      super(laf.getName() + (theme != null ? (" " + theme) : ""));
      this.javaSkinManager = javaSkinManager;
      this.setLookAndFeelInfo(laf);
      this.setTheme(theme);
      this.setAbstractLookAndFeel(alf);
      this.setMenu(menu);
   }

   /**
    * 
    */
   public void actionPerformed(ActionEvent e) {
      //System.out.println("LafAction#actionPerformed " + theme);
      if (getTheme() != null && getAbstractLookAndFeel() != null) {
         getAbstractLookAndFeel().setMyTheme(getTheme());
      }
      this.javaSkinManager.setApplicationLookAndFeel(getLookAndFeelInfo().getClassName());
      JMenu newMenuSelected = getActionRootMenu();
      //clear menu of current action
      LafAction currentAction = this.javaSkinManager.getCurrentAction();
      if (currentAction != null) {
         currentAction.getActionRootMenu().setIcon(null);
      }
      LafAction currentActionFav = this.javaSkinManager.getCurrentActionFav();
      if (currentActionFav != null) {
         currentActionFav.getActionRootMenu().setIcon(null);
         //also deselects the radio button

      }
      //there might be several
      if (newMenuSelected != null) {
         newMenuSelected.setIcon(this.javaSkinManager.getIconSelection());
      }
      //iterate over all menu and set the right icons.
      if (newMenuSelected == this.javaSkinManager.getMenuFavorite()) {
         this.javaSkinManager.syncReal(this); //
         this.javaSkinManager.setCurrentActionFav(this);
      } else {
         //we must clear favs so that when go from fav to regular, selection deselects. setSelected(false) does not work bug?
         this.javaSkinManager.getLafButtonGroupFav().clearSelection();
         this.javaSkinManager.syncFav(this);
         this.javaSkinManager.setCurrentAction(this);
      }
   }

   public JMenu getActionRootMenu() {
      return getMenu();
   }

   public UIManager.LookAndFeelInfo getInfo() {
      return getLookAndFeelInfo();
   }

   public boolean isLf(String lf) {
      return getLookAndFeelInfo().getClassName().equals(lf);
   }

   public boolean isMatch(LafAction action) {
      return isMatch(action.getLookAndFeelInfo().getName(), action.getTheme());
   }

   public boolean isMatch(String lafName, String themeOutside) {
      if (lafName.equals(getLookAndFeelInfo().getName())) {
         String themeMine = getTheme();
         if (themeMine == null && themeOutside == null) {
            return true;
         } else if (themeMine != null && themeOutside != null) {
            return themeMine.equals(themeOutside);
         }
      }
      return false;
   }

   String getTheme() {
      return theme;
   }

   void setTheme(String theme) {
      this.theme = theme;
   }

   JMenu getMenu() {
      return menu;
   }

   void setMenu(JMenu menu) {
      this.menu = menu;
   }

   UIManager.LookAndFeelInfo getLookAndFeelInfo() {
      return laf;
   }

   void setLookAndFeelInfo(UIManager.LookAndFeelInfo laf) {
      this.laf = laf;
   }

   AbstractLookAndFeel getAbstractLookAndFeel() {
      return alf;
   }

   void setAbstractLookAndFeel(AbstractLookAndFeel alf) {
      this.alf = alf;
   }

}