import javax.swing.*;
import java.util.*;

public class SLGame
{
	
   // These arrays and variables are required only for part II
   private Snake snakes[] = new Snake[15];    // array Can store up to 15 Snake objects
   private Ladder ladders[] = new Ladder[15]; // array Can store up to 15 Ladder objects
   private Trap traps[] = new Trap[5]; // array Can store up to 5 Trap objects
   String name[] = new String[2];  // array for storing the names
   // initalising variables for custom setup and array checking
   int snakesCount = 0;
   int laddersCount = 0;
   int trapsCount = 0;
   
   // Creating a Board, dice and a Scanner objects
   Board bd = new Board();
   Dice dice = bd.getDice();
   Scanner scan = new Scanner(System.in);
	  
   // Custom Setup Settings
   public void customSetup(Board bd) {
		  bd.clear();
	      boolean ok = false; 
	      // initialising ok variable. ok would become true as you go through each step, but if it becomes false, the program will ask the user to re-enter values
	      // Setting between 0 and 15 ladders
		  laddersCount = (getInt("How many ladders would you like? (max 15)", 0,15) - 1);
		  for (int l=0; l <= laddersCount; l++){
			  do {
				  int b = getInt("Where do you want the start of ladder " + (l+1) + " to be?", 2, 90);
				  int t = getInt("Where do you want the end of ladder " + (l+1) + " to be?", 11, 99);
				  ok = true;
				  //comparing with all previous ladders to ensure no violation
				  for (int p=0; p < l; p++){
					  if (ladders[p].getBottom() == b || ladders[p].getTop() == b || 
							  ladders[p].getBottom() == t ) {
						  ok = false;
						  plainMessage("Ladder tops and bases cannot conflict. Please reenter.");
					  }
				  }
				  //checking ladder tops and bases to ensure position order is correct
				  if (t-b <= 0){
					  ok = false;
					  plainMessage("Ladder top cannot be lower than ladder base. Please reenter.");
				  }
				  else { //when there are no more conflicts, the ladder will be added to the array
					  Ladder ladder = new Ladder(b,t);
					  bd.add(ladder);
					  ladders[l] = ladder;
				  }
			  } while (ok == false); //repeats until no conflicts
		  }
		  
		  //setting between 0 and 15 snakes. Maximum snakes depend on number of ladders set in array.
		  snakesCount = (getInt ("How many snakes would you like? (max " + (15-(laddersCount +1)) + ")", 0, (15-laddersCount+1) ) - 1);
		  for (int s=0; s <= snakesCount; s++){
			  do {
				  int h = getInt("Where do you want the head of snake " + (s+1) + " to be?", 11, 100);
				  int t = getInt("Where do you want the tail of snake " + (s+1) + " to be?", 1, 90);
				  ok = true;
				  //comparing with all previous snakes to ensure no violation
				  for (int k=0; k < s; k++){
					  if (snakes[k].getHead() == h || snakes[k].getTail() == h 
							  || snakes[k].getHead() == t ) {
						ok = false;
						plainMessage("Snakes cannot conflict with other snakes. Please reenter.");  
					  }
				  }
				  //comparing with all ladders to ensure no violation
				  for (int e=0; e<=laddersCount; e++){
					  if (ladders[e].getBottom() == h || ladders[e].getTop() == h 
							  || ladders[e].getBottom() == t || ladders[e].getTop() == t) {
						ok = false;
						plainMessage("Snakes cannot conflict with ladders. Please reenter.");  
					  }
				  	}
				  //checking snake heads and tails to ensure position order is correct
				  if (h-t <= 1){
					  ok = false;
					  plainMessage("Snake tail cannot be higher than head. Please reenter.");
			  		}
				  else { //when there are no more conflicts, the snake will be added to the array
					  Snake snake = new Snake(h,t);
					  bd.add(snake);
					  snakes[s] = snake;
			  		}
			} while (ok == false); //repeats until no conflicts
		  }

		  // Setting between 0 and 5 traps
		  trapsCount = (getInt ("How many traps would you like? (max 5)", 0,5) - 1);
		  int dur = 0; //initialising duration variable
		  for (int t=0; t <= trapsCount; t++){
			  do {
				  int loc = getInt("Where do you want trap " + (t+1) + " to be?", 2, 99);
				  ok = true;
				  //comparing with all previous traps to ensure no violation
				  for (int r=0; r < t; r++){
					  if (loc == traps[r].getLocation()){
						  ok = false;
						  plainMessage("Traps cannot be in the same place as other traps.\nReenter within valid values.");
					  }
				  }
				  //comparing with all ladders to ensure no violation
				  for (int a=0; a <= laddersCount; a++){
					  if (loc == ladders[a].getBottom() || loc == ladders[a].getTop()){
						  ok = false;
						  plainMessage("Traps cannot be in the same place as tops and bottoms of ladders.\nReenter within valid values.");
					  	}
				  }
				  //comparing with all snakes to ensure no violation
				  for (int n=0; n <= snakesCount; n++){
					  if (loc == snakes[n].getHead() || loc == snakes[n].getTail()){
						  ok = false;
						  plainMessage("Traps cannot be in the same place as tops and bottoms of ladders.\nReenter within valid values.");
					  }
				  }		  
				  dur = getInt("How many turns to you want it to be set for? (From 3 to 5)", 3, 5);
				  Trap trap = new Trap(loc, dur);
				  bd.add(trap);
				  traps[t] = trap;
			  } while (ok == false);
		  }
		  plainMessage((laddersCount +1) + " ladders, " 
		  + (snakesCount +1) + " snakes and " + (trapsCount +1) + " traps set on board");
		  //the (+1)s are there because arrays start from 0.
   }
     
