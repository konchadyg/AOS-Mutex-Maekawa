//========================================================
//|     CS6378 - PROJECT 2                               |
//|     Member 1: Piyush Makani [PXM140430]              |
//|     Member 2: Konchady Gaurav Shenoy [KXS168430]     |
//|     Instructor: Dr. Neeraj Mittal                    |
//|     Oct 2016                                         |
//|     The University of Texas at Dallas                |
//========================================================


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;



public class Node extends Thread {
	
	static int identifier = 3;
	static int requestsMadeYet = 0;
	static int timestamp = 0;
	
	static int totalNoOfNodes;
	static int interRequestDelay;
	static int csExecutionTime;
	static int totalRequests;
	
	static NodeLocation[] nodeLocations;
	
	static int[] quorumsMembers;
	
	static HashSet<Integer> grantSet = new HashSet<Integer>();
	static HashSet<Integer> failedSet = new HashSet<Integer>();
	static PriorityQueue < Message >  queue = new PriorityQueue < Message > () ;
	
	static PrintWriter pw = null;
	
	public static void main(String[] args) throws InterruptedException
	{
		identifier = Integer.parseInt(args[0]);
		readConfigFile();
		
		//initialise timestamp
		timestamp = (new Random()).nextInt(400)+1; 
		//if (identifier == 0) {timestamp=10;}
		//else if (identifier == 1) {timestamp=8;}
		//else if (identifier == 2) {timestamp=5;}
		
		
		//start listening as a server
		mutualExclusionService();
	
		System.out.println("Identfier "+identifier+ " with TS of value "+timestamp);
		Thread.sleep(10000);
		
		//for(int i=0;i<20;i++)
		requestsMadeYet++;
		csEnter();
		
		
	}
	
	
	



	public static void readConfigFile()
	{
		BufferedReader br = null;
		try {
				String sCurrentLine;
				//br = new BufferedReader(new FileReader("/home/010/p/px/pxm140430/aos/project/config.txt"));
				br = new BufferedReader(new FileReader("/home/012/k/kx/kxs168430/proj2/code/config.txt"));

				//Extracting total nodes
				while ((sCurrentLine = br.readLine()) != null) 
				{
					if((int)sCurrentLine.charAt(0)>=48 && (int)sCurrentLine.charAt(0)<=57)
					{
						String[] splitArray = sCurrentLine.trim().replaceAll("\\s{2,}", " ").split("\\s+");	
						totalNoOfNodes = Integer.parseInt(splitArray[0]);
						interRequestDelay = Integer.parseInt(splitArray[1]);
						csExecutionTime = Integer.parseInt(splitArray[2]);
						totalRequests = Integer.parseInt(splitArray[3]);
						break;
					}	
				}
				
//				System.out.println(totalNoOfNodes);
//				System.out.println(interRequestDelay);
//				System.out.println(csExecutionTime);
//				System.out.println(totalRequests);
				
				//Extracting Location of all nodes.
				nodeLocations = new NodeLocation[totalNoOfNodes];
				for(int i=0;i<totalNoOfNodes;)
				{	sCurrentLine = br.readLine();
					if(!sCurrentLine.equals(null) && !sCurrentLine.equals("") && (int)sCurrentLine.charAt(0)>=48 && (int)sCurrentLine.charAt(0)<=57)
					{	
						sCurrentLine = sCurrentLine.trim();
						String[] splitArray = sCurrentLine.replaceAll("\\s{2,}", " ").split("\\s+");
						nodeLocations[i]=new NodeLocation(splitArray[1],Integer.parseInt(splitArray[2])); 
						i++;
					}
				}
				
				//testing if dataStructure nodeLocations is populated correctly or not
				/*	System.out.println("Node Location : ");
					for(NodeLocation n :nodeLocations )
					{	
						System.out.println(n.serverName);
						System.out.println(n.portNumber);
						System.out.println();
					}
				*/
				for(int i=0;i<totalNoOfNodes;)
				{	sCurrentLine = br.readLine();
					if(!sCurrentLine.equals(null) && !sCurrentLine.equals("") && (int)sCurrentLine.charAt(0)>=48 && (int)sCurrentLine.charAt(0)<=57)
					{	
						sCurrentLine = sCurrentLine.trim();
						String[] splitArray = sCurrentLine.replaceAll("\\s{2,}", " ").split("\\s+");
						if(Integer.parseInt(splitArray[0]) == identifier)
						{	quorumsMembers = new int[splitArray.length-1];
							for(int x=0; x<quorumsMembers.length; x++)
								{quorumsMembers[x] = Integer.parseInt(splitArray[x+1]);}
						}
						i++;
					}
				}
				
				
				
				
				//testing if dataStructure quorumsMembers is populated correctly or not
			/*	for(int x=0; x<quorumsMembers.length; x++)
				{System.out.println(quorumsMembers[x]);
				}
			*/

		}
		catch (IOException e) {
				e.printStackTrace();
			} 
		finally {
			try {
				if (br != null)br.close();
				}
			catch (IOException ex) {
				ex.printStackTrace();
				}
			}
		//System.out.println("Read Config file function finished here");
	}
	


