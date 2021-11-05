package net.minecraft.server;

public class MobEffectAttackDamage extends MobEffectList {

    protected MobEffectAttackDamage(int i, boolean flag, int j) {
        super(i, flag, j);
    }

    public double a(int i, AttributeModifier attributemodifier) {
        return amount * (double) (i + 1);
    }
}
