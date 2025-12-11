package net.mobilelize.betterplayervisibility.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.fullbright.FullBright;
import net.mobilelize.betterplayervisibility.client.highlight.BaseHighlight;
import net.mobilelize.betterplayervisibility.client.highlight.HighlightPlayerModeOption;
import net.mobilelize.betterplayervisibility.client.highlight.HighlightPlayers;
import net.mobilelize.betterplayervisibility.client.ping.BasePing;
import net.mobilelize.betterplayervisibility.client.ping.EnumsPing;
import net.mobilelize.betterplayervisibility.client.ping.Ping;
import net.mobilelize.betterplayervisibility.client.priority.BasePriorityGroup;
import net.mobilelize.betterplayervisibility.client.priority.Priority;
import net.mobilelize.betterplayervisibility.client.utils.ChatOrActionBar;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;

import java.util.List;
import java.util.Map;

import static net.mobilelize.betterplayervisibility.client.utils.DefaultValues.defaultEntitiesVisibilityCycleMap;
import static net.mobilelize.betterplayervisibility.client.utils.DefaultValues.defaultVisibilityCycleMap;

public class ConfigMenu {
    public static Screen create(Screen parent) {

        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) return null;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Better Player Visibility"))
                .setTransparentBackground(true)
                .setSavingRunnable(ConfigMenu::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        playerVisibility(builder, entryBuilder);
        highlightPlayers(builder, entryBuilder);
        entityVisibility(builder, entryBuilder);
        ping(builder, entryBuilder);
        priority(builder, entryBuilder);
        general(builder, entryBuilder);
        commands(builder, entryBuilder);

        return builder.build();
    }

