/*
 *     File: Backend.java
 *     Last Modified: 8/14/20, 1:24 PM
 *     Project: BackPacksPlus
 *     Copyright (C) 2020 CoachL_ck
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.coachluck.backpacksplus.utils;

import io.github.coachluck.backpacksplus.Main;
import io.github.coachluck.backpacksplus.commands.MainCommand;
import io.github.coachluck.backpacksplus.listeners.BackPackCloseListener;
import io.github.coachluck.backpacksplus.listeners.BackPackCraftListener;
import io.github.coachluck.backpacksplus.listeners.BackPackUseListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class Backend {
    private final Main plugin;

    public Backend(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Ensures that the configuration is updated to the newest version
     * @param version the config version
     */
    public static void checkConfigVersion(int version) {
        final int CONFIGURATION_VERSION = 0;
        if(version >= CONFIGURATION_VERSION)
            return;

        // Add new defaults
    }
    /**
     * Checks for plugin updates
     */
    public void checkForUpdates() {
        if(!plugin.getConfig().getBoolean("Check-For-Update"))
            return;

        new UpdateChecker(plugin, 82612).getVersion(version -> {
            int old = Integer.parseInt(plugin.getDescription().getVersion().replaceAll("\\.", ""));
            int newVer = Integer.parseInt(version.replaceAll("\\.", ""));
            if (old >= newVer) {
                ChatUtil.logMsg("&bYou are running the latest version.");
                return;
            }
            plugin.updateMsg = true;
            ChatUtil.logMsg("&aThere is a new update available. &ehttps://www.spigotmc.org/resources/backpacksplus.82612/");
        });
    }

    public void registerListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new BackPackCraftListener(), plugin);
        pm.registerEvents(new BackPackUseListener(), plugin);
        pm.registerEvents(new BackPackCloseListener(), plugin);
        plugin.getCommand("bpp").setExecutor(new MainCommand(plugin));
        plugin.getCommand("bpp").setPermissionMessage(ChatUtil.format(plugin.getMessages().getString("General.Permission")));
    }

    public Integer sendBackPackItems(Player targetToReceive, ItemStack itemToGive, int amount) {
        int amt = amount;
        if(amt < 1) amt = 1;
        if(amt > 64) amt = 64;

        itemToGive.setAmount(amt);
        targetToReceive.getInventory().addItem(itemToGive);

        return amt;
    }
}
