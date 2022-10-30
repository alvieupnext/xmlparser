import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public class XMLToCSV {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        ArticleHandler articleHandler = new ArticleHandler();
        saxParser.parse("dblp.xml", articleHandler);
        List<Article> articles = articleHandler.getList();
        articles.forEach(System.out::println);
    }

}
