package com.cobelpvp;

public enum CobelSpigot {
    INSTANCE;

    private CobelSpigotConfig config;

    public void init() {
        this.config = new CobelSpigotConfig();
    }

    public CobelSpigotConfig getConfig() {
        return this.config;
    }

    public void setConfig(CobelSpigotConfig config) {
        this.config = config;
    }
}
