package com.voranix.ottoke.common.cooking;

import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class FreidoraMenu extends AbstractContainerMenu {
    private final BlockPos pos;
    private final Level level;
    private final ContainerData data;

    public FreidoraMenu(int containerId, Inventory playerInventory, BlockPos pos, Level level) {
        this(containerId, playerInventory, (FreidoraBlockEntity) level.getBlockEntity(pos), level);
    }

    public FreidoraMenu(int containerId, Inventory playerInventory, FreidoraBlockEntity blockEntity, Level level) {
        super(ModMenuTypes.FREIDORA.get(), containerId);
        this.pos = blockEntity.getBlockPos();
        this.level = level;
        this.data = blockEntity.getData();

        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), FreidoraBlockEntity.INPUT_SLOT, 56, 35));
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), FreidoraBlockEntity.OUTPUT_SLOT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        this.addPlayerInventorySlots(playerInventory, 8, 84);
        this.addDataSlots(this.data);
    }

    private void addPlayerInventorySlots(Inventory inventory, int x, int y) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                int slotX = x + column * 18;
                int slotY = y + row * 18;
                this.addSlot(new Slot(inventory, column + row * 9 + 9, slotX, slotY));
            }
        }

        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(inventory, column, x + column * 18, y + 58));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null) {
            return true;
        }
        return stillValid(ContainerLevelAccess.create(this.level, this.pos), player, ModBlocks.FREIDORA.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (index == FreidoraBlockEntity.OUTPUT_SLOT) {
                if (!this.moveItemStackTo(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index != FreidoraBlockEntity.INPUT_SLOT) {
                if (!this.moveItemStackTo(slotStack, FreidoraBlockEntity.INPUT_SLOT, FreidoraBlockEntity.OUTPUT_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    public int getCookingProgress() {
        return this.data.get(0);
    }

    public int getCookingTotalTime() {
        return this.data.get(1);
    }
}
