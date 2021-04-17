package main;

import Dao.DaoForTaskOne;
import View.ElementBuilder;
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
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        String rdf4jServer = "http://localhost:8080/rdf4j-server/";
        String repositoryID = "szepmuveszeti";
        Repository db = new HTTPRepository(rdf4jServer, repositoryID);
        initExtender(db);
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


    public static void task1Init( Scene scene){

        getInformationUsedForExtension().keySet().stream().forEach(i -> {
            List<Statement> properties1 = queryActorPropertiesById(getInformationUsedForExtension().get(i).get(0));
            ElementBuilder.buildTableForArtistProperties("#table" + (i + 1), properties1, scene);
            ElementBuilder.fillLabel("#name" + (i+1), getNameFromActorProperties(properties1), scene);
        });

    }
    public static void main(String[] args) {
        launch(args);
    }

    public static void addEventHandlers(Scene scene){
        Button button = (Button)scene.lookup("#addProperties");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                for(int i = 0; i<3; i++) {
                    addNewStatements();
                    List<Statement> statements = DaoForTaskOne.queryActorPropertiesById(getInformationUsedForExtension().get(i).get(0));
                    ElementBuilder.refreshTableWithNewElements("#table" + (i+1), statements, scene);
                }

            }
        });

        Button searchButton = (Button)scene.lookup("#searchButton");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextField field = (TextField)scene.lookup("#inputName");
                DaoForTaskOne.getCreationsOfActor(field.getText());
            }
        });
    }
}
