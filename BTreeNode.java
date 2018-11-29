import java.util.*;
import java.io.*;

public class BTreeNode {

	static long[] nodeArray; //contains 14 long ints, which correspond to values

	//Constructor
	BTreeNode() {

		//instantiate array of longs
		this.nodeArray = new long[14];

		//fill with partitions, -1 means space is empty
		for (int i = 0; i < nodeArray.length; i++)
			nodeArray[i] = -1;
	}

	//prints array, used for debug purposes
	public static void printArray() {

		//prints each value on a new line
		for (int i = 0; i < nodeArray.length; i++)
			System.out.println(nodeArray[i]);
	}

	//inserts key & valueIndex into node array
	//requires key & valueIndex, which will be inserted
	public static void insertKey(long key, int valueIndex) {

		//starts at 2, which is index of 1st key
		//increments by 3, to next key
		for (int i = 2; i < 13; i += 3) {
			
			//if space is empty then insert key & value index
			if (isEmpty(i)) {
				nodeArray[i] = key; //insert key
				nodeArray[i + 1] = valueIndex; //insert valueIndex at space next to key
				break;
			}

			//if key to be inserted is less than key in array, shift elements
			if (key < nodeArray[i]) {
				shiftElements(i); //shift elements
				nodeArray[i] = key; //insert key
				nodeArray[i + 1] = valueIndex; //insert valueIndex at space next to key
				break;
			} 
		}
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
	//ex. if index of 1st key, shift from all elements from 1st key
	public static void shiftElements(int index) {

		//make copy of node array
		long[] temp = nodeArray;

		//go through each key in reverse and replace values
		//4th key is at index 11
		for (int i = 11; i >= index; i -= 3) {

			//get key & value index from temp
			long tempKey = temp[i];
			long tempValueIndex = temp[i + 1];

			//if it is on the 4th key, remove values
			if (i == 11)
				removeValues(i);
			//else shift the values up
			else if (i != 11) {
				//place values in higher index in nodeArray
				nodeArray[i + 3] = tempKey;
				nodeArray[i + 4] = tempValueIndex;

				//remove values where it was previously
				removeValues(i);
			} 

		}
		
	}

	//removes values in node array by replacing it with -1
	//requires index so it knows what to remove
	public static void removeValues(int index) {

		nodeArray[index] = -1;
		nodeArray[index + 1] = -1;
	}

	//returns node so it can be written to data.bt
	public static long[] returnNode (){
		return nodeArray;
	}


	//returns node array
	public static long[] getArray() {
		return nodeArray;
	}
}