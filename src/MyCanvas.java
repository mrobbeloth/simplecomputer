import java.awt.*;
public class MyCanvas extends Canvas
{
   public void paint(Graphics g)
   {
      g.drawString(
         "Instructions: STORE, LOAD, ADDI, SUBI, ADD, " +
         "SUB, MUL, DIV, INPUT, OUTPUT, JPOS, JZERO, HALT", 15, 15);
   }
}
