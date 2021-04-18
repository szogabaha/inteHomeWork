package Graphics.View;

import org.eclipse.rdf4j.model.Statement;

public class PhysicalThingView {

    private final String name;

    public PhysicalThingView(Statement statement) {
        name = statement.getObject().stringValue();
    }

    public String getName() {
        return name;
    }
}
