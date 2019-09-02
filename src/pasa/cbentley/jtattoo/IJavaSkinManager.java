package pasa.cbentley.jtattoo;

import java.util.prefs.Preferences;

public interface IJavaSkinManager {
   
   
   public static final String BUNDLE_KEY_FAV         = "menu.lookandfeel.favorite";

   public static final String BUNDLE_KEY_FAV_ADD     = "menu.lookandfeel.addfavorite";

   public static final String BUNDLE_KEY_FAV_REMOVE  = "menu.lookandfeel.removefavorite";

   public static final String BUNDLE_KEY_MAIN_MENU   = "menu.lookandfeel.main";

   public static final String BUNDLE_KEY_OTHERS      = "menu.lookandfeel.others";

   public static final String BUNDLE_KEY_SYSTEM      = "menu.lookandfeel.system";

   /**
    * Key String for {@link Preferences}
    */
   public static final String PREF_LOOKANDFEEL       = "LookAndFeel";

   /**
    * Key for listing Look/Theme favorite
    * <br>
    * Its a big String with 
    */
   public static final String PREF_LOOKANDFEEL_FAV   = "LookAndFeelFavs";

   /**
    * Theme Key String for {@link Preferences}
    */
   public static final String PREF_LOOKANDFEEL_THEME = "LookAndFeelTheme";
}
