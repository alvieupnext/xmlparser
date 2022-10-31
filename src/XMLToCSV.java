import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;

public class XMLToCSV {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        InproceedingsHandler inproceedingsHandler = new InproceedingsHandler();
        saxParser.parse("dblp.xml", inproceedingsHandler);
        List<Inproceedings> inproceedings = inproceedingsHandler.getInproceedingsList();
        inproceedings.forEach(System.out::println);
        List<Proceedings> proceedings = inproceedingsHandler.getProceedingsList();
        proceedings.forEach(System.out::println);
    }

}
