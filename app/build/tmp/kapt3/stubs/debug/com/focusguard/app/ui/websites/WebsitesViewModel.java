package com.focusguard.app.ui.websites;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/focusguard/app/ui/websites/WebsitesViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "blockedDomains", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/focusguard/app/data/BlockedDomain;", "getBlockedDomains", "()Lkotlinx/coroutines/flow/StateFlow;", "dao", "Lcom/focusguard/app/data/BlockedDomainDao;", "addDomain", "", "domain", "", "removeDomain", "Lkotlinx/coroutines/Job;", "app_debug"})
public final class WebsitesViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.focusguard.app.data.BlockedDomainDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.focusguard.app.data.BlockedDomain>> blockedDomains = null;
    
    public WebsitesViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.focusguard.app.data.BlockedDomain>> getBlockedDomains() {
        return null;
    }
    
    public final void addDomain(@org.jetbrains.annotations.NotNull()
    java.lang.String domain) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job removeDomain(@org.jetbrains.annotations.NotNull()
    java.lang.String domain) {
        return null;
    }
}