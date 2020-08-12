/*
 *     File: BackPackCraftListener.java
 *     Last Modified: 8/11/20, 2:19 PM
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

package io.github.coachluck.backpacksplus.listeners;

import io.github.coachluck.backpacksplus.Main;
import io.github.coachluck.backpacksplus.utils.BackPack;
import io.github.coachluck.backpacksplus.utils.ChatUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class BackPackCraftListener implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        final Player player = (Player) e.getWhoClicked();
        final ItemStack craftedItem = e.getCurrentItem();

        // TODO : Check if you can craft more than 1 backpack at once
        if(craftedItem.getAmount() > 1) {
            return;
        }
        for(BackPack backPack : plugin.backPacks) {
            ItemMeta loadedBackPackItem = backPack.getBackPackItem().getItemMeta();
            ItemMeta craftedItemMeta = craftedItem.getItemMeta();

            if(loadedBackPackItem.getDisplayName().equals(craftedItemMeta.getDisplayName())
                    && loadedBackPackItem.getLore().equals(craftedItemMeta.getLore())) {

                if(!player.hasPermission(backPack.getPermission())) {
                    e.setCurrentItem(null);
                    e.setResult(Event.Result.DENY);
                    e.setCancelled(true);
                    ChatUtil.msg(player, plugin.getMessages().getString("General.CraftPerm"));
                }
                else {
                    // TODO : Check if stackable after craft
                    ItemStack backPackItem = backPack.getBackPackItem();
                    PersistentDataContainer dataContainer = backPackItem.getItemMeta().getPersistentDataContainer();
                    String uuidString = UUID.randomUUID().toString();
                    dataContainer.set(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING, uuidString);
                    e.setCurrentItem(backPackItem);
                    e.setResult(Event.Result.ALLOW);
                    plugin.getMessages().getStringList("BackPack.OnCraft").forEach(s ->
                            ChatUtil.msg(player, s.replaceAll("%backpack%", backPack.getDisplayName())));
                }
                return;
            }
        }
    }
}
