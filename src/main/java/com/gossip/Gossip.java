 /**
 * 
 */
package com.gossip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author romiljain
 *
 */
public class Gossip {
	
	List<String> susceptibleNodes;
	
	List<String> infectedNodes;
	
	List<String> removedNodes;
	
	private Random random;
	
	public Gossip(){
		this.susceptibleNodes = getConnectedNodes();
		random = new Random();
		infectedNodes = new ArrayList<String>();
		removedNodes = new ArrayList<String>();
		Iterator<String> itr = this.susceptibleNodes.iterator();
		while(itr.hasNext()){
			String node = itr.next();
			String recipientNodePort = node.split(":")[1];
			Thread listenermode = new Thread(new MessageListener(recipientNodePort));
			listenermode.start();
		}
	}
	
	
	/*public static void main (String args[]){
		Gossip gossip = new Gossip();
		gossip.pushMessage("Hello World");
	}*/
	
	
	private List<String> getConnectedNodes() {
		susceptibleNodes = new ArrayList<String>();
		File nodeConfigFile = new File("config","nodes");
		try {
			BufferedReader br = new BufferedReader(new FileReader(nodeConfigFile));
			String line;
			while((line = br.readLine()) != null) {
				susceptibleNodes.add(line.trim());
			}
			br.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
		return susceptibleNodes;
	}
	
	public void pushMessage(String message) {
		int length = susceptibleNodes.size();
		while(length>0){
			String node = getRandomPeerNode();
			System.out.println("Random Node Selected: "+node);
			if(!infectedNodes.contains(node)){
				String recipientNodeIp =node.split(":")[0];
				int recipientNodePort = Integer.parseInt(node.split(":")[1]);
				// Send message and update the node 
				sendMessage (recipientNodeIp, recipientNodePort, message);
				// Move the selected node to Infected node.
				infectedNodes.add(node);
			}
			length--;
		}
	}
	
	
	public void pullMessage() {
		int length = susceptibleNodes.size();
		while(length>0){
			String node = getRandomPeerNode();
			// if selected node is infected then only pull the message
			if(infectedNodes.contains(node)){
				// get the message from selected node
			}
		}
	}
	
	public void sendMessage(String recipientNodeIp, int recipientNodePort, String msgToSend) {
		DatagramSocket socket;
		byte[] buf = new byte[1024];
		InetAddress address;
		try {
			//Initiate Socket
			socket = new DatagramSocket();
			address = InetAddress.getByName(recipientNodeIp);
			buf = msgToSend.getBytes();
			//Create packet to send to Recipient
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, recipientNodePort);
			socket.send(packet);
			//New packet for receiving acknowledgement. 
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println("#DEBUG# - Acknowledgement - " + received);
			//close the socket
			socket.close();
			//return received;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getRandomPeerNode() {
		String node = null;
		if(susceptibleNodes.size() > 0) {
				int randomIndex = random.nextInt(susceptibleNodes.size());
				node = susceptibleNodes.get(randomIndex);
		} 
		return node;
	}

}

class MessageListener implements Runnable{
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[1024];
	private String port;

	public MessageListener(String port) {
		this.port= port;
	}

	@Override
	public void run() {
		running = true;
		try {
			
			socket = new DatagramSocket(Integer.parseInt(port));
			//Wait for receiving message
			while (running) {
				System.out.println("#INFO# - Listener Running waiting for message on IP - "+
						InetAddress.getLocalHost().getHostAddress() + ", Port -"+port);
				//New packet for receiving message. 
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				//receive packet from socket
				socket.receive(packet);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				//Get message from packet
				String received 
				= new String(packet.getData(), 0, packet.getLength());
				System.out.println("#DEBUG# - Server Received Message - '" + received+ "', From - "+address+", Port - "+port);
				if (received.equals("end")) {
					running = false;
					continue;
				}
				buf = "ack-back".getBytes();
				DatagramPacket returnPacket = new DatagramPacket(buf, buf.length, address, port);
				socket.send(returnPacket);
			}
		socket.close();
		System.out.println("#INFO# - Listener TERMINATED on IP - "+
						InetAddress.getLocalHost().getHostAddress() + ", Port -"+port);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
