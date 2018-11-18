import java.util.*;
import java.io.*;

public class BTreeDB {
	
	public static void main( String[] args ) {

		try {
			RandomAccessFile file = new RandomAccessFile(args[0], "rwd");
			RandomAccessFile file2 = new RandomAccessFile(args[1], "rwd");

			Scanner sc = new Scanner(System.in);
			while (sc.hasNext()) {

				String input = sc.nextLine();

				if (input.equals("exit"))
					file.close();
					file2.close();
					return;
			}
		} catch (IOException e) {
			System.out.println("IOException in main");
		}
	}	
}