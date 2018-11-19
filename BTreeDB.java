import java.util.*;
import java.io.*;

public class BTreeDB {
	
	public static void main( String[] args ) {

		try {
			RandomAccessFile file = new RandomAccessFile(args[0], "rwd");
			RandomAccessFile file2 = new RandomAccessFile(args[1], "rwd");

			Scanner sc = new Scanner(System.in);
			while (sc.hasNext()) {

				String input = sc.nextLine(); //get input

				//for getting the inputs
				Scanner rd = new Scanner(input);
				String command = rd.next();

				//if input is "exit", close program
				if (command.equals("exit")) {
					file.close();
					file2.close();
					return;
				}

				long key = rd.nextLong();
				String value = rd.nextLine();


				
				//else if input is "insert", check if it is valid
				//first, make sure input has more than 1 value
				if (command.equals("insert")) {

					//if first value is an integer AND there is no string after
					//FOR NOW, ASSUME USER ALWAYS INPUTS CORRECT INPUT
					
					insert(key, value);

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

	//inserts value into tree
	public static void insert(long key, String word) {
		
		System.out.printf("Inserted %s\n", word);
	}

	//splits input into array to get values
	public static String[] splitInput(String input) {

		String[] split = input.split(" ");
		return split;
	}
}