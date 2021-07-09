package de.sranko_informatik.si_jar_loader;

import javax.management.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class JarLoaderNew {

    public JarLoaderNew(String publicKey) throws KeyException, InvocationTargetException, IllegalAccessException {

        if (!isPublicKeyValid(publicKey)) {
            throw new KeyException("Bad public key.");
        }
    }

    private boolean isPublicKeyValid(String publicKey) {

        try {

            //System.out.println("Public key wird geprüft...");

            // get configuration
            ConfigHandler handler;

            String configPath = System.getProperty("si.jarloader.config");
            if (configPath == null) {
                //System.out.println("Default-Konfiguration wird verwendet.");
                handler = ConfigHandler.getInstance();
            } else {
                //System.out.println(String.format("Konfiguration %s wird verwendet.", configPath));
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

    public void addFile(String path) throws IntrospectionException, MalformedURLException {
        File jarFile = new File(path);
        if (jarFile.exists()) {
            System.out.println("File existiert");
        }

        ClassLoader classLoader = addURLToSystemClassLoader(jarFile.toURI().toURL());

        //System.out.println("(afer) Class Loader : "
        //        + Arrays.toString(((URLClassLoader) classLoader).getURLs()));
    }

    public static ClassLoader addURLToSystemClassLoader(URL url) throws IntrospectionException {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> classLoaderClass = URLClassLoader.class;

        //System.out.println("(before) Class Loader : "
        //        + Arrays.toString(systemClassLoader.getURLs()));

        try {
            Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(systemClassLoader, new Object[]{url});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IntrospectionException("Error when adding url to system ClassLoader ");
        }

        return systemClassLoader;
    }
}
