package assignment6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static assignment6.TicketServer.bestAvailableSeat;
import static assignment6.TicketServer.ticket;

class TicketServer {
	static int PORT = 2222;
	// EE422C: no matter how many concurrent requests you get,
	// do not have more than three servers running concurrently
	private final static int MAXPARALLELTHREADS = 3;
	static int[][] ticket;
	private static boolean initialized = false;

	static void start(int portNumber) throws IOException {
		if (!initialized){
			ticket = createTickets();
			initialized = true;
		}
        if (Thread.activeCount() < MAXPARALLELTHREADS) {
            PORT = portNumber;
            Runnable serverThread = new ThreadedTicketServer();
            Thread t = new Thread(serverThread);
            t.start();
        }
	}
	
	// section A is rows [1,13] seats [8,21]
	// section B is rows [1,13] seats [1,7] and [22,28]
	// section C is rows [14,26] seats [8,21]
	// section D is rows [14,26] seats [1,7] and [22,28]
	private static int[][] createTickets(){
		int [][]tickets = new int[27][29];
		for (int i = 1; i<27; i++){
			for (int j = 1; j<29; j++){
				tickets[i][j] = 1;
			}			
		}
		return tickets;	
	}
	
	synchronized static int[] bestAvailableSeat(int[][] tickets){
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
	
	private synchronized static int[] checkSectionA(int[][] tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 1; i<14; i++){
			for (int j = 8; j<22; j++){
				if( tickets[i][j] ==1){
					seat[0] = 1;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					return seat;
				}
			}			
		}
		return seat;
	}
	
	private synchronized static int[] checkSectionB(int[][] tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 1; i<14; i++){
			for (int j = 1; j<8; j++){
				if( tickets[i][j] ==1){
					seat[0] = 2;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					return seat;
				}
				if( tickets[i][j+21] ==1){
					seat[0] = 2;	seat[1] = i;	seat[2] = j+21;	seat[3] = -1;
					tickets[i][j+21] = 0;	//sets the seat to taken
					return seat;
				}
			}			
		}
		return seat;
	}

	private synchronized static int[] checkSectionC(int[][] tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 14; i<27; i++){
			for (int j = 8; j<22; j++){
				if( tickets[i][j] ==1){
					seat[0] = 3;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					return seat;
				}
			}			
		}
		return seat;
	}
	
	private synchronized static int[] checkSectionD(int[][] tickets){
		int []seat = new int[4]; //0 holds section, 1 holds row, 2 holds seat. 3 holds a flag
		for (int i = 14; i<27; i++){
			for (int j = 1; j<8; j++){
				if( tickets[i][j] ==1){
					seat[0] = 4;	seat[1] = i;	seat[2] = j;	seat[3] = -1;
					tickets[i][j] = 0;	//sets the seat to taken
					return seat;
				}
				if( tickets[i][j+21] ==1){
					seat[0] = 4;	seat[1] = i;	seat[2] = j+21;	seat[3] = -1;
					tickets[i][j+21] = 0;	//sets the seat to taken
					return seat;
				}
			}			
		}
		return seat;
	}
	
}

class ThreadedTicketServer implements Runnable {

	//String hostname = "127.0.0.1";
	//String threadname = "X";
	//String testcase;
	//TicketClient sc;

	public void run() {
		System.out.flush();
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(TicketServer.PORT);
			while(true) {
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String input = in.readLine();
				if (input != null && input.equals("request")) {
					int[] seat = bestAvailableSeat(ticket);
					String output = seat[0] + " " + seat[1] + " " + seat[2] + " " + seat[3];
					//System.out.println("Server processed a request");
					out.println(output);
					//System.out.println("Server sent: " + output);
				}
			}
		}
		 catch (IOException e) {
			e.printStackTrace();
		}

	}
}