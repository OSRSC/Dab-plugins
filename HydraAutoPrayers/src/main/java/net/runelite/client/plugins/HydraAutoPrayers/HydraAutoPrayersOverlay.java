package net.runelite.client.plugins.HydraAutoPrayers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Objects;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class HydraAutoPrayersOverlay extends OverlayPanel
{
    private final HydraAutoPrayersConfig config;
    private final HydraAutoPrayersPlugin plugin;
    @Inject
    public HydraAutoPrayersOverlay(final HydraAutoPrayersPlugin plugin,final HydraAutoPrayersConfig config)
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
        Color color = new Color(255,200,0);
        panelComponent.getChildren().add(
                TitleComponent.builder()
                        .color(color)
                        .text("Hydra Auto Prayers")
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