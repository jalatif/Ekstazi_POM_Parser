import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
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
        expr = xpath.compile("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/text()");
        // run the query and get a nodeset
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        System.out.print("Surefire : " + (nodes.getLength() != 0) + " ");

        if (nodes.getLength() != 0) {
            expr = xpath.compile("count(/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/parent::*/preceding-sibling::*) + 1");
            result = expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.print("at plugin number : " + result + ", ");
        }

        //expr = xpath.compile("//dependencies/dependency[artifactId='junit']/artifactId/text()");
        expr = xpath.compile("/project/dependencies|/project/dependencyManagement/dependencies");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        System.out.print("Dependencies : " + (((NodeList) result).getLength() != 0) + ", ");

        expr = xpath.compile("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/excludes");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        System.out.print("Excludes : " + (((NodeList) result).getLength() != 0) + ", ");

        expr = xpath.compile("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/argLine");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        System.out.print("ArgLine : " + (((NodeList) result).getLength() != 0) + "");

        return true;
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
            for (String pom_path : poms){
                System.out.print("File : " + pom_path + ", ");
                pp.queryPom(pom_path);
                System.out.println();
            }
            //pp.queryPom(pom_file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
