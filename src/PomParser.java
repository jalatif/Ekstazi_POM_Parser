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
        String ek_version = "3.4.2";
        if (args.length > 0) path = args[0];

        ListDir ld = new ListDir();
        PomParser pp = new PomParser();
        ArrayList<String> poms = ld.ListDir(path);
        try {
            for (String pom_path : poms) {
                System.out.print("File : " + pom_path + ", ");
                pp.queryPom(pom_path, ek_version);
                System.out.println();
            }
            //pp.queryPom(pom_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean queryPom(String xml_file, String ekstazi_version) throws ParserConfigurationException, IOException, XPathException, SAXException {
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

        NodeList ekstazi_plugin = (NodeList) xpath.evaluate("/project/build//plugin[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/artifactId/text()", doc, XPathConstants.NODESET);
        System.out.print(" EkPlugin Present : " + (nodes.getLength() != 0) + ", ");

        if (nodes.getLength() != 0 && ekstazi_plugin.getLength() == 0) {
            expr = xpath.compile("count(/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/parent::*/preceding-sibling::*) + 1");
            result = expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.print("at plugin number : " + result + ", ");

            //expr = xpath.compile("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]");
            Node surefire_node = (Node) xpath.evaluate("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]", doc, XPathConstants.NODE);
            Element toInsert = doc.createElement("plugin");
            Element dsInsert = doc.createElement("dependencies");
            Element dInsert = doc.createElement("dependency");
            Element gInsert0 = doc.createElement("groupId");
            Element gInsert = doc.createElement("groupId");
            Element aInsert0 = doc.createElement("artifactId");
            Element aInsert = doc.createElement("artifactId");
            Element vInsert0 = doc.createElement("version");
            Element vInsert = doc.createElement("version");
            Element exsInsert = doc.createElement("executions");
            Element exInsert = doc.createElement("execution");
            Element idInsert = doc.createElement("id");
            Element goalsInsert = doc.createElement("goals");
            Element goalInsert = doc.createElement("goal");
            Element goalInsert2 = doc.createElement("goal");

            Text txt1 = doc.createTextNode("org.ekstazi");
            Text txt2 = doc.createTextNode("org.ekstazi.core");
            Text txt3 = doc.createTextNode(ekstazi_version);
            toInsert.appendChild(dsInsert);
            dsInsert.appendChild(dInsert);
            dInsert.appendChild(gInsert0);
            dInsert.appendChild(aInsert0);
            dInsert.appendChild(vInsert0);
            gInsert0.appendChild(txt1);
            aInsert0.appendChild(txt2);
            vInsert0.appendChild(txt3);


            toInsert.appendChild(gInsert);
            toInsert.appendChild(aInsert);
            toInsert.appendChild(vInsert);
            gInsert.appendChild(doc.createTextNode("org.ekstazi"));
            aInsert.appendChild(doc.createTextNode("ekstazi-maven-plugin"));
            vInsert.appendChild(doc.createTextNode(ekstazi_version));
            toInsert.appendChild(exsInsert);
            exsInsert.appendChild(exInsert);
            exInsert.appendChild(idInsert);
            idInsert.appendChild(doc.createTextNode("selection"));
            exInsert.appendChild(goalsInsert);
            goalsInsert.appendChild(goalInsert);
            goalsInsert.appendChild(goalInsert2);
            goalInsert.appendChild(doc.createTextNode("select"));
            goalInsert2.appendChild(doc.createTextNode("restore"));


            surefire_node.getParentNode().insertBefore(toInsert, surefire_node);



        }

        //expr = xpath.compile("//dependencies/dependency[artifactId='junit']/artifactId/text()");
        expr = xpath.compile("/project/dependencies|/project/dependencyManagement/dependencies");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        System.out.print("Dependencies : " + (((NodeList) result).getLength() != 0) + " ");

        NodeList ekstazi_dependency = (NodeList) xpath.evaluate("/project/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]|/project/dependencyManagement/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]", doc, XPathConstants.NODESET);
        System.out.print("Dependencies Present : " + (ekstazi_dependency.getLength() != 0) + ", ");

        if (((NodeList) result).getLength() != 0) {
            Node dependencies_node = (Node) xpath.evaluate("/project/dependencies|/project/dependencyManagement/dependencies", doc, XPathConstants.NODE);
            Element dInsert0 = doc.createElement("dependency");
            Element dInsert = doc.createElement("dependency");
            Element gInsert0 = doc.createElement("groupId");
            Element gInsert = doc.createElement("groupId");
            Element aInsert0 = doc.createElement("artifactId");
            Element aInsert = doc.createElement("artifactId");
            Element vInsert0 = doc.createElement("version");
            Element vInsert = doc.createElement("version");

            dInsert0.appendChild(gInsert0);
            dInsert0.appendChild(aInsert0);
            dInsert0.appendChild(vInsert0);
            gInsert0.appendChild(doc.createTextNode("org.ekstazi"));
            aInsert0.appendChild(doc.createTextNode("ekstazi-maven-plugin"));
            vInsert0.appendChild(doc.createTextNode(ekstazi_version));

            dependencies_node.insertBefore(dInsert0, dependencies_node.getFirstChild());
        }

        expr = xpath.compile("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/excludes");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        System.out.print("Excludes : " + (((NodeList) result).getLength() != 0) + " ");

        NodeList excludesFile = (NodeList) xpath.evaluate("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/excludes/excludesFile/text()", doc, XPathConstants.NODESET);
        System.out.print("ExcludesFile Present : " + (excludesFile.getLength() > 0 && excludesFile.item(0).getNodeValue().equalsIgnoreCase("myExcludes")) + ", ");
        if (((NodeList) result).getLength() != 0 && !(excludesFile.getLength() > 0 && excludesFile.item(0).getNodeValue().equalsIgnoreCase("myExcludes"))) {
            Node excludes_node = (Node) xpath.evaluate("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/excludes", doc, XPathConstants.NODE);
            Element excElement = doc.createElement("excludesFile");
            excElement.appendChild(doc.createTextNode("myExcludes"));
            excludes_node.insertBefore(excElement, excludes_node.getFirstChild());
        }

        expr = xpath.compile("/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/argLine");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        System.out.print("ArgLine : " + (((NodeList) result).getLength() != 0) + "");

        ////////////////////////////////////////////////////////
        // Write out the final xml file
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult _result = new StreamResult(xml_file);
            transformer.transform(source, _result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
