import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XMLToCSV {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        ArticleHandler handler = new ArticleHandler();
        saxParser.parse("dblp.xml", handler);
        List<Article> result = handler.getList();
        result.forEach(System.out::println);
//        Document doc = builder.parse(new File("dblp.xml"));
//        doc.normalize();
//        StreamSource xsl = new StreamSource(new File("filter.xslt"));
//        TransformerFactory tf = TransformerFactory.newInstance();
//        tf.newTransformer(xsl).transform(new DOMSource(doc), new StreamResult(new File("output.xml")));
    }

}
