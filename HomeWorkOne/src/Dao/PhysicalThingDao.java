package Dao;

import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import java.util.ArrayList;
import java.util.List;

import static Dao.IdentifierContainer.*;
import static Dao.IdentifierContainer.getLabel;

public class PhysicalThingDao extends AbstractDao {


    public static List<Statement> getCreationsOfActor(String actorName){
        actorName = actorName.trim();
        try (RepositoryConnection conn = getDb().getConnection()){

            RepositoryResult<Statement> result = conn.getStatements(null, Values.iri(getLabel()), Values.getValueFactory().createLiteral(actorName));
            List<Statement> resultStatements = new ArrayList<>();
            Iterations.addAll(result, resultStatements);
            if(resultStatements.isEmpty()) {
                return resultStatements;
            }

            List<Statement> creationHadParticipant = new ArrayList<>();
            result = conn.getStatements(null, Values.iri(getHadParticipant()), resultStatements.get(0).getSubject());;
            Iterations.addAll(result, creationHadParticipant);
            if(creationHadParticipant.isEmpty()){
                return creationHadParticipant;
            }

            List<Statement> physicalThingsPresentAt = new ArrayList<>();
            for(Statement statement: creationHadParticipant) {
                result = conn.getStatements(null, Values.iri(getWasPresentAt()), statement.getSubject());
                Iterations.addAll(result, physicalThingsPresentAt);
            }
            if(physicalThingsPresentAt.isEmpty()){
                return physicalThingsPresentAt;
            }

            List<Statement> paintingNames = new ArrayList<>();
            for(Statement statement: physicalThingsPresentAt) {
                result = conn.getStatements(statement.getSubject(), Values.iri(getLabel()), null);
                Iterations.addAll(result, paintingNames);
            }

            return paintingNames;

        } finally {
            getDb().shutDown();
        }
    }
}
