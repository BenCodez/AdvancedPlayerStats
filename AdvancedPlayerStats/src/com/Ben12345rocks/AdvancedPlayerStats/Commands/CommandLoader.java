package com.Ben12345rocks.AdvancedPlayerStats.Commands;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Util.Misc.StringUtils;
import com.Ben12345rocks.AdvancedPlayerStats.Main;
import com.Ben12345rocks.AdvancedPlayerStats.Users.User;
import com.Ben12345rocks.AdvancedPlayerStats.Users.UserManager;

import net.md_5.bungee.api.chat.TextComponent;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandLoader.
 */
public class CommandLoader {

	/** The instance. */
	static CommandLoader instance = new CommandLoader();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/**
	 * Gets the single instance of CommandLoader.
	 *
	 * @return single instance of CommandLoader
	 */
	public static CommandLoader getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new command loader.
	 */
	private CommandLoader() {
	}

	/**
	 * Instantiates a new command loader.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public CommandLoader(Main plugin) {
		CommandLoader.plugin = plugin;
	}

	public void loadCommands() {
		plugin.commands = new ArrayList<CommandHandler>();
		plugin.commands.add(new CommandHandler(new String[] { "Today" }, "AdvancedPlayerStats.Today",
				"See whose been online today") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				// sender.sendMessage(StringUtils.getInstance().colorize("&cGetting
				// today's online players"));
				ArrayList<String> msg = new ArrayList<String>();
				msg.add("&cName : Last Seen");
				for (Entry<User, Long> entry : plugin.getOnlineToday().entrySet()) {
					LocalDateTime lastOnline = LocalDateTime.ofInstant(Instant.ofEpochMilli(entry.getValue()),
							ZoneId.systemDefault());
					Duration dur = Duration.between(lastOnline, LocalDateTime.now());
					long hours = dur.toHours();
					long mins = dur.toMinutes();
					msg.add("&f" + entry.getKey().getPlayerName() + " : " + hours + ":" + mins + " ago");
				}

				sendMessage(sender, msg);

			}
		});

		plugin.commands.add(new CommandHandler(new String[] { "Alts", "(player)" }, "AdvancedPlayerStats.Alts",
				"Check for player alts") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				sendMessage(sender, "&cLooking for possible alts...");
				ArrayList<User> matched = plugin.getUserManager()
						.getMatchedIps(UserManager.getInstance().getAdvancedPlayerStatsUser(args[1]));
				ArrayList<String> msg = new ArrayList<String>();
				if (matched.size() > 0) {
					msg.add("&cPossible alt accounts (According to matching ips):");
					for (User user : matched) {
						msg.add("&c" + user.getPlayerName());
					}
				} else {
					msg.add("&cNo alt accounts found");
				}

				sendMessage(sender, msg);
			}
		});

		plugin.commands.add(
				new CommandHandler(new String[] { "Ontime" }, "AdvancedPlayerStats.Onime", "Check your ontime", false) {

					@Override
					public void execute(CommandSender sender, String[] args) {
						Player player = (Player) sender;
						User user = plugin.getUserManager().getAdvancedPlayerStatsUser(player);
						Duration dur = Duration.of(user.getOntime(), ChronoUnit.MILLIS);
						ArrayList<String> msg = new ArrayList<String>();
						msg.add("&cYour Ontime");
						msg.add("&c" + dur.toDays() + " Days " + dur.toHours() + " Hours " + dur.toMinutes()
								+ " Minutes");
						sendMessage(sender, msg);

					}
				});

		plugin.commands.add(new CommandHandler(new String[] { "Ontime", "(player)" }, "AdvancedPlayerStats.Onime.Other",
				"Check other player ontime") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				User user = plugin.getUserManager().getAdvancedPlayerStatsUser(args[1]);
				Duration dur = Duration.of(user.getOntime(), ChronoUnit.MILLIS);
				ArrayList<String> msg = new ArrayList<String>();
				msg.add("&c" + args[1] + " Ontime");
				msg.add("&c" + dur.toDays() + " Days " + dur.toHours() + " Hours " + dur.toMinutes() + " Minutes");
				sendMessage(sender, msg);

			}
		});

		plugin.commands
				.add(new CommandHandler(new String[] { "Perms" }, "AdvancedPlayerStats.Perms", "See all permissions") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						ArrayList<String> msg = new ArrayList<String>();

						for (CommandHandler handle : plugin.commands) {
							msg.add(handle.getHelpLineCommand("/aps") + " : " + handle.getPerm());
						}

						for (Permission perm : plugin.getDescription().getPermissions()) {
							msg.add(perm.getName());
						}

						Collections.sort(msg, String.CASE_INSENSITIVE_ORDER);

						sendMessage(sender, msg);
					}

				});

		plugin.commands
				.add(new CommandHandler(new String[] { "Help" }, "AdvancedPlayerStats.Help", "See all permissions") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						ArrayList<TextComponent> texts = new ArrayList<TextComponent>();
						HashMap<String, TextComponent> unsorted = new HashMap<String, TextComponent>();
						texts.add(StringUtils.getInstance().stringToComp("&bAdvancedPlayerStats Help"));

						boolean requirePerms = true;

						for (CommandHandler cmdHandle : plugin.commands) {
							if (sender.hasPermission(cmdHandle.getPerm()) && requirePerms) {
								unsorted.put(cmdHandle.getHelpLineCommand("/aps"), cmdHandle.getHelpLine("/aps"));
							} else if (!requirePerms) {
								unsorted.put(cmdHandle.getHelpLineCommand("/aps"), cmdHandle.getHelpLine("/aps"));
							}
						}

						ArrayList<String> unsortedList = new ArrayList<String>();
						unsortedList.addAll(unsorted.keySet());
						Collections.sort(unsortedList, String.CASE_INSENSITIVE_ORDER);
						for (String cmd : unsortedList) {
							texts.add(unsorted.get(cmd));
						}
						sendMessageJson(sender, texts);
					}

				});

		loadTabComplete();

	}

	public void loadTabComplete() {
		// for (CommandHandler cmd : plugin.commands) {
		// cmd.reloadTabComplete();
		// }
	}
}
