import java.util.*;
import java.io.*;

public class BTreeDB {

	//make node
	public static BTreeNode node1 = new BTreeNode();
	static boolean exists = false; //for existance check 
	public static void main( String[] args ) {

		try {
			
			BTreeManager btm = new BTreeManager(args[0]);
			ValuesManager vm = new ValuesManager(args[1]);
			//BTreeNode does not have getNode 
			//BTreeNode bn = btm.getNode();
			//temp index
			int index = 0;
			Scanner sc = new Scanner(System.in);
			while (sc.hasNext()) {

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
					String value = rd.nextLine().trim();
					
					//check if the key is present first
					exists = btm.present(key);
					if(exists == false){
						insertToVal(key, value, vm);
						insertToBT(key,index, btm);
					}
		
					else{
						System.out.println("ERROR: " + key +" already exists.");
					}
					//else if there is more than 2 elements

				}


				//else if invalid command, print "invalid command"
				else
					System.out.println("INVALID COMMAND");

			}
		} catch (IOException e) {
			System.out.println("IOException in main");
		}
	}	

	//inserts value into data.val
	public static void insertToVal(long key, String word, ValuesManager vm) throws IOException {
		
		System.out.printf("Inserted %s\n", word);
		vm.insert(key, word);

	}

	//inserts value into data.bt
	public static void insertToBT(long key, int index, BTreeManager btm) throws IOException {
		btm.insertToNode(key,index);


	}


}