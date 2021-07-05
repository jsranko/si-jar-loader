package de.sranko_informatik.si_jar_loader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JarLoader extends URLClassLoader{

    public JarLoader(String publicKey) throws KeyException {

        super(new URL[0]);

        if (!isPublicKeyValid(publicKey)) {
            throw new KeyException("Bad public key.");
        }
    }

    private boolean isPublicKeyValid(String publicKey) {

        try {

            System.out.println("Public key wird geprüft...");

            // get configuration
            ConfigHandler handler;

            String configPath = System.getProperty("application.config");
            if (configPath == null) {
                System.out.println("Default-Konfiguration wird verwendet.");
                handler = ConfigHandler.getInstance();
            } else {
                System.out.println(String.format("Konfiguration %s wird verwendet.", configPath));
                handler = ConfigHandler.getInstance(Paths.get(configPath));
            }

            Configuration config = handler.getConfig();

            // public from pgm
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");

            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            RSAPublicKey publicKeyFromPgm = (RSAPublicKey) kf.generatePublic(keySpecX509);

            // public key from keystore
            FileInputStream is = null;
            is = new FileInputStream(config.getSecurity().getKeystore().getName());

            KeyStore keystore = KeyStore.getInstance(config.getSecurity().getKeystore().getType());
            keystore.load(is, config.getSecurity().getKeystore().getPassword().toCharArray());

            PublicKey publicKeyFromKeyStore = keystore.getCertificate(config.getSecurity().getKeystore().getAlias()).getPublicKey();

            if (publicKeyFromPgm.equals(publicKeyFromKeyStore)) {
                return true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addFile(String path) throws MalformedURLException {
        // construct the jar url path
        String urlPath = "jar:file:" + path + "!/";

        // invoke the base method
        addURL(new URL(urlPath));
    }

    public void addFile(String paths[]) throws MalformedURLException {
        if (paths != null)
            for (int i = 0; i < paths.length; i++)
                addFile(paths[i]);
    }
}
