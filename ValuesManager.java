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

			System.out.println("DATA.VAL EXISTS"); //TEMP
			
		}
		else {
			System.out.println("DATA.VAL DOES NOT EXIST"); //TEMP
			this.file2 = new RandomAccessFile(name, "rwd");
			this.numRecords = 0;

			//write numRecords to the file
			file2.writeLong(numRecords);
		}
	}

	//Inserts values to data.val
	public static void insert(long key, String value) throws IOException {
		
		seekLocation = (8 + numRecords * 256);
		file2.seek(seekLocation);

		byte[] byteArray = value.getBytes("UTF8"); //coverts string to byte array
		file2.writeByte(byteArray.length); //writes length of byte array
		file2.write(byteArray); //writes actual byte array

		incrementRecords();
	}

	public static void closeData() throws IOException {
		file2.close();
	}

	//increments number of records & updates data.val
	public static void incrementRecords() throws IOException {
		numRecords++; //increment number of records
		file2.seek(0); //go to start of data.val, where number of records is stored

		//updates records at data.val
		file2.writeLong(numRecords);

		System.out.printf("Number of Records: %d\n", numRecords); //TEMP
	}

	//return number of records
	public static int getNumRecords() {
		return Math.toIntExact(numRecords);
	}

	public static String getString(int valueIndex) throws IOException {

		seekLocation = (8 + valueIndex * 256); //go to correct string length
		file2.seek(seekLocation);

		int length = file2.readByte(); //get length of byte array
		byte[] temp = new byte[length]; //to read byte array
		file2.read(temp); //read values to temp

		String value = new String(temp, "UTF-8");

		return value;

	}

}