	private static void mutualExclusionService() {
		
		//start listening as a server
		Node t1 = new Node();
		t1.start();
		//
	}

	public void run(){  
		Socket socket = null;
		try
        {	 
    		 int port = nodeLocations[identifier].portNumber; //knowing the portnumber I should host my server on 
    		 ServerSocket serverSocket = new ServerSocket(port);
             //System.out.println("Server Started and listening to the port "+ port);
             ObjectInputStream inStream = null;
             //Server is running always. This is done using this while(true) loop
             while(true)
             {
                 //Reading the message from the client
                 socket = serverSocket.accept();
                 inStream = new ObjectInputStream(socket.getInputStream());
                 Message m = (Message) inStream.readObject();
                 
//               System.out.println("Sever: object received");
//               System.out.println("originator: "+ m.originator);
//               System.out.println("timestamp: "+ m.timestamp);
//               System.out.println("request: "+ m.request);
//               System.out.println("grant: "+ m.grant);
//               System.out.println("release: "+ m.release);
//               System.out.println("inquire: "+ m.inquire);
//               System.out.println("yield: "+ m.yield);
//               System.out.println("failed: "+ m.failed);
//               System.out.println();
                 
                 //updating the timestamp
				 timestamp = ((timestamp>=m.timestamp)?timestamp:m.timestamp)+1;
                 
                 if(m.request == true)
                 {
					 
					 System.out.println(""+identifier +" : Request message received from "+m.originator);
                	 if(queue.size() == 0) //if queue is empty
                	 {
                		 queue.add(m);
						 timestamp++;
                		 Message msg = new Message();
            			 msg.originator =identifier;
            			 msg.grant = true;
            			 msg.timestamp = timestamp;
            			 sendMessage(msg, m.originator);
						System.out.println(""+identifier +" : queue was empty so added req in Q and sent GRANT to "+m.originator);
                	 }
                	 else //if queue is not empty
                	 {
                		 Message oldFront = queue.peek();
                		 queue.add(m);
                		 Message newFront = queue.peek();
                		 //a
                		 if(newFront.originator == oldFront.originator)
                		 {

							 timestamp++;
                			 Message msg = new Message();
                			 msg.originator =identifier;
                			 msg.failed = true;
                			 msg.timestamp = timestamp;
                			 sendMessage(msg, m.originator);
                			System.out.println(""+identifier +" : queue was not empty so added req in not at front Q and sent FAILED to "+m.originator); 
                		 }
                		 //b
                		 else if(newFront.originator != oldFront.originator){
                			 timestamp++;
                			 Message msg = new Message();
                			 msg.originator =identifier;
                			 msg.inquire = true;
                			 msg.timestamp = newFront.timestamp;
                			 sendMessage(msg, oldFront.originator);
							 System.out.println(""+identifier +" : queue was not empty so added req in at front Q and sent INQUIRE to "+oldFront.originator);
                			 
                		 }
                	 }
					 
					 System.out.println("Queue of "+identifier +": is "+ queue);
                 }
                 
                 else if(m.grant == true)
                 {
                	 grantSet.add(m.originator);
					 System.out.println(""+identifier +" : GRANT SET : " +grantSet + "grantSet size : "+ grantSet.size() +" quorum length : "+ quorumsMembers.length);
					 
					 
					 if(grantSet.size() == quorumsMembers.length)
					{	
						//System.out.println(""+identifier+" : Request Number : "+requestsMadeYet );
						
						System.out.println("@.............Process "+identifier+ " has entered the CS");
						
						pw = new PrintWriter(new FileOutputStream(new File("output.txt"),true));
						pw.println(identifier+"+");
						pw.close();
						
						
						Thread.sleep(csExecutionTime);
						
						System.out.println("@.............Process "+identifier+ " has left the CS");
						
						pw = new PrintWriter(new FileOutputStream(new File("output.txt"),true));
						pw.println(identifier+"-");
						pw.close();
						
						csLeave();
					}
                 }
                 
                 else if(m.failed == true)
                 {
                	 failedSet.add(m.originator);
                	 grantSet.remove(m.originator);
					 System.out.println(""+identifier +" : FAILED SET : " +failedSet);
                 }
                 
                 else if(m.inquire == true)
                 {
                	if(!failedSet.isEmpty()) //if failed set is not empty
                	{	timestamp++;
                		Message msg = new Message();
           			 	msg.originator =identifier;
           			 	msg.yield = true;
           			 	msg.timestamp = timestamp;
           			 	sendMessage(msg, m.originator);
                	}
					else
					{System.out.println("DEADLOCK : FailedSet is Empty and still node "+identifier+ " got an INQUIRE msg from "+m.originator);}
                 }
                 
                 else if(m.yield == true)
                 {		timestamp++;
                	 	Message msg = new Message();
        			 	msg.originator =identifier;
        			 	msg.grant = true;
        			 	msg.timestamp = timestamp;
        			 	sendMessage(msg, queue.peek().originator);
                 }
                 
                 else if(m.release == true)
                 {
                	 if(m.originator == queue.peek().originator)
                	 {
                		 queue.poll();
                		 if(!queue.isEmpty()) //if queue has more requests
                		 {	timestamp++;
                			 Message msg = new Message();
                			 msg.originator =identifier;
                			 msg.grant = true;
                			 msg.timestamp = timestamp;
                			 sendMessage(msg, queue.peek().originator);
							 System.out.println(""+identifier +" : Release message received from "+m.originator +" and sent grant mesg to "+ queue.peek().originator);
							 
						}
                	 }
					 else
					 {
						 System.out.println(""+identifier+"Not ERROR : received RELEASE message from "+m.originator+" but it was not in front of my queue");
							PriorityQueue < Message >  tempQueue = new PriorityQueue < Message > () ;
							int size = queue.size();
							for(int i=0; i<size; i++)
						    {
							   if(queue.peek().originator != m.originator)
							   {
								   Message msg = queue.poll();
								   tempQueue.add(msg);
							   }
							   else{queue.poll();}
							   
						   }
						   queue = tempQueue;
						   if(!queue.isEmpty()) //if queue has more requests
                		 {	timestamp++;
                			 Message msg = new Message();
                			 msg.originator =identifier;
                			 msg.grant = true;
                			 msg.timestamp = timestamp;
                			 sendMessage(msg, queue.peek().originator);
							 System.out.println(""+identifier +" : Release message received from "+m.originator +" and sent grant mesg to "+ queue.peek().originator);
							 
						}
					 }

                 }
             
             }
                 
        }
    	 catch (Exception e)
        {
            e.printStackTrace();
        }
    	finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){}
        }
	}
	
	
	
	public static void csEnter() throws InterruptedException
	{
		System.out.println("**Initating CSEnter fxn of node "+identifier);
		//part 1
		sendRequestToAll();
		
		//part 2
		/*boolean allAgree = false;
		
			while(allAgree == false)
			{	
				int x=grantSet.size();
				int y=quorumsMembers.length;
				if(x == y)
				{	
					System.out.println(""+identifier+ " : grantset size is same as quorum size.********");
					allAgree = true;
					break;
				}
				else
				{
					allAgree = false;
				}
			}
		*/
		//part 3
		
		/*System.out.println(".............Process "+identifier+ " has entered the CS");
		Thread.sleep(csExecutionTime);
		System.out.println(".............Process "+identifier+ " has left the CS");
		*/
		//csLeave();
		
	}
	
	
	public static void csLeave() throws InterruptedException
	{
		grantSet.clear();
		failedSet.clear();
		sendReleaseToAll();
		
		if(requestsMadeYet < totalRequests)
		{
			Thread.sleep(interRequestDelay);
			requestsMadeYet++;
			csEnter();
		}	
		
	}
	
	public static void sendRequestToAll()
	{		timestamp++;
		    for(int i=0; i<quorumsMembers.length; i++)
		    {
		    	Message m = new Message();
		    	m.timestamp = timestamp;
		    	m.request = true;
		    	m.originator = identifier;
				
				//System.out.println("Sending request message from "+identifier+ " to "+quorumsMembers[i]);
				
		    	sendMessage(m, quorumsMembers[i]);
		    }
	}
	
	
	public static void sendReleaseToAll()
	{		timestamp++;
		    for(int i=0; i<quorumsMembers.length; i++)
		    {
		    	Message m = new Message();
		    	m.timestamp = timestamp;
		    	m.release = true;
		    	m.originator = identifier;
		    	sendMessage(m, quorumsMembers[i]);
		    }
	}
	
	public static void sendMessage( Message m, int toNode )
	{
		Socket socket = null;
		ObjectOutputStream outputStream = null;
		NodeLocation nodeLocation = nodeLocations[toNode];
		try
        {
			//System.out.println("!!!!!!!!Process Identfier: "+identifier+" trying to connect to server "+nodeLocation.serverName+" port: "+nodeLocation.portNumber);
			socket = new Socket(nodeLocation.serverName, nodeLocation.portNumber);
            //Send the message to the server
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(m);
            //System.out.println("Token sent to "+nextNodeLocation.serverName + "at port " + nextNodeLocation.portNumber);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	
	
	
	
}
