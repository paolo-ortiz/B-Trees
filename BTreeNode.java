import java.util.*;
import java.io.*;

public class BTreeNode {

	static long[] nodeArray;
	
	public static void main (String[] args){

		//make an array of longs
		nodeArray = new long[14];

		//fill with partitions
		for (int i = 0; i < nodeArray.length; i++)
			nodeArray[i] = -1;

		//TEMP
		insertKey(10, 0);


		printArray();

	}

	//prints array, used for debug purposes
	public static void printArray() {

		for (int i = 0; i < nodeArray.length; i++)
			System.out.println(nodeArray[i]);
	}

	public static void insertKey(int key, int valueIndex) {

		for (int i = 2; i < 14; i += 3) {
			//if space is empty then insert key
			if (isEmpty(i)) {
				nodeArray[i] = key;
				insertValueIndex(i + 1, valueIndex);
				break;
			}
			//else if the current key is greater than the key in array, then move to next one
			//else if ()
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

		//if index is 4th key, get rid of the key & valueIndex
		if (index == 13) {
			nodeArray[index] = -1;
			nodeArray[index + 1] = -1;
		}

		for (int i = index + 3; i < 14; i += 3) {

			//check if next space is empty
			if (isEmpty(i + 3)) {
				//transfer key & value index to next spot
				
			}

			
			//if 
		}
	}
}