import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;

public class XMLToCSV {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        Handler handler = new Handler();
        saxParser.parse("dblp.xml", handler);
        List<Inproceedings> inproceedings = handler.getInproceedingsList();
        inproceedings.forEach(System.out::println);
        List<Proceedings> proceedings = handler.getProceedingsList();
        proceedings.forEach(System.out::println);
        List <Article> articles = handler.getArticleList();
        articles.forEach(System.out::println);
    }

}
