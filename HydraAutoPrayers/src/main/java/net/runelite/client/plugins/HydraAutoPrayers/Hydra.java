/*
 * Copyright (c) 2019, Lucas <https://github.com/lucwousin>
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
package net.runelite.client.plugins.HydraAutoPrayers;

import java.awt.image.BufferedImage;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;
import net.runelite.api.SpriteID;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
@Singleton
class Hydra
{
	@Getter(AccessLevel.PACKAGE)
	@RequiredArgsConstructor
	enum AttackStyle
	{
		MAGIC(1662, Prayer.PROTECT_FROM_MAGIC, SpriteID.PRAYER_PROTECT_FROM_MAGIC),
		RANGED(1663, Prayer.PROTECT_FROM_MISSILES, SpriteID.PRAYER_PROTECT_FROM_MISSILES);

		private final int projectileID;
		private final Prayer prayer;
		private final int spriteID;

		@Getter(AccessLevel.NONE)
		private BufferedImage image;

		BufferedImage getImage(SpriteManager spriteManager) {
			if (image == null) {
				BufferedImage tmp = spriteManager.getSprite(spriteID, 0);
				image = tmp == null ? null : ImageUtil.resizeImage(tmp, net.runelite.client.plugins.HydraAutoPrayers.HydraOverlay.IMGSIZE, net.runelite.client.plugins.HydraAutoPrayers.HydraOverlay.IMGSIZE);
			}

			return image;
		}
	}

	private final NPC npc;

	private net.runelite.client.plugins.HydraAutoPrayers.HydraPhase phase = net.runelite.client.plugins.HydraAutoPrayers.HydraPhase.ONE;

	private int attackCount = 0;
	private int nextSwitch = phase.getAttacksPerSwitch();

	@Setter(AccessLevel.PACKAGE)
	private int nextSpecial = 3;

	private AttackStyle nextAttack = AttackStyle.MAGIC;
	private AttackStyle lastAttack = AttackStyle.MAGIC;

	@Setter(AccessLevel.PACKAGE)
	private boolean weakened = false;

	void changePhase(net.runelite.client.plugins.HydraAutoPrayers.HydraPhase newPhase)
	{
		phase = newPhase;
		nextSpecial = 3;
		attackCount = 0;
		weakened = false;

		if (newPhase == net.runelite.client.plugins.HydraAutoPrayers.HydraPhase.FOUR)
		{
			weakened = true;
			switchStyles();
			nextSwitch = phase.getAttacksPerSwitch();
		}
	}

	private void switchStyles()
	{
		nextAttack = lastAttack == AttackStyle.MAGIC
			? AttackStyle.RANGED
			: AttackStyle.MAGIC;
	}

	void handleAttack(int id)
	{
		if (id != nextAttack.getProjectileID())
		{
			if (id == lastAttack.getProjectileID())
			{
				// If the current attack isn't what was expected and we accidentally counted 1 too much
				return;
			}

			// If the current attack isn't what was expected and we should have switched prayers
			switchStyles();
			nextSwitch = phase.getAttacksPerSwitch() - 1;
		}
		else
		{
			nextSwitch--;
		}

		lastAttack = nextAttack;
		attackCount++;

		if (nextSwitch <= 0)
		{
			switchStyles();
			nextSwitch = phase.getAttacksPerSwitch();
		}
	}

	int getNextSpecialRelative()
	{
		return nextSpecial - attackCount;
	}
}
