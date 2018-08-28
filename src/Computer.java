// Computer.java

/************************************************************************
   A Simple Computer Simulator
   AUTHOR: 
      Barry G. Adams
   ADDRESS: 
      Department of Mathematics and Computer Science,
      Laurentian University,
   EMAIL
      barry@bethel.cs.laurentian.ca
   Modified:
      Curt Hill
   Address:
      Departement of Mathematics
      Valley City State University
   EMAIL:
      Curt_Hill@mail.vcsu.nodak.edu 
************************************************************************/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Computer extends Applet implements ActionListener,
   Runnable
{
   private static final Color lightBlue = new Color(180,180,255);

   private Thread process;
   private boolean running = false;
   private boolean waitingInput = false;
   private boolean wasRunning = false;
   private boolean threadStopped = true; 
   
   private MyCanvas canvas;      // to display instruction mnemonics

   private MyTextField[] memory; // 100 by 100 memory array
   private Label[] rowLabel;     // labels 0 to 9 for its rows
   private Label[] colLabel;     // labels 0 to 9 for its columns
   private Label head;           // dummy label for grid top left
   

   private Label pcLabel;        // the program counter label and
   private MyTextField pcField;  // text field
   
   private Label axLabel;        // the accumulator label and
   private MyTextField axField;  // text field

   private Button run;           // to run a program
   private Button step;          // to single step through a program
   private Button halt;          // to halt execution of computer
   private Button clear;         // to clear memory and registers

   private Button inputDone;   // Confirms input is done

   private Button countDemo; // Loads a demo that counts
   private Button factDemo;    // Loads a demo that calculates factorials
   private Button sumDemo;    // Loads a demo that calculates factorials
   
   private MyTextField status;   // to display information messages
   

   private MyTextField speedText;
   private Label busDirection;
   private Label busStatus;
   private Label busValue;
   private Label phase;

   private TextField inText;
   private MyTextField outText;
   
   int speed = 500;
   Storage colored_cells;
   
   /*
      Set up the graphical interface as 3 panels: A north panel for
      the instruction mnemonic display, a center panel for the memory
      grid and its labels, and a south panel for the pc and ax fields,
      the buttons and status area.
   */
   

   public void init()
   {
      colored_cells = new Storage(4);
      setBackground(lightBlue);
     
      /*
         Construct the canvas for the mnemonics. Put it into the
         center of a border layout panel so that it will automatically
         expand horizontally.
      */
      
      canvas = new MyCanvas();
      canvas.setSize(getSize().width,20);
      canvas.setBackground(Color.white);
      Panel p0 = new Panel(new BorderLayout());
      p0.add("Center", canvas);      


      /*
         Construct the memory grid and labels and put them in a panel
         which defines an 11 by 11 grid layout.
      */
      
      head = new Label("MEM", Label.CENTER);

      memory = new MyTextField[100];
      rowLabel = new Label[10];
      colLabel = new Label[10];


      pcLabel = new Label("PC", Label.CENTER);
      pcField = new MyTextField("0", 4);
      axLabel = new Label("AX", Label.CENTER);
      axField = new MyTextField("0", 4);

      run = new Button("Run");
      step = new Button("Single Step");
      halt = new Button("Halt");
      clear = new Button("Clear");
      inputDone = new Button("                 ");
      inputDone.setForeground(Color.magenta);
      
      status = new MyTextField("", 30);
      status.setEditable(false);
      status.setBackground(Color.white);
      status.setText("Status Area");
      
      Panel p1 = new Panel(new GridLayout(11,11,2,2));

      /* Add column labels at top of grid */

      p1.add(head);
      for (int col = 0; col <= 9; col++)
      {
         colLabel[col] = new Label(String.valueOf(col), Label.CENTER);
         p1.add(colLabel[col]);
      }
      
      /* add memory cells and their row labels */

      for (int row = 0; row <= 9; row++)
      {
         // add row label
         
         rowLabel[row] = new Label(String.valueOf(row), Label.CENTER);
         p1.add(rowLabel[row]);
         
         // add row of memory cells
         
         for (int col = 0; col <= 9; col++)
         {
            int pos = 10*row + col;
            memory[pos] = new MyTextField("",4);
            p1.add(memory[pos]);
         }
      }
      
      /*
         Construct a panel for the bottom that contains pc and ax fields,
         the control buttons, and the status field.
      */
      
      Panel p2 = new Panel(new FlowLayout());
      p2.add(pcLabel);
      p2.add(pcField);
      p2.add(axLabel);
      p2.add(axField);
      p2.add(run);
      p2.add(step);
      p2.add(halt);
      p2.add(clear);
      p2.add(status);
      
      /*
         Construct a panel for the bus that shows things transfering
      */
      busDirection = new Label("  ");
      busValue = new Label("    ");
      busStatus = new Label("Idle");
      phase = new Label("CPU is idle");
      
      Panel bus = new Panel(new GridLayout(1,5));
      bus.add(new Label("Bus Activity"));
      bus.add(busStatus);
      bus.add(busDirection);
      bus.add(busValue);
      speedText = new MyTextField("500",6);
      Panel control = new Panel(new GridLayout(1,5));
      control.add(new Label("CPU internal phase"));
      control.add(phase);

      countDemo = new Button("Count to 5");
      factDemo = new Button("Calculate 4 factorial");
      sumDemo = new Button("Sum 5 numbers");

      Panel samples = new Panel(new GridLayout(1,6));
      samples.add(new Label("Delay (millisec):"));
      samples.add(speedText);
      samples.add(new Label("Sample programs:"));
      samples.add(countDemo);
      samples.add(factDemo);
      samples.add(sumDemo);

      Panel IO = new Panel(new GridLayout(1,7));
      IO.add(new Label("Input:"));

      inText = new  TextField(6);
      IO.add(inText);

      IO.add(inputDone);
      IO.add(new Label("       "));

      IO.add(new Label("Output:"));

      outText = new  MyTextField(" ",1);
      IO.add(outText);
      
      /*
         Finally put all panels in the applet panel using
         a Gridbag layout.
      */

      GridBagLayout gb = new GridBagLayout();
      setLayout(gb);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.gridwidth = 5;
      gbc.gridheight = 1;
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.fill = GridBagConstraints.BOTH;
      gb.setConstraints(p0,gbc);
      add(p0);

      gbc.gridx = 1;
      gbc.gridy = 2;
      gbc.gridheight = 5;
      gb.setConstraints(p1,gbc);
      add(p1);
      
      gbc.gridx = 1;
      gbc.gridy = 8;
      gbc.gridheight = 1;
      gb.setConstraints(bus,gbc);
      add(bus);
      
      gbc.gridx = 1;
      gbc.gridy = 9;
      gb.setConstraints(control,gbc);
      add(control);
      
      gbc.gridx = 1;
      gbc.gridy = 10;
      gbc.gridheight = 1;
      gb.setConstraints(p2,gbc);
      add(p2);

      gbc.gridx = 1;
      gbc.gridy = 11;
      gbc.gridheight = 1;
      gb.setConstraints(samples,gbc);
      add(samples);

      gbc.gridy = 12;
      gb.setConstraints(IO,gbc);
      add(IO);


      run.addActionListener(this);
      step.addActionListener(this);
      halt.addActionListener(this);
      clear.addActionListener(this);

      inputDone.addActionListener(this);

      countDemo.addActionListener(this);
      factDemo.addActionListener(this);
      sumDemo.addActionListener(this);

      inText.addActionListener(this);
   }
   

   private void demo1()
   {
      clear();
      memory[0].setText("load");
      memory[1].setText("99");
      memory[2].setText("subi");
      memory[3].setText("1");
      memory[4].setText("jpos");
      memory[5].setText("2");
      memory[6].setText("halt");
      memory[99].setText("5");
      pcField.setText("0");
   }

   private void demo2()
   {
      clear();
      memory[0].setText("load");
      memory[1].setText("32");
      memory[2].setText("store");
      memory[3].setText("31");
      memory[4].setText("load");
      memory[5].setText("30");
      memory[6].setText("mul");
      memory[7].setText("31");
      memory[8].setText("store");
      memory[9].setText("31");
      memory[10].setText("load");
      memory[11].setText("30");
      memory[12].setText("subi");
      memory[13].setText("1");
      memory[14].setText("store");
      memory[15].setText("30");
      memory[16].setText("jpos");
      memory[17].setText("6");
      memory[18].setText("output");
      memory[19].setText("31");
      memory[20].setText("halt");
      memory[30].setText("4");
      memory[31].setText("0");
      memory[32].setText("1");
      pcField.setText("0");
   }

   private void demo3()
   {  // Sum five numbers
      clear();
      memory[0].setText("load");
      memory[1].setText("1");
      memory[2].setText("input");
      memory[3].setText("52");
      memory[4].setText("load");
      memory[5].setText("51");
      memory[6].setText("add");
      memory[7].setText("52");
      memory[8].setText("store");
      memory[9].setText("51");
      memory[10].setText("load");
      memory[11].setText("50");
      memory[12].setText("subi");
      memory[13].setText("1");
      memory[14].setText("store");
      memory[15].setText("50");
      memory[16].setText("jpos");
      memory[17].setText("2");
      memory[18].setText("output");
      memory[19].setText("51");
      memory[20].setText("halt");
      memory[50].setText("5");
      memory[51].setText("0");
      pcField.setText("0");
   }

   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == run)
      {
         if (process == null || !process.isAlive())
         {
            process = new Thread(this, "Process");
            process.start();
            status.setText("Running");
         }
         else
         {
            resumeExecution();
         }
      }
      else if (e.getSource() == step)
      {
         if (! running && !waitingInput)
         {
            status.setText("Single step");
            executeInstruction();
         }
      }
      else if (e.getSource() == halt)
      {
         if (running)
         {
            suspendExecution();
         }
      }
      else if (e.getSource() == clear)
      {
         if (running)
         {
            suspendExecution();
         }
         // clear memory
         for(int i = 0;i<99;i++) {
           memory[i].setText(" ");
           }
         pcField.setText("0");
         axField.setText("0");
         inText.setText(" ");
         outText.setText(" ");
      }
      else if (e.getSource() == countDemo)
      {
         if (running)
         {
            suspendExecution();
         }
        demo1();         
      }
      else if (e.getSource() == factDemo)
      {
         if (running)
         {
            suspendExecution();
         }
        demo2();         
      }
      else if (e.getSource() == sumDemo)
      {
         if (running)
         {
            suspendExecution();
         }
        demo3();         
      }
      else if (e.getSource() == inputDone || e.getSource() == inText)
      {
         if (waitingInput)
         {  
            try {
                 int inputValue = Integer.valueOf(inText.getText().trim()).intValue();
                 inText.setText(" ");
                 busStatus.setText(" ");
                 busDirection.setText("Store");
                 sleepABit(speed);
                 int address = fetchAddress();
                 busValue.setText(String.valueOf(inputValue));
                 memory[address].setInt(inputValue);
                 memory[address].setForeground(Color.red);
                 colored_cells.add(address);
                 waitingInput = false;
                 inputDone.setLabel("    ");
                 if(wasRunning){
                   resumeExecution();
                   }
                 else {
                   status.setText("Single step");
                   }
                 }
            catch (NumberFormatException x) {inText.setText("Must be a number");};
         }
      }

   }
   
   public void start()
   {
      // run button starts a thread
   }

   /* 
      We don't use the thread's stop() method. It is not reliable.
      Instead we force the run method to terminate and this will
      stop the thread.
   */
   public void stop()
   {
      threadStopped = true;
      suspendExecution();
   }


   /* 
      Run the program beginning at current pc value until a halt or
      illegal instruction is encounterd.
   */
   
   public void run()
   {
      running = true;
      threadStopped = false;
      int instructionCode = Instruction.HALT;
      while (! threadStopped)
      {
         if (running)
         {
            instructionCode = executeInstruction();
            if (instructionCode == Instruction.HALT) break;
            if (instructionCode == Instruction.ILLEGAL) break;
         }
         sleepABit(speed);  // to slow things down
      }
      if (instructionCode == Instruction.HALT)
         status.setText("Normal Program Termination");
      phase.setText("CPU is idle");
      busDirection.setText(" ");
      busValue.setText(" ");
      process = null;
      running = false;
   }
   
   
   private void sleepABit(int milliseconds)
   {
      try 
      {
         process.sleep(milliseconds);
      }
      catch (InterruptedException e) {}
   }

   private void suspendExecution()
   {
      running = false;
      status.setText("Execution suspended");
      phase.setText("CPU is idle");
      busDirection.setText(" ");
      busValue.setText(" ");
   }
   
   private void resumeExecution()
   {
      running = true;
      status.setText("Running");
      phase.setText(" ");
   }
      
   /*
      Return the code for the instruction in memory location specified
      by the program counter pc. The code can be specified either as a
      number or a mnemonic.
   */
   
   private int fetchInstruction()
   {
      busStatus.setText(" ");
      busDirection.setText("Fetch");
      int pc = pcField.getAddress();
      int instruction = memory[pc].getInstruction();
      memory[pc].setForeground(Color.green);
      colored_cells.add(pc);
      busValue.setText(String.valueOf(instruction));
      pcField.setAddress(pc + 1);
      sleepABit(speed);
      return instruction;
   }
   
   /*
      Used by instructions whose data is found in a given memory
      location specified by the value in the next memory location
   */
   
   private int fetchAddress()
   {
      busStatus.setText(" ");
      busDirection.setText("Fetch");
      int pc = pcField.getAddress();
      int address = memory[pc].getAddress();
      memory[pc].setForeground(Color.green);
      colored_cells.add(pc);
      pcField.setAddress(pc + 1);
      busValue.setText(String.valueOf(address));
      sleepABit(speed);
      return address;
   }
   
   /*
      Used by the immediate instructions which expect the next
      memory location to contain a constant value rather than an
      address.
   */
   
   private int fetchConstant()
   {
      busStatus.setText(" ");
      busDirection.setText("Fetch");
      int pc = pcField.getAddress();
      int constant = memory[pc].getInt();
      memory[pc].setForeground(Color.green);
      colored_cells.add(pc);

      pcField.setAddress(pc + 1);
      busValue.setText(String.valueOf(constant));
      sleepABit(speed);
      return constant;
   }
     
   
   private int executeInstruction()
   {
      int address, ax, constant, memVal;
      phase.setText("Fetching instruction");
      // Clear any colors
      int [] loc = colored_cells.get();
      for(int i = 0;i<loc.length;i++)
            memory[loc[i]].setForeground(Color.black);
      colored_cells.clear();

      int instruction = fetchInstruction();
      busDirection.setText(" ");
      busValue.setText(" ");
      busStatus.setText(" ");
      phase.setText("Decoding instruction");
      // check the speed
      speed = speedText.getInt();
      sleepABit(speed);
      phase.setText("Executing instruction");
      
      if (instruction == Instruction.ILLEGAL)
      {
         int pc = pcField.getAddress() - 1; // pc references next instruction
         status.setText("Illegal instruction at location " + pc);
         return instruction;
      }     

      switch (instruction)
      {
         case Instruction.STORE:
            busStatus.setText(" ");
            busDirection.setText("Store");
            sleepABit(speed);
            address = fetchAddress();
            ax = axField.getInt();
            busValue.setText(String.valueOf(ax));
            memory[address].setInt(ax);
            memory[address].setForeground(Color.red);
            colored_cells.add(address);
            break;
         case Instruction.LOAD:
            address = fetchAddress();
            memVal = memory[address].getInt();
            memory[address].setForeground(Color.green);
            colored_cells.add(address);
            axField.setInt(memVal);
            break;
         case Instruction.ADDI:
            constant = fetchConstant();
            ax = axField.getInt();
            axField.setInt(ax + constant);
            break;
         case Instruction.SUBI:
            constant = fetchConstant();
            ax = axField.getInt();
            axField.setInt(ax - constant);
            break;
         case Instruction.ADD:
            address = fetchAddress();
            memVal = memory[address].getInt();
            memory[address].setForeground(Color.green);
            colored_cells.add(address);
            ax = axField.getInt();
            axField.setInt(ax + memVal);
            break;
         case Instruction.SUB:
            address = fetchAddress();
            memVal = memory[address].getInt();
            memory[address].setForeground(Color.green);
            colored_cells.add(address);
            ax = axField.getInt();
            axField.setInt(ax - memVal);
            break;
         case Instruction.MUL:
            address = fetchAddress();
            memVal = memory[address].getInt();
            memory[address].setForeground(Color.green);
            colored_cells.add(address);
            ax = axField.getInt();
            axField.setInt(ax * memVal);
            break;
         case Instruction.DIV:
            address = fetchAddress();
            memVal = memory[address].getInt();
            memory[address].setForeground(Color.green);
            colored_cells.add(address);
            ax = axField.getInt();
            axField.setInt(ax / memVal);
            break;
         case Instruction.INPUT:
            System.out.print("Input was executed, running: ");
            System.out.print(running);
            System.out.print(",   threadStopped: ");
            System.out.println(threadStopped);
            inputDone.setLabel("Enter input.");
            waitingInput = true;
            wasRunning = running;
            if(running){
               suspendExecution();
              }
            status.setText("Suspended awaiting input");
            break;
         case Instruction.OUTPUT:
            address = fetchAddress();
            memVal = memory[address].getInt();
            memory[address].setForeground(Color.green);
            colored_cells.add(address);
            outText.setInt(memVal);
            break;
         case Instruction.JPOS:
            address = fetchAddress();
            ax = axField.getInt();
            if (ax > 0) pcField.setAddress(address);
            break;
         case Instruction.JZERO:
            address = fetchAddress();
            ax = axField.getInt();
            if (ax == 0) pcField.setAddress(address);
            break;
         case Instruction.HALT:
            break;
         default:
      }
      busStatus.setText("Idle ");
      busDirection.setText(" ");
      busValue.setText(" ");
      sleepABit(speed);
      return instruction;
   }

   private void clear() {
      // Clear any colors
      int [] loc = colored_cells.get();
      for(int i = 0;i<loc.length;i++)
            memory[loc[i]].setForeground(Color.black);
      colored_cells.clear();
      for(int i = 0; i<100;i++)
        memory[i].setText(" ");
     }


   /*
      The applet can also be run as a standalone application
   */
   
   public static void main(String[] args)
   {
      CenteredFrame f = new CenteredFrame();
      Computer a = new Computer();
      a.setSize(600,300);
      a.init();
      
      f.add(a);
      f.setTitle("A Simple Computer Simulator");
      f.setSize(630,340);
      f.center();
      f.setVisible(true);
   }   
   
}

//////////////////////////////////////////////////////////////////////////

class Instruction
{
   public static final int STORE = 160;
   public static final int LOAD  = 161;
   public static final int ADDI   = 44;
   public static final int SUBI   = 45;
   public static final int ADD   = 50;
   public static final int SUB   = 51;
   public static final int MUL   = 52;
   public static final int DIV   = 53;
   public static final int INPUT   = 71;
   public static final int OUTPUT   = 72;
   public static final int JPOS   = 127;
   public static final int JZERO  = 128;
   public static final int HALT   = 0;
   public static final int ILLEGAL = 255;
}


