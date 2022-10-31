import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class InproceedingsHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private List<Inproceedings> inproceedingsList = new ArrayList<>();
    Inproceedings currentInproceeding;

    public List<Inproceedings> getInproceedingsList() {
        return inproceedingsList;
    }

    boolean inproceeding = false;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset the tag value
        currentValue.setLength(0);

        if (qName.equalsIgnoreCase("inproceedings")) {
            // new inproceeding
            currentInproceeding = new Inproceedings();
            currentInproceeding.setKey(attributes.getValue("key"));
            inproceeding = true;
        }
//        System.out.print("kaas");
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (inproceeding){
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
        // end of article
        if (qName.equalsIgnoreCase("inproceedings")) {
            inproceedingsList.add(currentInproceeding);
            inproceeding = false;
        }

    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
