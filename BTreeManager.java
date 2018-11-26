import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file;
	static long numNodes;
	static long seekLocation;

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

			System.out.println("DATA.BT EXISTS"); //TEMP
			
		}
		else {
			System.out.println("DATA.BT DOES NOT EXIST"); //TEMP
			this.file = new RandomAccessFile(name, "rwd");
			this.numNodes = 1;

			//create inital node
			BTreeNode node1 = new BTreeNode();
			insertToNode(node1); //TEMP

			//write numNodes to the file
			file.writeLong(numNodes);
		}

		
	}


	public static void insertToNode(BTreeNode node) throws IOException {

		node.printArray();
	}

	public static void closeData() throws IOException {
		file.close();
	}

	// public static BTreeNode getNode() throws IOException {
		
	// }

}