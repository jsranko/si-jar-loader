package de.sranko_informatik.si_jar_loader;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JarLoader extends URLClassLoader{

    public JarLoader(URL[] urls, String publicKey) throws KeyException {

        super(urls);

        if (!isPublicKeyValid(publicKey)) {
            throw new KeyException("Bad public key.");
        }
    }

    private boolean isPublicKeyValid(String publicKey) {

        try {

            // get configuration
            ConfigHandler handler;

            String configPath = System.getProperty("application.config");
            if (configPath == null) {
                handler = ConfigHandler.getInstance();
            } else {
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

    public static void main(String[] args) throws SecurityException, IllegalArgumentException, IOException, IllegalAccessException {

        String classToLoad = "com.mysql.jdbc.Driver";
        System.out.println(new File("").getAbsolutePath());

        try {
            // initialize with empty path
            URL urls[] = {};

            // create instance
            JarLoader loader = new JarLoader(urls, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxMIGO7GLsCUsLez8OEEb\n" +
                    "G92OiLnuwjDEoyU0nqBFsNmE3J/G77F/5yS34u/Lq4VLquGifw+dmNoWe3QLFFQP\n" +
                    "ZkdMSpBMJyHcNAAzAwuEvyzFjRgWM6PRlXmgXEEetVa62TOvsQdBP2NfR+JXwRs3\n" +
                    "xSrj75JCH42hACRsUU+2C1tI8hVvZmUJmIEl6oILSz7jZPK9OP5Rfs2CwS3NjdRL\n" +
                    "RwuTA1/B1cfbDZhcVxDAQEzuG39D+JFUEx+C1puGybY8IR9ad4Qzl2P21NiTBmdt\n" +
                    "2QqsDemlKc/7u0Ge4GmgVYIwXBhpdv4SI0lKDgwV1aMVzQlON3t7iU1HIUqauSyC\n" +
                    "SQIDAQAB");

            // widows
            String jarPath = "mysql-connector-java-8.0.25.jar";

            loader.addFile(jarPath);
            System.out.println("Second attempt...");

            // load the class
            loader.loadClass(classToLoad);
            System.out.println("Success");
        } catch (Exception ex) {
            System.out.println("Failed to load : " + ex.getMessage());
            ex.printStackTrace();
        }
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
