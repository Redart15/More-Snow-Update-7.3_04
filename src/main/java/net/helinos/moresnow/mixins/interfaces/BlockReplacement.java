package net.helinos.moresnow.mixins.interfaces;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;

public interface BlockReplacement {
	Block<?> beforeDestroyedByPlayer(World world, TilePosc tilepos, Side side, int data, Player player, ItemStack heldItem);
}
