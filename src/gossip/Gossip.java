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
	
	List<String> nodes;
	
	private Random random;
	
	public Gossip(){
		this.nodes = getConnectedNodes();
		random = new Random();
	}
	
	
	public static void main (String args[]){
		Gossip gossip = new Gossip();
		gossip.pushMessage("Hello World");
	}
	
	
	private List<String> getConnectedNodes() {
		nodes = new ArrayList<String>();
		File nodeConfigFile = new File("config","nodes");

		try {
			BufferedReader br = new BufferedReader(new FileReader(nodeConfigFile));
			String line;
			while((line = br.readLine()) != null) {
				nodes.add(line.trim());
			}
			br.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
		return nodes;
	}
	
	public void pushMessage(String message) {
		int length = nodes.size();
		while(length>0){
			String node = getRandomPeerNode();
			System.out.println(node);
			length--;
		}
	}
	
	private String getRandomPeerNode() {
		String node = null;
		if(nodes.size() > 0) {
				int randomIndex = random.nextInt(nodes.size());
				node = nodes.get(randomIndex);
		} 
		return node;
	}
	
	public void pullMessage() {
		
	}

}
