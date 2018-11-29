import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file; ////needs to be declared so it can be used by other methods
	static long numNodes; //total number of nodes
	static long seekLocation; //where file will seek to
	static long rootNodeIndex; //index of root node
	static int valueIndex = 0; //index of where value is located in data.val

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

			//instantiate data.val RAF
			this.file = new RandomAccessFile(name, "rwd");

			//seek num of nodes
			file.seek(seekLocation);
			//get num of nodes
			numNodes = file.readLong();

			//seek root node index
			file.seek(seekLocation + 8);
			//get root node index
			rootNodeIndex = file.readLong();			
		}
		//else if first time creating data.bt
		else {

			this.file = new RandomAccessFile(name, "rwd"); //instantiate data.bt RAF
			this.numNodes = 1; //set num of nodes to 1
			this.rootNodeIndex = 0; //set root node index to 0

			//create inital node
			this.initialNode = new BTreeNode();

			//write numNodes to the file
			file.writeLong(numNodes);

			//write rootNodeIndex to file
			file.writeLong(rootNodeIndex);

			//write values to node
			writeValuesToNode(initialNode);
		}
	}

	//used for initialization
	//writes node array to data.bt
	//requires node to be initialized
	public static void writeValuesToNode(BTreeNode node) throws IOException {

		long[] temp = node.getArray(); //get node array

		file.seek(16); //go to place after the first 2 longs MIGHT HAVE TO CHANGE SEEK VALUE

		//write all longs to data.bt
		for (int i = 0; i < temp.length; i++)
			file.writeLong(temp[i]);
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
	}

	//closes data.bt properly when program is closed
	public static void closeData() throws IOException {
		file.close();
	}

	public static int getValueIndex(long key) throws IOException {

		int tempIndex = 2;

		//go thru the 4 keys
		for(int seekLocation = 32; seekLocation < 128; seekLocation += 8) {
			file.seek(seekLocation); //seeks key

			//TODO: FIX -1

			// //if key is -1
			// if (key == -1 && file.readLong() == key) {
			// 	if (!(initialNode.isEmpty(tempIndex))) {
			// 		file.seek(seekLocation + 8); //gets the value index
			// 		long tempValIndex = file.readLong();

			// 		System.out.println(tempValIndex);
			// 		return Math.toIntExact(tempValIndex); //convert to int before returning
			// 	}
			// }

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