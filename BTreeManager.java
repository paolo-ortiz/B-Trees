import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file;
	static long numNodes; //total number of nodes
	static long seekLocation; //where file will seek to
	static long rootNodeIndex; //index of root node
	static int valueIndex = 0; //index of where value is located in data.val
	static boolean exists = false; // for exist checks
	static BTreeNode initialNode; //TEMPORARY
	static long[] stuffFromBT = new long[14]; //temporary for now (matthew) 
	static long move = 0; //initial value for seeking
	//Constructor
	BTreeManager(String name) throws IOException {

		seekLocation = 0;

		//creates temporary file
		//when data files do not exist, then it creates one
		File tempFile = new File(name);
		boolean fileExists = tempFile.exists();
		
		if (fileExists) {

			//instantiate data.val RAF
			this.file = new RandomAccessFile(name, "rwd");

			//seek num of nodes
			// file.seek(seekLocation);
			
			//get num of nodes
			// numNodes = file.readLong();

			//seek root node index
			// file.seek(seekLocation + 8);
			//get root node index
			// rootNodeIndex = file.readLong();
			

			//renews stuffFromBT
			for(int i = 0; i <stuffFromBT.length; i++){
				file.seek(seekLocation+8*(i+2));
				stuffFromBT[i] = file.readLong();	
			}
			
			//refresh node
			this.initialNode = new BTreeNode(stuffFromBT);
			System.out.println("DATA.BT EXISTS"); //TEMP
			
		}
		else {
			
			System.out.println("DATA.BT DOES NOT EXIST"); //TEMP

			//initialize values
			this.file = new RandomAccessFile(name, "rwd");
			
			this.numNodes = 1;
			this.rootNodeIndex = 0;

			
			//write numNodes to the file
			file.writeLong(numNodes);

			//write rootNodeIndex to file
			file.writeLong(rootNodeIndex);

			//write values to node
			writeValuesToBTree();
			
			//get values from the raf place them in an array to give to node
			this.initialNode = new BTreeNode(stuffFromBT);
		}
	}

	// chnaging the initial (temporary)
	public static void writeValuesToBTree() throws IOException {

		//create a size 14 array with -1
		for(int i =0; i<stuffFromBT.length; i++){
			stuffFromBT[i] = -1;
		}

		file.seek(16); //go to place after the first 2 longs MIGHT HAVE TO CHANGE SEEK VALUE

		//write all longs to data.bt
		for (int i = 0; i < stuffFromBT.length; i++)
			file.writeLong(stuffFromBT[i]);
	}
	
	
		// PREVIOUS WRITE VALUES TO NODE METHOD
		// public static void writeValuesToNode(BTreeNode node) throws IOException {

		// long[] temp = node.getArray();

		// file.seek(16); //go to place after the first 2 longs MIGHT HAVE TO CHANGE SEEK VALUE

		//write all longs to data.bt
		// for (int i = 0; i < temp.length; i++)
			// file.writeLong(temp[i]);
	// }

	
	public static void insertToNode (long key, int index) throws IOException {
		
		initialNode.insertKey(key,valueIndex); //insert key into node array

		//seek to correct position SEEK POSITION IS TEMPORARY
		file.seek(16);

		//get node array
		long[] temp = initialNode.getArray();

		//write array to file
		for (int i = 0; i < temp.length; i++)
			file.writeLong(temp[i]);
		
		valueIndex++;

		//initialNode.printArray();
	}

	//check if key already exists
	public static boolean present(long key){
		
		exists = initialNode.existance(key);
		//works
		return exists;
		
	}
	
	public static void closeData() throws IOException {
		file.close();
	}
	
	//for selecting code taken from git
	
}