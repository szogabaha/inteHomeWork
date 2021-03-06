package Graphics;

import Graphics.View.StatementView;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.eclipse.rdf4j.model.Statement;

import java.util.List;

public class TableBuilder {

    private TableBuilder() { }
    public static void buildTableForArtistProperties(String tableId, Scene scene) {
        TableView tb = (TableView) scene.lookup(tableId);

        TableColumn subjectColumn = new TableColumn("Subject");
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

        TableColumn predColumn = new TableColumn("Predicate");
        predColumn.setCellValueFactory(new PropertyValueFactory<>("predicate"));

        TableColumn objectColumn = new TableColumn("Object");
        objectColumn.setCellValueFactory(new PropertyValueFactory<>("object"));


        tb.getColumns().addAll(subjectColumn, predColumn, objectColumn);

    }

    public static void refreshTableWithNewElements(String tableId, List<Statement> newElements, Scene scene){
        TableView<StatementView> tb = (TableView) scene.lookup(tableId);
        List<StatementView> views= StatementView.createViewListFrom(newElements);

        tb.getItems().clear();
        views.forEach(view -> {
                tb.getItems().add(view);
        });

    };

    public static void fillLabel(String labelId, String text, Scene scene){
        Label label = (Label) scene.lookup(labelId);
        label.setText(text);
    }

}
