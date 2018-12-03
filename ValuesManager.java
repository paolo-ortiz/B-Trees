import java.util.*;
import java.io.*;

public class ValuesManager {

	static RandomAccessFile file2; //needs to be declared so it can be used by other methods
	static long numRecords; //number of records in data.val
	static long seekLocation; //pointer for data.val

//-------------------------------------------------------------------------------------

	//Constructor
	//requires name of .val file
	ValuesManager(String name) throws IOException {

		seekLocation = 0; //initial read position

		//creates temporary file
		//when data files do not exist, then it creates one
		File tempFile = new File(name);
		boolean fileExists = tempFile.exists();

		//if data.val already exists
		if (fileExists) {

			//instantiate data.val RAF
			this.file2 = new RandomAccessFile(name, "rwd");

			//seek num of records
			file2.seek(seekLocation);
			
			//get number of records
			numRecords = file2.readLong();
		}
		//else if first time creating data.val
		else {

			this.file2 = new RandomAccessFile(name, "rwd"); //instantiate data.val RAF
			this.numRecords = 0; //set number of records to 0

			//write numRecords to the file
			file2.writeLong(numRecords);
		}
	}

//-------------------------------------------------------------------------------------

	//Inserts values to data.val
	//requires value to be placed in data.val
	public static void insert(String value) throws IOException {
		
		//everytime it writes value, it seeks 256+ bytes
		seekLocation = (8 + numRecords * 256);
		file2.seek(seekLocation);

		byte[] byteArray = value.getBytes("UTF8"); //coverts string to byte array
		file2.writeByte(byteArray.length); //writes length of byte array
		file2.write(byteArray); //writes actual byte array

		//update number of records
		incrementRecords();
	}

//-------------------------------------------------------------------------------------

	//closes data.val properly when program is closed
	public static void closeData() throws IOException {
		file2.close();
	}

//-------------------------------------------------------------------------------------

	//increments number of records & updates data.val
	public static void incrementRecords() throws IOException {
		numRecords++; //increment number of records
		file2.seek(0); //go to start of data.val, where number of records is stored

		//updates records at data.val
		file2.writeLong(numRecords);
	}

//-------------------------------------------------------------------------------------

	//return number of records
	public static int getNumRecords() {
		return Math.toIntExact(numRecords);
	}

//-------------------------------------------------------------------------------------

	//gets string value from data.val
	//requires valueIndex to determine how much to seek
	public static String getString(int valueIndex) throws IOException {

		seekLocation = (8 + valueIndex * 256); //go to correct string length
		file2.seek(seekLocation);

		int length = file2.readByte(); //get length of byte array
		byte[] temp = new byte[length]; //create byte array to read string
		file2.read(temp); //read values to temp array 

		String value = new String(temp, "UTF-8"); //convert bytes to string

		return value; //return converted string

	}

}