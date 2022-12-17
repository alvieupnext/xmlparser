package com.javadevcentral.jmh.demo;

import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.session.OrderingType;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Project {

    public String E1(IDocumentSession session) {
        return session.query(Proceedings.class)
                .whereEquals("booktitle", "PODS")
                .skip(0).take(1)
                .selectFields(String.class, "publisher")
                .firstOrDefault();
    }

    public List<String> E2(IDocumentSession session) {
        return session.query(Article.class)
                .containsAny("authors", Collections.singletonList("Martin Grohe"))
                .andAlso()
                .whereEquals("journal", "Theory Comput. Syst.")
                .selectFields(String.class, "title")
                .orderBy("title")
                .toList();
    }

    public Integer M1(IDocumentSession session) {
        return session.query(Inproceedings.class)
                .whereEquals("booktitle", "SIGMOD Conference")
                .andAlso()
                .whereEquals("year", 2022)
                .count();
    }

    public Integer M2(IDocumentSession session) {
        String oldest_journal =
                session.query(Article.class)
                        .orderBy("year")
                        .selectFields(String.class, "journal")
                        //skip the three error Articles
                        .skip(3)
                        .take(1)
                        .firstOrDefault();
        System.out.println(oldest_journal);
        return session.query(Article.class)
                .whereEquals("journal", oldest_journal)
                .count();
    }

    public Integer M3(IDocumentSession session) {
        List<Integer> numbers = session
                .query(Inproceedings.class)
                .groupBy("year", "booktitle")
                .selectCount()
                .whereEquals("booktitle","CIDR")
                .selectFields(Integer.class, "Count")
                .toList();
        Collections.sort(numbers);
        //median calculation
        if (numbers.size() % 2 == 0){
            return (numbers.get(numbers.size()/2-1) + numbers.get(numbers.size()/2))/2;
        }
        else{
            return numbers.get((numbers.size() + 1)/2 - 1);
        }
    }

    public List<Integer> M4(IDocumentSession session) {
        List<CountByBooktitleYearAuthorsCount> countByBooktitleYearAuthorsCount = session.query(Inproceedings.class)
                .groupBy("booktitle", "year", "authors.Count")
                .selectKey("booktitle")
                .selectKey("year")
                .selectKey("authors.Count", "authorscount")
                .selectCount()
                .whereRegex("booktitle", "SIGMOD")
                .andAlso()
                .whereGreaterThan("authors.Count", 10)
                .orderByDescending("count", OrderingType.DOUBLE)
                .ofType(CountByBooktitleYearAuthorsCount.class)
                .toList();
        //create new hashmap to keep track of all publication counts per year (as the CountByBooktitleYearAuthorsCount list will have separate entities for authorCounts 11, 12,13, ...)
        HashMap<Integer, Integer> mostPublications = new HashMap<>();
        for (CountByBooktitleYearAuthorsCount c: countByBooktitleYearAuthorsCount){
            Integer previousCount = mostPublications.getOrDefault(c.getYear(), 0);
            mostPublications.put(c.getYear(), previousCount + c.getCount());
        }
        Integer max = Collections.max(mostPublications.values());
        List<Integer> result = mostPublications.entrySet().stream()
                .filter(map -> Objects.equals(map.getValue(), max))
                .map(map -> map.getKey())
                .collect(Collectors.toList());
        System.out.println(max);
        return result;
    }

    public List<CountByEditor> M5(IDocumentSession session) {
        List<CountByEditor> M5 = session.advanced().documentQuery(Proceedings.class)
                .groupBy("editors[]", "booktitle")
                .selectKey("editors[]", "editor")
                .selectCount()
                .whereEquals("booktitle", "PODS")
                .orderByDescending("count")
                .ofType(CountByEditor.class)
                .toList();
        int max = M5.get(0).getCount();
        return M5.stream().filter(c -> c.getCount() == max).collect(Collectors.toList());
    }

    public Integer M6(IDocumentSession session) {
        //20 people is the sweetspot for performance and validity, same answer gets returned when setting this value to 10000
        int amountOfPeople = 20;
        List<CountByAuthor> articleBest = session.advanced().documentQuery(Article.class)
                .groupBy("authors[]")
                .selectKey("authors[]", "author")
                .selectCount()
                .ofType(CountByAuthor.class)
                .orderByDescending("count", OrderingType.DOUBLE)
                .take(amountOfPeople).toList();

        List<CountByAuthor> inproceedingsBest = session.advanced().documentQuery(Inproceedings.class)
                .groupBy("authors[]")
                .selectKey("authors[]", "author")
                .selectCount()
                .ofType(CountByAuthor.class)
                .orderByDescending("count", OrderingType.DOUBLE)
                .take(amountOfPeople).toList();

        HashMap<String, Integer> authorCount = new HashMap<>();
        //populate hashmap with the best authors in Articles
        for (CountByAuthor article: articleBest){
            authorCount.put(article.getAuthor(), article.getCount());
        }

        for (CountByAuthor article: inproceedingsBest){
            int currentArticleCount = authorCount.getOrDefault(article.getAuthor(), 0);
            authorCount.put(article.getAuthor(), article.getCount() + currentArticleCount);
        }

        Integer max = Collections.max(authorCount.values());
        List<String> bestPublished = authorCount.keySet().stream().filter(author -> authorCount.get(author) == max).collect(Collectors.toList());
        System.out.println(bestPublished);
        return session.query(Inproceedings.class)
                .whereIn("authors", bestPublished)
                .selectFields(String.class, "booktitle")
                .distinct()
                .count();

    }

    public HashMap<String, Map<String, Integer>> H1(IDocumentSession session) {
        //First step, get all authors that published to the ICDT in 2020
                List<Inproceedings> authorGroups = session
                        .query(Inproceedings.class)
                        .whereEquals("booktitle", "ICDT")
                        .andAlso()
                        .whereEquals("year", 2020)
                        .toList();
                List<String> authors = new ArrayList<String>();
                for (Inproceedings authorGroup : authorGroups ){
                    authors.addAll(authorGroup.getAuthors());
                }
                System.out.println(authors);
                List<Inproceedings> confArticles = session
                        .query(Inproceedings.class)
                        .containsAny("authors", authors)
                        .toList();
                List<Article> jourArticles = session
                        .query(Article.class)
                        .containsAny("authors", authors)
                        .toList();
                HashMap<String, HashMap<String, Integer>> frequentCoAuthors = new HashMap<>();
                //get co-author count from conference article
                for (Inproceedings authorGroup : confArticles) {
                    List<String> articleAuthors = authorGroup.getAuthors();
                    //authors from the ICDT in 2020 posted in this article
                    List<String> mutualAuthors = articleAuthors.stream().distinct().filter(authors::contains).collect(Collectors.toList());
                    for (String author : mutualAuthors) {
                        HashMap<String, Integer> coAuthorCount = frequentCoAuthors.getOrDefault(author, new HashMap<>());
                        for (String name : articleAuthors) {
                            //if name does not correspond to the author
                            if (!name.equalsIgnoreCase(author)) {
                                Integer currentCount = coAuthorCount.getOrDefault(name, 0);
                                coAuthorCount.put(name, currentCount + 1);
                            }
                        }
                        frequentCoAuthors.put(author, coAuthorCount);
                    }
                }
                for (Article authorGroup : jourArticles) {
                    List<String> articleAuthors = authorGroup.getAuthors();
                    //authors from the ICDT in 2020 posted in this article
                    List<String> mutualAuthors = articleAuthors.stream().distinct().filter(authors::contains).collect(Collectors.toList());
                    for (String author : mutualAuthors) {
                        HashMap<String, Integer> coAuthorCount = frequentCoAuthors.getOrDefault(author, new HashMap<>());
                        for (String name : articleAuthors) {
                            //if name does not correspond to the author
                            if (!name.equalsIgnoreCase(author)) {
                                Integer currentCount = coAuthorCount.getOrDefault(name, 0);
                                coAuthorCount.put(name, currentCount + 1);
                            }
                        }
                        frequentCoAuthors.put(author, coAuthorCount);
                    }
                }
                HashMap<String, Map<String, Integer>> bestCoAuthor = new HashMap<>();
                for (String author: frequentCoAuthors.keySet()){
                    HashMap<String, Integer> count = frequentCoAuthors.get(author);
                    Integer max = Collections.max(count.values());
                    Map<String, Integer> result = count.entrySet().stream()
                            .filter(map -> Objects.equals(map.getValue(), max))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    bestCoAuthor.put(author, result);
                }
                return bestCoAuthor;
    }

    public static Set<String> getCoAuthors(String author, IDocumentSession session) {
        Set<String> coAuthors = new HashSet<>();
        List<Authors> coAuthorsArt = session
                .query(Article.class)
                .containsAny("authors", Collections.singletonList(author))
                .selectFields(Authors.class, "authors")
                .toList();
        List<Authors> coAuthorsInp = session
                .query(Inproceedings.class)
                .containsAny("authors", Collections.singletonList(author))
                .selectFields(Authors.class, "authors")
                .toList();
        for (Authors authorObject: coAuthorsArt) {
            coAuthors.addAll(authorObject.getAuthors());
        }
        for (Authors authorObject: coAuthorsInp) {
            coAuthors.addAll(authorObject.getAuthors());
        }
        coAuthors.remove(author);
        return coAuthors;
    }

    public Integer H2(IDocumentSession session) {
        session.advanced().setMaxNumberOfRequestsPerSession(150);
        class Pair {
            final String first;
            final Integer second;
            public Pair(String first, Integer second) {
                this.first = first;
                this.second = second;
            }
        }
        String erdos = "Paul Erdös";
        Queue<Pair> q = new LinkedList<>();
        q.add(new Pair("Dan Suciu", 1));
        Integer result = 0;
        while (!q.isEmpty()) {
            Pair shortest = q.poll();
            String authorName = shortest.first;
            Integer length = shortest.second;
            Set<String> coAuthors = getCoAuthors(authorName, session);
            if (coAuthors.contains(erdos)) {
                result = length;
                break;
            } else {
                for (String coAuthor: coAuthors) {
                    q.add(new Pair(coAuthor, length + 1));
                }
            }
        }
        return result;
    }

    class Pair {
        final String first;
        final Integer second;
        public Pair(String first, Integer second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "Author='" + first + '\'' +
                    ",Ketsman =" + second +
                    '}';
        }
    }

    //Bonus Query: get all authors that have an Ketsman number lower or equal than 2. (Ketsman number is the same principle as the Erdos number but using Bas Ketsman)
    public List<Pair> B2(IDocumentSession session) {
        session.advanced().setMaxNumberOfRequestsPerSession(300000);
        String ketsman = "Bas Ketsman";
        Queue<Pair> q = new LinkedList<>();
        q.add(new Pair(ketsman, 0));
        List<Pair> LowerOrEq4 = new ArrayList<>();
        while (!q.isEmpty()) {
            Pair pair = q.poll();
            String authorName = pair.first;
            Integer length = pair.second;
            LowerOrEq4.add(pair);
            Set<String> coAuthors = getCoAuthors(authorName, session);
            for (String coAuthor: coAuthors) {
                if (length + 1 < 3){
                    q.add(new Pair(coAuthor, length + 1));
                }
                else {break;}
            }
        }
        return LowerOrEq4;
    }

    public static void main(String[] args){
        String dbUrl = "http://127.0.0.1:8080";
        String dbName = "DBLP";
        try (IDocumentStore store = new DocumentStore(
                new String[]{dbUrl}, dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                Project p = new Project();
//                System.out.println("E1");
//                System.out.println(p.E1(currentSession));
//                System.out.println("E2");
//                System.out.println(p.E2(currentSession));
//                System.out.println("M1");
//                System.out.println(p.M1(currentSession));
//                System.out.println("M2");
//                System.out.println(p.M2(currentSession));
//                System.out.println("M3");
//                System.out.println(p.M3(currentSession));
                System.out.println("M4");
                System.out.println(p.M4(currentSession));
//                System.out.println("M5");
//                System.out.println(p.M5(currentSession));
//                System.out.println("M6");
//                System.out.println(p.M6(currentSession));
//                System.out.println("H1");
//                System.out.println(p.H1(currentSession));
//                System.out.println("H2");
//                System.out.println(p.H2(currentSession));
                //Uncomment to see result, commented because it doesn't make the previous queries visible
//                System.out.println("B2");
//                System.out.println(p.B2(currentSession));
            }
        }
    }
}
