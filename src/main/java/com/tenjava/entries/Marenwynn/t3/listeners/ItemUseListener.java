package com.tenjava.entries.Marenwynn.t3.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.tenjava.entries.Marenwynn.t3.data.Data;
import com.tenjava.entries.Marenwynn.t3.data.Msg;
import com.tenjava.entries.Marenwynn.t3.data.PlayerData;

public class ItemUseListener implements Listener {

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent useEvent) {
        if (!(useEvent.getRightClicked() instanceof Player))
            return;

        Player p = useEvent.getPlayer();
        Player t = (Player) useEvent.getRightClicked();

        ItemStack is = p.getItemInHand();

        // Player can use items on other players to heal them while sneaking
        if (!p.isSneaking() || is == null)
            return;

        if (is.isSimilar(Data.customItems.get("splint"))) {
            mendBones(p, t);
            useEvent.setCancelled(true);
        } else if (is.isSimilar(Data.customItems.get("gauze"))) {
            bandage(p, t);
            useEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent useEvent) {
        Player p = useEvent.getPlayer();
        Action a = useEvent.getAction();

        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack is = useEvent.getItem();

        if (is == null)
            return;

        if (is.isSimilar(Data.customItems.get("splint"))) {
            mendBones(p, p);
            useEvent.setCancelled(true);
        } else if (is.isSimilar(Data.customItems.get("gauze"))) {
            bandage(p, p);
            useEvent.setCancelled(true);
        }
    }

    public void mendBones(Player p, Player t) {
        PlayerData td = Data.getPlayerData(t.getUniqueId());

        if (td.hasBrokenLegs()) {
            td.setWalkSpeed(td.getWalkSpeed() + 0.1F);
            td.setBrokenLegs(false);

            useItemInHand(p);
            t.setWalkSpeed(td.getWalkSpeed());

            if (p == t) {
                Msg.MEND_LEGS.sendTo(p);
            } else {
                Msg.MEND_LEGS_OTHER.sendTo(p, t.getName());
                Msg.MEND_LEGS_OTHER_NOTICE.sendTo(t, p.getName());
            }
        } else if (td.hasBrokenArm()) {
            td.setBrokenArm(false);
            useItemInHand(p);

            if (p == t) {
                Msg.MEND_ARM.sendTo(p);
            } else {
                Msg.MEND_ARM_OTHER.sendTo(p, t.getName());
                Msg.MEND_ARM_OTHER_NOTICE.sendTo(t, p.getName());
            }
        }

        Data.savePlayer(t.getUniqueId());
    }

    public void bandage(Player p, Player t) {
        PlayerData td = Data.getPlayerData(t.getUniqueId());
        UUID targetUUID = t.getUniqueId();

        if (td.isBleeding()) {
            Data.bleedTasks.get(targetUUID).cancel();
            td.setWalkSpeed(td.getWalkSpeed() + 0.1F);
            td.setBleeding(false);
            Data.savePlayer(targetUUID);

            useItemInHand(p);
            t.setWalkSpeed(td.getWalkSpeed());

            if (p == t) {
                Msg.BANDAGE_SELF.sendTo(p);
            } else {
                Msg.BANDAGE_OTHER.sendTo(p, t.getName());
                Msg.BANDAGE_OTHER_NOTICE.sendTo(t, p.getName());
            }
        }
    }

    public void useItemInHand(Player p) {
        ItemStack is = p.getItemInHand();
        is.setAmount(is.getAmount() - 1);
        p.setItemInHand(is);
    }

}
