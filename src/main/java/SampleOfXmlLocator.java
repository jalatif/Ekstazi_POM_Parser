/**
 * Created by manshu on 10/25/14.
 */

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class SampleOfXmlLocator extends DefaultHandler {
    private Locator locator;
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }
    public void startElement(String uri, String localName, String qName, Attributes attrs)
            throws SAXException {
        System.out.println(qName + "->" + locator.getLineNumber());
        if (qName.equals("build")) {
            System.out.println("here process element start");
        } else {
            String location = "";
            if (locator != null) {
                location = locator.getSystemId(); // XML-document name;
                location += " line " + locator.getLineNumber();
                location += ", column " + locator.getColumnNumber();
                location += ": ";
            }
            //System.out.println(location);
            //throw new SAXException(location + "Illegal element");
        }
    }
    public static void main(String[] args) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser parser = factory.newSAXParser();
        parser.parse("/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/commons-math/pom.xml", new SampleOfXmlLocator());
    }
}