   // A method to print a message and to read an int value in the range specified
   int getInt(String message, int from, int to)
   {
	   String s;
	   int n = 0;
	   boolean invalid;
	   do {
		 invalid = false;
	     s = (String)JOptionPane.showInputDialog(
	      bd,  message,  "Customized Dialog",
	          JOptionPane.PLAIN_MESSAGE);	
	      try {
	         n = Integer.parseInt(s);
	         if (n < from || n > to )
	    	     plainMessage("Re-enter: Input not in range " + from + " to " + to);
	      }
	      catch (NumberFormatException nfe)
	      {
	    	  plainMessage("Re-enter: Invalid number");
	    	  invalid = true;
	      }
	   } while ( invalid || n < from || n > to);
	   return n;
   }

   // A method to print a message and to read a String
   String getString(String message)
   {
	   String s = (String)JOptionPane.showInputDialog(
	      bd,  message,  "Customized Dialog",
	          JOptionPane.PLAIN_MESSAGE);	
	   return s;
   }   

   // A method to print a message
   void plainMessage(String message)
   {
        JOptionPane.showMessageDialog(bd,
		    message, "A prompt message",
		    JOptionPane.PLAIN_MESSAGE);
   }
   
   // The main method implementing the game logic for Part A
   // For Part A you may use the hard coded values in the setup() method 
   // You will need to change the code for Part B
   // For user interaction use the methods getString, getInt and plainMessage
   // For display/erase on the board use bd.addMessage(), bd.clearMessages()

