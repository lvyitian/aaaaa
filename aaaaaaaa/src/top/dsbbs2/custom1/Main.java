
package top.dsbbs2.custom1;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import top.dsbbs2.common.closure.Reference;
import top.dsbbs2.common.command.CommandRegistry;
import top.dsbbs2.common.config.SimpleConfig;
import top.dsbbs2.common.lambda.INoThrowsRunnable;
import top.dsbbs2.custom1.config.struct.Config;

public class Main extends JavaPlugin implements Listener
{
  private static final List<Integer> availableSlots = Main.addAndReturn(Main.fromTo(0, 35), -106);
  private static final List<Integer> enderSlots = Main.fromTo(0, 26);
  public final SimpleConfig<Config> config = new SimpleConfig<Config>(
      this.getDataFolder().getAbsolutePath() + File.separator + "config.json", "UTF8", Config.class)
  {
    {
      INoThrowsRunnable.invoke(this::loadConfig);
    }
  };

  @SuppressWarnings("deprecation")
  @Override
  public void onEnable()
  {
    CommandRegistry.regCom(this.getName(), CommandRegistry.newPluginCommand("cusrel", this));
    Bukkit.getPluginManager().registerEvents(this, this);
    Bukkit.getScheduler().runTaskTimer(this, () ->
    {
      for (final Player player : Bukkit.getOnlinePlayers()) {
        final World world = player.getWorld();
        if (this.config.getConfig().worlds.contains(world.getName())) {
          Arrays.asList(103, 102, 101, 100).parallelStream()
              .filter(i -> this.config.getConfig().item_ids.contains(player.getInventory().getItem(i).getTypeId()))
              .forEach(i ->
              {
                final Optional<Integer> temp = Main.availableSlots.parallelStream()
                    .filter(i2 -> player.getInventory().getItem(i2).getType() == Material.AIR).findFirst();
                if (temp.isPresent()) {
                  final int t = temp.get();
                  final ItemStack is = player.getInventory().getItem(i);
                  player.getInventory().setItem(i, null);
                  player.getInventory().setItem(t, is);
                } else {
                  final Optional<Integer> temp2 = Main.enderSlots.parallelStream()
                      .filter(i2 -> player.getEnderChest().getItem(i2).getType() == Material.AIR).findFirst();
                  if (temp2.isPresent()) {
                    final int t = temp.get();
                    final ItemStack is = player.getInventory().getItem(i);
                    player.getInventory().setItem(i, null);
                    player.getEnderChest().setItem(t, is);
                  } else {
                    player.sendMessage("背包和末影箱空间不足");
                    if (player.getActivePotionEffects().parallelStream()
                        .noneMatch(i2 -> i2.getType() == PotionEffectType.SLOW_DIGGING)) {
                      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 255, true, false),
                          true);
                    }
                    if (player.getActivePotionEffects().parallelStream()
                        .noneMatch(i2 -> i2.getType() == PotionEffectType.WEAKNESS)) {
                      player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 255, true, false), true);
                    }
                    if (player.getActivePotionEffects().parallelStream()
                        .noneMatch(i2 -> i2.getType() == PotionEffectType.CONFUSION)) {
                      player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 255, true, false), true);
                    }
                    if (player.getActivePotionEffects().parallelStream()
                        .noneMatch(i2 -> i2.getType() == PotionEffectType.UNLUCK)) {
                      player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 20, 255, true, false), true);
                    }
                  }
                }
              });
        }
      }
    }, 0, 1);
  }

  @SafeVarargs
  public static <T> List<T> addAndReturn(final List<T> o, final T... e)
  {
    o.addAll(Arrays.asList(e));
    return o;
  }

  public static Vector<Integer> fromTo(final int start, final int end)
  {
    final Vector<Integer> ret = new Vector<>();
    for (int i = start; i <= end; i++) {
      ret.add(i);
    }
    return ret;
  }

  @SuppressWarnings("deprecation")
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onPlayerInteract(final PlayerInteractEvent e)
  {
    if (this.config.getConfig().worlds.contains(e.getPlayer().getWorld().getName())) {
      Optional.ofNullable(e.getItem()).ifPresent(i ->
      {
        if (this.config.getConfig().item_ids.contains(i.getTypeId())) {
          e.setCancelled(true);
          e.getPlayer().sendMessage("此世界禁止使用此物品");
        }
      });
    }
  }

  @SuppressWarnings("deprecation")
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onPlayerInteractEntity(final PlayerInteractEntityEvent e)
  {
    if (this.config.getConfig().worlds.contains(e.getPlayer().getWorld().getName())) {
      Optional.ofNullable(e.getPlayer().getInventory().getItem(e.getHand())).ifPresent(i ->
      {
        if (this.config.getConfig().item_ids.contains(i.getTypeId())) {
          e.setCancelled(true);
          e.getPlayer().sendMessage("此世界禁止使用此物品");
        }
      });
    }
  }

  @SuppressWarnings("deprecation")
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onPlayerTeleport(final PlayerTeleportEvent e)
  {
    if (this.config.getConfig().worlds.contains(e.getTo().getWorld().getName())) {
      final Reference<Boolean> isret = new Reference<>(false);
      Arrays.asList(103, 102, 101, 100).parallelStream()
          .filter(i -> this.config.getConfig().item_ids.contains(e.getPlayer().getInventory().getItem(i).getTypeId()))
          .forEach(i ->
          {
            if (isret.value) {
              return;
            }
            final Optional<Integer> temp = Main.availableSlots.parallelStream()
                .filter(i2 -> e.getPlayer().getInventory().getItem(i2).getType() == Material.AIR).findFirst();
            if (temp.isPresent()) {
              final int t = temp.get();
              final ItemStack is = e.getPlayer().getInventory().getItem(i);
              e.getPlayer().getInventory().setItem(i, null);
              e.getPlayer().getInventory().setItem(t, is);
            } else {
              final Optional<Integer> temp2 = Main.enderSlots.parallelStream()
                  .filter(i2 -> e.getPlayer().getEnderChest().getItem(i2).getType() == Material.AIR).findFirst();
              if (temp2.isPresent()) {
                final int t = temp.get();
                final ItemStack is = e.getPlayer().getInventory().getItem(i);
                e.getPlayer().getInventory().setItem(i, null);
                e.getPlayer().getEnderChest().setItem(t, is);
              } else {
                isret.value = true;
                return;
              }
            }
          });
      if (isret.value) {
        e.setCancelled(true);
        e.getPlayer().sendMessage("背包和末影箱空间不足");
      }
    }
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
  {
    if ("cusrel".equalsIgnoreCase(command.getName())) {
      if (sender.isOp()) {
        INoThrowsRunnable.invoke(this.config::loadConfig);
        sender.sendMessage("重载完毕");
      }
    }
    return super.onCommand(sender, command, label, args);
  }

}
