package Dao;

import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import java.util.*;

import static java.util.Arrays.asList;

public class DaoForTaskOne {
    private static final String ACTORS_BASE = IdentifierUtils.getIdBase()+ "E39_Actor/";  //0e65972d-ce68-3ad4-952d-063967f0a705
    private static final String IS_IDENTIFIED_BY_PREDICATE = "http://erlangen-crm.org/current/P131_is_identified_by";
    private static final String HAS_NOTE= "http://erlangen-crm.org/current/P3_has_note";
    private static final String LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
    private static final String HAD_PARTICIPANT = "http://erlangen-crm.org/current/P11_had_participant";
    private static final String WAS_PRESENT_AT = "http://erlangen-crm.org/current/P12i_was_present_at";

    private static Repository db;


    private static Map<Integer, List<String>> informationUsedForExtension = new HashMap<>();

    public static Map<Integer, List<String>> getInformationUsedForExtension() {
        return informationUsedForExtension;
    }

    public static void initExtender(Repository db){
        DaoForTaskOne.db = db;

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
        IRI actor = Values.iri(ACTORS_BASE + id);
        List<Statement> actorStatements = new ArrayList<>();
        try (RepositoryConnection conn = db.getConnection()) {

            RepositoryResult<Statement> result = conn.getStatements(actor, null, null);
            Iterations.addAll(result, actorStatements);

        } finally {
            db.shutDown();
        }
        return actorStatements;
    }

    public static String getNameFromActorProperties(List<Statement> properties){
        try (RepositoryConnection conn = db.getConnection()){
            Statement identifiedBy = properties.stream().filter(statement -> statement.getPredicate().equals(Values.iri(IS_IDENTIFIED_BY_PREDICATE))).findFirst().get();
            IRI actorAppellation = Values.iri(identifiedBy.getObject().stringValue());
            String result = conn.getStatements(actorAppellation, Values.iri(HAS_NOTE), null).next().getObject().stringValue();
            return result;

        } finally {
            db.shutDown();
        }

    }

    public static void addStatement(Statement statement) {
        try (RepositoryConnection conn = db.getConnection()) {
            conn.add(statement);
        } finally {
        }

    }

    public static void removeStatement(Statement statement) {
        try (RepositoryConnection conn = db.getConnection()) {
            conn.remove(statement);
        } finally {

        }

    }
    public static List<Statement> addNewStatements(){
        List<Statement> statementsToAdd = getExtensionRelatedStatements();
        statementsToAdd.forEach(DaoForTaskOne::addStatement);
        return statementsToAdd;
    }

    public static void removeAddedStatements(){
        List<Statement> addedStatements = getExtensionRelatedStatements();
        addedStatements.forEach(DaoForTaskOne::removeStatement);
    }

    public static List<Statement> getExtensionRelatedStatements() {
        List<Statement> statements= new ArrayList<>();
        informationUsedForExtension.values().forEach(information -> {
            IRI actor = Values.iri(ACTORS_BASE + information.get(0));
            IRI predicate = Values.iri(LABEL);
            Value nationality = Values.getValueFactory().createLiteral(information.get(1));
            Value movement = Values.getValueFactory().createLiteral(information.get(2));
            Statement nationalityStatement = SimpleValueFactory.getInstance().createStatement(actor, predicate, nationality);
            Statement movementStatement = SimpleValueFactory.getInstance().createStatement(actor, predicate, movement);
            statements.add(nationalityStatement);
            statements.add(movementStatement);
        });
        return statements;
    }

    public static List<Statement> getCreationsOfActor(String actorName){
        actorName = actorName.trim();
        try (RepositoryConnection conn = db.getConnection()){

            RepositoryResult<Statement> result = conn.getStatements(null, Values.iri(LABEL), Values.getValueFactory().createLiteral(actorName));
            List<Statement> resultStatements = new ArrayList<>();
            Iterations.addAll(result, resultStatements);
            if(resultStatements.isEmpty()) {
                return resultStatements;
            }

            List<Statement> creationHadParticipant = new ArrayList<>();
            result = conn.getStatements(null, Values.iri(HAD_PARTICIPANT), resultStatements.get(0).getSubject());;
            Iterations.addAll(result, creationHadParticipant);
            if(creationHadParticipant.isEmpty()){
                return creationHadParticipant;
            }

            List<Statement> physicalThingsPresentAt = new ArrayList<>();
            for(Statement statement: resultStatements) {
                result = conn.getStatements(null, Values.iri(WAS_PRESENT_AT), statement.getSubject());
                Iterations.addAll(result, physicalThingsPresentAt);
            }
            if(physicalThingsPresentAt.isEmpty()){
                return physicalThingsPresentAt;
            }

            List<Statement> paintingNames = new ArrayList<>();
            for(Statement statement: physicalThingsPresentAt) {
                result = conn.getStatements(statement.getSubject(), Values.iri(LABEL), null);
                Iterations.addAll(result, paintingNames);
            }

            return paintingNames;

        } finally {
            db.shutDown();
        }
    }
}
