package gossip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;


/**
 * 
 * @author bgupta
 *
 */
public class GossipNode {
	private String ip;
	public String getIp() {
		return ip;
	}
	private String port;
	public String getPort() {
		return port;
	}
	private String message;
	List<String> susceptibleNodes;
	List<String> infectedNodes;
	List<String> removedNodes;
	Gossip gossip;
	Messaging messaging;
	public GossipNode(String ip, String port, String message) {
		//Initialize a Node. 
		gossip = new Gossip();
		messaging = new Messaging();
		this.ip=ip;
		this.port=port;
		this.message=message;
		susceptibleNodes = gossip.getConnectedNodes();
		//Start the Listener thread for PULL.
		Thread listenermode = new Thread(new Listener(this.port));
		listenermode.start();
		//TODO: Start the Announcer thread for PUSH.
	}

	public void sendMessage(GossipNode recipientNode, String msgToSend) {
		DatagramSocket socket;
		byte[] buf = new byte[1024];
		InetAddress address;
		try {
			//Initiate Socket
			socket = new DatagramSocket();
			address = InetAddress.getByName(recipientNode.getIp());
			buf = msgToSend.getBytes();
			//Create packet to send to Recipient
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(recipientNode.getPort()));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}
		//messaging.sendMessage(recipientNode.getIp(), recipientNode.getPort(), "Hello");
	}
}

class Announcer implements Runnable{
	int waitInterval = 300;
	public void run() {

	}
}

class Listener implements Runnable{
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[1024];
	private String port;
	
	/**
	 * Initialize listener on localhost.
	 * @param port - port on localhost on which listener will be listening. 
	 */
	public Listener(String port) {
		this.port= port;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		running = true;
		try {
			
			socket = new DatagramSocket(Integer.parseInt(port));
			//Wait for receiving message
			while (running) {
				System.out.println("#INFO# - Listener Running waiting for message on IP - "+
						InetAddress.getLocalHost().getHostAddress() + ", Port -"+port);
				//New packet for recieving message. 
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}