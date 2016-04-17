package assignment6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TicketServer {
	static int PORT = 2222;
	// EE422C: no matter how many concurrent requests you get,
	// do not have more than three servers running concurrently
	final static int MAXPARALLELTHREADS = 3;

	public static void start(int portNumber) throws IOException {
		PORT = portNumber;
		Runnable serverThread = new ThreadedTicketServer();
		Thread t = new Thread(serverThread);
		int [][]ticket = createTickets();
		int [] seat = bestAvailableSeat(ticket);
		t.start();
	}
	
	// section A is rows [1,13] seats [8,21]
	// section B is rows [1,13] seats [1,7] and [22,28]
	// section C is rows [14,26] seats [8,21]
	// section D is rows [14,26] seats [1,7] and [22,28]
	public static int[][] createTickets(){
		int [][]tickets = new int[27][29];
		for (int i = 1; i<27; i++){
			for (int j = 1; j<29; j++){
				tickets[i][j] = 1;
			}			
		}
		return tickets;	
	}
	
	public static int[] bestAvailableSeat(int[][]tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		
		seat = checkSectionA(tickets);
		if (seat[3]!= -1){
			seat = checkSectionB(tickets);
			if (seat[3]!= -1){
				seat = checkSectionC(tickets);
				if (seat[3]!= -1){
					seat = checkSectionD(tickets);
				}
			}
		}
		
		return seat;	
	}
	
	public static int[] checkSectionA(int[][]tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 1; i<14; i++){
			for (int j = 8; j<22; j++){
				if( tickets[i][j] ==1){
					seat[0] = 1;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					i =14;
					break;
				}
			}			
		}
		return seat;
	}
	
	public static int[] checkSectionB(int[][]tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 1; i<14; i++){
			for (int j = 1; j<8; j++){
				if( tickets[i][j] ==1){
					seat[0] = 2;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					i = 14;
					break;
				}
				if( tickets[i][j+21] ==1){
					seat[0] = 2;	seat[1] = i;	seat[2] = j+21;	seat[3] = -1;
					tickets[i][j+21] = 0;	//sets the seat to taken
					i = 14;
					break;
				}
			}			
		}
		return seat;
	}

	public static int[] checkSectionC(int[][]tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 14; i<27; i++){
			for (int j = 8; j<22; j++){
				if( tickets[i][j] ==1){
					seat[0] = 3;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					i = 27;
					break;
				}
			}			
		}
		return seat;
	}
	
	public static int[] checkSectionD(int[][]tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 14; i<27; i++){
			for (int j = 1; j<8; j++){
				if( tickets[i][j] ==1){
					seat[0] = 4;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					i = 27;
					break;
				}
				if( tickets[i][j+21] ==1){
					seat[0] = 4;	seat[1] = i;	seat[2] = j+21;	seat[3] = -1;
					tickets[i][j+21] = 0;	//sets the seat to taken
					i = 27;
					break;
				}
			}			
		}
		return seat;
	}
	
}

class ThreadedTicketServer implements Runnable {

	String hostname = "127.0.0.1";
	String threadname = "X";
	String testcase;
	TicketClient sc;

	public void run() {
		// TODO 422C
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(TicketServer.PORT);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}