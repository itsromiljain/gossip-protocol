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

/**
 * @author romiljain
 *
 */
public class Gossip {
	
	public static void main (String args[]){
		Gossip gossip = new Gossip();
		List<String> nodeList = gossip.getConnectedNodes();
		
		System.out.println(nodeList);
	}
	
	
	public ArrayList<String> getConnectedNodes() {
		ArrayList<String> nodesList = new ArrayList<String>();
		File nodeConfigFile = new File("config","nodes");

		try {
			BufferedReader br = new BufferedReader(new FileReader(nodeConfigFile));
			String line;
			while((line = br.readLine()) != null) {
				nodesList.add(line.trim());
			}
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
		return nodesList;
	}
	
	public void pushMessage() {
		
	}
	
	public void pullMessage() {
		
	}

}
