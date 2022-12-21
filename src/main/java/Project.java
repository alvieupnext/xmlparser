import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.session.OrderingType;

import java.util.*;
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
                .whereEquals("booktitle", "CIDR")
                .selectFields(Integer.class, "Count")
                .toList();
        Collections.sort(numbers);
        //median calculation
        if (numbers.size() % 2 == 0) {
            return (numbers.get(numbers.size() / 2 - 1) + numbers.get(numbers.size() / 2)) / 2;
        } else {
            return numbers.get((numbers.size() + 1) / 2 - 1);
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
        for (CountByBooktitleYearAuthorsCount c : countByBooktitleYearAuthorsCount) {
            Integer previousCount = mostPublications.getOrDefault(c.getYear(), 0);
            mostPublications.put(c.getYear(), previousCount + c.getCount());
        }
        Integer max = Collections.max(mostPublications.values());
        List<Integer> result = mostPublications.entrySet().stream()
                .filter(map -> Objects.equals(map.getValue(), max))
                .map(Map.Entry::getKey)
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
        for (CountByAuthor article : articleBest) {
            authorCount.put(article.getAuthor(), article.getCount());
        }

        for (CountByAuthor article : inproceedingsBest) {
            int currentArticleCount = authorCount.getOrDefault(article.getAuthor(), 0);
            authorCount.put(article.getAuthor(), article.getCount() + currentArticleCount);
        }

        Integer max = Collections.max(authorCount.values());
//        System.out.println(max);
        List<String> bestPublished = authorCount.keySet().stream().filter(author -> authorCount.get(author).equals(max)).collect(Collectors.toList());
        System.out.println(bestPublished);
        return session.query(Inproceedings.class)
                .whereIn("authors", Collections.singleton(bestPublished))
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
        //These are all the authors
        List<String> authors = new ArrayList<>();
        //flatten into singular lists
        for (Inproceedings authorGroup : authorGroups) {
            authors.addAll(authorGroup.getAuthors());
        }
        //Get Inproceedings to which one of the authors has published
        List<Inproceedings> confArticles = session
                .query(Inproceedings.class)
                .containsAny("authors", Collections.singleton(authors))
                .toList();
        //Get Articles to which one of the authors has published
        List<Article> jourArticles = session
                .query(Article.class)
                .containsAny("authors", Collections.singleton(authors))
                .toList();
        //Create a nested hashmap
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
                        //get current count and increment it
                        Integer currentCount = coAuthorCount.getOrDefault(name, 0);
                        coAuthorCount.put(name, currentCount + 1);
                    }
                }
                //after you have updated the coAuthor hashmap, update the author hashmap
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
                        //get current count and increment it
                        Integer currentCount = coAuthorCount.getOrDefault(name, 0);
                        coAuthorCount.put(name, currentCount + 1);
                    }
                }
                //after you have updated the coAuthor hashmap, update the author hashmap
                frequentCoAuthors.put(author, coAuthorCount);
            }
        }
        //Create a new hashmap that tracks the best coAuthor per author.
        HashMap<String, Map<String, Integer>> bestCoAuthor = new HashMap<>();
        for (String author : frequentCoAuthors.keySet()) {
            HashMap<String, Integer> count = frequentCoAuthors.get(author);
            Integer max = Collections.max(count.values());
            Map<String, Integer> result = count.entrySet().stream()
                    .filter(map -> Objects.equals(map.getValue(), max))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            bestCoAuthor.put(author, result);
        }
        return bestCoAuthor;
    }

    /**
     * Gets a set of authors that have collaborated with a given author
     *
     * @param author  The author.
     * @param session The database session.
     * @return A set of co-authors.
     */
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
        for (Authors authorObject : coAuthorsArt) {
            coAuthors.addAll(authorObject.getAuthors());
        }
        for (Authors authorObject : coAuthorsInp) {
            coAuthors.addAll(authorObject.getAuthors());
        }
        coAuthors.remove(author);
        return coAuthors;
    }

    public Integer H2(IDocumentSession session) {
        //Increase max number of requests
        session.advanced().setMaxNumberOfRequestsPerSession(200);
        String erdos = "Paul Erdös";
        //Create new queue
        Queue<Pair> q = new LinkedList<>();
        //Add Dan Suciu to the queue
        q.add(new Pair("Dan Suciu", 1));
        Integer result = 0;
        //While the queue is not empty
        while (!q.isEmpty()) {
            //get element from queue
            Pair shortest = q.poll();
            String authorName = shortest.first;
            Integer length = shortest.second;
            //get coAuthors for the author
            Set<String> coAuthors = getCoAuthors(authorName, session);
            //if any of the coAuthors are erdos
            if (coAuthors.contains(erdos)) {
                //store length in result, break out the while loop
                result = length;
                break;
            } else {
                //queue every single coAuthor, with an incremented length
                for (String coAuthor : coAuthors) {
                    q.add(new Pair(coAuthor, length + 1));
                }
            }
        }
        return result;
    }

    //Pair class that returns an author and it's corresponding Ketsman number
    static class Pair {
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
                    ",Number =" + second +
                    '}';
        }
    }

    //Bonus Query: get the distance between an author and themself via collaborations. A low distance can indicate the existance of author communities (where the same people collaborate often)
    public Integer B1(String author, IDocumentSession session) {
        //Increase max number of requests
        session.advanced().setMaxNumberOfRequestsPerSession(300000);
        //Create a queue
        Queue<Pair> q = new LinkedList<>();
        //Get all co-authors of the author
        Set<String> coAuthors = getCoAuthors(author, session);
        //Get the coAuthors of the coAuthors
        Set<String> coCoAuthorsAll = new HashSet<>();
        for (String coAuthor : coAuthors) {
            Set<String> coCoAuthors = getCoAuthors(coAuthor, session);
            coCoAuthorsAll.addAll(coCoAuthors);
        }
        coCoAuthorsAll.remove(author);
        Integer result = 0;
        //Enqueue all coCoAuthors with distance 2
        for (String coCoAuthor : coCoAuthorsAll) {
            q.add(new Pair(coCoAuthor, 2));
        }
        while (!q.isEmpty()) {
            Pair pair = q.poll();
            String authorName = pair.first;
            Integer distance = pair.second;
            // If the authors name matches
            if (authorName.equalsIgnoreCase(author)) {
                result = distance;
                break;
            } else {
                //get the co-authors
                Set<String> coAuth = getCoAuthors(authorName, session);
                for (String coAuthor : coAuth) {
                    q.add(new Pair(coAuthor, distance + 1));
                }
            }
        }
        return result;
    }

    //Bonus Query: get all authors that have an Ketsman number lower or equal than 2. (Ketsman number is the same principle as the Erdos number but using Bas Ketsman)
    public List<Pair> B2(IDocumentSession session) {
        //Increase max number of requests
        session.advanced().setMaxNumberOfRequestsPerSession(300000);
        String ketsman = "Bas Ketsman";
        //Create a queue
        Queue<Pair> q = new LinkedList<>();
        //Enqueue ketsman
        q.add(new Pair(ketsman, 0));
        //Keep a list of all authors with a ketsman number lower or equal than 2.
        List<Pair> LowerOrEq2 = new ArrayList<>();
        while (!q.isEmpty()) {
            //dequeue element
            Pair pair = q.poll();
            String authorName = pair.first;
            Integer length = pair.second;
            //Add the pair to the list
            LowerOrEq2.add(pair);
            //get all co-authors
            Set<String> coAuthors = getCoAuthors(authorName, session);
            for (String coAuthor : coAuthors) {
                //break if the length surpasses 3.
                if (length + 1 < 3) {
                    q.add(new Pair(coAuthor, length + 1));
                } else {
                    break;
                }
            }
        }
        return LowerOrEq2;
    }

    public static void main(String[] args) {
        String dbUrl = "http://127.0.0.1:8080";
        //Make sure dbName matches the name that you have given the database
        String dbName = "DBLP";
        try (IDocumentStore store = new DocumentStore(
                new String[]{dbUrl}, dbName)
        ) {
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                Project p = new Project();
                System.out.println("E1");
                System.out.println(p.E1(currentSession));
                System.out.println("E2");
                System.out.println(p.E2(currentSession));
                System.out.println("M1");
                System.out.println(p.M1(currentSession));
                System.out.println("M2");
                System.out.println(p.M2(currentSession));
                System.out.println("M3");
                System.out.println(p.M3(currentSession));
                System.out.println("M4");
                System.out.println(p.M4(currentSession));
                System.out.println("M5");
                System.out.println(p.M5(currentSession));
                System.out.println("M6");
                System.out.println(p.M6(currentSession));
                System.out.println("H1");
                System.out.println(p.H1(currentSession));
                System.out.println("H2");
                System.out.println(p.H2(currentSession));
                //Uncomment to see result, commented because it doesn't make the previous queries visible
//                System.out.println("B1");
//                System.out.println(p.B1("P. Paanah", currentSession));
                //Uncomment to see result, commented because it doesn't make the previous queries visible
//                System.out.println("B2");
//                System.out.println(p.B2(currentSession));
            }
        }
    }
}