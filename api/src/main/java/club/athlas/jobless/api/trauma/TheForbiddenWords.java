package club.athlas.jobless.api.trauma;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface TheForbiddenWords {

    void summonTheJobGhost(@NotNull String soul);

    void summonTheWholeJobFam(String @NotNull ... souls);

    void sendBackToTheVoid(@NotNull String soul);

    void exileTheJobFam(String @NotNull ... souls);

    boolean isJobHaunting(@NotNull String phrase);

    Set<String> getSpooks();

}
