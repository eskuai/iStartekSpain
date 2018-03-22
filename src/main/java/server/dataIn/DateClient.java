package server.dataIn;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DateClient {

	public static void main(String args[]) throws IOException {

		Socket clientSocket = new Socket("localhost", 8899);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
		byte[] buf = new byte[] { 36, 36, 0, -17, -65, -67, 0, 0, 0, 31, -17, -65, -67, -17, -65, -67, -17, -65, -67,
				-17, -65, -67, 85, 49, 56, 50, 50, 48, 52, 46, 48, 48, 48, 44, 65, 44, 52, 48, 49, 48, 46, 57, 51, 49,
				49, 44, 78, 44, 48, 48, 51, 52, 48, 46, 51, 53, 50, 56, 44, 87, 44, 48, 46, 48, 48, 44, 49, 57, 56, 44,
				50, 49, 48, 51, 49, 56, 44, 44, 42, 49, 53, 124, 48, 46, 57, 124, 54, 51, 48, 124, 50, 48, 48, 48, 124,
				48, 48, 48, 48, 44, 48, 48, 48, 48, 44, 48, 49, 48, 51, 44, 48, 50, 65, 48, 124, 48, 48, 68, 54, 48, 48,
				48, 51, 48, 52, 56, 56, 48, 48, 54, 70, 53, 52, 49, 56, 124, 48, 53, 124, 48, 48, 48, 48, 48, 67, 57,
				50, 124, 48, 57, -17, -65, -67, 54 };
		
		outToServer.write(buf);	
		outToServer.write('\r');
		outToServer.write('\n');
		clientSocket.close();
	}
}
