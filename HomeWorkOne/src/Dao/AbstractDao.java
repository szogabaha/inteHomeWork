package Dao;

import org.eclipse.rdf4j.repository.Repository;

public class AbstractDao {

    private static Repository db;

    public static void initDao(Repository db){
        AbstractDao.db = db;
    }

    public static Repository getDb() {
        return db;
    }

}
