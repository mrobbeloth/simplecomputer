/*
   A smart TextField class that knows about instructions and addresses
*/

import java.awt.*;

public class MyTextField extends TextField
{
   public MyTextField(String text, int width)
   {
      super(text, width);
   }
   
   public int getInstruction()
   {
      try
      {
         int inst = Integer.valueOf(getText().trim()).intValue();
         if (legalInstruction(inst))
            return inst;
         else
            return Instruction.ILLEGAL;
      }
      catch (NumberFormatException e)
      {
         return decode(getText().trim());
      }
   }

   public int getAddress()
   {
      try
      {
         return Integer.valueOf(getText().trim()).intValue();
      }
      catch (NumberFormatException e)
      {
         return 0;
      }
   }
   
   public void setAddress(int addr)
   {
      if (0 <= addr && addr <= 99)
         setText(String.valueOf(addr));
      else if (addr < 0)
         setText("0");
      else
         setText("99");
   }
 
   public int getInt()
   {
      try
      {
         return Integer.valueOf(getText().trim()).intValue();
      }
      catch (NumberFormatException e)
      {
         return 0;
      }
   }
   
   public void setInt(int addr)
   {
      setText(String.valueOf(addr));
   } 
 
   public boolean legalInstruction(int inst)
   {
      if (inst == Instruction.STORE || inst == Instruction.LOAD ||
          inst == Instruction.ADDI  || inst == Instruction.SUBI ||
          inst == Instruction.ADD   || inst == Instruction.SUB  ||
          inst == Instruction.MUL   || inst == Instruction.DIV  ||
          inst == Instruction.JPOS  || inst == Instruction.JZERO ||
          inst == Instruction.INPUT  || inst == Instruction.OUTPUT ||
          inst == Instruction.HALT)
         return true;
      else
         return false;
   }

   public int decode(String mnemonic)
   {
      String s = mnemonic.toUpperCase();
      if (s.equals("STORE")) return Instruction.STORE;
      else if (s.equals("LOAD")) return Instruction.LOAD;
      else if (s.equals("ADDI")) return Instruction.ADDI;
      else if (s.equals("SUBI")) return Instruction.SUBI;
      else if (s.equals("ADD")) return Instruction.ADD;
      else if (s.equals("SUB")) return Instruction.SUB;
      else if (s.equals("MUL")) return Instruction.MUL;
      else if (s.equals("DIV")) return Instruction.DIV;
      else if (s.equals("JPOS")) return Instruction.JPOS;
      else if (s.equals("JZERO")) return Instruction.JZERO;
      else if (s.equals("INPUT")) return Instruction.INPUT;
      else if (s.equals("OUTPUT")) return Instruction.OUTPUT;
      else if (s.equals("HALT")) return Instruction.HALT;
      else if (s.equals("")) return Instruction.HALT;
      else return Instruction.ILLEGAL;
   }
}
