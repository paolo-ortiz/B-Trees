import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file; ////needs to be declared so it can be used by other methods
	static long numNodes; //total number of nodes
	static long seekLocation; //where file will seek to
	static long rootNodeIndex; //index of root node
	static int valueIndex = 0; //index of where value is located in data.val

	static long[] BTreeValues = new long[14];

	static ArrayList<BTreeNode> arrListOfBTreeNodes; //contains all node arrays
	static ArrayList<long[]> arrListOfLongArrays; //contains all long[]

//-------------------------------------------------------------------------------------

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

			//instantiate array list
			arrListOfBTreeNodes = new ArrayList<BTreeNode>();

			//get number of nodes 
			file.seek(0);
			numNodes = file.readLong();

			//updates the values in each node array
			for (int nodeIndex = 0; nodeIndex < numNodes; nodeIndex++) {

				file.seek(16 + nodeIndex*112); 

				int move = 0; //temporary
				for(int cursor = 0; cursor < 112; cursor += 8){
					BTreeValues[move] = file.readLong(); 
					move++;
					
				}

				BTreeNode node = new BTreeNode(BTreeValues);

				arrListOfBTreeNodes.add(node);
			}
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

			//create a size 14 array with -1s
			for (int i = 0; i < BTreeValues.length; i++)
				BTreeValues[i] = -1;

			//write values to node
			//starts at 16 since it is the first node
			writeValuesToBTree(16, BTreeValues);

			//create inital node
			BTreeNode initialNode = new BTreeNode(BTreeValues);
			long[] temp = getNodeValues(initialNode, 0);
			
			//instantiate array lists
			arrListOfBTreeNodes = new ArrayList<BTreeNode>();
			arrListOfLongArrays = new ArrayList<long[]>();

			//add initial node to array list
			arrListOfBTreeNodes.add(initialNode);

			//add temp values to array list
			arrListOfLongArrays.add(temp);
		}
	}

//-------------------------------------------------------------------------------------

	//reads node values from data.bt & puts it into array
	//requires node to read files & nodeNumber to know where to seek
	public static long[] getNodeValues(BTreeNode node, int nodeNumber) throws IOException {
		
		long[] temp = new long[14]; //create temp array

		file.seek(8 + (nodeNumber * 56)); //reads all the values in node

		//updates the values in node array
		for (int i = 0; i < temp.length; i++) {
			
			temp[i] = file.readLong(); //updates values
		}

		return temp;
	}

//-------------------------------------------------------------------------------------

	//used for initialization
	//writes node array to data.bt
	//requires starting location to know where to seek
	public static void writeValuesToBTree(long seekLocation, long[] BTreeValues) throws IOException {

		//create a size 14 array with -1s
		for (int i = 0; i < BTreeValues.length; i++)
			BTreeValues[i] = -1;

		//go to place after first 2 longs TEMP VALUE
		file.seek(seekLocation);

		//write all longs to data.bt
		for (int i = 0; i < BTreeValues.length; i++)
			file.writeLong(BTreeValues[i]);
	}

