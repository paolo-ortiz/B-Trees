import java.util.*;
import java.io.*;

public class BTreeDB {
	
	public static void main( String[] args ) {

		try {
			RandomAccessFile file = new RandomAccessFile(args[0], "rwd");
			RandomAccessFile file2 = new RandomAccessFile(args[1], "rwd");
		} catch (IOException e) {
			System.out.println("IOException in main");
		}

	}
		
		
}