import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
 
public class pom_parser {
 private static void printNode(NodeList nodeList) {
 
    for (int count = 0; count < nodeList.getLength(); count++) {
 
	Node tempNode = nodeList.item(count);
 
	// make sure it's element node.
	if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
 
		// get node name and value
		System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
		System.out.println("Node Value =" + tempNode.getTextContent());
 
		if (tempNode.hasAttributes()) {
 
			// get attributes names and values
			NamedNodeMap nodeMap = tempNode.getAttributes();
 
			for (int i = 0; i < nodeMap.getLength(); i++) {
 
				Node node = nodeMap.item(i);
				System.out.println("attr name : " + node.getNodeName());
				System.out.println("attr value : " + node.getNodeValue());
 
			}
 
		}
 
		if (tempNode.hasChildNodes()) {
 
			// loop again if has child nodes
			printNode(tempNode.getChildNodes());
 
		}
 
		System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
 
	}
 
    }
 }
 
  public static void main(String argv[]) {
 
    try {
 
	File fXmlFile = new File("pom.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();
 
	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
 
	NodeList nList = doc.getElementsByTagName("build");
	//printNode(nList);
 
	System.out.println("----------------------------");

	Node nNode = nList.item(0);
	System.out.println(nNode.getNodeName());
	Element eElement = (Element) nNode;
	Node plugins = eElement.getElementsByTagName("plugins").item(0);
 	System.out.println(plugins.getNodeName());
 	eElement = (Element) plugins;
	NodeList plugin = eElement.getElementsByTagName("plugin");
 	//System.out.println(plugin.getNodeName());
 	System.out.println(plugin.getLength());
 	for (int temp = 0; temp < plugin.getLength(); temp++){
 		Node plg = plugin.item(temp);
 		if (plg.getNodeType() == Node.ELEMENT_NODE){
 			eElement = (Element) plg;
 			System.out.println(eElement.getElementsByTagName("artifactId").item(0).getTextContent());
 		}
 	}
 	
	// for (int temp = 0; temp < nList.getLength(); temp++) {
 
	// 	Node nNode = nList.item(temp);
 
	// 	System.out.println("\nCurrent Element :" + nNode.getNodeName());
	// 	if (nNode.getNodeName().equals("plugins")){
	// 		System.out.println("Plugins Node");
	// 	}
 
	// 	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
	// 		Element eElement = (Element) nNode;

	// 		//System.out.println("First Name : " + eElement.getElementsByTagName("dependency").item(0).getTextContent());
	// 		// System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
	// 		// System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
	// 		// System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
 
	// 	}
	// }
    } catch (Exception e) {
	e.printStackTrace();
    }
  }
 
}