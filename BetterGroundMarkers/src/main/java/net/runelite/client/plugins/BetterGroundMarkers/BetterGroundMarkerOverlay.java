/*
 * Copyright (c) 2018, TheLonelyDev <https://github.com/TheLonelyDev>
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.BetterGroundMarkers;

import java.awt.*;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

@Singleton
public class BetterGroundMarkerOverlay extends Overlay
{
	private final Client client;
	private final BetterGroundMarkerPlugin plugin;
	private final BetterGroundMarkerConfig config;

	@Inject
	private BetterGroundMarkerOverlay(final Client client, final BetterGroundMarkerPlugin plugin, final BetterGroundMarkerConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.LOW);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		List<BetterGroundMarkerWorldPoint> points = plugin.getPoints();
		for (BetterGroundMarkerWorldPoint groundMarkerWorldPoint : points)
		{
			drawTile(graphics, groundMarkerWorldPoint);
		}

		return null;
	}

	private void drawTile(Graphics2D graphics, BetterGroundMarkerWorldPoint groundMarkerWorldPoint)
	{
		WorldPoint point = groundMarkerWorldPoint.getWorldPoint();
		if (point.getPlane() != client.getPlane())
		{
			return;
		}

		LocalPoint lp = LocalPoint.fromWorld(client, point);
		if (lp == null)
		{
			return;
		}

		Polygon poly = Perspective.getCanvasTilePoly(client, lp);
		if (poly == null)
		{
			return;
		}

		Color color = config.markerColor();
		switch (groundMarkerWorldPoint.getGroundMarkerPoint().getGroup())
		{
			case 2:
				color = config.markerColor2();
				break;
			case 3:
				color = config.markerColor3();
				break;
			case 4:
				color = config.markerColor4();
				break;
			case 5:
				color = config.markerColor5();
				break;
			case 6:
				color = config.markerColor6();
				break;
			case 7:
				color = config.markerColor7();
				break;
			case 8:
				color = config.markerColor8();
				break;
			case 9:
				color = config.markerColor9();
				break;
			case 10:
				color = config.markerColor10();
				break;
			case 11:
				color = config.markerColor11();
				break;
			case 12:
				color = config.markerColor12();
		}
		renderPolygon(graphics, poly, color);
	}

	public void renderPolygon(Graphics2D graphics, Shape poly, Color color)
	{
		int cRed = color.getRed();
		int cGreen = color.getGreen();
		int cBlue = color.getBlue();
		Color noOutline = new Color(cRed, cGreen, cBlue, 0);
		Color noOutlineFill = new Color(cRed, cGreen, cBlue, config.opacity());

		switch (config.swapTileMode()) {
			case DEFAULT:
				graphics.setColor(color);
				final Stroke originalStroke = graphics.getStroke();
				graphics.setStroke(new BasicStroke(2));
				graphics.draw(poly);
				graphics.setColor(new Color(0, 0, 0, 50));
				graphics.fill(poly);
				graphics.setStroke(originalStroke);
				break;
			case NO_OUTLINE:
				graphics.setColor(noOutline);
				final Stroke originalStroke2 = graphics.getStroke();
				graphics.setStroke(new BasicStroke(2));
				graphics.draw(poly);
				graphics.setColor(new Color(0, 0, 0, config.opacity()));
				graphics.fill(poly);
				graphics.setStroke(originalStroke2);
				break;
			case NO_OUTLINE_COLOR:
				graphics.setColor(noOutline);
				final Stroke originalStroke3 = graphics.getStroke();
				graphics.setStroke(new BasicStroke(2));
				graphics.draw(poly);
				graphics.setColor(noOutlineFill);
				graphics.fill(poly);
				graphics.setStroke(originalStroke3);
				break;
		}
	}
}