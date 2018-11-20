import java.util.*;
import java.io.*;

public class ValuesManager {


	//Constructor
	ValuesManager(String name) throws IOException {

		
		long numRecords;

		//creates temporary file
		//when data files do not exist, then it creates one
		File tempFile = new File(name);
		boolean fileExists = tempFile.exists();

		if (fileExists) {
			//read number of records
			

			//insert values

			System.out.println("FILE EXISTS");
		}
		else {
			System.out.println("FILE DOES NOT EXIST");
			numRecords = 0;
			RandomAccessFile file2 = new RandomAccessFile(name, "rwd");
			
		}
	}

	public static void insert(long key, String value) {

		file2.writeLong(key);
	}

}