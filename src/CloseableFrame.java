import java.awt.*;
import java.awt.event.*;

public class CloseableFrame extends Frame implements WindowListener
{

   public CloseableFrame()
   {
      this("Default Closeable Frame");
   }

   public CloseableFrame(String title)
   {
      super(title);
      addWindowListener(this);
   }

   /* This method is called when the user clicks the close box of
      a window. Here we just exit the program */

   
   public void windowClosing(WindowEvent e)
   {
      e.getWindow().dispose();
      System.exit(0);
   }

   /* These 6 methods are part of the WindowListener interface but
      have empty bodies. */
   
   public void windowActivated(WindowEvent e) {}
   public void windowClosed(WindowEvent e) {}
   public void windowDeactivated(WindowEvent e) {}
   public void windowDeiconified(WindowEvent e) {}
   public void windowIconified(WindowEvent e) {}
   public void windowOpened(WindowEvent e) {}
   
}
