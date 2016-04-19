package assignment6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ThreadedTicketClient implements Runnable {
    private String hostname = "127.0.0.1";
    private String threadname = "X";
    private TicketClient sc;
	static String serverOutput;

	ThreadedTicketClient(TicketClient sc, String hostname, String threadname) {
		this.sc = sc;
		this.hostname = hostname;
		this.threadname = threadname;
	}


	public void run() {

		System.out.flush();
		try {
			Socket echoSocket = new Socket(hostname, TicketServer.PORT);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			out.println("request");
			System.out.println("Client sent request");
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			serverOutput = in.readLine();
			echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TicketClient {
	private ThreadedTicketClient tc;
	//String result = "dummy";
	private String hostName = "";
	private String threadName = "";

	private TicketClient(String hostname, String threadname) {
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

	void requestTicket() {
		//int[] seat = bestAvailableSeat(ticket);
		tc.run();
		String output = printTicketSeat(ThreadedTicketClient.serverOutput);
		System.out.println("Client received: " + ThreadedTicketClient.serverOutput);
		System.out.println(output);
	}

	private String printTicketSeat(String name){
		String []array = name.split("");
		String output;
		if(array[4].equals("-")){
			output = hostName + ", " + threadName + " got seat" + " " + array[2] + array[3] + " in row " + array[1];
		}
		else{
			output = hostName + ", " + threadName + " got seat" + " " + array[2] + " in row " + array[1];
		}
		return output;
	}
	
	void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
