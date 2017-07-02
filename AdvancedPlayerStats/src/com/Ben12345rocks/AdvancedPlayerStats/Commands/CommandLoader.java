package com.Ben12345rocks.AdvancedPlayerStats.Commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedPlayerStats.Main;

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
				
			}
		});

		loadTabComplete();

	}

	public void loadTabComplete() {
		
	}
}