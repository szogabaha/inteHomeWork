package Dao;

public class IdentifierUtils {
    private static final String idBase = "http://data.szepmuveszeti.hu/id/collections/museum/";
    private IdentifierUtils(){ }

    public static String getIdBase() {
        return idBase;
    }

}
