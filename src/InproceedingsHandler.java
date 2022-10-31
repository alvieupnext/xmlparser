import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class InproceedingsHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private List<Inproceedings> list = new ArrayList<>();
    Inproceedings currentArticle;

    public List<Inproceedings> getList() {
        return list;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset the tag value
        currentValue.setLength(0);

        if (qName.equalsIgnoreCase("article")) {
            // new staff
            currentArticle = new Inproceedings();
            currentArticle.setId(attributes.getValue("key"));
        }
//        System.out.print("kaas");
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("author")){
            currentArticle.addAuthor(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("title")){
            currentArticle.setTitle(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("pages")){
            currentArticle.setPages(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("year")){
            currentArticle.setYear(Integer.parseInt(currentValue.toString()));
        }
        if (qName.equalsIgnoreCase("booktitle")){
            currentArticle.setBooktitle(currentValue.toString());
        }

        // end of article
        if (qName.equalsIgnoreCase("article")) {
            list.add(currentArticle);
        }

    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
