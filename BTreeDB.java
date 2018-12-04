import java.util.*;
import java.io.*;

public class BTreeDB {
	
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
					return;
				}
				
				//else if input is "insert", check if it is valid
				//first, make sure input has more than 1 value
				if (command.equals("insert")) {

					long key = rd.nextLong();
					String value = rd.nextLine().trim();

					//check if key already exists
					boolean exists = btm.isPresent(key);

					//if it does not exist, then insert
					if (exists == false) {
						int tempIndex = vm.getNumRecords();

						insertToVal(value, vm);
						insertToBT(key, tempIndex, btm);
						System.out.printf("%d inserted\n", key);
					}
					//else if key already exists 
					else 
						System.out.printf("ERROR: %d already exists\n", key);	
				}
				else if (command.equals("select")) {

					long key = rd.nextLong(); //gets key from command

					int valueIndex = btm.getValueIndex(key); //get value index

					select(key, valueIndex, vm);
				}

				//else if invalid command, print "invalid command"
				else
					System.out.println("ERROR: Invalid Command");

			}
		} catch (IOException e) {
			System.out.println("IOException in main");
		}
	}	

//-------------------------------------------------------------------------------------

	//inserts value into data.val
	public static void insertToVal(String word, ValuesManager vm) throws IOException {
		vm.insert(word);
	}

//-------------------------------------------------------------------------------------

	//inserts value into data.bt
	public static void insertToBT(long key, int index, BTreeManager btm) throws IOException {
		btm.insertToNode(key,index);
	}

//-------------------------------------------------------------------------------------

	public static void select(long key, int valueIndex, ValuesManager vm) throws IOException {

		if (valueIndex != -1) { //a value of -1 means key is not in btree
			String value = vm.getString(valueIndex); //get string
			System.out.printf("%d ==> %s\n", key, value);
		} else 
			System.out.printf("ERROR: %d does not exist\n", key);
	}
}