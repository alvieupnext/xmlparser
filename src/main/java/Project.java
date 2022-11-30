import com.google.common.collect.Lists;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.queries.QueryData;
import net.ravendb.client.documents.session.IDocumentSession;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
//                        .query(Proceedings.class)
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
//                         .query(inproceedings.class)
//                         .groupBy("Year", "booktitle")
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
//                System.out.println(result);
                //H1
                //First step, get all authors that published to the ICDT in 2020
                List<Inproceedings> authors = session
                        .query(Inproceedings.class)
                        .whereEquals("booktitle", "ICDT")
                        .andAlso()
                        .whereEquals("year", 2020)
                        .ofType(Inproceedings.class)
                        .take(10)
                        .toList();
                System.out.println(authors);
            }

        }
    }
}
