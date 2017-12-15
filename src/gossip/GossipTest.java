package gossip;
/**
 * 
 * @author bgupta
 *
 */
public class GossipTest {

	public static void main(String[] args) {
		System.out.println("Initiating Node 1");
		//Update IP below for testing
		GossipNode node1 = new GossipNode("192.168.1.9","4445","");

		System.out.println("Initiating Node 2");
		//Update IP below for testing
		GossipNode node2 = new GossipNode("192.168.1.9","4446","");

		//Test message sent from Node 1 to Node 2.
		node1.sendMessage(node2, "Hello");
		node1.sendMessage(node2, "end");
		
	}

}
