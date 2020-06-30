
package top.dsbbs2.common.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Lists;

public final class CommandRegistry
{
  private CommandRegistry()
  {
  }

  private static Vector<Command> commands = new Vector<>();

  public static Vector<Command> getCommands()
  {
    return CommandRegistry.commands;
  }

  public static PluginCommand setTabCom(final PluginCommand pc, final TabCompleter tc)
  {
    pc.setTabCompleter(tc);
    return pc;
  }

  public static PluginCommand setComDesc(final PluginCommand pc, final String desc)
  {
    pc.setDescription(desc);
    return pc;
  }

  public static PluginCommand setComPerM(final PluginCommand pc, final String m)
  {
    pc.setPermissionMessage(m);
    return pc;
  }

  public static PluginCommand setComUsa(final PluginCommand pc, final String u)
  {
    pc.setUsage(u);
    return pc;
  }

  public static PluginCommand setComPer(final PluginCommand pc, final String p)
  {
    pc.setPermission(p);
    return pc;
  }

  public static PluginCommand setComAlias(final PluginCommand pc, final String... alias)
  {
    final List<String> a = new ArrayList<>();
    a.addAll(Lists.newArrayList(alias));
    pc.setAliases(a);
    return pc;
  }

  public static void regComWithoutRecording(final String prefix, final Command c)
  {
    try {
      final Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
      cmap.setAccessible(true);
      final SimpleCommandMap scmap = (SimpleCommandMap) cmap.get(Bukkit.getServer());
      scmap.register(prefix, c);
    } catch (final Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public static void regCom(final String prefix, final Command c)
  {
    try {
      final Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
      cmap.setAccessible(true);
      final SimpleCommandMap scmap = (SimpleCommandMap) cmap.get(Bukkit.getServer());
      scmap.register(prefix, c);
      CommandRegistry.commands.add(c);
    } catch (final Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public static void regComWithCompleter(final String prefix, final PluginCommand c)
  {
    CommandRegistry.regCom(prefix, CommandRegistry.setTabCom(c, (TabCompleter) c.getExecutor()));
  }

  public static PluginCommand newPluginCommand(final String name, final CommandExecutor ce, final Plugin plugin)
  {
    try {
      final PluginCommand pc = CommandRegistry.newPluginCommand(name, plugin);
      pc.setExecutor(ce);
      return pc;
    } catch (final Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public static PluginCommand newPluginCommand(final String name, final Plugin plugin)
  {
    try {
      final Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
      c.setAccessible(true);
      return c.newInstance(name, plugin);
    } catch (final Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
