package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.W;
import nl.Steffion.BlockHunt.Managers.MessageManager;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDremove extends DefaultCMD {

	@Override
	public boolean execute(Player player, Command cmd, String label, String[] args) {
		if (player != null) {
			if (args.length <= 1) {
				MessageManager.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + BlockHunt.CMDremove.usage);
			} else {
				for (Arena arena : W.arenaList) {
					if (args[1].equalsIgnoreCase(arena.arenaName)) {
						MessageManager.sendFMessage(player, ConfigC.normal_removeRemovedArena, "name-" + args[1]);
						W.arenas.getFile().set(args[1], null);
						for (String sign : W.signs.getFile().getKeys(false)) {
							if (W.signs.getFile().get(sign + ".arenaName").toString().equalsIgnoreCase(args[1])) {
								Location signLoc = (Location) W.signs.getFile().get(sign + ".location");
								signLoc.getBlock().setType(Material.AIR);
								signLoc.getWorld().playEffect(signLoc, Effect.MOBSPAWNER_FLAMES, 0);
								signLoc.getWorld().playSound(signLoc, Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
								W.signs.getFile().set(sign, null);
							}
						}

						W.arenas.save();
						W.signs.load();

						W.arenaList.remove(arena);
						return true;
					}
				}

				MessageManager.sendFMessage(player, ConfigC.error_noArena, "name-" + args[1]);
			}
		} else {
			MessageManager.sendFMessage(player, ConfigC.error_onlyIngame);
		}
		return true;
	}
}
