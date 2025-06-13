package club.athlas.jobless.server.scarywords;

import club.athlas.jobless.api.trauma.TheForbiddenWords;
import org.jetbrains.annotations.NotNull;

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
    public void summonTheWholeJobFam(@NotNull Set<String> words) {
        nightmareFuel.addAll(
                words.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void sendBackToTheVoid(@NotNull String soul) {
        nightmareFuel.remove(soul.toLowerCase());
    }

    @Override
    public void exileTheJobFam(@NotNull Set<String> words) {
        nightmareFuel.removeAll(
                words.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet())
        );
    }

    @Override
    public boolean isJobHaunting(@NotNull String word) {
        if (word.isEmpty()) return false;
        return nightmareFuel.contains(word.toLowerCase());
    }

    @Override
    public Set<String> getSpooks() {
        return Collections.unmodifiableSet(nightmareFuel);
    }

}