    public static void general( ConfigBuilder builder, ConfigEntryBuilder entryBuilder){
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

        general.addEntry(entryBuilder.startStrField(Text.literal("Prefix"), ConfigManager.configData.prefix)
                .setDefaultValue("&b[BPV]")
                .setTooltip(Text.literal("Set a custom prefix."))
                .setSaveConsumer(ConfigMenu::setPrefix)
                .build());

        general.addEntry(entryBuilder.startEnumSelector(Text.of("Keybinds Activation Feedback"), ChatOrActionBar.class, ConfigManager.configData.chatOrActionBar)
                .setSaveConsumer(newValue -> ConfigManager.configData.chatOrActionBar = newValue)
                .setTooltip(Text.literal("Sets which action appears when activating actions though key binds."))
                .setDefaultValue(ChatOrActionBar.ACTIONBAR)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Full Bright Enabled"), FullBright.isEnabled())
                .setDefaultValue(false)
                .setTooltip(Text.literal("Enable or disable full bright."))
                .setSaveConsumer(FullBright::toggle)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Visible Barriers Enabled"), ConfigManager.configData.visibleBarrier)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Sets the visibility of barriers."))
                .setSaveConsumer(ConfigMenu::setVisibleBarriers)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Visible Barriers Particles Enabled"), ConfigManager.configData.visibleBarrierParticles)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Sets the visibility of barriers particles for visible barriers."))
                .setSaveConsumer(newValue -> ConfigManager.configData.visibleBarrierParticles = newValue)
                .build());
    }

    private static void playerVisibility(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory visibility = builder.getOrCreateCategory(Text.of("Player Visibility"));

        // Enable Toggle
        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Show All Players"), ConfigManager.configData.showAllPlayers)
                .setSaveConsumer(newValue -> ConfigManager.configData.showAllPlayers = newValue)
                .setTooltip(Text.literal("All players visibility."))
                .setDefaultValue(true)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Reverse Visibility"), ConfigManager.configData.reversedVisibility)
                .setSaveConsumer(newValue -> ConfigManager.configData.reversedVisibility = newValue)
                .setTooltip(Text.literal("Reverses players visibility."))
                .setDefaultValue(false)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Invisible Main Player"), ConfigManager.configData.invisibleMainPlayer)
                .setSaveConsumer(newValue -> ConfigManager.configData.invisibleMainPlayer = newValue)
                .setTooltip(Text.literal("Makes the main players invisible."))
                .setDefaultValue(false)
                .build());

        // Enable Radius Toggle
        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility Radius Enabled"), ConfigManager.configData.visibilityRadiusEnabled)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityRadiusEnabled = newValue)
                .setTooltip(Text.literal("Enables player visibility radius."))
                .setDefaultValue(false)
                .build());

        // Radius Value
        visibility.addEntry(entryBuilder.startIntField(Text.of("Visibility Radius"), ConfigManager.configData.visibilityRadius)
                .setMin(0)
                .setTooltip(Text.literal("Sets the visibility radius."))
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityRadius = newValue) // Prevent negative values
                .setDefaultValue(4)
                .build());

        // Enable Radius Toggle
        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility Size Enabled"), ConfigManager.configData.visibilityChangeSizeEnabled)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityChangeSizeEnabled = newValue)
                .setTooltip(Text.literal("Enables visibility size."))
                .setDefaultValue(true)
                .build());

        // Radius Value
        visibility.addEntry(entryBuilder.startFloatField(Text.of("Visibility Size"), ConfigManager.configData.visibilityChangeSize)
                .setMin(0)
                .setMax(0.9375F)
                .setTooltip(Text.literal("Sets the visibility size."))
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityChangeSize = newValue) // Prevent negative values
                .setDefaultValue(0.3F)
                .build());

        // Enum Selector (Dropdown)
        visibility.addEntry(entryBuilder.startEnumSelector(Text.of("Visibility Mode"), EnumsVisibility.class, ConfigManager.configData.visibility)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibility = newValue)
                .setTooltip(Text.literal("Sets the visibility mode."))
                .setDefaultValue(EnumsVisibility.WHITELIST)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility Name Tag"), ConfigManager.configData.visibilityNameTagEnabled)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityNameTagEnabled = newValue)
                .setTooltip(Text.literal("Enables name tag visibility."))
                .setDefaultValue(false)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility Spawn Sprinting Particles"), ConfigManager.configData.visibilitySpawnSprintingParticles)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilitySpawnSprintingParticles = newValue)
                .setTooltip(Text.literal("Enables the spawning of sprinting particles."))
                .setDefaultValue(false)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility Show Fire Effect"), ConfigManager.configData.visibilityShowFire)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityShowFire = newValue)
                .setTooltip(Text.literal("Enables the fire effect on other players."))
                .setDefaultValue(false)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility Show Shadows"), ConfigManager.configData.visibilityShowShadows)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityShowShadows = newValue)
                .setTooltip(Text.literal("Enables the shadows on other players."))
                .setDefaultValue(false)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility NPC"), ConfigManager.configData.visibilityNPCEnabled)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityNPCEnabled = newValue)
                .setTooltip(Text.literal("Sets NPC's visibility. Checks if the players name is possible."))
                .setDefaultValue(true)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility NPC 3 Characters"), ConfigManager.configData.visibilityNPC3Characters)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityNPC3Characters = newValue)
                .setTooltip(Text.literal("Sets if player's with lest than 3 characters in there names should be considered NPC's."))
                .setDefaultValue(true)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility NPC Tab list"), ConfigManager.configData.visibilityNPCTabListEnabled)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityNPCTabListEnabled = newValue)
                .setTooltip(Text.literal("Sets if player's not on the tab list should be considered NPC's."))
                .setDefaultValue(false)
                .build());

        // Enum Toggles as Booleans
        SubCategoryBuilder enumCategory = entryBuilder.startSubCategory(Text.of("Toggle Visibility Cycle")).setTooltip(Text.literal("Sets the visibility cycle."));

        Map<EnumsVisibility, Boolean> cycle = defaultVisibilityCycleMap();
        for (EnumsVisibility option : EnumsVisibility.values()) {
            enumCategory.add(entryBuilder.startBooleanToggle(Text.of(option.name()), ConfigManager.configData.cycleVisibility.getOrDefault(option, cycle.get(option)))
                    .setSaveConsumer(newValue -> ConfigManager.configData.cycleVisibility.put(option, newValue))
                    .setDefaultValue(cycle.get(option))
                    .build());
        }

        visibility.addEntry(enumCategory.build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("Whitelist"), ConfigManager.configData.visibilityList)
                .setSaveConsumer(value -> ConfigManager.configData.visibilityList = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("2nd Whitelist"), ConfigManager.configData.visibilityList2)
                .setSaveConsumer(value -> ConfigManager.configData.visibilityList2 = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("3rd Whitelist"), ConfigManager.configData.visibilityList3)
                .setSaveConsumer(value -> ConfigManager.configData.visibilityList3 = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("4th Whitelist"), ConfigManager.configData.visibilityList4)
                .setSaveConsumer(value -> ConfigManager.configData.visibilityList4 = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("5th Whitelist"), ConfigManager.configData.visibilityList5)
                .setSaveConsumer(value -> ConfigManager.configData.visibilityList5 = value)
                .build());
    }

    private static void entityVisibility(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory visibility = builder.getOrCreateCategory(Text.of("Entity's Visibility"));

        // Enable Toggle
        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Show All Entities"), ConfigManager.configData.showAllEntities)
                .setSaveConsumer(newValue -> ConfigManager.configData.showAllEntities = newValue)
                .setTooltip(Text.literal("All entities visibility."))
                .setDefaultValue(true)
                .build());

        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Reverse Visibility"), ConfigManager.configData.reversedEntitiesVisibility)
                .setSaveConsumer(newValue -> ConfigManager.configData.reversedEntitiesVisibility = newValue)
                .setTooltip(Text.literal("Reverses entities visibility."))
                .setDefaultValue(true)
                .build());

        // Enable Radius Toggle
        visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility Radius Enabled"), ConfigManager.configData.entitiesVisibilityRadiusEnabled)
                .setSaveConsumer(newValue -> ConfigManager.configData.entitiesVisibilityRadiusEnabled = newValue)
                .setTooltip(Text.literal("Enables entities visibility radius."))
                .setDefaultValue(false)
                .build());

        // Radius Value
        visibility.addEntry(entryBuilder.startIntField(Text.of("Visibility Radius"), ConfigManager.configData.entitiesVisibilityRadius)
                .setMin(0)
                .setTooltip(Text.literal("Sets the visibility radius."))
                .setSaveConsumer(newValue -> ConfigManager.configData.entitiesVisibilityRadius = newValue) // Prevent negative values
                .setDefaultValue(4)
                .build());

        // Enum Selector (Dropdown)
        visibility.addEntry(entryBuilder.startEnumSelector(Text.of("Visibility Mode"), EntitiesEnumsVisibility.class, ConfigManager.configData.entitiesVisibility)
                .setSaveConsumer(newValue -> ConfigManager.configData.entitiesVisibility = newValue)
                .setTooltip(Text.literal("Sets the visibility mode."))
                .setDefaultValue(EntitiesEnumsVisibility.WHITELIST)
                .build());

        //visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility NPC"), ConfigManager.configData.visibilityNPCEnabled)
        //        .setSaveConsumer(newValue -> ConfigManager.configData.visibilityNPCEnabled = newValue)
        //        .setTooltip(Text.literal("Enable or disable NPC's visibility."))
        //        .setDefaultValue(true)
        //        .build());
