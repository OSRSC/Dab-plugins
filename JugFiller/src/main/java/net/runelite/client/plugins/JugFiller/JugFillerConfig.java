package net.runelite.client.plugins.JugFiller;

import net.runelite.client.config.*;


@ConfigGroup("JugFillerConfig")
public interface JugFillerConfig extends Config {

	@ConfigTitle(
			keyName = "delayConfig",
			name = "Sleep Delay Configuration lul",
			description = "Configure how the bot handles sleep delays",
			position = 0
	)
	String delayConfig = "delayConfig";

	@Range(
			min = 0,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepMin",
			name = "Sleep Min",
			description = "",
			position = 1,
			section = "delayConfig"
	)
	default int sleepMin() {
		return 60;
	}

	@Range(
			min = 0,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepMax",
			name = "Sleep Max",
			description = "",
			position = 2,
			section = "delayConfig"
	)
	default int sleepMax() {
		return 350;
	}

	@Range(
			min = 0,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepTarget",
			name = "Sleep Target",
			description = "",
			position = 3,
			section = "delayConfig"
	)
	default int sleepTarget() {
		return 120;
	}

	@Range(
			min = 0,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepDeviation",
			name = "Sleep Deviation",
			description = "",
			position = 4,
			section = "delayConfig"
	)
	default int sleepDeviation() { return 150; }

	@ConfigItem(
			keyName = "sleepWeightedDistribution",
			name = "Sleep Weighted Distribution",
			description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
			position = 5,
			section = "delayConfig"
	)
	default boolean sleepWeightedDistribution() { return false; }

	@ConfigTitle(
			keyName = "delayTickConfig",
			name = "Game Tick Configuration",
			description = "Configure how the bot handles game tick delays, 1 game tick equates to roughly 600ms",
			position = 10
	)
	String delayTickConfig = "delayTickConfig";

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayMin",
			name = "Game Tick Min",
			description = "",
			position = 11,
			section = "delayTickConfig"
	)
	default int tickDelayMin() {
		return 1;
	}

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayMax",
			name = "Game Tick Max",
			description = "",
			position = 12,
			section = "delayTickConfig"
	)
	default int tickDelayMax() {
		return 3;
	}

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayTarget",
			name = "Game Tick Target",
			description = "",
			position = 13,
			section = "delayTickConfig"
	)
	default int tickDelayTarget() {
		return 2;
	}

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayDeviation",
			name = "Game Tick Deviation",
			description = "",
			position = 14,
			section = "delayTickConfig"
	)
	default int tickDelayDeviation() {
		return 1;
	}

	@ConfigItem(
			keyName = "tickDelayWeightedDistribution",
			name = "Game Tick Weighted Distribution",
			description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
			position = 15,
			section = "delayTickConfig"
	)
	default boolean tickDelayWeightedDistribution() {
		return false;
	}

	@ConfigTitle(
			keyName = "instructionsTitle",
			name = "Instructions",
			description = "",
			position = 16
	)
	String instructionsTitle = "instructionsTitle";

	@ConfigItem(
			keyName = "instruction",
			name = "",
			description = "Instructions. Don't enter anything into this field",
			position = 20,
			title = "instructionsTitle"
	)
	default String instruction() {
		return "Start in Hosidius Kitchen with empty jugs in your bank";
	}

	@ConfigItem(
			keyName = "logout",
			name = "Log out when out of items",
			description = "",
			position = 40
	)
	default boolean logout() { return false; }

	@ConfigItem(
			keyName = "startButton",
			name = "Start/Stop",
			description = "Test button that changes variable value",
			position = 50
	)
	default Button startButton() {
		return new Button();
	}
}