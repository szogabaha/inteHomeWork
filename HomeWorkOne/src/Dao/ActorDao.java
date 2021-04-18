package Dao;

import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import java.util.*;

import static Dao.IdentifierContainer.*;
import static java.util.Arrays.asList;

public class ActorDao extends AbstractDao {




    private static Map<Integer, List<String>> informationUsedForExtension = new HashMap<>();

    public static Map<Integer, List<String>> getInformationUsedForExtension() {
        return informationUsedForExtension;
    }

    public static void initExtender(){ ;

        String JULES_NATIONALITY = "Francia";
        String JULES_MOVEMENT = "Realizmus";
        String JULES_ID = "0e65972d-ce68-3ad4-952d-063967f0a705";
        informationUsedForExtension.put(0, asList(JULES_ID, JULES_NATIONALITY, JULES_MOVEMENT));

        String FRANCOIS_NATIONALITY = "Francia";
        String FRANCOIS_MOVEMENT = "Reneszánsz";
        String FRANCOIS_ID =  "04c69065-24cd-377e-833f-e26ddaddc62f";
        informationUsedForExtension.put(1, asList(FRANCOIS_ID, FRANCOIS_NATIONALITY, FRANCOIS_MOVEMENT));


        String WILLEM_NATIONALITY = "Holland";
        String WILLEM_MOVEMENT = "Barokk";
        String WILLEM_ID =  "a3ec6dd8-d538-312a-981e-5b24726ce062";
        informationUsedForExtension.put(2, asList(WILLEM_ID, WILLEM_NATIONALITY, WILLEM_MOVEMENT));

    }
    public static List<Statement> queryActorPropertiesById(String id) {
        IRI actor = Values.iri(getActorsBase() + id);
        List<Statement> actorStatements = new ArrayList<>();
        try (RepositoryConnection conn = getDb().getConnection()) {
            RepositoryResult<Statement> result = conn.getStatements(actor, null, null);
            Iterations.addAll(result, actorStatements);

        }catch (Exception e) {
            throw e;
        }
        finally {
            getDb().shutDown();
        }
        return actorStatements;
    }

    public static String getNameFromActorProperties(List<Statement> properties){
        try (RepositoryConnection conn = getDb().getConnection()){
            Statement identifiedBy = properties.stream().filter(statement -> statement.getPredicate().equals(Values.iri(getIsIdentifiedByPredicate()))).findFirst().get();
            IRI actorAppellation = Values.iri(identifiedBy.getObject().stringValue());
            String result = conn.getStatements(actorAppellation, Values.iri(getHasNote()), null).next().getObject().stringValue();
            return result;

        } finally {
            getDb().shutDown();
        }

    }

    public static void addStatement(Statement statement) {
        try (RepositoryConnection conn = getDb().getConnection()) {
            conn.add(statement);
        }

    }
    public static List<Statement> addNewStatements(){
        List<Statement> statementsToAdd = getExtensionRelatedStatements();
        statementsToAdd.forEach(ActorDao::addStatement);
        return statementsToAdd;
    }


    public static void removeStatement(Statement statement) {
        try (RepositoryConnection conn = getDb().getConnection()) {
            conn.remove(statement);
        }

    }
    public static void removeAddedStatements(){
        try {
        List<Statement> addedStatements = getExtensionRelatedStatements();
        addedStatements.forEach(ActorDao::removeStatement);
        } catch (Exception e){
            //Db is not open, do nothing (Exception thrown has already been handled previously)
        }
    }

    public static List<Statement> getExtensionRelatedStatements() {
        List<Statement> statements= new ArrayList<>();
        informationUsedForExtension.values().forEach(information -> {
            IRI actor = Values.iri(getActorsBase() + information.get(0));
            IRI predicate = Values.iri(getLabel());
            Value nationality = Values.getValueFactory().createLiteral(information.get(1));
            Value movement = Values.getValueFactory().createLiteral(information.get(2));
            Statement nationalityStatement = SimpleValueFactory.getInstance().createStatement(actor, predicate, nationality);
            Statement movementStatement = SimpleValueFactory.getInstance().createStatement(actor, predicate, movement);
            statements.add(nationalityStatement);
            statements.add(movementStatement);
        });
        return statements;
    }


}
