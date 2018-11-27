import java.util.*;
import java.io.*;

public class BTreeManager {

	static RandomAccessFile file;
	static long numNodes;
	static long seekLocation;
	static long rootNodeIndex;

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

	public static void insertToNode(BTreeNode node) throws IOException {

		
	}

	public static void closeData() throws IOException {
		file.close();
	}

}