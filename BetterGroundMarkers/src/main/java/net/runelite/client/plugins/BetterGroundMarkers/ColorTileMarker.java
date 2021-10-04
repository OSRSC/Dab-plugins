package net.runelite.client.plugins.BetterGroundMarkers;

import java.awt.Color;
import lombok.Value;
import net.runelite.api.coords.WorldPoint;

/**
 * Used to denote marked tiles and their colors.
 * Note: This is not used for serialization of ground markers; see {@link BrushMarkerPoint}
 */
@Value
class ColorTileMarker
{
    public WorldPoint worldPoint;
    public Color color;
}