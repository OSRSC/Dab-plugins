package net.runelite.client.plugins.MazeTeleGrab;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.util.Text;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;
import javax.inject.Inject;
import static net.runelite.api.MenuAction.SPELL_CAST_ON_NPC;

@Extension
@SuppressWarnings("unused")
@PluginDescriptor(
		name = "Maze Telegrab",
		description = "Left click telegrabs the maze guardian at MTA"
)
@Slf4j
public class MazeTeleGrabPlugin extends Plugin
{
	private boolean inMTA = false;
	private static final int[] MTA_REGIONS = {
			13463
		};

	@Inject
	private Client client;


	protected void startUp()
	{
		inMTA = checkArea();
	}

	protected void shutDown()
	{
		inMTA = false;
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged state)
	{

	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (inMTA && !Text.sanitize(event.getTarget()).contains("Maze Guardian") || (event.isForceLeftClick())) return;
		if (event.getOpcode() == MenuAction.NPC_FIRST_OPTION.getId())
		{
			{
				setSelectSpell(WidgetInfo.SPELL_TELEKINETIC_GRAB);
				MenuEntry e = event.clone();
				e.setOption("Cast " + client.getSelectedSpellName());
				e.setForceLeftClick(true);
				insert(e);
			}
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (inMTA && event.getMenuOption().contains("Cast"))
		{
			event.setMenuAction(SPELL_CAST_ON_NPC);
			event.setActionParam(0);
			event.setWidgetId(0);
		}
	}

	private void setSelectSpell(WidgetInfo info)
	{
		final Widget widget = client.getWidget(info);
		assert widget != null;
		client.setSelectedSpellName("<col=00ff00>" + widget.getName() + "</col>");
		client.setSelectedSpellWidget(widget.getId());
		client.setSelectedSpellChildIndex(-1);
	}

	private void insert(MenuEntry e)
	{
		client.insertMenuItem(
				e.getOption(),
				e.getTarget(),
				e.getOpcode(),
				e.getIdentifier(),
				e.getParam0(),
				e.getParam1(),
				true
		);
	}

	private boolean checkArea() {
		return Arrays.equals(client.getMapRegions(), MTA_REGIONS) && client.isInInstancedRegion();
	}
}