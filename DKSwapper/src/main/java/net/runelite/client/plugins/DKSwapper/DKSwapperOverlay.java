package net.runelite.client.plugins.DKSwapper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Objects;
import javax.inject.Inject;

import net.runelite.client.plugins.DKSwapper.DKSwapperConfig;
import net.runelite.client.plugins.DKSwapper.DKSwapperPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class DKSwapperOverlay extends OverlayPanel
{
    private final DKSwapperConfig config;
    private final DKSwapperPlugin plugin;
    @Inject
    public DKSwapperOverlay(final DKSwapperPlugin plugin,final DKSwapperConfig config)
    {
        super(plugin);
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
    }
    @Override
    public Dimension render(Graphics2D graphics)

    {
        if (config.hide()){return null;}
        Color color = new Color(255,200,0); //can be changed on side panel
        panelComponent.getChildren().add(
                TitleComponent.builder()
                        .color(color)
                        .text("DKSwapper")
                        .build()
        );
        add("Enabled", plugin.isEnabled());
        return super.render(graphics);
    }
    private void add(Object left, Object right)
    {
        panelComponent.getChildren().add(
                LineComponent.builder()
                        // cant call .toString directly because it might be null
                        .left(Objects.toString(left))
                        .right(Objects.toString(right))
                        .build()
        );
    }
}