/**
 * 
 */
package gossip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	}
	
	
	public static void main (String args[]){
		Gossip gossip = new Gossip();
		gossip.pushMessage("Hello World");
	}
	
	
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
			System.out.println(node);
			// Update the node with the message
			// Transmit the message
			// Move the selected node to Infected node.
			infectedNodes.add(node);
			length--;
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

}
