package de.sranko_informatik.si_jar_loader;

public class Configuration {
    private Security security;

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    @Override
    public String toString() {
        if (security == null) {
            return "";
        }
        return security.toString();
    }
}
