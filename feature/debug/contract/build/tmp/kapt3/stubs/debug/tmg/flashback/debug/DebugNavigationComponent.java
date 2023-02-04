package tmg.flashback.debug;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Ltmg/flashback/debug/DebugNavigationComponent;", "", "getDebugMenuItems", "", "Ltmg/flashback/debug/model/DebugMenuItem;", "navigateTo", "", "id", "", "contract_debug"})
public abstract interface DebugNavigationComponent {
    
    public abstract void navigateTo(@org.jetbrains.annotations.NotNull
    java.lang.String id);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<tmg.flashback.debug.model.DebugMenuItem> getDebugMenuItems();
}