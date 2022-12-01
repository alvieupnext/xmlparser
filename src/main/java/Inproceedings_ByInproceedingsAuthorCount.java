import net.ravendb.client.documents.indexes.AbstractIndexCreationTask;

public class Inproceedings_ByInproceedingsAuthorCount extends AbstractIndexCreationTask {
    public Inproceedings_ByInproceedingsAuthorCount() {
        map = "docs.Inproceedings.Select(inproceeding => new {" +
                "Booktitle = inproceeding.Booktitle" +
                "Authors_Count = inproceeding.Authors.Count" +
                ")}";
    }
}
