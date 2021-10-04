package net.runelite.client.plugins.CerbAutoPrayers.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CerberusAttack
{
  private final int tick;
  private final Cerberus.Attack attack;
}