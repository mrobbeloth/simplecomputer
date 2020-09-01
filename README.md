A Java Applet for a Simple Computer Simulator
This program demonstrates the workings of a simple computer. It has 100 memory locations in a 10 by 10 grid with addresses 0 to 99 numbered from the top left row by row. The computer has a program counter which is stored in register PC and a single accumulator named AX. You may single step through a program or run it in one sequence. You may also specify the delay speed to observe the actions of the machine. Memory cells that are referenced become green for one instruction, those that are changed become red. When input is needed the execution is suspended, so the user may type a number in the Input box and click on the Enter Input button.

The input values must be given as integers (base 10), and all arithmetic instructions interpret their values as integers.

When entering values into memory locations, you may use the mnemonic strings below for each instruction code (case insensitive), and integers for immediate (literal) values or memory locations. When entering a series of instructions, the TAB key may be used to move to the next memory location.

When using the Run or Single Step button, the processor will start executing the instruction at the location given in the PC register.

While simple, this simulator demonstrates some of the essential aspects of machine and assembly language programming.

This applet was originally written by Barry G. Adams of Laurentian University. Several features were added by Curt Hill and Dr. Kasper (for use at MVNU).

The following table shows the instructions that are available for this computer:

| Mnemonic	Code (1st word) |	Code (2nd Word)	|Description
-------------------------------------------------------------------------------------------------------------
|STORE 160	                | n               | Store the contents of AX in memory location  n
|LOAD	 161	                | n	              | Load AX with the contents of location n
|ADDI	 44	                  | n	              | Add n to AX (add immediate)
|ADD	 50	                  | n               | Add the contents of location n to AX
|SUBI	 45	                  | n	              | Subtract the contents of location n to AX
|SUB	51	                  | n	              | Subtract the contents of location n from AX
|MUL	52	                  | n	              | Multiply the contents of AX by the contents of location n
|DIV	53	                  | n	              | Divide the contents of AX by the contents of location n
|INPUT	71	                | n	              | Enable input button and accept input into location n
|OUTPUT	72	                | n	              | Output the contents of location n
|JPOS	127	                  | n               | Jump to the instruction in location n if AX > 0
|JZERO	128	                | n	              | Jump to the instruction in location n if AX=0
|HALT                       |	0	              | Halt the execution
----------------------------------------------------------------------------------------------------------------
