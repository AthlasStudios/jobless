package club.athlas.jobless.api.trauma;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface TheForbiddenWords {

    void summonTheJobGhost(@NotNull String soul);

    void summonTheWholeJobFam(Set<String> words);

    void sendBackToTheVoid(@NotNull String soul);

    void exileTheJobFam(Set<String> words);

    boolean isJobHaunting(@NotNull String phrase);

    void flushTheSpookyWords();

    Set<String> getSpooks();

}
