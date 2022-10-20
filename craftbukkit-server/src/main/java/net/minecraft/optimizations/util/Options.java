package net.minecraft.optimizations.util;

import com.cobelpvp.CobelSpigot;

public enum Options {
    PEARL_GATE(true, "options.pearls.fencegates"),
    PEARL_COBWEB(true, "options.pearls.cobweb"),
    PEARL_STAIRS(true, "options.pearls.taliban.stairs"),
    PEARL_SLABS(true, "options.pearls.taliban.slabs"),
    PEARL_COBBLEWALL(true, "options.pearls.taliban.cobblewall"),
    PEARL_BED(true, "options.pearls.taliban.bed"),
    PEARL_ENDPORTAL(true, "options.pearls.taliban.endportal"),
    PEARL_PISTON(true, "options.pearls.taliban.piston"),
    PEARL_PARTICLES(true, "option.pearl-particles"),
    DAMAGE_PEARLS(true, "option.damage-player-for-pearling"),
    PEARL_ANTI_GLITCH(true, "option.pearl-antiglitch"),
    PEARL_CRITICAL_BLOCK(true, "option.critical-block-tali-pearl");

    private final String path;
    private final boolean def;

    private Options(Boolean def, String path) {
        this.def = def;
        this.path = path;
    }

    public boolean getBooleanValue() {
        return CobelSpigot.INSTANCE.getConfig().getValue(this);
    }

    public String getPath() {
        return this.path;
    }

    public boolean isDef() {
        return this.def;
    }
}
