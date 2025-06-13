package club.athlas.jobless.api;

import club.athlas.jobless.api.exceptions.WoooowUMightGetEmployedDoingThisException;
import club.athlas.jobless.api.trauma.TheForbiddenWords;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;

public interface JoblessAPI {

    static JoblessAPI getAPI() {
        throw new WoooowUMightGetEmployedDoingThisException("Coding might get you employed D:");
    }

    static @Nullable JoblessAPI callTheUnemploymentAgency() {
        if (Bukkit.getPluginManager().getPlugin("InventoryTracker") == null) return null;
        RegisteredServiceProvider<JoblessAPI> serviceProvider = Bukkit.getServicesManager().getRegistration(JoblessAPI.class);
        if (serviceProvider == null) return null;

        return serviceProvider.getProvider();
    }

    TheForbiddenWords getTheForbiddenWords();

}
