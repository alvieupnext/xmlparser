import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.session.OrderingType;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Project {
    public static void main(String[] args) {
        // Setting up document store
        try (IDocumentStore store = new DocumentStore(
                new String[]{ "http://127.0.0.1:8080" }, "Project")
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();

            try (IDocumentSession session = store.openSession()) {
                // E1
//                List<String> E1 = session
//                        .query(Proceedings.class)
//                        .whereEquals("Booktitle", "PODS")
//                        .skip(0).take(1)
//                        .selectFields(String.class, "Publisher")
//                        .toList();
//                System.out.println(E1);

                // E2
//                List<String> E2 = session
//                        .query(Article.class)
//                        .containsAny("Authors", Collections.singletonList("Martin Grohe"))
//                        .andAlso()
//                        .whereEquals("Journal", "Theory Comput. Syst.")
//                        .skip(0)
//                        .selectFields(String.class, "Title")
//                        .orderByDescending("Title")
//                        .toList();
//                Collections.reverse(E2);
//                System.out.println(E2);

                // M4
                List<String> M4 = session
                        .query(Inproceedings.class)
                        .whereRegex("Booktitle", "SIGMOD")
                        .andAlso()
                        .whereGreaterThan("Authors.Count", 10)
                        .selectFields(String.class, "Title")
                        .toList();
                System.out.println(M4);

                // M5
//                Integer M5 = session
//                        .query(Proceedings.class)
//                        .whereEquals("Booktitle", "PODS")
//                        .selectFields(String.class, "Title")
//                        .count();
//                System.out.println(M5);
            }
        }
    }
}
