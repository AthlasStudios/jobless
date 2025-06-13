package club.athlas.jobless.server.task;

import club.athlas.jobless.Jobless;
import club.athlas.jobless.unemployementcfg.constantfear.UnemploymentSetting;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class WordFetcher extends BukkitRunnable {

    private final Jobless plugin;

    public WordFetcher(Jobless plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        String sourceUrl = plugin.getFearSettingsManager().getString(UnemploymentSetting.WORDS_SOURCE, "");

        if (sourceUrl == null || sourceUrl.isEmpty()) return;

        plugin.getLogger().log(Level.INFO, "Getting remote words from " + sourceUrl);

        plugin.getUnpaidInternDownloader().scrapeJobTrauma(sourceUrl).whenComplete((stuff, throwable) -> {
            if (throwable != null) {
                plugin.getLogger().log(Level.SEVERE, "Unable to get custom words", throwable);
                return;
            }

            plugin.getLogger().log(Level.INFO, "Custom words loaded from " + sourceUrl);
            plugin.getTheForbiddenWords().summonTheWholeJobFam(stuff);
        });

    }

}
