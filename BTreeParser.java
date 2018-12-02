import java.io.*;

public class BTreeParser {
	public static void main(String[] args) throws IOException {
		String first, second, hex, utf8;
		long s, nodeID;

		RandomAccessFile raw = new RandomAccessFile(args[0], "rwd");
		PrintWriter parsed = new PrintWriter("parsedDataBt.txt", "UTF-8");

		parsed.printf("Number of Nodes: %d\n", raw.readLong());
		parsed.printf("Parent Node: %d\n", raw.readLong());
		parsed.println();

		nodeID = 0;

		while( true ) {
			try {
				parsed.printf("NodeID: %d\n", nodeID++);
				
				parsed.printf("Parent: %d\n", raw.readLong());
				
				parsed.printf("1st Child: %d\n", raw.readLong());
				parsed.printf("1st Key: %d\n", raw.readLong());
				parsed.printf("1st Value Offset: %d\n", raw.readLong());

				parsed.printf("2nd Child: %d\n", raw.readLong());
				parsed.printf("2nd Key: %d\n", raw.readLong());
				parsed.printf("2nd Value Offset: %d\n", raw.readLong());
				
				parsed.printf("3rd Child: %d\n", raw.readLong());
				parsed.printf("3rd Key: %d\n", raw.readLong());
				parsed.printf("3rd Value Offset: %d\n", raw.readLong());

				parsed.printf("4th Child: %d\n", raw.readLong());
				parsed.printf("4th Key: %d\n", raw.readLong());
				parsed.printf("4th Value Offset: %d\n", raw.readLong());
				
				parsed.printf("5th Child: %d\n", raw.readLong());
				parsed.println();

			} catch ( EOFException e ) {
				break;
			}
		}

		parsed.flush();	
	}
}