import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Handler extends DefaultHandler {
    //Keeps track of current value
    private StringBuilder currentValue = new StringBuilder();
    //Lists for the different kind of articles
    private List<String[]> inproceedingsList = new ArrayList<>();
    private List<String[]> articleList = new ArrayList<>();
    private List<String[]> proceedingsList = new ArrayList<>();

    //method to remove last character
    private String removeLastChar(String s)
    {
        if (s.length() == 1){
            return s;
        }
        else {
            //returns the string after removing the last character
            return s.substring(0, s.length() - 1);
        }
    }

    Inproceedings currentInproceeding;
    Article currentArticle;
    Proceedings currentProceeding;

    //getters for the list
    public List<String[]> getInproceedingsList() { return inproceedingsList; }

    public List<String[]> getArticleList() {
        return articleList;
    }

    public List<String[]> getProceedingsList() {
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
        if (qName.equalsIgnoreCase("article")){
            //new article
            currentArticle = new Article();
            currentArticle.setKey(attributes.getValue("key"));
            current = "article";
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //depending on current, update the right value
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
        if (current == "article"){
            if(qName.equalsIgnoreCase("title")){
                currentArticle.setTitle(currentValue.toString());
            }
            if(qName.equalsIgnoreCase("author")){
                currentArticle.addAuthor(currentValue.toString());
            }
            if(qName.equalsIgnoreCase("journal")){
                currentArticle.setJournal(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("volume")){
                currentArticle.setVolume(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("number")){
                currentArticle.setNumber(currentValue.toString());
            }
            if (qName.equalsIgnoreCase("year")){
                currentArticle.setYear(Integer.parseInt(currentValue.toString()));
            }
        }
        // end of conference article
        if (qName.equalsIgnoreCase("inproceedings")) {
            //this structure will save the authors as a list of strings
            String authorsString = "[";
            for (int i = 0; i < currentInproceeding.getAuthors().size(); i++){
                authorsString  = authorsString + "\"" + currentInproceeding.getAuthors().get(i).replace("\"", "\'") + "\"" + ",";
            }
            authorsString = removeLastChar(authorsString) + "]";
            //serialize the inproceeding
            String [] inproceedingString = {
                    currentInproceeding.getKey(),
                    authorsString,
                    currentInproceeding.getTitle(),
                    currentInproceeding.getPages(),
                    Integer.toString(currentInproceeding.getYear()),
                    currentInproceeding.getBooktitle()
            };
            inproceedingsList.add(inproceedingString);
            System.out.println(inproceedingString);
            current = "none";
        }
        //end of conference proceedings
        if (qName.equalsIgnoreCase("proceedings")) {
            //this structure will save the editors as a list of strings
            String editorsString = "[";
            for (int i = 0; i < currentProceeding.getEditors().size(); i++){
                editorsString = editorsString + "\"" + currentProceeding.getEditors().get(i).replace("\"", "\'") + "\"" + ",";
            }
            editorsString = removeLastChar(editorsString) + "]";
            String [] proceedingsString = {
                    currentProceeding.getKey(),
                    editorsString,
                    currentProceeding.getTitle(),
                    currentProceeding.getBooktitle(),
                    currentProceeding.getPublisher(),
                    currentProceeding.getVolume(),
                    Integer.toString(currentProceeding.getYear())
            };
            //serialize the proceeding
            proceedingsList.add(proceedingsString);
            current = "none";
        }
        if (qName.equalsIgnoreCase("article")){
            //this structure will save the authors as a list of strings
            String authorsString = "[";
            for (int i = 0; i < currentArticle.getAuthors().size(); i++){
                authorsString  = authorsString + "\"" + currentArticle.getAuthors().get(i).replace("\"", "\'") + "\"" + ",";
            }
            authorsString = removeLastChar(authorsString) + "]";
            //serialize the article
            String[] articleString = {
                    currentArticle.getKey(),
                    authorsString,
                    currentArticle.getTitle(),
                    currentArticle.getJournal(),
                    currentArticle.getVolume(),
                    currentArticle.getNumber(),
                    Integer.toString(currentArticle.getYear())
            };
            articleList.add(articleString);
            current = "none";
        }
    }

    public void initializeHeaders() {
        //initialize the csv headers and add them to the list
        String[] articleHeader = {"key","authors", "title", "journal", "volume", "number", "year"};
        articleList.add(articleHeader);
        String[] inproceedingHeader = {"key", "authors", "title", "pages", "year", "booktitle"};
        inproceedingsList.add(inproceedingHeader);
        String[] proceedingsHeader = {"key", "editors", "title", "booktitle", "publisher", "volume", "year"};
        proceedingsList.add(proceedingsHeader);
    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