//-------------------------------------------------------------------------------------

	// //inserts node to node array & updates data.bt
	// //requires key & index, which will be written to data.bt
	// public static void insertToNodee (long key, int index) throws IOException {
		
	// 	//go through each node in arraylist
	// 	for (int i = 0; i < arrListOfBTreeNodes.size(); i++) {

	// 		BTreeNode currentNode = arrListOfBTreeNodes.get(i); //get node
	// 		boolean insertedWhenFull = false; //if values was inserted when node is already full, set to true 
	// 		long lastKey = 0, lastValueIndex = 0; //set temporary values

	// 		//if trying to insert, but has no child node

	// 		//if node is full, save last values
	// 		if (currentNode.isFull()) {

	// 			//if key is greater than all values in node
	// 			if (currentNode.keyIsGreatestValue(key)) {
	// 				lastKey = key;
	// 				lastValueIndex = index;
	// 			} else {

	// 				lastKey = currentNode.getLastKey();
	// 				lastValueIndex = currentNode.getLastValueIndex();
	// 			}
	// 			insertedWhenFull = true;
	// 		}

	// 		//insert key & valueIndex into node array
	// 		currentNode.insertKey(key, index);

	// 		//get node array
	// 		long[] temp = currentNode.getArray();
	
	// 		valueIndex++; //increment valueIndex

	// 		if (insertedWhenFull) 
	// 			splitRootNode(currentNode, lastKey, lastValueIndex);
	// 		else {

	// 			// //write array to file
	// 			// for (int j = 0; j < temp.length; j++) {
	// 			// 	file.seek(8 * (j + 2));
	// 			// 	file.writeLong(temp[j]);
	// 			// }
	// 			seekLocation = 16 + i * 112;
	// 			//writeArrayToBTree(seekLocation, temp);
	// 		}

			
	// 	}

	// 	//start at root node
	// }

	public static void insertToNode2(long key, int index, long nodeIndex) throws IOException {

		//1st run starts at root node
		BTreeNode currentNode = arrListOfBTreeNodes.get(Math.toIntExact(nodeIndex)); //convert long to int
		long[] currentValues = currentNode.getArray();
		boolean insertedWhenFull = false; //if values was inserted when node is already full, set to true 
		long lastKey = 0, lastValueIndex = 0; //set temporary values

		//if current node is already full, save last values
		if (currentNode.isFull()) {

				//if key is greater than all values in node
				if (currentNode.keyIsGreatestValue(key)) {
					lastKey = key;
					lastValueIndex = index;
				} else {
					lastKey = currentNode.getLastKey();
					lastValueIndex = currentNode.getLastValueIndex();
				}
				insertedWhenFull = true;
		}

		//if key to be inserted is less than 1st key in node
		if (key < currentValues[2]) {

			//if there is no child, insert key in node
			if (currentValues[1] == -1)
				currentNode.insertKey(key, index);
			//else go to child node
			else
				insertToNode2(key, index, currentValues[1]);
		}

		//else if key to be inserted is greater than 1st key
		else if (key > currentValues[2] && key < currentValues[5]) {

			//if there is no child, insert key in node
			if (currentValues[4] == -1)
				currentNode.insertKey(key, index);
			//else go to child node
			else
				insertToNode2(key, index, currentValues[4]);
		}

		//else if key to be inserted is greater than 2nd key
		else if (key > currentValues[5] && key < currentValues[8]) {

			//if there is no child, insert key in node
			if (currentValues[7] == -1)
				currentNode.insertKey(key, index);
			//else go to child node
			else
				insertToNode2(key, index, currentValues[7]);
		}

		//else if key to be inserted is greater than 3rd key
		else if (key > currentValues[8] && key < currentValues[11]) {

			//if there is no child, insert key in node
			if (currentValues[10] == -1)
				currentNode.insertKey(key, index);
			//else go to child node
			else
				insertToNode2(key, index, currentValues[10]);
		}

		//else if key to be inserted is greater than 4th key
		else if (key > currentValues[11]) {

			//if there is no child, insert key in node
			if (currentValues[13] == -1)
				currentNode.insertKey(key, index);
			//else go to child node
			else
				insertToNode2(key, index, currentValues[13]);
		}

		valueIndex++; //increment valueIndex

		if (insertedWhenFull) 
			splitRootNode(currentNode, lastKey, lastValueIndex);
	}
//-------------------------------------------------------------------------------------

	public static void splitNode() {

		//get last value in node

		//insert key in node

		//create 2 new nodes

		//push middle to parent node

		//push to other nodes

		//add nodes to arraylist

		//
	}

//-------------------------------------------------------------------------------------

	public static void splitRootNode(BTreeNode node, long lastKey, long lastValueIndex) throws IOException {

		//get node values
		long[] tempBTreeValues = node.getArray();

		//put left 2 values in a new node
		long[] leftChildValues = new long[14];
		for (int i = 0; i < 14; i++) {

			if (i == 2) { //if at 1st key
				leftChildValues[2] = tempBTreeValues[2]; //set key
				leftChildValues[3] = tempBTreeValues[3]; //set value index

			} else if (i == 5) { //if at 2nd key
				leftChildValues[5] = tempBTreeValues[5]; //set key
				leftChildValues[6] = tempBTreeValues[6]; //set value index

			} else if (i == 3 || i == 6) 
				continue;
			else
				leftChildValues[i] = -1;		
		}
		//set parent of left child TEMP VALUE
		leftChildValues[0] = 2;
		
		//put right 2 values in new node
		long[] rightChildValues = new long[14];
		for (int i = 0; i < 14; i++) {

			if (i == 2) { //if at 3rd key
				rightChildValues[i] = tempBTreeValues[11]; //set key
				rightChildValues[i + 1] = tempBTreeValues[12]; //set value index

			} else if (i == 5) { //if at 4th key
				rightChildValues[i] = lastKey; //set key
				rightChildValues[i + 1] = lastValueIndex; //set value index

			} else if (i == 3 || i == 6)
				continue;
			else
				rightChildValues[i] = -1;
		}
		//set parent of right child TEMP VALUE
		rightChildValues[0] = 2;

		//put middle value into root node
		long[] root = new long[14];
		for (int i = 0; i < 14; i++) {

			if (i == 2) { //if at 1st key
				root[i] = tempBTreeValues[i + 6]; //set key
				root[i + 1] = tempBTreeValues[i + 7]; //set value index

			} else if (i != 3)
				root[i] = -1;
		}
		//set children of root node TEMP VALUE
		root[1] = 0;
		root[4] = 1;
		
	    overwriteNode(0, leftChildValues); //node 0 gets left children //overwriting everything
	    writeNewNode(rightChildValues); //node 1 gets right children
	    writeNewNode(root); //node 2 gets middle value

	    //update root node index
	    updateRootNodeIndex(2);
	    
	}

