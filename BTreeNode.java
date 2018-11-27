import java.util.*;
import java.io.*;

public class BTreeNode {

	static long[] nodeArray;

	//Constructor
	BTreeNode() {

		//make an array of longs
		this.nodeArray = new long[14];

		//fill with partitions
		for (int i = 0; i < nodeArray.length; i++)
			nodeArray[i] = -1;

		//TEMP
		System.out.println("NODE CREATED");
	}

	//prints array, used for debug purposes
	public static void printArray() {

		for (int i = 0; i < nodeArray.length; i++)
			System.out.println(nodeArray[i]);
	}

	public static void insertKey(long key, int valueIndex) {

		for (int i = 2; i < 14; i += 3) {
			//if space is empty then insert key
			if (isEmpty(i)) {
				nodeArray[i] = key;
				insertValueIndex(i + 1, valueIndex);
				break;
			}
			//if key to be inserted is less than key in array, shift elements
			if (key < nodeArray[i]) {
				shiftElements(i);
				nodeArray[i] = key;
				insertValueIndex(i + 1, valueIndex);
				break;
			} 
		}
	}

	//inserts value index to array, called by insertKey()
	public static void insertValueIndex(int index, int valueIndex) {

		nodeArray[index] = valueIndex;
	}

	//checks if space is empty
	public static boolean isEmpty(int index) {

		//if the value index to right of key is -1, then it is empty
		if (nodeArray[index + 1] == -1)
			return true;
		else
			return false;
	}

	//shifts all elements up from current index
	//ex. if index of 1st key, shift from 2nd key onwards
	public static void shiftElements(int index) {

		//make copy of node array
		long[] temp = nodeArray;

		//go through each key in reverse and replace values
		for (int i = 11; i >= index; i -= 3) {

			//get key & value index from temp and add to higher place in nodeArray
			long tempKey = temp[i];
			long tempValueIndex = temp[i + 1];

			//if it is on the 4th key, remove values
			if (i == 11)
				removeValues(i);
			//else shift the values up
			else if (i != 11) {
				//replace values in nodeArray
				nodeArray[i + 3] = tempKey;
				nodeArray[i + 4] = tempValueIndex;

				//remove values where it was previously
				removeValues(i);
			} 

		}
		
	}

	//removes values in node array by replacing it with -1
	public static void removeValues(int index) {

		nodeArray[index] = -1;
		nodeArray[index + 1] = -1;
	}

	//get node so we write it to the file
	public static long[] returnNode (){
		return nodeArray;
	}


	//returns node array
	public static long[] getArray() {
		return nodeArray;
	}
}