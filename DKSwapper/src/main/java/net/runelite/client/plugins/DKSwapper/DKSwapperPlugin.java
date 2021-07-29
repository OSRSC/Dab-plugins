package net.runelite.client.plugins.DKSwapper;

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
		name = "DKSwapper", //this is what shows up in plugin list
		description = "Swaps Prayers at DKS",
		tags = {"dab", "dks", "prayer", "auto"}
)
@Slf4j
public class DKSwapperPlugin extends Plugin {
	// Injects our config
	@Inject
	private DKSwapperConfig config;

	@Inject
	private DKSwapperOverlay overlay;

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
	DKSwapperConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(DKSwapperConfig.class);
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