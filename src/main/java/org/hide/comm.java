package org.hide;

import com.destroystokyo.paper.profile.PlayerProfile;
import it.unimi.dsi.fastutil.Pair;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.SkinProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class comm implements CommandExecutor {
    Map<UUID, Pair<String, String>> hidden = new WeakHashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p))
            return true;

        if (command.getName().equalsIgnoreCase("hide")) {
            String a = p.getName();
            UUID c = p.getUniqueId();
            String b;
            if (hidden.containsKey(c)) {
                try {
                    Hide.skinsRestorerAPI.getPlayerStorage().setSkinIdOfPlayer(c,
                            Hide.skinsRestorerAPI.getSkinStorage().findOrCreateSkinData(hidden.get(c).second())
                                    .get()
                                    .getIdentifier());
                    Hide.skinsRestorerAPI.getSkinApplier(Player.class).applySkin(p);
                } catch (DataRequestException | MineSkinException e) {
                    throw new RuntimeException(e);
                }
                PlayerProfile profile = p.getPlayerProfile();
                profile.setName(hidden.get(c).first());
                p.setPlayerProfile(profile);

                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.hidePlayer(p);
                    players.showPlayer(p);
                });
                b = Hide.nolongerhidden;
                hidden.remove(c);
            } else {
                Optional<SkinProperty> o = Hide.skinsRestorerAPI.getPlayerStorage().getSkinOfPlayer(c);
                hidden.put(c, Pair.of(a, o.isPresent() ? o.get().toString() : Hide.skin));
                try {
                    Hide.skinsRestorerAPI.getPlayerStorage().setSkinIdOfPlayer(c,
                            Hide.skinsRestorerAPI.getSkinStorage().findOrCreateSkinData(Hide.skin).get().getIdentifier());
                    Hide.skinsRestorerAPI.getSkinApplier(Player.class).applySkin(p);
                } catch (DataRequestException | MineSkinException e) {
                    throw new RuntimeException(e);
                }
                PlayerProfile profile = p.getPlayerProfile();
                profile.setName(Hide.name);
                p.setPlayerProfile(profile);

                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.hidePlayer(p);
                    players.showPlayer(p);
                });
                p.setPlayerListName(a);
                b = Hide.hidden;
            }

            p.sendActionBar(b);
        } else if (command.getName().equalsIgnoreCase("ahide")) {
            if (!p.isOp()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7You must be an operator to use this command."));
                return true;
            }

            int i = strings.length > 1 ? Integer.parseInt(strings[0]) : 100;
            Collection<Player> found =
                    p.getWorld()
                            .getNearbyPlayers(p.getLocation(),
                                    i,
                                    i,
                                    i).stream().filter(r ->
                                    hidden.containsKey(r.getUniqueId()) &&
                                            !r.getName().equals(p.getName())).toList();
            Bukkit.getLogger().warning(found.toString());
            if (found.size() > 0) {
                p.sendMessage("Found these players near you that are hidden: " + found.stream().map(Player::getName).toList());
                return true;
            }

            p.sendMessage("Couldn't find anyone that's hidden near you.");
        }
        else if (command.getName().equalsIgnoreCase("areload")) {
            if (!p.isOp()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7You must be an operator to use this command."));
                return true;
            }

            Hide.load();
            p.sendMessage("Successfully reloaded the plugin.");
        }

        return true;
    }
}