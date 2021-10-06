package net.runelite.client.plugins.MazeTeleGrab;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("mtahelper")
public interface MTAHelperConfig extends Config {

  @ConfigSection(
      name = "Alchemy Room",
      position = 0,
      description = "",
      closedByDefault = true
  )
  String alchemySection = "alchemySection";

  @ConfigSection(
      name = "Enchantment Room",
      position = 1,
      description = "",
      closedByDefault = true
  )
  String enchantSection = "enchantSection";

  @ConfigSection(
      name = "Bones Room",
      position = 0,
      description = "",
      closedByDefault = true
  )
  String boneSection = "boneSection";

  @ConfigItem(
      name = "Alchemy Spell",
      keyName = "alchspell",
      description = "Choose which Alchemy spell to use.",
      position = 0,
      section = alchemySection
  )
  default AlchSpells alchSpell(){
    return AlchSpells.HIGH_ALCH;
  }

  @ConfigItem(
      name = "Enchantment Spell",
      keyName = "enchantSpell",
      description = "Choose which Enchantment spell to use.",
      position = 0,
      section = enchantSection
  )
  default EnchantSpells enchantSpell(){
    return EnchantSpells.LVL6_ENCHANT;
  }

  @ConfigItem(
      name = "Bones Spell",
      keyName = "boneSpell",
      description = "Choose which Bones spell to use.",
      position = 0,
      section = boneSection
  )
  default BonesSpells bonesSpell(){
    return BonesSpells.B2B;
  }

  @Getter
  @AllArgsConstructor
  public enum AlchSpells{
    LOW_ALCH("Low Alch", WidgetInfo.SPELL_LOW_LEVEL_ALCHEMY),
    HIGH_ALCH("High Alch", WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY);

    private String name;
    private WidgetInfo spell;

    @Override
    public String toString()
    {
      return getName();
    }
  }

  @Getter
  @AllArgsConstructor
  public enum EnchantSpells{
    LVL1_ENCHANT("Lvl-1 Enchant", WidgetInfo.SPELL_LVL_1_ENCHANT),
    LVL2_ENCHANT("Lvl-2 Enchant", WidgetInfo.SPELL_LVL_2_ENCHANT),
    LVL3_ENCHANT("Lvl-3 Enchant", WidgetInfo.SPELL_LVL_3_ENCHANT),
    LVL4_ENCHANT("Lvl-4 Enchant", WidgetInfo.SPELL_LVL_4_ENCHANT),
    LVL5_ENCHANT("Lvl-5 Enchant", WidgetInfo.SPELL_LVL_5_ENCHANT),
    LVL6_ENCHANT("Lvl-6 Enchant", WidgetInfo.SPELL_LVL_6_ENCHANT),
    LVL7_ENCHANT("Lvl-7 Enchant", WidgetInfo.SPELL_LVL_7_ENCHANT);

    private String name;
    private WidgetInfo spell;

    @Override
    public String toString()
    {
      return getName();
    }
  }

  @Getter
  @AllArgsConstructor
  public enum BonesSpells{
    B2B("Bones to Bananas", WidgetInfo.SPELL_BONES_TO_BANANAS),
    B2P("Bones to Peaches", WidgetInfo.SPELL_BONES_TO_PEACHES);

    private String name;
    private WidgetInfo spell;

    @Override
    public String toString()
    {
      return getName();
    }
  }
}
