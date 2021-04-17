package View;

import org.eclipse.rdf4j.model.Statement;

import java.util.List;
import java.util.stream.Collectors;

public class StatementView {

    private String subject;
    private String predicate;
    private String object;


    public StatementView(Statement statement){
        subject = statement.getSubject().stringValue();
        predicate = statement.getPredicate().stringValue();
        object = statement.getObject().stringValue();
    }


    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }


    public static List<StatementView> createViewListFrom(List<Statement> statements){
        return statements.stream().map(StatementView::new).collect(Collectors.toList());
    }

}