   public void control()
   {   
      String name1 = getString("Player 1 name : ");
      String name2 = getString("Player 2 name : ");  
      int p1Location = 1;
      int p2Location = 1;
      bd.setPiece(1,p1Location);
      bd.setPiece(2,p2Location);
      int turnno = 1; //recording turn number
      int p1Miss = 0;
      int p2Miss = 0;
      int val = 0;
      boolean GameFinish = false;

      do {
    	  bd.clearMessages();  // clears the display board  
    	  bd.addMessage("Current Players are");
    	  bd.addMessage("Player 1 : " + name1);      
    	  bd.addMessage("Player 2 : " + name2);
    	  bd.addMessage("Turn number : " + turnno);
    	  bd.addMessage("Continue until");
    	  bd.addMessage("a player gets to 100");  
    	  bd.addMessage("Danger: Traps/Snakes");    
    	  bd.addMessage("Enjoy the ladders!");

	      //Player 1 dice
	      if (p1Miss > 0) {
	     	plainMessage(name1 +" misses a turn. Turns remaining: " + p1Miss);
	      }
	      else {
	    	  val = getInt(name1 + ": Enter 0 to throw dice.", 0, 0);
	    	  if ( val == 0)
	    		  val = dice.roll();
	      }
	      
	      // *** Exceeding 100 Protocol *** (exact only, miss a turn)
	      if (p1Location + val > 100) {
	          plainMessage(name1 +" misses a turn.");
	      }
	      else if (p1Miss == 0){
	          p1Location += val;
	    	  plainMessage(name1 + ": moving to " + p1Location);
	    	  bd.setPiece(1, p1Location);
	      }
	      
	      // P1 Ladder, Snake and Trap protocols
	      //going up ladders
	      for (int l=0; l<=laddersCount; l++){
	          if (ladders[l] != null && p1Location == ladders[l].getBottom()){
	        	  p1Location = ladders[l].getTop();
	        	  plainMessage(name1 + ": moving to " + p1Location);
	        	  bd.setPiece(1, p1Location);
	          }
	      }
	      //going down snakes
	      for (int s=0; s<=snakesCount; s++){
	    	  if (snakes[s] != null && p1Location == snakes[s].getHead()){
	        	  p1Location = snakes[s].getTail();
	        	  plainMessage(name1 + ": going down a snake to " + p1Location);
	        	  bd.setPiece(1, p1Location);
	          }
	      }
	      //trap protocol
	      for (int t=0; t<=trapsCount; t++){
	    	  if (traps[t] != null && p1Location == traps[t].getLocation() && p1Miss == 0){
	        	  plainMessage(name1 + " misses three turns!");
	        	  p1Miss = traps[t].getDuration();
	          }
	      }
	      
	      // *** Rolling 6 Protocol *** (one extra turn ONLY) Exceeding 100 Protocol included
	      //If a player rolls a 6 and lands on a trap, the extra turn is not triggered.
	      //Ladders, snakes and traps have no effect during the second turn.
	      if (val == 6 && p1Miss == 0) { 
	    	  plainMessage(name1 +" gets another turn!");
	    	  val = getInt(name1 + ": Enter 0 to throw dice.", 0, 0);
	    	  if ( val == 0)
	            val = dice.roll();
	    	  if (p1Location + val > 100)
	        	plainMessage(name1 +" misses a turn.");
	    	  else
	            p1Location += val;
	            plainMessage(name1 + ": moving to " + p1Location);
	            bd.setPiece(1, p1Location);
	      }
	      
	      if (p1Miss > 0)
	      p1Miss--; //This is so that the missed turn counter goes down each turn.
	      
	      // P1 winning protocol
	      if (p1Location == 100) {
	    	  GameFinish = true;
	          plainMessage(name1 + " has won the game at turn " + turnno);
	          break;
	      }
	      
	      //Player 2 dice
	      if (p2Miss > 0)
	    	  plainMessage(name2 +" misses a turn. Turns remaining: " + p2Miss);
	   	  else { 
	   		  val = getInt(name2 + ": Enter 0 to throw dice. Enter 1 - 6 for Testing.", 0, 0);
	   		  if ( val == 0)
	   			  val = dice.roll();
	   	  }
	      
	      // *** Exceeding 100 Protocol *** (exact only, miss a turn)
	      if (p2Location + val > 100)
	    	  plainMessage(name2 +" misses a turn.");
	      else if (p2Miss == 0) {
	    	  p2Location += val;
	    	  plainMessage(name2 + ": moving to " + p2Location);
	    	  bd.setPiece(2, p2Location);
	      }
	      
	      // P2 Ladder, Snake and Trap protocols
	      //going up ladders
	      for (int l=0; l<=laddersCount; l++){
	          if (ladders[l] != null && p2Location == ladders[l].getBottom()){
	        	  p2Location = ladders[l].getTop();
	        	  plainMessage(name2 + ": moving to " + p2Location);
	        	  bd.setPiece(2, p2Location);
	          }
	      }
	      //going down snakes
	      for (int s=0; s<=snakesCount; s++){
	    	  if (snakes[s] != null && p2Location == snakes[s].getHead()){
	        	  p2Location = snakes[s].getTail();
	        	  plainMessage(name2 + ": going down a snake to " + p2Location);
	        	  bd.setPiece(2, p2Location);
	          }
	      }
	      //trap protocol
	      for (int t=0; t<=trapsCount; t++){
	    	  if (traps[t] != null && p2Location == traps[t].getLocation() && p2Miss == 0){
	        	  plainMessage(name2 + " misses three turns!");
	        	  p2Miss = traps[t].getDuration();
	          }
	      }
	      
	      // *** Rolling 6 Protocol *** (one extra turn ONLY) Exceeding 100 Protocol included
	      //If a player rolls a 6 and lands on a trap, the extra turn is not triggered.
	      //Ladders, snakes and traps have no effect during the second turn.
	      if (val == 6 && p2Miss == 0) {
	    	  plainMessage(name2 +" gets another turn!");
	    	  val = getInt(name2 + ": Enter 0 to throw dice.", 0, 0);
	    	  if ( val == 0)
	    		  val = dice.roll();
	    	  if (p2Location + val > 100)
	    		  plainMessage(name2 +" misses a turn.");
	    	  else
	    		  p2Location += val;
	    	  	  plainMessage(name2 + ": moving to " + p2Location);
	              bd.setPiece(2, p2Location);
	       }
	      if (p2Miss > 0)
	      p2Miss--; //This is so that the missed turn counter goes down each turn.
	            
	      // P2 winning protocol
	      if (p2Location == 100) {
	    	  GameFinish = true;
	    	  plainMessage(name2 + " has won the game at turn " + turnno);
	    	  break;
	      }
	      turnno++; // increase turn counter before returning to loop
      } while (GameFinish == false); 
   }

   //menu method
   public void menu()
   {
	   int option;
	   do {
		   option = getInt("Custom setup...1\nPlay...2\nExit...3",1,3);
		   if (option == 1)
			   customSetup(bd);
		   else if (option == 2)
			   control();
	   } while (option != 3);
   }
   
   // The very first method to be called
   // This method constructs a SLGame object and calls its control method 
   public static void main(String[] args)
   {
       SLGame slg = new SLGame();
       slg.menu();
   }
}
