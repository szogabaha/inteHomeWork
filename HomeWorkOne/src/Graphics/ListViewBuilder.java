package Graphics;

import Graphics.View.PhysicalThingView;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import org.eclipse.rdf4j.model.Statement;

import java.util.List;
import java.util.stream.Collectors;

public class ListViewBuilder {
    public static void buildTableForArtistProperties(String listViewId, List<Statement> statementList, Scene scene) {
        List<PhysicalThingView> physicalThingViews = statementList.stream().map(PhysicalThingView::new).collect(Collectors.toList());
        ListView listView = (ListView) scene.lookup(listViewId);
        listView.getItems().clear();
        listView.getItems().addAll(physicalThingViews.stream().map(PhysicalThingView::getName).distinct().collect(Collectors.toList()));
    }
}
