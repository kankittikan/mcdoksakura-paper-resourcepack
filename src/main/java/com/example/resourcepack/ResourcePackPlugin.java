package com.example.resourcepack;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;

public final class ResourcePackPlugin extends JavaPlugin implements Listener {

	private String packUrl;
	private String packSha1;
	private boolean requirePack;
	private String promptMessage;
	private String kickMessage;

	@Override
	public void onEnable() {
		// Save default config if not present and load values
		saveDefaultConfig();
		loadConfigValues();

		// Register events
		Bukkit.getPluginManager().registerEvents(this, this);

		// Register command
		getCommand("resourcepackreload").setExecutor((sender, command, label, args) -> {
			reloadConfig();
			loadConfigValues();
			sender.sendMessage(Component.text("Resource pack configuration reloaded."));
			return true;
		});
	}

	private void loadConfigValues() {
		FileConfiguration cfg = getConfig();
		this.packUrl = cfg.getString("resource-pack.url", "");
		this.packSha1 = cfg.getString("resource-pack.sha1", "");
		this.requirePack = cfg.getBoolean("resource-pack.require", true);
		this.promptMessage = cfg.getString("resource-pack.prompt", "This server requires a resource pack. Click Yes to continue.");
		this.kickMessage = cfg.getString("resource-pack.kick-message", "You must accept the server resource pack to play.");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (packUrl == null || packUrl.isEmpty()) {
			return;
		}

		// Send the resource pack. Prefer including SHA-1 when provided.
		try {
			if (packSha1 != null && !packSha1.isEmpty()) {
				player.setResourcePack(packUrl, packSha1);
			} else {
				player.setResourcePack(packUrl);
			}
		} catch (Throwable t) {
			// Fallback for any API mismatch; attempt minimal call
			try {
				player.setResourcePack(packUrl);
			} catch (Throwable ignored) {
				getLogger().warning("Failed to send resource pack to " + player.getName() + ": " + t.getMessage());
			}
		}
	}

	@EventHandler
	public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
		if (!requirePack) {
			return;
		}

		switch (event.getStatus()) {
			case DECLINED:
			case FAILED_DOWNLOAD:
				// Kick the player next tick to ensure event processing completes
				Bukkit.getScheduler().runTask(this, () -> {
					event.getPlayer().kick(Component.text(kickMessage));
				});
				break;
			default:
				break;
		}
	}
}