//
        //visibility.addEntry(entryBuilder.startBooleanToggle(Text.of("Visibility NPC Tab list"), ConfigManager.configData.visibilityNPCTabListEnabled)
        //        .setSaveConsumer(newValue -> ConfigManager.configData.visibilityNPCTabListEnabled = newValue)
        //        .setTooltip(Text.literal("Enable or disable if player's not on the tab list should be considered NPC's."))
        //        .setDefaultValue(false)
        //        .build());

        // Enum Toggles as Booleans
        SubCategoryBuilder enumCategory = entryBuilder.startSubCategory(Text.of("Toggle Entities Visibility Cycle")).setTooltip(Text.literal("Sets the visibility cycle."));

        Map<EntitiesEnumsVisibility, Boolean> cycle = defaultEntitiesVisibilityCycleMap();
        for (EntitiesEnumsVisibility option : EntitiesEnumsVisibility.values()) {
            enumCategory.add(entryBuilder.startBooleanToggle(Text.of(option.name()), ConfigManager.configData.entitiesCycleVisibility.getOrDefault(option, cycle.get(option)))
                    .setSaveConsumer(newValue -> ConfigManager.configData.entitiesCycleVisibility.put(option, newValue))
                    .setDefaultValue(cycle.get(option))
                    .build());
        }

        visibility.addEntry(enumCategory.build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("Whitelist"), ConfigManager.configData.entitiesVisibilityList)
                .setSaveConsumer(value -> ConfigManager.configData.entitiesVisibilityList = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("2nd Whitelist"), ConfigManager.configData.entitiesVisibilityList2)
                .setSaveConsumer(value -> ConfigManager.configData.entitiesVisibilityList2 = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("3rd Whitelist"), ConfigManager.configData.entitiesVisibilityList3)
                .setSaveConsumer(value -> ConfigManager.configData.entitiesVisibilityList3 = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("4th Whitelist"), ConfigManager.configData.entitiesVisibilityList4)
                .setSaveConsumer(value -> ConfigManager.configData.entitiesVisibilityList4 = value)
                .build());

        // Whitelist Editable List
        visibility.addEntry(entryBuilder.startStrList(Text.of("5th Whitelist"), ConfigManager.configData.entitiesVisibilityList5)
                .setSaveConsumer(value -> ConfigManager.configData.entitiesVisibilityList5 = value)
                .build());
    }

    private static void highlightPlayers(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory highlight = builder.getOrCreateCategory(Text.of("Highlight Players"));
        for (BaseHighlight group : HighlightPlayers.getGroupList()) {
            highlight.addEntry(highlightGroup(group, entryBuilder));
        }
    }

    private static SubCategoryListEntry highlightGroup(BaseHighlight group, ConfigEntryBuilder entryBuilder) {
        SubCategoryBuilder subCategoryBuilder = entryBuilder.startSubCategory(Text.literal(group.group.name())).setExpanded(false);

        subCategoryBuilder.add(entryBuilder.startBooleanToggle(Text.literal(group.group.name() + " List Enabled"), group.highlightEnabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Enable or disable the " + group.group.name().toLowerCase() + " section."))
                .setSaveConsumer(newValue -> group.highlightEnabled = newValue)
                .build());

        subCategoryBuilder.add(entryBuilder.startEnumSelector(Text.literal(group.group.name() + " Identifier Mode"), HighlightPlayerModeOption.class, group.identifierMode)
                .setDefaultValue(HighlightPlayerModeOption.TAG)
                .setTooltip(Text.literal("Choose between Off, Both, Tag, or Name."))
                .setSaveConsumer(newValue -> group.identifierMode = newValue)
                .build());

        subCategoryBuilder.add(entryBuilder.startStrField(Text.literal(group.group.name() + " Tag"), group.tag)
                .setDefaultValue("⬤")
                .setTooltip(Text.literal("Set the tag."))
                .setSaveConsumer(newValue -> group.tag = newValue)
                .build());

        subCategoryBuilder.add(entryBuilder.startColorField(Text.literal(group.group.name() + " Identifier Color"), group.identifierColor)
                .setDefaultValue(group.defaultIdentifierColor)
                .setTooltip(Text.literal("Choose the identifier's color in hex format."))
                .setSaveConsumer(group::setIdentifierColor)
                .build());

        subCategoryBuilder.add(entryBuilder.startStrList(Text.literal(group.group.name() + " List"), group.list)
                .setSaveConsumer(newValue -> group.list = newValue)
                .build());

        return subCategoryBuilder.build();
    }

    private static void ping(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory ping = builder.getOrCreateCategory(Text.of("Ping"));

        ping.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show Ping"), ConfigManager.configData.showPing)
                .setSaveConsumer(newValue -> ConfigManager.configData.showPing = newValue)
                .setTooltip(Text.literal("Shows players ping."))
                .setDefaultValue(false)
                .build());

        ping.addEntry(entryBuilder.startBooleanToggle(Text.literal("Use Default Ping Text"), ConfigManager.configData.useDefaultPingText)
                .setSaveConsumer(newValue -> ConfigManager.configData.useDefaultPingText = newValue)
                .setTooltip(Text.literal("Sets if ping should only use the default ping text for all ping."))
                .setDefaultValue(true)
                .build());

        ping.addEntry(entryBuilder.startStrField(Text.literal("Default Ping Text"), ConfigManager.configData.defaultPingText)
                .setSaveConsumer(newValue -> ConfigManager.configData.defaultPingText = newValue)
                .setTooltip(Text.literal("Sets the default ping text."))
                .setDefaultValue("%ping%ms")
                .build());

        List<BasePing> reversed = Ping.getPingGroupList().reversed();

        for (BasePing group : reversed) {
            if (group.ping == EnumsPing.NO) continue;

            ping.addEntry(entryBuilder
                    .startColorField(Text.literal(group.formattedName + " Ping Color"), group.color)
                    .setSaveConsumer(newValue -> group.color = newValue)
                    .setTooltip(Text.literal("Sets the " + group.formattedName.toLowerCase() + " ping color."))
                    .setDefaultValue(group.defaultColor)
                    .build()
            );

            ping.addEntry(entryBuilder
                    .startIntField(Text.literal(group.formattedName + " Ping Range"), group.range)
                    .setSaveConsumer(newValue -> group.range = newValue)
                    .setMin(0)
                    .setTooltip(Text.literal("Sets the " + group.formattedName.toLowerCase() + " ping range."))
                    .setDefaultValue(group.defaultRange)
                    .build()
            );
        }

        SubCategoryBuilder allPingText = entryBuilder.startSubCategory(Text.literal("All Ping Text")).setTooltip(Text.literal("Sets all of the ping text."));

        for (BasePing group : reversed){
            if (group.ping == EnumsPing.NO) continue;

            allPingText.add(entryBuilder
                    .startStrField(Text.literal( group.formattedName + " Ping Text"), group.text)
                    .setSaveConsumer(newValue -> group.text = newValue)
                    .setTooltip(Text.literal("Sets the " + group.formattedName.toLowerCase() + " ping text."))
                    .setDefaultValue(group.defaultText)
                    .build());
        }

        ping.addEntry(allPingText.build());

        ping.addEntry(entryBuilder.startBooleanToggle(Text.literal("No Ping Enabled"), ConfigManager.configData.showNoPing)
                .setSaveConsumer(newValue -> ConfigManager.configData.showNoPing = newValue)
                .setTooltip(Text.literal("Sets if no ping should show."))
                .setDefaultValue(false)
                .build());

        ping.addEntry(entryBuilder.startStrField(Text.literal("No Ping Text"), ConfigManager.configData.noPing.text)
                .setSaveConsumer(newValue -> ConfigManager.configData.noPing.text = newValue)
                .setTooltip(Text.literal("Sets the no ping text."))
                .setDefaultValue(ConfigManager.configData.noPing.defaultText)
                .build());

        ping.addEntry(entryBuilder.startColorField(Text.literal("No Ping Color"), ConfigManager.configData.noPing.color)
                .setSaveConsumer(newValue -> ConfigManager.configData.noPing.color = newValue)
                .setTooltip(Text.literal("Sets the no ping color."))
                .setDefaultValue(ConfigManager.configData.noPing.defaultColor)
                .build());
    }

    public static void priority(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory priority = builder.getOrCreateCategory(Text.literal("Priority"));

        priority.addEntry(entryBuilder.startBooleanToggle(Text.literal("Priority List Show Beginning Text"), ConfigManager.configData.priorityShowBeginningText)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Sets if the priority text should appear at the start of the list."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityShowBeginningText = newValue)
                .build());

        priority.addEntry(entryBuilder.startBooleanToggle(Text.literal("Priority Use Cached List"), ConfigManager.configData.priorityUseCachedList)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Sets if the priority should show the list of all players that was saved outside your render distance."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityUseCachedList = newValue)
                .build());

        priority.addEntry(entryBuilder.startBooleanToggle(Text.literal("Priority Show Entire List on Search"), ConfigManager.configData.priorityShowEntireListForSearch)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Sets if the priority list should show the entire list when searching for a player."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityShowEntireListForSearch = newValue)
                .build());

        priority.addEntry(entryBuilder.startBooleanToggle(Text.literal("Priority Remove Last Separator Enabled"), ConfigManager.configData.priorityRemoveLastSeparator)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Sets if the priority should remove the last separator."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityRemoveLastSeparator = newValue)
                .build());

        priority.addEntry(entryBuilder.startStrField(Text.literal("Priority List Beginning Text"), ConfigManager.configData.priorityBeginningText)
                .setDefaultValue("&3Pickup Priority based on people around you.")
                .setTooltip(Text.literal("Sets the priority list beginning text."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityBeginningText = newValue)
                .build());

        priority.addEntry(entryBuilder.startStrField(Text.literal("Priority Separator Text"), ConfigManager.configData.prioritySeparatorText)
                .setDefaultValue("&8,%space%")
                .setTooltip(Text.literal("Sets the priority separator text."))
                .setSaveConsumer(newValue -> ConfigManager.configData.prioritySeparatorText = newValue)
                .build());

        priority.addEntry(entryBuilder.startStrField(Text.literal("Priority Search For Text"), ConfigManager.configData.prioritySearchedForPlayerText)
                .setDefaultValue("&5%n%:&d%p%%s%")
                .setTooltip(Text.literal("Sets the priority search for text."))
                .setSaveConsumer(newValue -> ConfigManager.configData.prioritySearchedForPlayerText = newValue)
                .build());

        priority.addEntry(entryBuilder.startStrField(Text.literal("Priority Search For Not Found Text"), ConfigManager.configData.prioritySearchedForNotFoundText)
                .setDefaultValue("&c%p% &3pickup priority couldn't be found.")
                .setTooltip(Text.literal("Sets the priority search for not found text."))
                .setSaveConsumer(newValue -> ConfigManager.configData.prioritySearchedForNotFoundText = newValue)
                .build());

        priority.addEntry(entryBuilder.startStrField(Text.literal("Priority List Main Player Text"), ConfigManager.configData.priorityMainPlayerText)
                .setDefaultValue("&l&6%n%:&e%p%&r%s%")
                .setTooltip(Text.literal("Sets the priority list main player text."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityMainPlayerText = newValue)
                .build());

        priority.addEntry(entryBuilder.startStrField(Text.literal("Priority List Normal Player Text"), ConfigManager.configData.priorityNormalText)
                .setDefaultValue("&3%n%:&b%p%%s%")
                .setTooltip(Text.literal("Sets the priority list normal player text."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityNormalText = newValue)
                .build());

        priority.addEntry(entryBuilder.startStrField(Text.literal("Priority Cached Player Text"), ConfigManager.configData.priorityCachedText)
                .setDefaultValue("&8%n%:&7%p%%s%")
                .setTooltip(Text.literal("Sets the priority cached text."))
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityCachedText = newValue)
                .build());

        SubCategoryBuilder priorityGroupsEnabled = entryBuilder.startSubCategory(Text.literal("Priority Groups Enabled"));

        for (BasePriorityGroup priorityGroup : Priority.getPriorityGroupList()) {
            priorityGroupsEnabled.add(entryBuilder.startBooleanToggle(Text.literal("Priority List " + priorityGroup.highlightGroup.formattedName + " Enabled"), priorityGroup.showPriorityGroup)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Sets the priority list " + priorityGroup.highlightGroup.formattedName.toLowerCase() + " should be enabled."))
                    .setSaveConsumer(newValue -> priorityGroup.showPriorityGroup = newValue)
                    .build());
        }

        priority.addEntry(priorityGroupsEnabled.build());

        SubCategoryBuilder subCategoryBuilderText = entryBuilder.startSubCategory(Text.literal("Priority Groups Text"));

        for (BasePriorityGroup priorityGroup : Priority.getPriorityGroupList()) {
            subCategoryBuilderText.add(entryBuilder.startStrField(Text.literal("Priority List " + priorityGroup.highlightGroup.formattedName + " Player Text"), priorityGroup.priorityText)
                    .setDefaultValue("%a%%n%:%c%%p%%s%")
                    .setTooltip(Text.literal("Sets the priority list " + priorityGroup.highlightGroup.formattedName.toLowerCase() + " player text."))
                    .setSaveConsumer(newValue -> priorityGroup.priorityText = newValue)
                    .build());
        }

        priority.addEntry(subCategoryBuilderText.build());

        priority.addEntry(entryBuilder.startTextDescription(TextFormatter.formatText("""
                &3Place Holders
              
                &f%n% &3= &bPriority Number
                &f%p% &3= &bPlayer Name
                &f%s% &3= &bSeparator Text
                &f%space% &3= &bSpace
                
                &3Highlight Group
                
                &f%a% &3= &bAccent Color
                &f%c% &3= &bColor
                """)).build());
    }

    public static void commands(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory commands = builder.getOrCreateCategory(Text.literal("Commands"));

        commands.addEntry(entryBuilder.startTextDescription(TextFormatter
                        .formatText("&3All &badditions and removals &3of aliases requires the player to switch server's example, hub-1 to hub-2, or by leaving the world there in, to update the commands&3."))
                .build());

        commands.addEntry(entryBuilder.startTextDescription(TextFormatter
                        .formatText("&b&lIMPORTANT: &r&3Client commands have &bhigher priority &3than Server commands. So be sure to pick a name that servers don't already use!"))
                .build());

        commands.addEntry(entryBuilder.startTextDescription(TextFormatter
                        .formatText("&3Player Visibility:"))
                .build());

        commands.addEntry(entryBuilder.startStrList(Text.literal("/visibility Aliases List"), ConfigManager.configData.visibilityCommandAliases)
                .setSaveConsumer(newValue -> ConfigManager.configData.visibilityCommandAliases = newValue.stream().map(String::toLowerCase).toList())
                .build());

        commands.addEntry(entryBuilder.startTextDescription(TextFormatter
                        .formatText("&3Entity visibility:"))
                .build());

        commands.addEntry(entryBuilder.startStrList(Text.literal("/entitiesvisibility Aliases List"), ConfigManager.configData.entitiesVisibilityCommandAliases)
                .setSaveConsumer(newValue -> ConfigManager.configData.entitiesVisibilityCommandAliases = newValue)
                .build());

        commands.addEntry(entryBuilder.startTextDescription(TextFormatter
                        .formatText("&3Ping:"))
                .build());

        commands.addEntry(entryBuilder.startStrList(Text.literal("/pingget Aliases List"), ConfigManager.configData.pingCommandAliases)
                .setSaveConsumer(newValue -> ConfigManager.configData.pingCommandAliases = newValue)
                .build());

        commands.addEntry(entryBuilder.startTextDescription(TextFormatter
                        .formatText("&3Priority:"))
                .build());

        commands.addEntry(entryBuilder.startStrList(Text.literal("/priority Aliases List"), ConfigManager.configData.priorityCommandAliases)
                .setSaveConsumer(newValue -> ConfigManager.configData.priorityCommandAliases = newValue)
                .build());
    }

    private static void saveConfig() {
        ConfigManager.saveConfig();
    }

    private static void setVisibleBarriers(boolean newValue) {
        if (ConfigManager.configData.visibleBarrier == newValue) return;
        ConfigManager.configData.visibleBarrier = newValue;
        if (MinecraftClient.getInstance().worldRenderer != null) MinecraftClient.getInstance().worldRenderer.reload();
    }

    private static void setPrefix(String newValue) {
        ConfigManager.configData.prefix = newValue;
        BetterPlayerVisibilityClient.PREFIX = TextFormatter.formatText(newValue).append(" ");
    }
}
