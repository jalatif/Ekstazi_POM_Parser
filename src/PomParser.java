import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by manshu on 10/8/14.
 */
public class PomParser {
    public static void main(String args[]) {
        String pom_file = "/home/manshu/Templates/EXEs/CS527SE/Homework/pom_parser/pom.xml";
        String current_path = System.getProperty("user.dir");
        System.out.println("Current Path = " + current_path);
        String path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/commons-math/";
        if (args.length > 0) path = args[0];

        ListDir ld = new ListDir();
        PomParser pp = new PomParser();
        ArrayList<String> poms = ld.ListDir(path);
        try {
//            for (String pom_path : poms){
//                System.out.print("File : " + pom_path + ", ");
//                pp.queryPom(pom_path);
//                System.out.println();
//            }
            pp.queryPom(pom_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean queryPom(String xml_file) throws ParserConfigurationException, IOException, XPathException, SAXException {
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

            expr = xpath.compile("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]");
            Node surefire_node = (Node) xpath.evaluate("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]", doc, XPathConstants.NODE);
            Element toInsert = doc.createElement("plugin");
            Element dsInsert = doc.createElement("dependencies");
            Element dInsert = doc.createElement("dependency");
            Element gInsert = doc.createElement("groupId");
            Element aInsert = doc.createElement("artifactId");
            Element vInsert = doc.createElement("version");
            Text txt1 = doc.createTextNode("org.ekstazi");
            Text txt2 = doc.createTextNode("org.ekstazi.core");
            Text txt3 = doc.createTextNode("3.4.2");
            toInsert.appendChild(dsInsert);
            dsInsert.appendChild(dInsert);
            dInsert.appendChild(gInsert);
            dInsert.appendChild(aInsert);
            dInsert.appendChild(vInsert);
            gInsert.appendChild(txt1);
            aInsert.appendChild(txt2);
            vInsert.appendChild(txt3);
            surefire_node.getParentNode().insertBefore(toInsert, surefire_node);

            ////////////////////////////////////////////////////////
            // Write out the final xml file
            // Use a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = tFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult _result = new StreamResult("demo1.xml");
                transformer.transform(source, _result);
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }


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
}
