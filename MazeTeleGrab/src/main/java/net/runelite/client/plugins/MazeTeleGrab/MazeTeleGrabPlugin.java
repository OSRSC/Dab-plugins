package net.runelite.client.plugins.MazeTeleGrab;

import static net.runelite.api.MenuAction.SPELL_CAST_ON_NPC;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import org.pf4j.Extension;

@Extension
@SuppressWarnings("unused")
@PluginDescriptor(
		name = "<html><font color=#19C2FF>Maze Telegrab",
		description = "Left click telegrabs the maze guardian at MTA"
)
@Slf4j
public class MazeTeleGrabPlugin extends Plugin
{

/*	@Inject
	private MTAHelperConfig config;*/
	@Inject
	private Client client;

	private Item item;

	private static final int TeleArea = 13463;
	private static final int MTAArea = 13462;
	private MenuEntryAdded menuEntryAdded;

/*	@Provides
	MTAHelperConfig provideConfig(ConfigManager configManager){
		return configManager.getConfig(MTAHelperConfig.class);
	}*/

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!checkTeleArea() && !Text.sanitize(event.getTarget()).contains("Maze Guardian") || (event.isForceLeftClick()))
			return;
			if (event.getOpcode() == MenuAction.NPC_FIRST_OPTION.getId())
			{
				setSelectSpell(WidgetInfo.SPELL_TELEKINETIC_GRAB);
				MenuEntry e = event.clone();
				e.setOption("Cast " + client.getSelectedSpellName());
				e.setForceLeftClick(true);
				insert(e);
			}
		/*if (!checkAlchArea() && (event.isForceLeftClick()) &&
				(!Text.sanitize(event.getTarget()).contains("Emerald")))
			return;
			if (event.getOpcode() == MenuAction.ITEM_USE.getId()) {
				setSelectSpell(config.alchSpell().getSpell());
				MenuEntry e = event.clone();
				e.setOption("Cast " + client.getSelectedSpellName());
				e.setForceLeftClick(true);
				insert(e);
			}

		if (!checkEnchantArea() && !Text.sanitize(event.getTarget()).contains("Dragonstone") && (event.isForceLeftClick()))
			return;
			if (event.getOpcode() == MenuAction.ITEM_FIRST_OPTION.getId()) {
				setSelectSpell(config.enchantSpell().getSpell());
				MenuEntry e = event.clone();
				e.setOption("Cast " + client.getSelectedSpellName());
				e.setForceLeftClick(true);
				insert(e);
			}


		if (!checkBonesArea() && !Text.sanitize(event.getTarget()).contains("Animals' bones") && (event.isForceLeftClick()))
			return;
			if (event.getOpcode() == MenuAction.ITEM_FIRST_OPTION.getId())
			{
				setSelectSpell(config.bonesSpell().getSpell());
				MenuEntry e = event.clone();
				e.setOption("Cast " + client.getSelectedSpellName());
				e.setForceLeftClick(true);
				insert(e);
			}*/
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuOption().contains("Cast") && event.getMenuTarget().contains("Maze Guardian"))
		{
			event.setMenuAction(SPELL_CAST_ON_NPC);
			event.setActionParam(0);
			event.setWidgetId(0);
		}
/*		if (event.getMenuOption().contains("Cast") &&
				(event.getMenuTarget()).contains("Emerald"))
		{
			event.setMenuAction(ITEM_USE_ON_WIDGET);
			event.setWidgetId(WidgetInfo.INVENTORY.getPackedId());
			event.setId(6896);
		}*/

	}

	private void setSelectSpell(WidgetInfo info)
	{
		final Widget widget = client.getWidget(info);
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

	private boolean checkTeleArea() {
		return client.getLocalPlayer().getWorldLocation().getRegionID() == TeleArea;
	}
/*
	private boolean checkAlchArea() {
		return client.getLocalPlayer().getWorldLocation().getRegionID() == MTAArea && client.getLocalPlayer().getWorldLocation().getPlane() == 2;
	}

	private boolean checkBonesArea() {
		return client.getLocalPlayer().getWorldLocation().getRegionID() == MTAArea && client.getLocalPlayer().getWorldLocation().getPlane() == 1;
	}

	private boolean checkEnchantArea() {
		return client.getLocalPlayer().getWorldLocation().getRegionID() == MTAArea && client.getLocalPlayer().getWorldLocation().getPlane() == 0;
	}*/
}