// CenteredFrame.java

/*
   A closeable frame which has a center method to center a frame
   on the screen. Call it just before showing the frame
*/

import java.awt.*;

public class CenteredFrame extends CloseableFrame
{
   public CenteredFrame()
   {
      this("Default Centered Frame");
   }

   public CenteredFrame(String title)
   {
      super(title);
   }

   public void center()
   {
      Dimension screenSize = getToolkit().getScreenSize();
      Dimension frameSize = getSize();
      int xLocation = (screenSize.width - frameSize.width) / 2;
      int yLocation = (screenSize.height - frameSize.height) / 2;
      setLocation(xLocation, yLocation);
   }   
}
