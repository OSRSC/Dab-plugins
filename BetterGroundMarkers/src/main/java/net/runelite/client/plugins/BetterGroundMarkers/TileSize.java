package net.runelite.client.plugins.BetterGroundMarkers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TileSize
{
    ONE("1",1),
    THREE("3x3",3),
    FIVE("5x5", 5);

    private final String name;
    @Getter
    private final int size;

    @Override
    public String toString()
    {
        return name;
    }
}