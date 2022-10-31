import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class InproceedingsHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private List<Inproceedings> inproceedingsList = new ArrayList<>();
    private List<Article> articleList = new ArrayList<>();
    private List<Proceedings> proceedingsList = new ArrayList<>();

    Inproceedings currentInproceeding;
    Article currentArticle;
    Proceedings currentProceeding;

    public List<Inproceedings> getInproceedingsList() {
        return inproceedingsList;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public List<Proceedings> getProceedingsList() {
        return proceedingsList;
    }

    String current = "none";

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset the tag value
        currentValue.setLength(0);

        if (qName.equalsIgnoreCase("inproceedings")) {
            // new inproceeding
            currentInproceeding = new Inproceedings();
            currentInproceeding.setKey(attributes.getValue("key"));
            current = "inproceedings";
        }
        if (qName.equalsIgnoreCase("proceedings")){
            //new proceeding
            currentProceeding = new Proceedings();
            currentProceeding.setKey(attributes.getValue("key"));
            current = "proceedings";
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (current == "inproceedings"){
            if (qName.equalsIgnoreCase("author")){
                currentInproceeding.addAuthor(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("title")){
                currentInproceeding.setTitle(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("pages")){
                currentInproceeding.setPages(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("year")){
                currentInproceeding.setYear(Integer.parseInt(currentValue.toString()));
            }
            if (qName.equalsIgnoreCase("booktitle")){
                currentInproceeding.setBooktitle(currentValue.toString());
            }
        }
        if (current == "proceedings"){
            if (qName.equalsIgnoreCase("editor")){
                currentProceeding.addEditor(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("title")){
                currentProceeding.setTitle(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("booktitle")){
                currentProceeding.setBooktitle(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("publisher")){
                currentProceeding.setPublisher(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("volume")){
                currentProceeding.setVolume(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("year")){
                currentProceeding.setYear(Integer.parseInt(currentValue.toString()));
            }

        }
        // end of article
        if (qName.equalsIgnoreCase("inproceedings")) {
            inproceedingsList.add(currentInproceeding);
            current = "none";
        }
        if (qName.equalsIgnoreCase("proceedings")) {
            proceedingsList.add(currentProceeding);
            current = "none";
        }


    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