//-------------------------------------------------------------------------------------

	//writes new node array to data.bt
	//used when current node is full
	//requires long[], which will be written
	public static void writeNewNode(long[] BTreeValues) throws IOException { 		

		//goes to last place in data.bt
		seekLocation = 16 + numNodes * 112; 

		//write values
		writeArrayToBTree(seekLocation, BTreeValues);

		//add new node to array list
		BTreeNode tempNode = new BTreeNode(BTreeValues);
		arrListOfBTreeNodes.add(tempNode);

		//update number of nodes
		incrementNodes();
	}

//-------------------------------------------------------------------------------------

	//overwrites a node already in array list
	//requires nodeIndex of node to be rewritten
	//requires array of values to overwrite node
	public static void overwriteNode(int nodeIndex, long[] newNodeValues) throws IOException {

		BTreeNode newNode = new BTreeNode(newNodeValues); //create new node
		arrListOfBTreeNodes.set(nodeIndex, newNode); //replace node in arraylist

		seekLocation = 16 + nodeIndex * 112; //go to location of node
		writeArrayToBTree(seekLocation, newNodeValues); //overwrite values in data.bt


	}

//-------------------------------------------------------------------------------------

	public static void writeArrayToBTree(long seekLocation, long[] nodeValues) throws IOException {
		

		//write all longs to data.bt
		for (int i = 0; i < nodeValues.length; i++){
			//go to place after first 2 longs TEMP VALUE
			file.seek(seekLocation);
			file.writeLong(nodeValues[i]);
			seekLocation+=8;
		}
		
	}

//-------------------------------------------------------------------------------------	

	//check if key already exists
	//requires key to check if is present;
	public static boolean isPresent(long key) {

		//go through each node & check if key is in node
		for (int i = 0; i < arrListOfBTreeNodes.size(); i++) {

			BTreeNode tempNode = arrListOfBTreeNodes.get(i); //gets node
			boolean exists = tempNode.keyExists(key); //check if key exists in node

			//if key exists, return true
			if (exists)
				return true;
		}

		//else if key is not found, return false
		return false;
	}

//-------------------------------------------------------------------------------------

	//returns value index of key
	//requires key to get value index
	public static int getValueIndex(long key) throws IOException {

		int tempIndex = 2;

		//go through each node in array list
		for (int i = 0; i < arrListOfBTreeNodes.size(); i++) {

			BTreeNode tempNode = arrListOfBTreeNodes.get(i);

			//go through the 4 keys
			for(int seekLocation = 32; seekLocation < 128; seekLocation += 8) {
				file.seek(seekLocation); //seeks key

				//TODO: FIX -1

				//if key is -1
				if (key == -1 && file.readLong() == key) {
					if (!(tempNode.isEmpty(tempIndex))) {
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
		}
		return -1;
			
	}

//-------------------------------------------------------------------------------------

	//increments number of records & updates data.bt
	public static void incrementNodes() throws IOException {

		numNodes++; //increment number of nodes
		file.seek(0); //go to start of data.bt, where number of nodes is stored

		//updates records at data.bt
		file.writeLong(numNodes);
	}	

//-------------------------------------------------------------------------------------

	//return root node index
	public static long getRootNodeIndex() throws IOException {

		file.seek(8);
		return file.readLong();
	}

//-------------------------------------------------------------------------------------

	//updates root node index in data.bt
	//hardcoded for now
	public static void updateRootNodeIndex(long newRootNodeIndex) throws IOException {

		rootNodeIndex = newRootNodeIndex; //get new root node
		file.seek(8); //go to where root node index is stored

		//update root node index
		file.writeLong(rootNodeIndex);
	}

//-------------------------------------------------------------------------------------

	//closes data.bt properly when program is closed
	public static void closeData() throws IOException {
		file.close();
	} 
}