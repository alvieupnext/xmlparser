import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;

public class XMLToCSV {
    public static void makeFile(String fileName, List<String[]> listOfElements) {
        File myFile = new File(fileName);
        try {
            FileWriter outputfile = new FileWriter(myFile);
            CSVWriter writer = new CSVWriter(outputfile);
            for (int i = 0; i < listOfElements.size(); i++) {
                String[] element = listOfElements.get(i);
                writer.writeNext(element);
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        Handler handler = new Handler();
        handler.initializeHeaders();
        saxParser.parse("dblp.xml", handler);
        makeFile("Article.csv", handler.getArticleList());
        makeFile("Inproceedings.csv", handler.getInproceedingsList());
        makeFile("Proceedings.csv", handler.getProceedingsList());
        /*List<Inproceedings> inproceedings = handler.getInproceedingsList();
        inproceedings.forEach(System.out::println);
        List<Proceedings> proceedings = handler.getProceedingsList();
        proceedings.forEach(System.out::println);
        List <Article> articles = handler.getArticleList();
        articles.forEach(System.out::println);
         */
    }
}
