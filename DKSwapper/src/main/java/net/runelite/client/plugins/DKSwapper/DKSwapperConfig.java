package net.runelite.client.plugins.DKSwapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("DKSwapperConfig")
public interface DKSwapperConfig extends Config {

	@ConfigItem(
			keyName = "keybind",
			name = "Toggle Key",
			description = "Toggles the plugin",
			position = 1
	)
	default Keybind keybind()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hide", //disables the widget
			name = "Privacy",
			description = "Toggles the overlay",
			position = 2
	)
	default boolean hide()
	{
		return false;
	}
}