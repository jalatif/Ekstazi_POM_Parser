package EkIntegration;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 * Created by manshu on 10/27/14.
 */
public class PomParser {

    private Document doc;
    private String ekstazi_version;
    private String xml_file;
    private XPathExpression expr;
    private XPathFactory xFactory;
    private XPath xpath;

    public static void main(String args[]) {

        String path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/temp_ekstazi/continuum";
        //path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/temp_ekstazi/cucumber/needle";
        //path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/temp_ekstazi/cucumber";
        path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/temp_ekstazi/ekZone/wildfly";
        path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/demo";

        String ek_version = "4.2.0";
        String surefire_version = "2.13";
        boolean surefire_force = false;
        int max_depth = -1;

        if (args.length > 0)
            path = args[0];
        if (args.length > 1){
            try{
                max_depth = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe){
                System.out.println("Wrong parameter");
                System.exit(99);
            }
        }
        if (args.length > 2)
            ek_version = args[2];
        if (args.length > 3){
            surefire_version = args[3];
            surefire_force = true;
        }

        ListDir ld = new ListDir();
        PomParser pp = new PomParser();

        try {
            ArrayList<String> poms = ld.ListDir(path, max_depth);
            for (String pom_path : poms) {
                System.out.print("File : " + pom_path + ", ");
                pp.queryPom(pom_path, ek_version, surefire_version, surefire_force, path);
                System.out.println();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Some configuration is not valid. Check path or other parameters");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertExcludesFile(Node configuration_node, String project_path)
    {
        //String path = xml_file_path.substring(0, xml_file_path.lastIndexOf("/"));
        Element excElement = doc.createElement("excludesFile");
        excElement.appendChild(doc.createTextNode("${java.io.tmpdir}/myExcludes"))

        ;//project_path + "/myExcludes"));
        configuration_node.appendChild(excElement);
    }

    private void addSureFireVersion(Node surefire_node, boolean surefire_force, String surefire_new_version){
        Element versionNode = doc.createElement("version");
        if (!surefire_force)
            versionNode.appendChild(doc.createTextNode("2.13"));
        else
            versionNode.appendChild(doc.createTextNode(surefire_new_version));
        surefire_node.appendChild(versionNode);
    }
    private void insertDependency(Node node){

        Element dInsert0 = doc.createElement("dependency");
        Element gInsert0 = doc.createElement("groupId");
        Element aInsert0 = doc.createElement("artifactId");
        Element vInsert0 = doc.createElement("version");

        dInsert0.appendChild(gInsert0);
        dInsert0.appendChild(aInsert0);
        dInsert0.appendChild(vInsert0);
        gInsert0.appendChild(doc.createTextNode("org.ekstazi"));
        aInsert0.appendChild(doc.createTextNode("ekstazi-maven-plugin"));
        vInsert0.appendChild(doc.createTextNode(ekstazi_version));

        node.insertBefore(dInsert0, node.getFirstChild());

    }

    private void insertBuild(Node node){
        //Element build = doc.createElement("build");
        Element plugins = doc.createElement("plugins");
        insertSurefire(plugins);
        //build.appendChild(plugins);
        node.insertBefore(plugins, node.getFirstChild());
    }

    private void insertSurefire(Node plugins)
    {
        Element plugin = doc.createElement("plugin");
        Element groupId = doc.createElement("groupId");
        Element artifactId = doc.createElement("artifactId");
        Element configuration = doc.createElement("configuration");
        groupId.setTextContent("org.apache.maven.plugin");
        artifactId.setTextContent("maven-surefire-plugin");
        plugins.appendChild(plugin);
        plugin.appendChild(groupId);
        plugin.appendChild(artifactId);
        plugin.appendChild(configuration);
    }

    private void insertPlugin(Node surefire_node)
    {
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
        if (ekstazi_version.startsWith("3"))
            idInsert.appendChild(doc.createTextNode("selection"));
        else
            idInsert.appendChild(doc.createTextNode("select"));
        exInsert.appendChild(goalsInsert);
        goalsInsert.appendChild(goalInsert);
        goalsInsert.appendChild(goalInsert2);
        if (ekstazi_version.startsWith("3"))
            goalInsert.appendChild(doc.createTextNode("selection"));
        else
            goalInsert.appendChild(doc.createTextNode("select"));
        goalInsert2.appendChild(doc.createTextNode("restore"));

        surefire_node.getParentNode().insertBefore(toInsert, surefire_node);
    }

    private Node getNode(String search_expression) throws XPathException {
        Node node = (Node) xpath.evaluate(search_expression, doc, XPathConstants.NODE);
        return node;
    }

    private String getNodeValue(String search_expression) throws XPathException {
        String node_val = (String) xpath.evaluate(search_expression, doc, XPathConstants.STRING);
        return node_val;
    }

    private NodeList getNodeList(String search_expression) throws XPathException {
        NodeList nodelist = (NodeList) xpath.evaluate(search_expression, doc, XPathConstants.NODESET);
        return nodelist;
    }

    private void writeXml(){
        //////////////////////////////////////////////////////////////////
        //Write out the final xml file again into the same pom.xml file//
        /////////////////////////////////////////////////////////////////
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            //transformer.setOutputProperty(OutputKeys.ENCODING, "US-ASCII");
            //transformer.setErrorListener(OutputKeys.);
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            doc.setXmlStandalone(true);

            //FileUtils.copyFile(new File(xml_file), new File(xml_file+"_orig"));

            DOMSource source = new DOMSource(doc);
            String modified_xml_file = xml_file.substring(0, xml_file.lastIndexOf("/") + 1) + "ekstazi_" + xml_file.substring(xml_file.lastIndexOf("/") + 1);
            StreamResult _result = new StreamResult(modified_xml_file);
            transformer.transform(source, _result);
            boolean minimized = MinimizeDiff.MinimizeDiff(xml_file, modified_xml_file);
            if (!minimized){
                File foriginal = new File(xml_file);
                File fmodified = new File(modified_xml_file);
                FileUtils.moveFile(foriginal, fmodified);
            }else{
                File fmodified = new File(modified_xml_file);
                FileUtils.forceDelete(fmodified);
            }

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean queryPom(String xml_file, String ekstazi_version, String surefire_new_version, boolean surefire_force, String project_path) throws ParserConfigurationException, IOException, XPathException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.ekstazi_version = ekstazi_version;
        this.xml_file = xml_file;
        this.expr = null;
        this.xFactory = XPathFactory.newInstance();
        try{
            this.doc = builder.parse(xml_file);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Craaaaaaaaash");
            return false;
        }
        this.doc.getDocumentElement().normalize();
        this.xpath = xFactory.newXPath();

        Node build = getNode("/project/build");
        if(build == null) {
            System.out.println("Build Not Present !!");
            Node project_node = getNode("/project");
            Node project_artifact_node = getNode("/project/artifactId");
            Element build_node = doc.createElement("build");
            if (project_artifact_node != null && project_artifact_node.getNextSibling() != null){
                project_node.insertBefore(build_node, project_artifact_node.getNextSibling());
                insertBuild(build_node);
            }
        }
        else {
            Node plugins = getNode("/project/build/plugins|/project/build/pluginManagement/plugins");
            if (plugins == null){
                insertBuild(build);
                System.out.println("Added plugins node Now !!");
            }
            else
                System.out.println("Plugins Present");
        }
        NodeList surefire_plugins_node = getNodeList("//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]");
        ArrayList<String> surefire_plugin_paths = new ArrayList<String>();

        for (int n = 0; n < surefire_plugins_node.getLength(); n++){
            Node current_node = surefire_plugins_node.item(n);
            String surefire_node_path = "";
            while (current_node.getParentNode() != null){
                //System.out.println("Surefire address = " + current_node.getNodeName());
                current_node = current_node.getParentNode();
                if (current_node.getParentNode() != null)
                    surefire_node_path = "/" + current_node.getNodeName() + surefire_node_path;
            }
            if (surefire_plugin_paths.contains(surefire_node_path))
                continue;
            surefire_plugin_paths.add(surefire_node_path);
            System.out.println(surefire_node_path);
        }
        System.out.println("Num surefires = " + surefire_plugins_node.getLength());
//
//        Node plugins_node = getNode("/project/build/plugins/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/text()");
//        Node pluginsM_node = getNode("/project/build/pluginManagement/plugins/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/text()");
//
//        String plugin_path = "/project/build/plugins";
//        String pluginM_path = "/project/build/pluginManagement/plugins";
//
//        if (plugins_node == null) {
//            if (pluginsM_node != null){
//                plugins_node = pluginsM_node;
//                plugin_path = pluginM_path;
//            }
//        }
//
        if (surefire_plugin_paths.size() == 0)
            surefire_plugin_paths.add("");

        for (String plugin_path : surefire_plugin_paths) {
            System.out.println();
            System.out.println("\nUsing surefire path = " + plugin_path);
            NodeList nodes = getNodeList(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/text()");
            NodeList surefire_plugin = nodes;
            System.out.print("Surefire : " + (nodes.getLength() != 0) + " ");

            NodeList ekstazi_plugin = getNodeList(plugin_path + "/plugin[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/artifactId/text()");
            System.out.print(" Ekstazi Plugin Present : " + (ekstazi_plugin.getLength() != 0) + ", ");
//
//            Node ekstazi_plugin = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/artifactId/text()");
//            System.out.print(" Ekstazi Plugin Present : " + (ekstazi_plugin != null) + ", ");
//
//            if (ekstazi_plugin != null){
//                String ekVersion = getNodeValue(plugin_path + "/plugin[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/version");
//                if (ekVersion.equals(ekstazi_version))
//            }
            Node surefire_node = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]");

            if (surefire_node == null && !plugin_path.equals("")) {
                Node plugins = getNode(plugin_path);
                //            if(plugins == null)
                //                plugins = getNode("/project/build/pluginManagement/plugins");
                //
                //            if(plugins == null)
                //                plugins = getNode("/project/build/pluginManagement");

                if (plugins != null)
                    insertSurefire(plugins);

                surefire_node = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]");
            }

            if (surefire_node != null) {
                String surefire_version = getNodeValue(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/version");
                if (!surefire_version.equals("")) {
                    try {
                        double version = Double.parseDouble(surefire_version);
                        //if (version <= 2.10 || version > 2.2) {
                        if (!surefire_force) {
                            if (version < 2.13) {
                                System.out.println("\nVersion not supported = " + surefire_version);
                                Node version_node = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/version");
                                version_node.setTextContent(surefire_new_version);
                                System.out.println("Surfire version upgraded to " + surefire_new_version);
                            } else {
                                System.out.println("\nVersion Supported = " + surefire_version);
                            }
                        } else {
                            System.out.println("\nPrevious Version = " + surefire_version);
                            Node version_node = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/version");
                            version_node.setTextContent(surefire_new_version);
                            System.out.println("Surfire version forcefully upgraded to " + surefire_new_version);
                        }
                    } catch (NumberFormatException ex) {
                        if (surefire_force) {
                            System.out.println("\nPrevious Version = " + surefire_version);
                            Node version_node = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/version");
                            version_node.setTextContent(surefire_new_version);
                            System.out.println("Surefire version forcefully upgraded to " + surefire_new_version);
                        } else {
                            System.out.println("Creates variable as version of surefire");
                        }

                    }
                } else {
                    addSureFireVersion(surefire_node, surefire_force, surefire_new_version);
                }
            }

            NodeList argline_nodes = getNodeList(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]//argLine");
            Node argline_node = null;
            for (int i = 0; i < argline_nodes.getLength(); i++) {
                argline_node = argline_nodes.item(i);
                if (argline_node != null) {
                    System.out.println("ArgLine present in this one");
                    String argText = argline_node.getTextContent();
                    if (!argText.contains("${argLine}")) {
                        argline_node.setTextContent("${argLine} " + argText);
                        System.out.println("Now argLine content = " + argline_node.getTextContent());
                    } else
                        System.out.println("No change required");

                } else {
                    System.out.println("ArgLine not present");
                }
            }
            //Adding Ekstazi Plugin
            nodes = getNodeList(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/text()");
            if (nodes.getLength() != 0 && ekstazi_plugin.getLength() == 0) {
                //expr = xpath.compile("count(/project/build//plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId/parent::*/preceding-sibling::*) + 1");
                //result = expr.evaluate(doc, XPathConstants.NUMBER);
                //System.out.print("at plugin number : " + result + ", ");

                insertPlugin(surefire_node);
            }

            //Adding dependencies tag and ekstazi dependency
            NodeList dependencies = getNodeList("/project/dependencies|/project/dependencyManagement/dependencies");
            System.out.print("Dependencies : " + (dependencies.getLength() != 0) + " ");

            NodeList ekstazi_dependency = getNodeList("/project/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]|/project/dependencyManagement/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]");
            System.out.print("Ekstazi Dependency : " + (ekstazi_dependency.getLength() != 0) + " ");

            if (ekstazi_dependency.getLength() != 0){
                String ekVersion = getNodeValue("/project/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/version|/project/dependencyManagement/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/version");
                System.out.println("Ekstazi Version = " + ekVersion);
                Node ek_version_node = getNode("/project/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/version|/project/dependencyManagement/dependencies/dependency[artifactId[contains(text(), 'ekstazi-maven-plugin')]]/version");
                if (!ekVersion.equals(ekstazi_version))
                    ek_version_node.setTextContent(ekstazi_version);
            }

            if (dependencies.getLength() == 0) {
                Node project_node = getNode("/project");
                Node project_artifact_node = getNode("/project/artifactId");
                Element dependencies_node = doc.createElement("dependencies");
                if (project_artifact_node != null && project_artifact_node.getNextSibling() != null) {
                    project_node.insertBefore(dependencies_node, project_artifact_node.getNextSibling());
                    insertDependency(dependencies_node);
                }
            } else if (ekstazi_dependency.getLength() == 0) {
                Node dependencies_node = getNode("/project/dependencies|/project/dependencyManagement/dependencies");
                insertDependency(dependencies_node);
            }

            //Adding Excludes Configuration
            NodeList excludes_configuration = getNodeList(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration");
            System.out.print("Excludes : " + (excludes_configuration.getLength() != 0) + " ");

            NodeList excludesFile = getNodeList(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/excludesFile/text()");
            System.out.print("ExcludesFile Present : " + (excludesFile.getLength() > 0 && excludesFile.item(0).getNodeValue().equalsIgnoreCase("myExcludes")) + ", ");

            if (surefire_plugin.getLength() != 0 && excludes_configuration.getLength() == 0) {
                surefire_node = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]");
                Node artifactId = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/artifactId");
                Element configuration = doc.createElement("configuration");
                surefire_node.insertBefore(configuration, artifactId.getNextSibling());
                insertExcludesFile(configuration, project_path);
            } else if (excludesFile.getLength() == 0 && excludes_configuration.getLength() != 0) {
                Node configuration_node = getNode(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration");
                insertExcludesFile(configuration_node, project_path);
            }

            //Check ArgsLine
            NodeList argsLine = getNodeList(plugin_path + "/plugin[artifactId[contains(text(), 'maven-surefire-plugin')]]/configuration/argLine");
            System.out.print("ArgLine : " + (argsLine.getLength() != 0) + "");
        }

        //Write to file
        writeXml();

        return true;
    }


}