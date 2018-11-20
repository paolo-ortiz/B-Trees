import java.util.*;
import java.io.*;

public class ValuesManager {

	static RandomAccessFile file2; //needs to be declared so it can be used by other methods

	//Constructor
	ValuesManager(String name) throws IOException {

		long numRecords;

		//creates temporary file
		//when data files do not exist, then it creates one
		File tempFile = new File(name);
		boolean fileExists = tempFile.exists();

		if (fileExists) {
			//do something

			//instantiate data.val RAF
			this.file2 = new RandomAccessFile(name, "rwd");

			System.out.println("FILE EXISTS"); //TEMP
		}
		else {
			System.out.println("FILE DOES NOT EXIST"); //TEMP
			numRecords = 0;
			
			
		}
	}

	/**
	* Inserts values to data.val
	*/
	public static void insert(long key, String value) throws IOException {

		byte[] byteArray = value.getBytes(); //coverts string to byte array
		file2.writeShort(byteArray.length); //writes length of byte array
		file2.write(byteArray); //writes actual byte array
	}

}