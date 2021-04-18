package Dao;

public class IdentifierContainer {

    private static final String ACTORS_BASE = "http://data.szepmuveszeti.hu/id/collections/museum/E39_Actor/";
    private static final String IS_IDENTIFIED_BY_PREDICATE = "http://erlangen-crm.org/current/P131_is_identified_by";
    private static final String HAS_NOTE= "http://erlangen-crm.org/current/P3_has_note";
    private static final String LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
    private static final String HAD_PARTICIPANT = "http://erlangen-crm.org/current/P11_had_participant";
    private static final String WAS_PRESENT_AT = "http://erlangen-crm.org/current/P12i_was_present_at";

    public static String getActorsBase() {
        return ACTORS_BASE;
    }

    public static String getIsIdentifiedByPredicate() {
        return IS_IDENTIFIED_BY_PREDICATE;
    }

    public static String getHasNote() {
        return HAS_NOTE;
    }

    public static String getLabel() {
        return LABEL;
    }

    public static String getHadParticipant() {
        return HAD_PARTICIPANT;
    }

    public static String getWasPresentAt() {
        return WAS_PRESENT_AT;
    }
}
