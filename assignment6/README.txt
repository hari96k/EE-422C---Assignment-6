Names: Sahil Shah and Hari Kosuru
EIDs: ss63683 and hk8663

/*
 Threaded Tickets
 Solves EE422C programming assignment #6
 @author Sahil Shah (ss63683), Hari Kosuru (hk8663)
 @version 1.06 04-20-2016
 */

Java code: 
		TestTicketOffice.java (junit test cases)
	    TicketServer.java
	    TicketClient.java

GitHub:		
https://github.com/hari96k/EE-422C---Assignment-6.git
		
*** Our Design *** :
We treated each ticket office as a third-party client that communicates with our server.
We will always only have one server, which runs in a while loop for all requests sent.
Also, each request will only output one ticket, ie we do not assume a never ending que when there is one request.
	Reference our test case named randomNumberOfThreadedClientsTest() to guarantee a random output greater than our total number of seats
We also combined markAvailableSeat() into our bestAvailableSeat() function because we felt it was more natural to synchronize threads this way.



--------------------------
|      |          |      |
|      |          |      |
|   D  |     C    |   D  |
|      |          |      |
|      |          |      |
--------------------------      <--- this is our seating arrangement.
|	   |          |      |			 Seats are sold from sections A-D. The fist seat sold
|      |          |      |           in each section is at the bottom right.
|   B  |	 A	  |	  B	 |			 Seats are sold right to left and rows are
|      |          |      |           sold top to bottom
|      |          |      |
--------------------------
	// section A is rows [1,13] seats [108,121]
	// section B is rows [1,13] seats [101,107] and [122,128]
	// section C is rows [14,26] seats [108,121]
	// section D is rows [14,26] seats [101,7] and [122,28]
	
	
	