package net.runelite.client.plugins.JugFiller;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;

import static net.runelite.api.MenuAction.ITEM_USE_ON_GAME_OBJECT;
import static net.runelite.client.plugins.JugFiller.JugFillerState.*;

@Slf4j
@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(
		name = "<html><font color=#19C2FF>Jug Filler",
		description = "Fills empty jugs with water at Hosidius Kitchen",
		tags = {"dab", "jug"}
)
public class JugFillerPlugin extends Plugin {

	@Inject
	private Client client;

	@Inject
	private JugFillerConfig config;

	@Inject
	private JugFillerOverlay overlay;

	@Inject
	public iUtils utils;

	@Inject
	private InterfaceUtils interfaceUtils;

	@Inject
	public PlayerUtils playerUtils;

	@Inject
	public MenuUtils menu;

	@Inject
	public ObjectUtils object;

	@Inject
	public InventoryUtils inventory;

	@Inject
	public MouseUtils mouse;

	@Inject
	public BankUtils bank;

	@Inject
	ExecutorService executorService;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private CalculationUtils calc;

	MenuEntry targetMenu;
	Widget bankItem;
	Instant botTimer;
	Player player;
	JugFillerState state;
	GameObject bankChest;
	GameObject Sink;

	boolean startJugFiller;
	long sleepLength;
	int tickLength;
	int timeout;
	int totalJugsFilled;
	int jugsPH;
	int startJugs;
	int currentJugs;
	int jugID;
	int jugofwaterID;
	int totalProfit;
	int profitPH;

	@Provides
	JugFillerConfig provideConfig(final ConfigManager configManager) {
		return configManager.getConfig(JugFillerConfig.class);
	}

	@Override
	protected void startUp() {
	}
	//what happens when you startup the plugin

	@Override
	protected void shutDown() {
		resetVals();
	}
	//What happens when you shutdown the plugin

	private void resetVals() {
		log.info("Stopping putting water in jugs");
		startJugFiller = false;
		botTimer = null;
		overlayManager.remove(overlay);
	}
	//resets all your values

	@Subscribe
	private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked) {
		if (!configButtonClicked.getGroup().equalsIgnoreCase("JugFillerConfig")) {
			return;
		}
		log.info("button {} pressed!", configButtonClicked.getKey());
		if (configButtonClicked.getKey().equals("startButton")) {
			if (!startJugFiller) {
				startJugFiller = true;
				botTimer = Instant.now();
				initCounters();
				state = null;
				targetMenu = null;
				jugID = ItemID.JUG;
				jugofwaterID = ItemID.JUG_OF_WATER;
				botTimer = Instant.now();
				overlayManager.add(overlay);
			} else {
				resetVals();
			}
		}
	}
	//what happens when pressing start/stop

	private long sleepDelay() {
		sleepLength = calc.randomDelay(config.sleepWeightedDistribution(),
				config.sleepMin(),
				config.sleepMax(),
				config.sleepDeviation(),
				config.sleepTarget());
		return sleepLength;
	}
	// this is where your sleep delays are calculated
	private int tickDelay() {
		tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(),
				config.tickDelayMin(),
				config.tickDelayMax(),
				config.tickDelayDeviation(),
				config.tickDelayTarget());
		log.info("tick delay for {} ticks", tickLength);
		return tickLength;
	}
	// this is where your tick delays are calculated

	private void initCounters() {
		timeout = 0;
		totalJugsFilled = 0;
		startJugs = 0;
		jugsPH = 0;
		currentJugs = 0;
		totalProfit = 0;
		profitPH = 0;
	}


	private void updateTotals() {
		totalJugsFilled += inventory.getItemCount(jugofwaterID, false);
	}

	public void updateStats() {
		updateTotals();
		jugsPH = (int) getPerHour(totalJugsFilled);
	}

		public long getPerHour(int quantity) {
			Duration timeSinceStart = Duration.between(botTimer, Instant.now());
			if (!timeSinceStart.isZero()) {
				return (int) ((double) quantity * (double) Duration.ofHours(1).toMillis() / (double) timeSinceStart.toMillis());
			}
			return 0;
		}

	public JugFillerState getState() {

		bankChest = object.findNearestGameObject(ObjectID.BANK_CHEST_21301);
		Sink = object.findNearestGameObject(ObjectID.SINK_1763);

		if (bank.isOpen() && inventory.isEmpty()){
			return WITHDRAW_ALL_ITEM;
		}

		if (inventory.containsItem(ItemID.JUG) && inventory.containsItem(ItemID.JUG_OF_WATER)){
			return ANIMATING;
		}

		if (bank.isOpen()&& inventory.containsItem(ItemID.JUG)){
			return CLOSE_BANK;
		}

		if (Sink != null){
			if (inventory.containsItem(ItemID.JUG) && inventory.isFull()&& !bank.isOpen())
			return FILL_JUG;
		}

		if (inventory.containsItem(ItemID.JUG_OF_WATER)&& bank.isOpen()){
			return DEPOSIT_ALL;
		}

		if (inventory.isEmpty()){
			return OPEN_BANK;
		}

		if (inventory.containsItem(ItemID.JUG_OF_WATER) && !inventory.containsItem(ItemID.JUG)){
			return OPEN_BANK;
		}
		if (!inventory.containsItem(ItemID.JUG_OF_WATER) && !bank.containsAnyOf(ItemID.JUG)) {
			if (config.logout()) {
				interfaceUtils.logout();
			}
			return OUT_OF_ITEM;
		}
		return IDLE;

	}
	@Subscribe
	private void onGameTick(GameTick event) {
		if (!startJugFiller) {
			return;
		}
		player = client.getLocalPlayer();
		if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN) {
			if (!client.isResized()) {
				utils.sendGameMessage("client must be set to resizable");
				startJugFiller = false;
				return;
			}
			state = getState();
			log.debug(state.name());
			switch (state) {
				case TIMEOUT:
					timeout--;
					break;
				case OPEN_BANK:
					utils.doGameObjectActionMsTime(bankChest, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
					break;
				case WITHDRAW_ALL_ITEM:
					bank.withdrawAllItem(ItemID.JUG);
					timeout = tickDelay();
					break;
				case FILL_JUG:
					WidgetItem Jug = inventory.getWidgetItem(jugID);
					Sink = object.findNearestGameObject(ObjectID.SINK_1763);
					if(Jug != null){
						targetMenu = new MenuEntry("", "", Sink.getId(), ITEM_USE_ON_GAME_OBJECT.getId(),
								Sink.getSceneMinLocation().getX(), Sink.getSceneMinLocation().getY(), false);
						utils.doModifiedActionMsTime(targetMenu, Jug.getId(), Jug.getIndex(), ITEM_USE_ON_GAME_OBJECT.getId(), Sink.getConvexHull().getBounds(), sleepDelay());
					}
					break;
				case ANIMATING:
					break;
				case DEPOSIT_ALL:
					bank.depositAll();
					updateStats();
					break;
				case CLOSE_BANK:
					bank.close();
					break;
				case OUT_OF_ITEM:
					utils.sendGameMessage("You have run out of jugs");
					if (config.logout()) {
						interfaceUtils.logout();
					}
					startJugFiller = false;
					resetVals();
					break;
			}
		}
	}
}
