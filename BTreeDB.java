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
				String[] inputArr = splitInput(input); //split input


				//if input is "exit", close program
				if (inputArr[0].equals("exit")) {
					file.close();
					file2.close();
					return;
				}
				//else if input is "insert", check if it is valid
				//first, make sure input has more than 1 value
				else if (inputArr[0].equals("insert") && inputArr.length > 1) {
					//if first value is an integer AND there is no string after
					if (isInteger(inputArr[1]) && inputArr.length == 2) {
						insert(inputArr[1], "");
						System.out.println("Inserted");
					}
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
	public static void insert(Object hash, String word) {
		
		System.out.printf("Inserted %s", word);
	}

	//splits input into array to get values
	public static String[] splitInput(String input) {

		String[] split = input.split(" ");
		return split;
	}

	public static boolean isInteger(Object object) {
		
		if (object instanceof Integer)
			return true;
		// else {
		// 	String string = object.toString();

		// 	try {
		// 		Integer.parseInt(string);
		// 	} catch (Exception e) {
		// 		return false;
		// 	}
		// }
		return false;
	}
}