/* Sahil Shah and Hari Kosuru
 * ss63683 and hk8663
 * EE 422C - Assignment 6
 */

package assignment6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


//	Threaded version of Ticket Client
// Represents our Box Office, but allows for multithreading (ie multiple box offices)
class ThreadedTicketClient implements Runnable {
    private String hostname = "127.0.0.1";
    private String threadname = "X";
    private TicketClient sc;
	private String serverOutput;

	ThreadedTicketClient(TicketClient sc, String hostname, String threadname) {
		this.sc = sc;
		this.hostname = hostname;
		this.threadname = threadname;
	}

	// Used to transmit server output to TicketClient objects
	String getServerOutput(){
		return serverOutput;
	}

	public synchronized void run() {

		System.out.flush();
		try {
			Socket echoSocket = new Socket(hostname, TicketServer.PORT);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			out.println("request");
			//System.out.println("Client sent request");
			//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			serverOutput = in.readLine();
			echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

// Ticket Client represents Box Office
class TicketClient {
	private ThreadedTicketClient tc;
	//String result = "dummy";
	private String hostName = "";
	private String threadName = "";
	private String serverOutput;
	private boolean flag = true;

	TicketClient(String hostname, String threadname) {
		tc = new ThreadedTicketClient(this, hostname, threadname);
		hostName = hostname;
		threadName = threadname;
	}

	TicketClient(String name) {
		this("localhost", name);
	}

	TicketClient() {
		this("localhost", "unnamed client");
	}

	// Requests ticket from the threaded object of this ticket client object
	void requestTicket() {
		tc.run();
		String serverOutput = tc.getServerOutput();
		if (serverOutput.equals("0 0 0 0")){
			if (flag){
				System.out.println("Sorry, the show is sold out!");
				flag = false;
			}
		}
		else {
			String output = printTicketSeat(serverOutput);
			//System.out.println("Client received: " + serverOutput);
			System.out.println(output);
		}
	}

	// Outputs to the console the reservation
	private String printTicketSeat(String name){
		String []array = name.split("[ ]+");
		int rowNumber = Integer.parseInt(array[1]);
		int section = Integer.parseInt(array[0]);
		int seatNumber = Integer.parseInt(array[2]) + 100;
		return threadName + " reserved seat" + " " + seatNumber + " in row "
				+ getCharForNumber(rowNumber) + " in section " + getCharForNumber(section);
	}

	// Converts to Char
	private String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	
/*	void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}*/
}
