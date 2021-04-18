package main;

import Dao.AbstractDao;
import Dao.DaoForTaskOne;
import Dao.PhysicalThingDao;
import Graphics.ListViewBuilder;
import Graphics.TableBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import java.util.List;

import static Dao.DaoForTaskOne.*;

public class Main extends Application {

    //Actor id for the first task of the homework

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main/sample.fxml"));
        primaryStage.setTitle("Inte HF1");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        String rdf4jServer = "http://localhost:8080/rdf4j-server/";
        String repositoryID = "szepmuveszeti";
        Repository db = new HTTPRepository(rdf4jServer, repositoryID);
        initExtender();
        AbstractDao.initDao(db);
        task1Init(root.getScene());
        addEventHandlers(root.getScene());

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                removeAddedStatements();
                Platform.exit();
                System.exit(0);
            }
        });
    }


    public static void task1Init(Scene scene) {

        for (int i=0; i<3; i++){
            TableBuilder.buildTableForArtistProperties("#table" + (i + 1), scene);
        }

        try {
            getInformationUsedForExtension().keySet().stream().forEach(i -> {
                List<Statement> statements = queryActorPropertiesById(getInformationUsedForExtension().get(i).get(0));
                TableBuilder.fillLabel("#name" + (i + 1), getNameFromActorProperties(statements), scene);
                TableBuilder.refreshTableWithNewElements("#table" + (i+1), statements, scene);
            });
        } catch (Exception e) {
            TableBuilder.fillLabel("#exceptionLabel", e.getMessage() + e.getCause().getMessage(), scene);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void addEventHandlers(Scene scene) {
        Button addButton = (Button) scene.lookup("#addProperties");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    for (int i = 0; i < 3; i++) {
                        addNewStatements();
                        List<Statement> statements = DaoForTaskOne.queryActorPropertiesById(getInformationUsedForExtension().get(i).get(0));
                        TableBuilder.refreshTableWithNewElements("#table" + (i + 1), statements, scene);
                    }
                } catch (Exception ex) {
                    TableBuilder.fillLabel("#exceptionLabel", ex.getMessage() + ex.getCause().getMessage(), scene);
                }

            }
        });

        Button deleteButton = (Button) scene.lookup("#deleteProperties");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    for (int i = 0; i < 3; i++) {
                        DaoForTaskOne.removeAddedStatements();
                        List<Statement> statements = DaoForTaskOne.queryActorPropertiesById(getInformationUsedForExtension().get(i).get(0));
                        TableBuilder.refreshTableWithNewElements("#table" + (i + 1), statements, scene);
                    }
                } catch (Exception ex) {
                    TableBuilder.fillLabel("#exceptionLabel", ex.getMessage() + ex.getCause().getMessage(), scene);
                }
            }
        });

        Button searchButton = (Button) scene.lookup("#searchButton");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    TextField field = (TextField) scene.lookup("#inputName");
                    List<Statement> physicalThingStatements = PhysicalThingDao.getCreationsOfActor(field.getText());
                    ListViewBuilder.buildTableForArtistProperties("#paintingNamesView", physicalThingStatements, scene);
                } catch (Exception ex) {
                    TableBuilder.fillLabel("#exceptionLabel", ex.getMessage() + ex.getCause().getMessage(), scene);
                }
            }
        });


    }
}
