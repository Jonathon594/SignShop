
package org.wargamer2010.signshop.listeners.sslisteners;

import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.wargamer2010.signshop.configuration.SignShopConfig;
import org.wargamer2010.signshop.events.SSCreatedEvent;
import org.wargamer2010.signshop.events.SSPreTransactionEvent;
import org.wargamer2010.signshop.player.SignShopPlayer;
import org.wargamer2010.signshop.util.itemUtil;

public class SimpleBlacklister implements Listener {

    private boolean runBlacklistCheck(ItemStack[] isItems, SignShopPlayer ssPlayer, Map<String, String> messageParts) {
        if(isItems == null)
            return false;
        ItemStack blacklisted = SignShopConfig.isAnyItemOnBlacklist(isItems);
        if(blacklisted != null) {
            if(ssPlayer.isOp()) {
                messageParts.put("!blacklisted_item", itemUtil.formatData(blacklisted.getData(), blacklisted.getDurability()));
                ssPlayer.sendMessage(SignShopConfig.getError("item_on_blacklist_but_op", messageParts));
                return false;
            }

            messageParts.put("!blacklisted_item", itemUtil.formatData(blacklisted.getData(), blacklisted.getDurability()));
            ssPlayer.sendMessage(SignShopConfig.getError("item_on_blacklist", messageParts));
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSSBuildEvent(SSCreatedEvent event) {
        if(event.isCancelled())
            return;
        boolean isBlacklisted = this.runBlacklistCheck(event.getItems(), event.getPlayer(), event.getMessageParts());
        if(isBlacklisted)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSSPreTransactionEvent(SSPreTransactionEvent event) {
        if(event.isCancelled())
            return;
        boolean isBlacklisted = this.runBlacklistCheck(event.getItems(), event.getPlayer(), event.getMessageParts());
        if(isBlacklisted)
            event.setCancelled(true);
    }
}
