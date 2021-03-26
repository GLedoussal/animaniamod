package com.animania.api.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;

public interface ICold extends IAnimaniaAnimal {
    public DataParameter<Boolean> getColdParam();

    default boolean getCold()
    {
        DataParameter<Boolean> param = getColdParam();
        if (param != null)
            return this.getBoolFromDataManager(param);
        return false;
    }

    default void setCold(boolean cold)
    {
        EntityLivingBase e = (EntityLivingBase) this;
        DataParameter<Boolean> param = getColdParam();
        if (param != null)
            e.getDataManager().set(param, cold);
    }

    public int getColdDamageTimer();

    public void setColdDamageTimer(int i);
}