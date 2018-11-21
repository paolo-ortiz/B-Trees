import java.util.*;
import java.io.*;

public class ValuesManager {

	static RandomAccessFile file2; //needs to be declared so it can be used by other methods
	static long numRecords;

	//Constructor
	ValuesManager(String name) throws IOException {

		long i = 0; //initial read position

		//creates temporary file
		//when data files do not exist, then it creates one
		File tempFile = new File(name);
		boolean fileExists = tempFile.exists();

		if (fileExists) {

			//instantiate data.val RAF
			this.file2 = new RandomAccessFile(name, "rwd");

			//seek values
			file2.seek(i);
			i += 256;

			//get number of records
			numRecords = file2.readLong();

			System.out.println("FILE EXISTS"); //TEMP
			System.out.println(numRecords);
		}
		else {
			System.out.println("FILE DOES NOT EXIST"); //TEMP
			this.file2 = new RandomAccessFile(name, "rwd");
			this.numRecords = 0;
		}
	}

	/**
	* Inserts values to data.val
	*/
	public static void insert(long key, String value) throws IOException {

		byte[] byteArray = value.getBytes(); //coverts string to byte array
		file2.writeShort(byteArray.length); //writes length of byte array
		file2.write(byteArray); //writes actual byte array
		//closeData();

	}

	public static void closeData() throws IOException {
		file2.close();
	}

}