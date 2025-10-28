package io.github.schntgaispock.gastronomicon.util.item;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemUtil {

    public static int hashIgnoreAmount(ItemStack stack) {
        if (stack == null) return 0;

        int hash = 1;

        hash = hash * 31 + stack.getType().hashCode();
        hash = hash * 31 + (stack.hasItemMeta() ? stack.getItemMeta().hashCode() : 0);

        return hash;
    }

    public static int getSickleTier(@Nonnull ItemStack item) {
        final SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem != null) {
            return switch (sfItem.getId()) {
                case "GN_WOODEN_SICKLE" -> 1;
                case "GN_STEEL_SICKLE" -> 2;
                case "GN_REINFORCED_SICKLE" -> 3;
                default -> 0;
            };
        } else {
            return 0;
        }
    }

    public static boolean isSeed(@Nonnull Material material) {
        return switch (material) {
            case WHEAT_SEEDS, POTATO, CARROT, BEETROOT_SEEDS, PUMPKIN_SEEDS, MELON_SEEDS -> true;
            default -> false;
        };
    }

    @Nonnull
    public static Material getPlacedBlock(@Nonnull Material seed) {
        return switch (seed) {
            case WHEAT_SEEDS -> Material.WHEAT;
            case POTATO -> Material.POTATOES;
            case CARROT -> Material.CARROTS;
            case BEETROOT_SEEDS -> Material.BEETROOTS;
            case PUMPKIN_SEEDS -> Material.PUMPKIN_STEM;
            case MELON_SEEDS -> Material.MELON_STEM;
            default -> Material.AIR;
        };
    }

    @ParametersAreNonnullByDefault
    public static void giveItems(Player player, ItemStack... items) {
        player.getInventory().addItem(items).forEach((__, item) -> {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        });
    }

    public static String getPotionName(PotionEffectType type) {
        return switch (type.getName()) {
            case "SLOW" -> "Slowness";
            case "FAST_DIGGING" -> "Haste";
            case "SLOW_DIGGING" -> "Mining Fatigue";
            case "INCREASE_DAMAGE" -> "Strength";
            case "HEAL" -> "Instant Health";
            case "HARM" -> "Instant Damage";
            case "CONFUSION" -> "Nausea";
            case "DAMAGE_RESISTANCE" -> "Resistance";
            case "UNLUCK" -> "Bad Luck";
            default -> WordUtils.capitalizeFully(type.getName().replaceAll("_", " "));
        };
    }

    /**
     * Copied from Slimefun's ItemUtils, this version returns the empty container
     * version of the item instead of replacing it
     * 
     * @param item
     * @param amount
     * @param replaceConsumables
     */
    public static Optional<Material> consumeItem(@Nonnull ItemStack item, int amount, boolean replaceConsumables) {
        if (item.getType() != Material.AIR && item.getAmount() > 0) {
            if (replaceConsumables) {
                switch (item.getType()) {
                    case POTION:
                    case DRAGON_BREATH:
                    case HONEY_BOTTLE:
                        item.setAmount(0);
                        return Optional.of(Material.GLASS_BOTTLE);
                    case WATER_BUCKET:
                    case LAVA_BUCKET:
                    case MILK_BUCKET:
                        item.setAmount(0);
                        return Optional.of(Material.BUCKET);
                    default:
                        break;
                }
            }

            if (item.getAmount() <= amount) {
                item.setAmount(0);
            } else {
                item.setAmount(item.getAmount() - amount);
            }
        }
        return Optional.empty();
    }

}
