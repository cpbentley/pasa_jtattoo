package pasa.cbentley.jtattoo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

public class RunJavaSkinManagerDemo {
   /**
    * Simple test method
    * @param args
    */
   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            final Preferences prefs = Preferences.userNodeForPackage(RunJavaSkinManagerDemo.class);
            final JavaSkinManager lfm = new JavaSkinManager(prefs);
            lfm.setIconSelected(new Icon() {
               public void paintIcon(Component c, Graphics g, int x, int y) {
                  g.setColor(Color.blue.darker());
                  g.fillRect(x, y, 16, 16);
               }

               public int getIconWidth() {
                  return 16;
               }

               public int getIconHeight() {
                  return 16;
               }
            });
            final JFrame jf = new JFrame("Look and Feel Frame");
            jf.addWindowListener(new WindowAdapter() {
               public void windowClosing(WindowEvent e) {
                  lfm.prefsSave();
                  System.exit(0);
               }
            });
            JMenuBar mb = new JMenuBar();
            mb.add(lfm.getRootMenu());
            JMenu jm = new JMenu("Options");
            final JRadioButtonMenuItem jiUndecorated = new JRadioButtonMenuItem("Undecorated");
            jiUndecorated.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  boolean isDecorated = !jf.isUndecorated();
                  boolean isDecoSupport = JFrame.isDefaultLookAndFeelDecorated();
                  System.out.println("isDecorated=" + isDecorated + " isDecoSupport=" + isDecoSupport);
                  jf.dispose();
                  jf.setUndecorated(true);
                  jf.setVisible(true);
               }
            });
            final JRadioButtonMenuItem jiDecorated = new JRadioButtonMenuItem("Decorated");
            jiDecorated.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  boolean isUndecorated = jf.isUndecorated();
                  if (isUndecorated) {
                     jf.dispose();
                     jf.setUndecorated(false);
                     jf.setVisible(true);
                  }
               }
            });
            ButtonGroup group = new ButtonGroup();
            group.add(jiDecorated);
            group.add(jiUndecorated);
            jm.add(jiDecorated);
            jm.add(jiUndecorated);

            mb.add(jm);
            jf.setJMenuBar(mb);

            JButton but = new JButton("Hello World!");
            jf.getContentPane().add(but);
            jf.pack();
            jf.setSize(300, 200);
            jf.setLocation(400, 400);
            jf.setVisible(true);

            if (jf.isUndecorated()) {
               jiUndecorated.setSelected(true);
            } else {
               jiDecorated.setSelected(true);
            }
         }
      });
   }
}
