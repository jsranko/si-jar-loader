package de.sranko_informatik.si_jar_loader;

public class Security {
    private boolean enable;
    private KeyStoreConfig keystore;

    public Security() {
    }

    public Security(boolean enable, KeyStoreConfig keystore) {
        this.enable = enable;
        this.keystore = keystore;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public KeyStoreConfig getKeystore() {
        return keystore;
    }

    public void setKeystore(KeyStoreConfig keystore) {
        this.keystore = keystore;
    }

    @Override
    public String toString() {
        if (keystore != null) {
            return String.format("enabled:%s, keystore:%s",
                    isEnable(), getKeystore().toString());
        } else {
            return String.format("enabled:%s", isEnable());
        }

    }
}
