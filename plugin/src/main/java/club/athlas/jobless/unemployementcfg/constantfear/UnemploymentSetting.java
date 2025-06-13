package club.athlas.jobless.unemployementcfg.constantfear;

public enum UnemploymentSetting {

    USE_PREDEFINED_WORDS("use-predefined-words"),
    WORDS_SOURCE("words-source"),
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
