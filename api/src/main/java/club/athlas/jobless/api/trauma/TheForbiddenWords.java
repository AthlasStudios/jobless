package club.athlas.jobless.api.trauma;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface TheForbiddenWords {

    Set<String> THE_SCARY_STUFF = Set.of(
            "job",
            "work",
            "resume",
            "application",
            "hiring",
            "interview",
            "salary",
            "boss",
            "promotion",
            "contract",
            "overtime",
            "deadline",
            "meeting",
            "paycheck",
            "benefits",
            "vacation",
            "office",
            "colleague",
            "cubicle",
            "internship",
            "fired",
            "raise",
            "training",
            "career",
            "employment",
            "shift",
            "union",
            "retirement",
            "taxes"
    );

    void summonTheJobGhost(@NotNull String soul);

    void summonTheWholeJobFam(String @NotNull ... souls);

    void sendBackToTheVoid(@NotNull String soul);

    void exileTheJobFam(String @NotNull ... souls);

    boolean isJobHaunting(@NotNull String phrase);

    Set<String> getSpooks();

}
