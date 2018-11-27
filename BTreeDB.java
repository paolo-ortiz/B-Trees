import java.util.*;
import java.io.*;

public class BTreeDB {
	
	public static void main( String[] args ) {

		try {
			
	

			BTreeManager btm = new BTreeManager(args[0]);
			ValuesManager vm = new ValuesManager(args[1]);

			Scanner sc = new Scanner(System.in);
			while (sc.hasNext()) {
				String value;

				String input = sc.nextLine(); //get input

				//for getting the inputs
				Scanner rd = new Scanner(input);
				String command = rd.next();

				//if input is "exit", close program
				if (command.equals("exit")) {
					btm.closeData();
					vm.closeData();
					//file2.close();
					return;
				}
				
				//else if input is "insert", check if it is valid
				//first, make sure input has more than 1 value
				if (command.equals("insert")) {
					
					long key = rd.nextLong();
					
					if(rd.hasNextLine() == true) {
						
					 value = rd.nextLine().trim();	//checks if there is any more input 
					}
					
					
					else {
						
					 value = ""; 	//if no input is found, then a blank is added instead
					}
					
					
					insertToVal(key, value, vm);

					//else if there is more than 2 elements

				}

				//insert to node dapat to
				else if (command.equals("insertTest")) {
					// insert to node will  a key but will mess with value index somewhere
					//btm.insertToNode();
				}


				//else if invalid command, print "invalid command"
				else
					System.out.println("INVALID COMMAND");

			}
		} catch (IOException e) {
			System.out.println("IOException in main");
		}
	}	

	//inserts value into tree
	public static void insertToVal(long key, String word, ValuesManager vm) throws IOException {
		
		System.out.printf("Inserted %s\n", word);
		vm.insert(key, word);

	}

	
}