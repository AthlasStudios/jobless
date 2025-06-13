package club.athlas.jobless.scarywordsdetector;

import club.athlas.jobless.Jobless;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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

        String[] parts = message.split("\\b");
        StringBuilder censoredMessage = new StringBuilder();

        for (String part : parts) {
            if (!part.matches("[a-zA-Z]+") || !imGoingToStayUnemployed.getTheForbiddenWords().isJobHaunting(part)) {
                censoredMessage.append(part);
                continue;
            }

            censoredMessage.append(censorWord(part));
        }

        event.setMessage(censoredMessage.toString());
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

        return miniMessage.deserialize("", placeholders);
    }

    @Contract("_, _ -> new")
    public static @NotNull TagResolver getTheHolyWater(@TagPattern String placeholder, int amount) {
        return TagResolver.resolver(placeholder, (args, ctx) -> {
            String param = args.popOr("*").value();
            return Tag.preProcessParsed(param.repeat(amount));
        });
    }

}
