import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file;
	static long numNodes; //total number of nodes
	static long seekLocation; //where file will seek to
	static long rootNodeIndex; //index of root node
	static int valueIndex = 0; //index of where value is located in data.val
	static long[] thingsTopPutInFile = new long[14];

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
			file.seek(seekLocation);
			//get num of nodes
			numNodes = file.readLong();

			//seek root node index
			file.seek(seekLocation + 8);
			//get root node index
			rootNodeIndex = file.readLong();

			System.out.println("DATA.BT EXISTS"); //TEMP
			
		}
		else {
			System.out.println("DATA.BT DOES NOT EXIST"); //TEMP

			//initialize values
			this.file = new RandomAccessFile(name, "rwd");
			this.numNodes = 1;
			this.rootNodeIndex = 0;


			//create inital node
			BTreeNode node1 = new BTreeNode();
			//might not need this
			thingsTopPutInFile = node1.returnNode(); 

			//might not need this
			// //initial write to file
			// for(int i = 0; i < thingsTopPutInFile.length;i++){
			// 	file.writeLong(thingsTopPutInFile[i]);
			// }

			//write numNodes to the file
			file.writeLong(numNodes);

			//write rootNodeIndex to file
			file.writeLong(rootNodeIndex);

			//write values to node
			writeValuesToNode(node1);
		}
	}

	public static void writeValuesToNode(BTreeNode node) throws IOException {

		long[] temp = node.getArray();

		file.seek(16); //go to place after the first 2 longs MIGHT HAVE TO CHANGE SEEK VALUE

		//write all longs to data.bt
		for (int i = 0; i < temp.length; i++)
			file.writeLong(temp[i]);
	}

	public static void insertToNode ( long key, int valueIndex) throws IOException {
		// create node here that pulls value from random access file
		BTreeNode node = new BTreeNode(); 
		node.insertKey(key,valueIndex); 
		//write to file then?
		file.seek(16);
		thingsTopPutInFile = node.getArray();
		for(int i = 0; i < thingsTopPutInFile.length; i++){
			file.writeLong(thingsTopPutInFile[i]);
		}
		//node.insertKey(key, valueIndex);
		valueIndex++;
	}

	public static void closeData() throws IOException {
		file.close();
	}

	// public static BTreeNode getNode() {
	// 	return node1;
	// } 
}