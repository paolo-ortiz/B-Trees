import java.util.*;
import java.io.*;

public class ValuesManager {

	static RandomAccessFile file2; //needs to be declared so it can be used by other methods
	static long numRecords;
	static long seekLocation;

	//Constructor
	ValuesManager(String name) throws IOException {

		seekLocation = 0; //initial read position

		//creates temporary file
		//when data files do not exist, then it creates one
		File tempFile = new File(name);
		boolean fileExists = tempFile.exists();

		if (fileExists) {

			//instantiate data.val RAF
			this.file2 = new RandomAccessFile(name, "rwd");

			//seek num of records
			file2.seek(seekLocation);

			//get number of records
			numRecords = file2.readLong();

			System.out.println("FILE EXISTS"); //TEMP
			
		}
		else {
			System.out.println("FILE DOES NOT EXIST"); //TEMP
			this.file2 = new RandomAccessFile(name, "rwd");
			this.numRecords = 0;

			//write numRecords to the file
			file2.writeLong(numRecords);
		}
	}

	/**
	* Inserts values to data.val
	*/
	public static void insert(long key, String value) throws IOException {

		file2.seek(seekLocation);

		byte[] byteArray = value.getBytes(); //coverts string to byte array
		file2.writeShort(byteArray.length); //writes length of byte array
		file2.write(byteArray); //writes actual byte array

		seekLocation += 256; //CHANGE THIS VALUE

		incrementRecords();
	}

	public static void closeData() throws IOException {
		file2.close();
	}

	//increments number of records & updates val
	public static void incrementRecords() throws IOException {
		numRecords++;
		file2.seek(0);

		//updated records at val
		file2.writeLong(numRecords);

		System.out.printf("Number of Records: %d\n", numRecords);
	}

}