import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file; ////needs to be declared so it can be used by other methods
	static long numNodes; //total number of nodes
	static long seekLocation; //where file will seek to
	static long rootNodeIndex; //index of root node
	static int valueIndex = 0; //index of where value is located in data.val

	static long[] BTreeValues = new long[14]; //TEMP

	static BTreeNode initialNode; //TEMPORARY

	//Constructor
	//requires name of .bt file
	BTreeManager(String name) throws IOException {

		seekLocation = 0; //initial read position

		//creates temporary file
		//when data files do not exist, then it creates one
		File tempFile = new File(name);
		boolean fileExists = tempFile.exists();

		//if data.bt already exists
		if (fileExists) {

			//instantiate data.bt RAF
			this.file = new RandomAccessFile(name, "rwd");

			//updates the values in node array
			for (int i = 0; i < BTreeValues.length; i++) {
				file.seek(seekLocation + 8 * (i + 2)); //reads all the values in BTree, minus header
				BTreeValues[i] = file.readLong(); //updates values
			}	

			//refresh values
			this.initialNode = new BTreeNode(BTreeValues);
		}
		//else if first time creating data.bt
		else {

			this.file = new RandomAccessFile(name, "rwd"); //instantiate data.bt RAF
			this.numNodes = 1; //set num of nodes to 1
			this.rootNodeIndex = 0; //set root node index to 0
			
			//write numNodes to the file
			file.writeLong(numNodes);

			//write rootNodeIndex to file
			file.writeLong(rootNodeIndex);

			//write values to node
			//starts at 16 since it is the first node
			writeValuesToBTree(16);

			//create inital node
			this.initialNode = new BTreeNode(BTreeValues);

		}
	}

	//used for initialization
	//writes node array to data.bt
	//requires starting location to know where to seek
	public static void writeValuesToBTree(long seekLocation) throws IOException {

		//create a size 14 array with -1s
		for (int i = 0; i < BTreeValues.length; i++)
			BTreeValues[i] = -1;

		//go to place after first 2 longs TEMP VALUE
		file.seek(seekLocation);

		//write all longs to data.bt
		for (int i = 0; i < BTreeValues.length; i++)
			file.writeLong(BTreeValues[i]);
	}

	//inserts node to node array & updates data.bt
	//requires key & index, which will be written to data.bt
	public static void insertToNode (long key, int index) throws IOException {
		
		initialNode.insertKey(key,valueIndex); //insert key & valueIndex into node array

		//seek to correct position SEEK POSITION IS TEMPORARY
		file.seek(16);

		//get node array
		long[] temp = initialNode.getArray();

		//write array to file
		for (int i = 0; i < temp.length; i++)
			file.writeLong(temp[i]);
		
		valueIndex++; //increment valueIndex

		//TEMP
		if (initialNode.isFull())
			System.out.println("Node is Full");
		//TEMP
	}

//-------------------------------------------------------------------------------------

	//writes new node array to data.bt
	//used when current node is full
	public static void writeNewNode() throws IOException {

		//goes to last place in data.bt
		seekLocation = 16 + numNodes * 112; 

		//write values
		writeValuesToBTree(seekLocation);
	}

	//check if key already exists
	//requires key to check if is present;
	public static boolean isPresent(long key) {

		//checks if key is in node
		boolean exists = initialNode.keyExists(key);

		return exists;
	}

	//closes data.bt properly when program is closed
	public static void closeData() throws IOException {
		file.close();
	}

	//returns value index of key
	//requires key to get value index
	public static int getValueIndex(long key) throws IOException {

		int tempIndex = 2;

		//go thru the 4 keys
		for(int seekLocation = 32; seekLocation < 128; seekLocation += 8) {
			file.seek(seekLocation); //seeks key

			//TODO: FIX -1

			//if key is -1
			if (key == -1 && file.readLong() == key) {
				if (!(initialNode.isEmpty(tempIndex))) {
					file.seek(seekLocation + 8); //gets the value index
					long tempValIndex = file.readLong();

					System.out.println(tempValIndex);
					return Math.toIntExact(tempValIndex); //convert to int before returning
				}
			}

			//if key matches
			if (file.readLong() == key) {
				file.seek(seekLocation + 8); //gets the value index
				long tempValIndex = file.readLong();

				return Math.toIntExact(tempValIndex); //convert to int before returning

			
			}

			tempIndex += 3;

		}

		return -1;
			
	}


}