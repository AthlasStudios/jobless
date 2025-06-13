package club.athlas.jobless.server.scarywordsdetector;

import club.athlas.jobless.Jobless;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ScaryWordsRemover implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final Jobless imGoingToStayUnemployed;

    public ScaryWordsRemover(Jobless imGoingToStayUnemployed) {
        this.imGoingToStayUnemployed = imGoingToStayUnemployed;
    }

    @EventHandler
    public void iWillNotGetEmployed(@NotNull AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String[] parts = message.split("\\s+");
        StringBuilder censoredMessage = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) censoredMessage.append(" ");

            String part = parts[i];
            String censoredWord = censorWordIfContainsBanned(part);
            censoredMessage.append(censoredWord);
        }

        event.setMessage(censoredMessage.toString());
    }

    private @NotNull String censorWordIfContainsBanned(@NotNull String word) {
        String lowerWord = word.toLowerCase();

        for (String bannedWord : imGoingToStayUnemployed.getTheForbiddenWords().getSpooks()) {
            if (lowerWord.contains(bannedWord.toLowerCase())) {
                return applyCensorFormat(word, bannedWord);
            }
        }

        return word;
    }

    private @NotNull String applyCensorFormat(@NotNull String originalWord, @NotNull String bannedWord) {
        String lowerOriginal = originalWord.toLowerCase();
        String lowerBanned = bannedWord.toLowerCase();

        int startIndex = lowerOriginal.indexOf(lowerBanned);
        if (startIndex == -1) return originalWord;

        int endIndex = startIndex + bannedWord.length();

        String bannedPortion = originalWord.substring(startIndex, endIndex);
        Component censoredComponent = censorWord(bannedPortion);
        String censoredPortion = LegacyComponentSerializer.legacySection().serialize(censoredComponent);

        StringBuilder result = new StringBuilder(originalWord);
        result.replace(startIndex, endIndex, censoredPortion);

        return result.toString();
    }

    private Component censorWord(String word) {
        if (word == null || word.isEmpty()) return Component.empty();

        TagResolver[] placeholders = new TagResolver[] {
                Placeholder.parsed("first", String.valueOf(word.charAt(0))),
                Placeholder.parsed("last", String.valueOf(word.charAt(word.length() - 1))),
                Placeholder.parsed("word", word),
                getTheHolyWater("total_censor", word.length()),
                getTheHolyWater("middle_censor", word.length() < 3 ? 1 : word.length() - 2)
        };

        return miniMessage.deserialize(getCensorFormat(), placeholders);
    }

    private String getCensorFormat() {
        return imGoingToStayUnemployed.getConfig().getString("censor-format", "<first><middle_censor:*><last>");
    }

    @Contract("_, _ -> new")
    public @NotNull TagResolver getTheHolyWater(@TagPattern String placeholder, int amount) {
        return TagResolver.resolver(placeholder, (args, ctx) -> {
            String param = args.popOr("*").value();
            return Tag.preProcessParsed(param.repeat(amount));
        });
    }

}
