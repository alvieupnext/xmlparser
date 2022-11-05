import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.LinkedList;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

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
        saxParser.parse("testdoc.xml", handler);
        makeFile("article.csv", handler.getArticleList());
        makeFile("inprocedings.csv", handler.getInproceedingsList());
        makeFile("procedings.csv", handler.getProceedingsList());
        /*List<Inproceedings> inproceedings = handler.getInproceedingsList();
        inproceedings.forEach(System.out::println);
        List<Proceedings> proceedings = handler.getProceedingsList();
        proceedings.forEach(System.out::println);
        List <Article> articles = handler.getArticleList();
        articles.forEach(System.out::println);
         */
    }
}
