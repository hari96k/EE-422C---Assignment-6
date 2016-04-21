/* Sahil Shah and Hari Kosuru
 * ss63683 and hk8663
 * EE 422C - Assignment 6
 */

package assignment6;

import static org.junit.Assert.fail;
import org.junit.Test;

import java.util.Random;

public class TestTicketOffice {

	//	Creates one server and one client
	//	Sends one request
	@Test
	public void basicServerTest() {
		try {
			TicketServer.start(16789);
		} catch (Exception e) {
			fail();
		}
		TicketClient client = new TicketClient();
		client.requestTicket();
	}

	// Creates one serves and two clients
	// Two requests each
	@Test
	public void testServerCachedHardInstance() {
		try {
			TicketServer.start(16790);
		} catch (Exception e) {
			fail();
		}
		TicketClient client1 = new TicketClient("localhost", "c1");
		TicketClient client2 = new TicketClient("localhost", "c2");
		client1.requestTicket();
		client2.requestTicket();

	}

	//	Creates one server and three clients
	//	Three requests
	@Test
	public void twoNonConcurrentServerTest() {
		try {
			TicketServer.start(16791);
		} catch (Exception e) {
			fail();
		}
		TicketClient c1 = new TicketClient("nonconc1");
		TicketClient c2 = new TicketClient("nonconc2");
		TicketClient c3 = new TicketClient("nonconc3");
		c1.requestTicket();
		c2.requestTicket();
		c3.requestTicket();
	}

	//	Creates one server and three clients
	//	3 Synchronized Threads
	@Test
	public void twoConcurrentServerTest() {
		try {
			TicketServer.start(16792);
		} catch (Exception e) {
			fail();
		}
		final TicketClient c1 = new TicketClient("conc1");
		final TicketClient c2 = new TicketClient("conc2");
		final TicketClient c3 = new TicketClient("conc3");
		Thread t1 = new Thread() {
			public void run() {
				c1.requestTicket();
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				c2.requestTicket();
			}
		};
		Thread t3 = new Thread() {
			public void run() {
				c3.requestTicket();
			}
		};
		t1.start();
		t2.start();
		t3.start();
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

		//	Creates one server and one client
		//	Atleast 800+ requests (random)
		@Test
		public void randomNumberOfClientsTest() {
			try {
				TicketServer.start(16792);
			} catch (Exception e) {
				fail();
			}
			Random r = new Random();
			int numberOfClients = r.nextInt(1000 - 800) + 800;
			final TicketClient c1 = new TicketClient("Booth A");
			for (int i = 0; i <= numberOfClients; i++){
				c1.requestTicket();
			}
		}


		//	Creates one server and three clients
		//	Atleast 800+ requests (random)
		//	3 Synchronized Threads
		@Test
		public void randomNumberOfThreadedClientsTest() {
			try {
				TicketServer.start(16792);
			} catch (Exception e) {
				fail();
			}
			Random r = new Random();
			int numberOfClients = r.nextInt(1000 - 800) + 800;
			//String clientName = "client ";
			final TicketClient c1 = new TicketClient("Booth A");
			final TicketClient c2 = new TicketClient("Booth B");
			final TicketClient c3 = new TicketClient("Booth C");
			for (int i = 0; i <= numberOfClients; i++){
				Thread t1 = new Thread() {
					public void run() {
						c1.requestTicket();
					}
				};
				Thread t2 = new Thread() {
					public void run() {
						c2.requestTicket();
					}
				};
				Thread t3 = new Thread() {
					public void run() {
						c3.requestTicket();
					}
				};
				t1.start();
				t2.start();
				t3.start();
				try {
					t1.join();
					t2.join();
					t3.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

}