package club.athlas.jobless.unemployementcfg.constantfear;

public enum UnemploymentSetting {

    GENERIC_DEBUG("generic.debug"),
    EXTERNAL_WORDS_SOURCE("external-words.source"),
    EXTERNAL_WORDS_FETCH_CONTINUOSLY("external-words.fetch-continuously"),
    EXTERNAL_WORDS_FETCH_EVERY_TIME("external-words.fetch-every.time"),
    EXTERNAL_WORDS_FETCH_EVERY_TIME_UNIT("external-words.fetch-every.time-unit"),
    CUSTOM_WORDS("custom-words"),
    CENSOR_FORMAT("censor-format");

    private final String path;

    UnemploymentSetting(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
