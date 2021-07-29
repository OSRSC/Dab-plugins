package net.runelite.client.plugins.HydraAutoPrayers;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.iUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(
		name = "Hydra Auto Prayers",
		description = "Auto swaps prayers at Alchemical Hydra",
		tags = {"dab", "hydra", "prayer", "auto"}
)
@Slf4j
public class HydraAutoPrayersPlugin extends Plugin {
	// Injects our config
	@Inject
	private HydraAutoPrayersConfig config;

	@Inject
	private HydraAutoPrayersOverlay overlay;

	@Inject
	private KeyManager keyManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private iUtils utils;

	private ScheduledExecutorService executor;
	private Rectangle bounds;

	public boolean isEnabled() {
		return enabled;
	}

	private boolean enabled;

	private HotkeyListener toggleListener = new HotkeyListener(() -> config.keybind()) {
		@Override
		public void hotkeyPressed() {
			enabled = !enabled;
		}
	};

	// Provides our config
	@Provides
	HydraAutoPrayersConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(HydraAutoPrayersConfig.class);
	}

	@Override
	protected void startUp() {
		overlayManager.add(overlay);
		keyManager.registerKeyListener(toggleListener);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(toggleListener);
	}
}