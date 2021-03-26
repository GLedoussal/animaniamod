package com.animania.common.helper;

import com.animania.Animania;
import com.animania.config.AnimaniaConfig;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import sereneseasons.api.season.SeasonHelper;

public class SeasonsHelper {

    public static SeasonsEnum getSeason(World world) {
        if (Loader.isModLoaded("sereneseasons")) {
            switch (SeasonHelper.getSeasonState(world).getSeason()) {
                case AUTUMN:
                    return SeasonsEnum.AUTUMN;
                case SPRING:
                    return SeasonsEnum.SPRING;
                case WINTER:
                    return SeasonsEnum.WINTER;
            }
        }

        return SeasonsEnum.SUMMER;
    }

    public static boolean seasonIsIn(String[] seasons, World world) {
        SeasonsEnum s = getSeason(world);
        for (String season : seasons) {
            try {
                if(SeasonsEnum.valueOf(season).equals(s)) {
                    return true;
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        return false;
    }

    public static float getSeasonGrowthCoef(World world) {
        SeasonsEnum s = getSeason(world);

        if (s.equals(SeasonsEnum.WINTER)) return AnimaniaConfig.gameRules.winterGrowthCoef;
        if (s.equals(SeasonsEnum.AUTUMN)) return AnimaniaConfig.gameRules.autumnGrowthCoef;
        if (s.equals(SeasonsEnum.SPRING)) return AnimaniaConfig.gameRules.springGrowthCoef;

        return AnimaniaConfig.gameRules.summerGrowthCoef;
    }

    public static float getSeasonEggLayCoef(World world) {
        SeasonsEnum s = getSeason(world);

        if (s.equals(SeasonsEnum.WINTER)) return AnimaniaConfig.gameRules.winterEggLayCoef;
        if (s.equals(SeasonsEnum.AUTUMN)) return AnimaniaConfig.gameRules.autumnEggLayCoef;
        if (s.equals(SeasonsEnum.SPRING)) return AnimaniaConfig.gameRules.springEggLayCoef;

        return AnimaniaConfig.gameRules.summerEggLayCoef;
    }
}
