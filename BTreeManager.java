import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file; ////needs to be declared so it can be used by other methods
	static long numNodes; //total number of nodes
	static long seekLocation; //where file will seek to
	static long rootNodeIndex; //index of root node
	static int valueIndex = 0; //index of where value is located in data.val

	static long[] initialBTreeValues = new long[14];

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
					initialBTreeValues[move] = file.readLong(); 
					move++;
					
				}

				BTreeNode node = new BTreeNode(initialBTreeValues);

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

			//write values to node
			//starts at 16 since it is the first node
			writeValuesToBTree(16, initialBTreeValues);

			//create inital node
			BTreeNode initialNode = new BTreeNode(initialBTreeValues);
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

	//inserts node to node array & updates data.bt
	//requires key & index, which will be written to data.bt
	public static void insertToNode (long key, int index) throws IOException {
		
		//go through each node in arraylist
		for (int i = 0; i < arrListOfBTreeNodes.size(); i++) {

			BTreeNode currentNode = arrListOfBTreeNodes.get(i); //get node
			boolean insertedWhenFull = false; //if values was inserted when node is already full, set to true 
			long lastKey = 0, lastValueIndex = 0; //set temporary values

			//if node is full, save last values
			if (currentNode.isFull()) {
				lastKey = currentNode.getLastKey();
				lastValueIndex = currentNode.getLastValueIndex();
				insertedWhenFull = true;
			}

			//insert key & valueIndex into node array
			currentNode.insertKey(key, index);

			//get node array
			long[] temp = currentNode.getArray();

			if (insertedWhenFull) 
				splitRootNode(currentNode, lastKey, lastValueIndex);

			//write array to file
			for (int j = 0; j < temp.length; j++) {
				file.seek(8 * (j + 2));
				file.writeLong(temp[j]);
			}
	
			valueIndex++; //increment valueIndex

			
		}
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

		System.out.println("SPLIT");

		//get node values
		long[] tempBTreeValues = node.getArray();

		//put left 2 values in a new node
		long[] leftChildValues = new long[14];
		for (int i = 0; i < 14; i++) {

			if (i == 2) { //if at 1st key
				leftChildValues[i] = tempBTreeValues[i]; //set key
				leftChildValues[i + 1] = tempBTreeValues[i + 1]; //set value index

			} else if (i == 5) { //if at 2nd key
				leftChildValues[i] = tempBTreeValues[i]; //set key
				leftChildValues[i + 1] = tempBTreeValues[i + 2]; //set value index

			} else if (i != 3 || i != 6) //if value is not pointing to 1st 2 value indexes
				leftChildValues[i] = -1;
		}

		//put right 2 values in new node
		long[] rightChildValues = new long[14];
		for (int i = 0; i < 14; i++) {

			if (i == 8) { //if at 3rd key
				rightChildValues[i] = tempBTreeValues[i]; //set key
				rightChildValues[i + 1] = tempBTreeValues[i + 1]; //set value index

			} else if (i == 11) { //if at 4th key
				rightChildValues[i] = lastKey; //set key
				rightChildValues[i + 1] = lastValueIndex; //set value index

			} else if (i != 9 || i != 12) //if value is not pointing to last 2 value indexes
				rightChildValues[i] = -1;
		}

		writeNewNode(leftChildValues);
		BTreeNode leftChild = new BTreeNode(leftChildValues);
		arrListOfBTreeNodes.add(leftChild);

		writeNewNode(rightChildValues);
		BTreeNode rightChild = new BTreeNode(rightChildValues);
		arrListOfBTreeNodes.add(rightChild);

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

		//update number of nodes
		incrementNodes();

		// //add new node to array list
		// BTreeNode tempNode = new BTreeNode(BTreeValues);
		// arrListOfBTreeNodes.add(tempNode);
	}

//-------------------------------------------------------------------------------------

	public static void writeArrayToBTree(long seekLocation, long[] BTreeValues) throws IOException {

		//go to place after first 2 longs TEMP VALUE
		file.seek(seekLocation);

		//write all longs to data.bt
		for (int i = 0; i < BTreeValues.length; i++)
			file.writeLong(BTreeValues[i]);
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

	//closes data.bt properly when program is closed
	public static void closeData() throws IOException {
		file.close();
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
}