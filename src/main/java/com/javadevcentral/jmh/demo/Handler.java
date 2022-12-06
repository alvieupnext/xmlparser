package com.javadevcentral.jmh.demo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Handler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private List<String[]> inproceedingsList = new ArrayList<>();
    private List<String[]> articleList = new ArrayList<>();
    private List<String[]> proceedingsList = new ArrayList<>();

    Inproceedings currentInproceeding;
    Article currentArticle;
    Proceedings currentProceeding;

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
            currentArticle = new Article();
            currentArticle.setKey(attributes.getValue("key"));
            current = "article";
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
        // end of article
        if (qName.equalsIgnoreCase("inproceedings")) {
            String [] inproceedingString = {
                    currentInproceeding.getClass().toString(),
                    currentInproceeding.getKey(),
                    currentInproceeding.getAuthors().toString(),
                    currentInproceeding.getTitle(),
                    currentInproceeding.getPages(),
                    Integer.toString(currentInproceeding.getYear()),
                    currentInproceeding.getBooktitle()
            };
            inproceedingsList.add(inproceedingString);
            System.out.println(inproceedingString);
            current = "none";
        }
        if (qName.equalsIgnoreCase("proceedings")) {
            String [] proceedingsString = {
                    currentProceeding.getClass().toString(),
                    currentProceeding.getKey(),
                    currentProceeding.getEditors().toString(),
                    currentProceeding.getTitle(),
                    currentProceeding.getBooktitle(),
                    currentProceeding.getPublisher(),
                    currentProceeding.getVolume(),
                    Integer.toString(currentProceeding.getYear())
            };
            proceedingsList.add(proceedingsString);
            current = "none";
        }
        if (qName.equalsIgnoreCase("article")){
            String[] articleString = {
                    currentArticle.getClass().toString(),
                    currentArticle.getKey(),
                    currentArticle.getAuthors().toString(),
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
        String[] articleHeader = {"Class Name", "Key","Authors", "Title", "Journal", "Volume", "Number", "Year"};
        articleList.add(articleHeader);
        String[] inproceedingHeader = {"Class Name", "Key", "Authors", "Title", "Pages", "Year", "Booktitle"};
        inproceedingsList.add(inproceedingHeader);
        String[] proceedingsHeader = {"Class Name", "Key", "Editors", "Title", "Booktitle", "Publisher", "Volume", "Year"};
        proceedingsList.add(proceedingsHeader);
    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
