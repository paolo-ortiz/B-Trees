import java.util.*;
import java.io.*;

public class BTreeNode {

	static long[] nodeArray; //contains 14 long ints, which correspond to values

//-------------------------------------------------------------------------------------

	//Constructor
	//requires long[] to update values in node
	BTreeNode(long[] BTreeValues) {

		//instantiate array of longs
		this.nodeArray = new long[14];

		//fill with partitions, -1 means space is empty
		for (int i = 0; i < nodeArray.length; i++)
			nodeArray[i] = BTreeValues[i];
	}

//-------------------------------------------------------------------------------------

	//prints array, used for debug purposes
	public static void printArray() {

		//prints each value on a new line
		for (int i = 0; i < nodeArray.length; i++)
			System.out.println(nodeArray[i]);
	}

//-------------------------------------------------------------------------------------

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

//-------------------------------------------------------------------------------------

	//checks if space is empty
	public static boolean isEmpty(int index) {

		//if the value index to right of key is -1, then it is empty
		if (nodeArray[index + 1] == -1)
			return true;
		else
			return false;
	}

//-------------------------------------------------------------------------------------

	//checks if array contains 4 keys
	public static boolean isFull() {

		//go through all keys & check if they are not empty
		for (int i = 2; i < 13; i += 3) {
			if (isEmpty(i))
				return false; //return false if one space is empty
		}

		//return true if all spaced are filled
		return true;
	}

//-------------------------------------------------------------------------------------

	//checks if key exists in node array
	//requires key to be checked
	public static boolean keyExists (long key) {

		boolean exists = false;

		//goes through each key
		for (int i = 2; i < 14; i += 3) {

			//checks if key is -1 and value index is -1
			if (key == -1 && nodeArray[i + 1] == -1)
				exists = false;
			//if the key matches
			else if (key == nodeArray[i]) {
				exists = true;
				break;
			} else 
				exists = false;
		}

		return exists;
	}

//-------------------------------------------------------------------------------------

	//checks if key is less than any value in the node
	//returns false if key is greater than all values
	public static boolean keyFits(long key) {

		//go through each key
		for (int i = 2; i < 14; i += 3) {
			//if key is less than any value, return true
			if (key < nodeArray[i])
				return true;
		}

		//else return false if key is greater than all values
		return false;
	}

//-------------------------------------------------------------------------------------

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

//-------------------------------------------------------------------------------------

	//returns last key of node
	//used to keep key when shifting
	public static long getLastKey() {

		return 1;
	}

//-------------------------------------------------------------------------------------

	//removes values in node array by replacing it with -1
	//requires index so it knows what to remove
	public static void removeValues(int index) {

		nodeArray[index] = -1;
		nodeArray[index + 1] = -1;
	}

//-------------------------------------------------------------------------------------

	//returns node so it can be written to data.bt
	public static long[] returnNode (){
		return nodeArray;
	}

//-------------------------------------------------------------------------------------

	//returns node array
	public static long[] getArray() {
		return nodeArray;
	}

}