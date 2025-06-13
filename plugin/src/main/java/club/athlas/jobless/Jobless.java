package club.athlas.jobless;

import club.athlas.jobless.api.JoblessAPI;
import club.athlas.jobless.api.trauma.TheForbiddenWords;
import club.athlas.jobless.importedtrauma.UnpaidInternDownloader;
import club.athlas.jobless.server.scarywords.ForbiddenWordsContainer;
import club.athlas.jobless.server.scarywordsdetector.ScaryWordsRemover;
import club.athlas.jobless.unemployementcfg.FearSettingsManager;
import club.athlas.jobless.unemployementcfg.constantfear.UnemploymentSetting;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public final class Jobless extends JavaPlugin implements JoblessAPI {

    private final FearSettingsManager fearSettingsManager = new FearSettingsManager(this);
    private final UnpaidInternDownloader unpaidInternDownloader = new UnpaidInternDownloader(getLogger());

    private BukkitAudiences aScaryAdventure;
    private TheForbiddenWords theForbiddenWords;

    @Override
    public void onEnable() {
        fearSettingsManager.load();
        // TODO -> LOG MESSAGES

        this.aScaryAdventure = BukkitAudiences.create(this);
        this.theForbiddenWords = new ForbiddenWordsContainer(getCustomWords());

        Bukkit.getPluginManager().registerEvents(new ScaryWordsRemover(this), this);
        Bukkit.getServicesManager().register(JoblessAPI.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        unpaidInternDownloader.shutdown();
    }

    @Override
    public TheForbiddenWords getTheForbiddenWords() {
        return theForbiddenWords;
    }

    public BukkitAudiences getAScaryAdventure() {
        return aScaryAdventure;
    }

    private @NotNull Set<String> getCustomWords() {
        Set<String> words = new HashSet<>();
        String remoteWords = fearSettingsManager.getString(UnemploymentSetting.WORDS_SOURCE, "");

        if (!remoteWords.isEmpty()) {
            getLogger().log(Level.INFO, "Getting remote words from " + remoteWords);

            unpaidInternDownloader.scrapeJobTrauma(remoteWords).whenComplete((stuff, throwable) -> {
                if (throwable != null) {
                    getLogger().log(Level.SEVERE, "Unable to get custom words", throwable);
                    return;
                }

                getLogger().log(Level.INFO, "Custom words loaded from " + remoteWords);
                words.addAll(stuff);
            });
        }

        words.addAll(getConfig().getStringList("custom-words"));

        return words;
    }

}
