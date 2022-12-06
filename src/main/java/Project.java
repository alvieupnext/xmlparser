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
//                List<Integer> M4Years = session
//                        .query(Inproceedings.class)
//                        .whereRegex("booktitle", "SIGMOD")
//                        .andAlso()
//                        .whereGreaterThan("authors.Count", 10)
//                        .selectFields(Integer.class, "year")
//                        .toList();
//
//                // Source: https://stackoverflow.com/questions/19031213/java-get-most-common-element-in-a-list
//                Integer M4MostCommonYear = M4Years.stream()
//                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//                        .entrySet().stream().max((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
//                        .map(Map.Entry::getKey).orElse(null);
//
//                System.out.println(M4MostCommonYear);

                // M5
//                List<Proceedings> M5PODSProceedings = session
//                        .query(Proceedings.class)
//                        .whereEquals("booktitle", "PODS")
//                        .toList();
//
//                HashMap<String, Integer> EditorCount = new HashMap<String, Integer>();
//                for(Proceedings proceeding: M5PODSProceedings) {
//                    for(String editor: proceeding.getEditors()) {
//                        Integer count = EditorCount.getOrDefault(editor, 0);
//                        EditorCount.put(editor, count + 1);
//                    }
//                }
//
//                // Source: https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map
//                String M5MostCommonEditor = Collections.max(EditorCount.entrySet(), Map.Entry.comparingByValue()).getKey();
//                Integer M5MostCommonCount = EditorCount.get(M5MostCommonEditor);
//
//                System.out.println(M5MostCommonEditor + " : " + M5MostCommonCount);

                // H2

            }
        }
    }
}
