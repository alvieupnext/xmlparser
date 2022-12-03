import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.indexes.IndexDefinition;
import net.ravendb.client.documents.operations.indexes.PutIndexesOperation;
import net.ravendb.client.documents.queries.Query;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.session.OrderingType;

import static net.ravendb.client.documents.queries.GroupBy.array;
import static net.ravendb.client.documents.queries.GroupBy.field;

import java.util.*;
import java.util.stream.Collectors;

public class Project {
    public static void main(String[] args) {
        // Setting up document store
        try (IDocumentStore store = new DocumentStore(
                new String[]{ "http://127.0.0.1:8080" }, "DBLP")
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();

            try (IDocumentSession session = store.openSession()) {
                // E1
//                List<String> E1 = session
//                        .query(Proceedings.class)
//                        .whereEquals("booktitle", "PODS")
//                        .skip(0).take(1)
//                        .selectFields(String.class, "publisher")
//                        .toList();
//                System.out.println(E1.get(0));

                // E2
//                List<String> E2 = session
//                        .query(Article.class)
//                        .containsAny("authors", Collections.singletonList("Martin Grohe"))
//                        .andAlso()
//                        .whereEquals("journal", "Theory Comput. Syst.")
//                        .skip(0)
//                        .selectFields(String.class, "title")
//                        .orderBy("title")
//                        .toList();
//                System.out.println(E2);
                //M1
//                int M1 = session
//                        .query(Inproceedings.class)
//                        .whereEquals("booktitle", "SIGMOD Conference")
//                        .andAlso()
//                        .whereEquals("year", 2022)
//                        .count();
//                System.out.println(M1);
                //M2
//
//                 String oldest_journal = session
//                        .query(Article.class)
//                        .orderBy("Year")
//                         .orderBy("Month")
//                         .orderBy("Day")
//                         .selectFields(String.class, "journal")
//                         //skip the three error Articles
//                         .skip(3)
//                         .take(1)
//                         .firstOrDefault();
//                 int M2 = session
//                         .query(Article.class)
//                         .whereEquals("journal", oldest_journal)
//                         .count();
//                 System.out.println(M2);
                 // M3
//                 List<CountByConference> publications = session
//                         .query(Inproceedings.class)
//                         .groupBy("year", "booktitle")
//                         .selectCount()
//                         .whereEquals("booktitle","CIDR")
//                         .ofType(CountByConference.class)
//                         .toList();
//                List<Integer> numbers = new ArrayList<Integer>();
//                for (CountByConference publication: publications){
//                    numbers.add(publication.Count);
//                }
//                Collections.sort(numbers);
//                int result = 0;
//                //median calculation
//                if (numbers.size() % 2 == 0){
//                    result = (numbers.get(numbers.size()/2-1) + numbers.get(numbers.size()/2))/2;
//                }
//                else{
//                    result = numbers.get((numbers.size() + 1)/2 - 1);
//                }
                //M5
                int most_frequent_count = session.advanced().documentQuery(Proceedings.class)
                        .groupBy("editors[]", "booktitle")
                        .selectKey("editors[]", "editor")
                        .selectKey("booktitle")
                        .selectCount()
                        .whereEquals("booktitle", "PODS")
                        .orderByDescending("count")
                        .ofType(CountByAuthorAndConference.class)
                        .firstOrDefault()
                        .getCount()
                        ;
                List<CountByAuthorAndConference> M5 = session.advanced().documentQuery(Proceedings.class)
                        .groupBy("editors[]", "booktitle")
                        .selectKey("editors[]", "editor")
                        .selectKey("booktitle")
                        .selectCount()
                        .whereEquals("booktitle", "PODS")
                        .andAlso()
                        .whereEquals("count", most_frequent_count)
                        .ofType(CountByAuthorAndConference.class)
                        .toList();
                System.out.println(M5);

                //M6
                //20 people is the sweetspot for performance and validity, same answer gets returned when setting this value to 10000
//                int amountOfPeople = 20;
//                List<CountByAuthor> articleBest = session.advanced().documentQuery(Article.class)
//                        .groupBy("authors[]")
//                        .selectKey("authors[]", "author")
//                        .selectCount()
//                        .ofType(CountByAuthor.class)
//                        .orderByDescending("count", OrderingType.DOUBLE)
////                        .whereEquals("author", "Philip S. Yu")
//                        .take(amountOfPeople).toList();
//                System.out.println(articleBest);
//                List<CountByAuthor> inproceedingsBest = session.advanced().documentQuery(Inproceedings.class)
//                        .groupBy("authors[]")
//                        .selectKey("authors[]", "author")
//                        .selectCount()
//                        .ofType(CountByAuthor.class)
//                        .orderByDescending("count", OrderingType.DOUBLE)
////                        .whereEquals("author", "Philip S. Yu")
//                        .take(amountOfPeople).toList();
//                System.out.println(inproceedingsBest);
//                HashMap<String, Integer> authorCount = new HashMap<>();
//                //populate hashmap with the best authors in Articles
//                for (CountByAuthor article: articleBest){
//                    authorCount.put(article.getAuthor(), article.getCount());
//                }
//                for (CountByAuthor article: inproceedingsBest){
//                    int currentArticleCount = authorCount.getOrDefault(article.getAuthor(), 0);
//                    authorCount.put(article.getAuthor(), article.getCount() + currentArticleCount);
//                }
//                System.out.println(authorCount);
//                Integer max = Collections.max(authorCount.values());
//                System.out.println(max);
//                List<String> bestPublished = authorCount.keySet().stream().filter(author -> authorCount.get(author) == max).collect(Collectors.toList());
//                System.out.println(bestPublished);
//                List<String> conferences = session.query(Inproceedings.class)
//                        .whereIn("authors", Collections.singletonList(bestPublished))
//                        .selectFields(String.class, "booktitle")
//                        .distinct()
//                        .toList();
//                System.out.println(conferences);

                //H1
                //First step, get all authors that published to the ICDT in 2020
//                List<Inproceedings> authorGroups = session
//                        .query(Inproceedings.class)
//                        .whereEquals("booktitle", "ICDT")
//                        .andAlso()
//                        .whereEquals("year", 2020)
//                        .toList();
//                List<String> authors = new ArrayList<String>();
//                for (Inproceedings authorGroup : authorGroups ){
//                    for (String name: authorGroup.getAuthors()){
//                        authors.add(name);
//                    }
//                }
//                System.out.println(authors);
//                List<Inproceedings> confArticles = session
//                        .query(Inproceedings.class)
//                        .containsAny("authors", authors)
//                        .toList();
//                List<Article> jourArticles = session
//                        .query(Article.class)
//                        .containsAny("authors", authors)
//                        .toList();
//                HashMap<String, HashMap<String, Integer>> frequentCoAuthors = new HashMap<>();
//                //get co-author count from conference article
//                for (Inproceedings authorGroup : confArticles) {
//                    List<String> articleAuthors = authorGroup.getAuthors();
//                    //authors from the ICDT in 2020 posted in this article
//                    List<String> mutualAuthors = articleAuthors.stream().distinct().filter(authors::contains).collect(Collectors.toList());
//                    for (String author : mutualAuthors) {
//                        HashMap<String, Integer> coAuthorCount = frequentCoAuthors.getOrDefault(author, new HashMap<>());
//                        for (String name : articleAuthors) {
//                            //if name does not correspond to the author
//                            if (!name.equalsIgnoreCase(author)) {
//                                Integer currentCount = coAuthorCount.getOrDefault(name, 0);
//                                coAuthorCount.put(name, currentCount + 1);
//                            }
//                        }
//                        frequentCoAuthors.put(author, coAuthorCount);
//                    }
//                }
//                for (Article authorGroup : jourArticles) {
//                    List<String> articleAuthors = authorGroup.getAuthors();
//                    //authors from the ICDT in 2020 posted in this article
//                    List<String> mutualAuthors = articleAuthors.stream().distinct().filter(authors::contains).collect(Collectors.toList());
//                    for (String author : mutualAuthors) {
//                        HashMap<String, Integer> coAuthorCount = frequentCoAuthors.getOrDefault(author, new HashMap<>());
//                        for (String name : articleAuthors) {
//                            //if name does not correspond to the author
//                            if (!name.equalsIgnoreCase(author)) {
//                                Integer currentCount = coAuthorCount.getOrDefault(name, 0);
//                                coAuthorCount.put(name, currentCount + 1);
//                            }
//                        }
//                        frequentCoAuthors.put(author, coAuthorCount);
//                    }
//                }
//                HashMap<String, Map<String, Integer>> bestCoAuthor = new HashMap<>();
//                for (String author: frequentCoAuthors.keySet()){
//                    HashMap<String, Integer> count = frequentCoAuthors.get(author);
//                    Integer max = Collections.max(count.values());
//                    Map<String, Integer> result = count.entrySet().stream()
//                            .filter(map -> map.getValue() == max)
//                            .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
//                    bestCoAuthor.put(author, result);
//                }
//                System.out.println(bestCoAuthor);
            }

        }
    }
}
