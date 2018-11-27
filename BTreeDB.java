import java.util.*;
import java.io.*;

public class BTreeDB {

	//make node
	public static BTreeNode node1 = new BTreeNode();
	
	public static void main( String[] args ) {

		try {
			
			BTreeManager btm = new BTreeManager(args[0]);
			ValuesManager vm = new ValuesManager(args[1]);
			BTreeNode bn = btm.getNode();

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
					

					insertToVal(key, value, vm);

					//else if there is more than 2 elements

				}

				//insert to node dapat to
				else if (command.equals("insertTest")) {
					
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

	//inserts value into data.val
	public static void insertToVal(long key, String word, ValuesManager vm) throws IOException {
		
		System.out.printf("Inserted %s\n", word);
		vm.insert(key, word);

	}

	//inserts value into data.bt
	public static void insertToBT(long key, long index, BTreeManager btm) {
		


	}

	
}