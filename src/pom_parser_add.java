import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by manshu on 10/23/14.
 */
public class pom_parser_add {
    private Document doc;
    private String ekstazi_version;
    private String xml_file;
    private XPathExpression expr;
    private XPathFactory xFactory;
    private XPath xpath;


    public static void main(String args[]) {
        String pom_file = "/home/manshu/Templates/EXEs/CS527SE/Homework/pom_parser/pom.xml";
        String current_path = System.getProperty("user.dir");
        System.out.println("Current Path = " + current_path);
        String path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/commons-math/";
        path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/demo";
        String ek_version = "3.4.2";
        if (args.length > 0)
            path = args[0];
        if (args.length > 1)
            ek_version = args[1];
        ListDir ld = new ListDir();
        pom_parser_add pp = new pom_parser_add();
        ArrayList<String> poms = ld.ListDir(path);
        try {
            for (String pom_path : poms) {
                System.out.print("File : " + pom_path + ", ");
                pp.queryPom(pom_path, ek_version);
                System.out.println();
            }
            //pp.queryPom(pom_file, ek_version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertDependency(Node node){

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

        node.insertBefore(dInsert0, node.getFirstChild());

    }

    private void writeXml(){
        //////////////////////////////////////////////////////////////////
        // Write out the final xml file again into the same pom.xml file
        /////////////////////////////////////////////////////////////////
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult _result = new StreamResult(xml_file);
            transformer.transform(source, _result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private Node getNode(String search_expression) throws XPathException {
        Node node = (Node) xpath.evaluate(search_expression, doc, XPathConstants.NODE);
        return node;
    }

    private NodeList getNodeList(String search_expression) throws XPathException {
        NodeList nodelist = (NodeList) xpath.evaluate(search_expression, doc, XPathConstants.NODESET);
        return nodelist;
    }

    public void queryPom(String xml_file, String ekstazi_version) throws ParserConfigurationException, IOException, XPathException, SAXException {
        this.ekstazi_version = ekstazi_version;
        this.xml_file = xml_file;
        this.expr = null;
        this.xFactory = XPathFactory.newInstance();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        this.doc = builder.parse(xml_file);

        // create an XPath object
        this.xpath = xFactory.newXPath();

        NodeList dependencies = getNodeList("/project/dependencies|/project/dependencyManagement/dependencies");

        System.out.print("Dependencies tag : " + (dependencies.getLength() != 0) + " ");

        NodeList ekstazi_dependency = getNodeList("/project/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]|/project/dependencyManagement/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]");//(NodeList) xpath.evaluate(", doc, XPathConstants.NODESET);
        System.out.print("Ekstazi Dependencies Present : " + (ekstazi_dependency.getLength() != 0) + ", ");

        if (dependencies.getLength() == 0){
            Node project_node = getNode("/project");
            Node project_version_node = getNode("/project/version");
            Element dependencies_node = doc.createElement("dependencies");
            project_node.insertBefore(dependencies_node, project_version_node.getNextSibling());
            insertDependency(dependencies_node);
        }

        else if (ekstazi_dependency.getLength() == 0) {
            Node dependencies_node = getNode("/project/dependencies|/project/dependencyManagement/dependencies");
            insertDependency(dependencies_node);
        }

        writeXml();
    }
}
