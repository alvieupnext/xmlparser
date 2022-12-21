import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;

public class XMLToCSV {
    /**\
     * makeFile takes the following and creates a new csv.
     * @param fileName
     * @param listOfElements
     */
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
    //Run with the following JVM arguments -DentityExpansionLimit=10000000 -Xmx6g
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //Set Validating to true (reads the dblp.dtd and makes sure all characters are converted correctly)
        factory.setValidating(true);
        //use the SAXParser
        SAXParser saxParser = factory.newSAXParser();
        Handler handler = new Handler();
        //add the headers at the beginning of the lists
        handler.initializeHeaders();
        //parse the xml
        saxParser.parse("dblp.xml", handler);
        //after parsing, store create csvs from the lists
        makeFile("Article.csv", handler.getArticleList());
        makeFile("Inproceedings.csv", handler.getInproceedingsList());
        makeFile("Proceedings.csv", handler.getProceedingsList());
    }
}
