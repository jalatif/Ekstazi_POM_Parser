import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by manshu on 10/8/14.
 */
public class PomParser {
    public boolean queryPom(String xml_file) throws ParserConfigurationException, IOException, XPathException, SAXException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xml_file);

        XPathExpression expr = null;


        // create an XPathFactory
        XPathFactory xFactory = XPathFactory.newInstance();

        // create an XPath object
        XPath xpath = xFactory.newXPath();

        // compile the XPath expression
        //expr = xpath.compile("/build/plugins/plugin[artifactId='maven-surefire-plugin']/groupId/text()");//person[firstname='Lars']/lastname/text()");
        expr = xpath.compile("/project/build/plugins/plugin[artifactId='maven-surefire-plugin']/groupId/text()");
        // run the query and get a nodeset
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        // cast the result to a DOM NodeList
        NodeList nodes = (NodeList) result;
        //System.out.println(nodes.getLength());
        for (int i=0; i < nodes.getLength();i++){
            //System.out.println(nodes.item(i).getNodeValue());
        }
        return nodes.getLength() != 0;

//        // new XPath expression to get the number of people with name Lars
//        expr = xpath.compile("count(//person[firstname='Lars'])");
//        // run the query and get the number of nodes
//        Double number = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
//        System.out.println("Number of objects " +number);
//
//        // do we have more than 2 people with name Lars?
//        expr = xpath.compile("count(//person[firstname='Lars']) >2");
//        // run the query and get the number of nodes
//        Boolean check = (Boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);
//        System.out.println(check);
    }
    public static void main(String args[]){
        String pom_file = "/home/manshu/Templates/EXEs/CS527SE/Homework/pom_parser/pom.xml";
        String current_path = System.getProperty("user.dir");
        System.out.println("Current Path = " + current_path);
        String path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/commons-math/";
        if (args.length > 0)   path = args[0];

        ListDir ld = new ListDir();
        PomParser pp = new PomParser();
        ArrayList<String> poms = ld.ListDir(path);
        try{
            for (String pom_path : poms)
                System.out.println(pp.queryPom(pom_path));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
