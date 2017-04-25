package micdoodle8.mods.galacticraft.core.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class ItemCanisterGenericHandler implements IFluidHandlerItem, ICapabilityProvider
{
    @Nonnull
    protected ItemStack container;

    /**
     * @param container  The container itemStack, data is stored on it directly as NBT.
     * @param capacity   The maximum capacity of this fluid tank.
     */
    public ItemCanisterGenericHandler(@Nonnull ItemStack container)
    {
        this.container = container;
    }

    @Nonnull
    @Override
    public ItemStack getContainer()
    {
    	return container;
    }

    @Nullable
    public FluidStack getFluid()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	return ((ItemCanisterGeneric)container.getItem()).getFluid(container);
        }
    	return null;
    }

    protected void setFluid(FluidStack fluid)
    {
        if (this.canFillFluidType(fluid))
        {
        	((ItemCanisterGeneric)container.getItem()).setDamage(container, ItemCanisterGeneric.EMPTY - fluid.amount);
        }
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return new FluidTankProperties[] { new FluidTankProperties(getFluid(), ItemCanisterGeneric.EMPTY - 1) };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	return ((ItemCanisterGeneric)container.getItem()).fill(container, resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	return ((ItemCanisterGeneric)container.getItem()).drain(container, maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (this.canDrainFluidType(resource))
        {
        	return ((ItemCanisterGeneric)container.getItem()).drain(container, resource.amount, doDrain);
        }
        return null;
    }

    public boolean canFillFluidType(FluidStack fluid)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric && fluid != null && fluid.getFluid() != null && fluid.amount > 0)
        {
        	return ((ItemCanisterGeneric)container.getItem()).getAllowedFluid().equalsIgnoreCase(fluid.getFluid().getName());
        }
        return false;
    }

    public boolean canDrainFluidType(FluidStack fluid)
    {
        return this.canFillFluidType(fluid);
    }

    protected void setContainerToEmpty()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	((ItemCanisterGeneric)container.getItem()).setDamage(container, ItemCanisterGeneric.EMPTY);;
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (T) this : null;
    }

    public static class Consumable extends ItemCanisterGenericHandler
    {
        public Consumable(ItemStack container, int capacity)
        {
            super(container);
        }

//        @Override
//        protected void setContainerToEmpty()
//        {
//            super.setContainerToEmpty();
//        }
    }

    /**
     * No need to swap the container item for a different one when it's emptied, our code
     * already does this in ItemCanisterGeneric
     */
    public static class SwapEmpty extends ItemCanisterGenericHandler
    {
        public SwapEmpty(ItemStack container, ItemStack emptyContainer, int capacity)
        {
            super(container);
        }

//        @Override
//        protected void setContainerToEmpty()
//        {
//            super.setContainerToEmpty();
//        }
    }
}

