package com.tkisor.llrcore.datagen.lang;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.LogicalSide;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SimplifiedChineseLanguageProvider extends LanguageProvider implements RegistrateProvider {

    public static final ProviderType<SimplifiedChineseLanguageProvider> LANG = ProviderType.register("cn_lang", (var0, var1) -> new SimplifiedChineseLanguageProvider(var0, var1.getGenerator().getPackOutput()));
    private final AbstractRegistrate<?> owner;

    private SimplifiedChineseLanguageProvider(AbstractRegistrate<?> var1, PackOutput var2) {
        super(var2, var1.getModid(), "zh_cn");
        this.owner = var1;
    }

    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    public String getName() {
        return "Lang (zh_cn)";
    }

    protected void addTranslations() {
        this.owner.genData(LANG, this);
    }
}
