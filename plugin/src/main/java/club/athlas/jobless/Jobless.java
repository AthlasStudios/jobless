package club.athlas.jobless;

import club.athlas.jobless.api.JoblessAPI;
import club.athlas.jobless.api.trauma.TheForbiddenWords;
import club.athlas.jobless.importedtrauma.UnpaidInternDownloader;
import club.athlas.jobless.server.scarywords.ForbiddenWordsContainer;
import club.athlas.jobless.server.scarywordsdetector.ScaryWordsRemover;
import club.athlas.jobless.server.task.WordFetcher;
import club.athlas.jobless.unemployementcfg.FearSettingsManager;
import club.athlas.jobless.unemployementcfg.constantfear.UnemploymentSetting;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

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

        new WordFetcher(this).runTaskTimer(this, 0, 20);

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

    public FearSettingsManager getFearSettingsManager() {
        return fearSettingsManager;
    }

    public BukkitAudiences getAScaryAdventure() {
        return aScaryAdventure;
    }

    public UnpaidInternDownloader getUnpaidInternDownloader() {
        return unpaidInternDownloader;
    }

    private @NotNull Set<String> getCustomWords() {
        return new HashSet<>(getConfig().getStringList(UnemploymentSetting.CUSTOM_WORDS.getPath()));
    }

}
