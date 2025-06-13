package club.athlas.jobless.server.scarywords;

import club.athlas.jobless.api.trauma.TheForbiddenWords;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ForbiddenWordsContainer implements TheForbiddenWords {

    private final Set<String> nightmareFuel;

    public ForbiddenWordsContainer(Set<String> nightmareFuel) {
        this.nightmareFuel = nightmareFuel;
    }

    public ForbiddenWordsContainer() {
        this(new HashSet<>());
    }

    @Override
    public void summonTheJobGhost(@NotNull String soul) {
        nightmareFuel.add(soul.toLowerCase());
    }

    @Override
    public void summonTheWholeJobFam(String @NotNull ... souls) {
        nightmareFuel.addAll(
                Arrays.stream(souls)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void sendBackToTheVoid(@NotNull String soul) {
        nightmareFuel.remove(soul.toLowerCase());
    }

    @Override
    public void exileTheJobFam(String @NotNull ... souls) {
        Arrays.asList(souls).forEach(this::sendBackToTheVoid);
    }

    @Override
    public boolean isJobHaunting(@NotNull String word) {
        if (word.isEmpty()) return false;

        String normalized = normalizeWord(word.toLowerCase());
        return nightmareFuel.contains(normalized);
    }

    @Override
    public Set<String> getSpooks() {
        return Collections.unmodifiableSet(nightmareFuel);
    }

    private @NotNull String normalizeWord(@NotNull String word) {
        String substring = word.substring(0, word.length() - 3);

        if (word.endsWith("ies") && word.length() > 4) {
            return substring + "y";  // companies -> company
        }

        if (word.endsWith("s") && word.length() > 3) {
            return word.substring(0, word.length() - 1);        // jobs -> job
        }

        if (word.endsWith("ing") && word.length() > 5) {
            return substring;        // working -> work
        }

        if (word.endsWith("ed") && word.length() > 4) {
            return word.substring(0, word.length() - 2);        // hired -> hire
        }

        return word;
    }

}
