package de.timolia.lactea.translate;

import de.timolia.lactea.loader.module.LacteaModule;
import de.timolia.lactea.loader.module.ModuleDefinition;
import de.timolia.lactea.loader.startup.EnableContext;
import de.timolia.lactea.loader.startup.LoadContext;

/**
 * @author David (_Esel)
 */
@ModuleDefinition("lactea-translate")
public class TranslateModule extends LacteaModule {
    @Override
    public void onLoad(LoadContext context) throws Exception {
        System.out.println("load");
    }

    @Override
    public void onEnable(EnableContext context) throws Exception {
        System.out.println("enable " + getInjector().getInstance(TestConfig.class).toString());
    }
}
