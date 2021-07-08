package de.sranko_informatik.si_jar_loader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.security.KeyException;
import java.sql.*;

public class Application {

    public static void main(String[] args) throws KeyException, InvocationTargetException, IllegalAccessException, MalformedURLException, ClassNotFoundException, SQLException {
        Connection connect = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // create instance
        JarLoaderNew loader = new JarLoaderNew("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxMIGO7GLsCUsLez8OEEb\n" +
                "G92OiLnuwjDEoyU0nqBFsNmE3J/G77F/5yS34u/Lq4VLquGifw+dmNoWe3QLFFQP\n" +
                "ZkdMSpBMJyHcNAAzAwuEvyzFjRgWM6PRlXmgXEEetVa62TOvsQdBP2NfR+JXwRs3\n" +
                "xSrj75JCH42hACRsUU+2C1tI8hVvZmUJmIEl6oILSz7jZPK9OP5Rfs2CwS3NjdRL\n" +
                "RwuTA1/B1cfbDZhcVxDAQEzuG39D+JFUEx+C1puGybY8IR9ad4Qzl2P21NiTBmdt\n" +
                "2QqsDemlKc/7u0Ge4GmgVYIwXBhpdv4SI0lKDgwV1aMVzQlON3t7iU1HIUqauSyC\n" +
                "SQIDAQAB");

        // widows
        String jarPath = "./mysql-connector-java-8.0.25.jar";
        loader.addFile(jarPath);

        // load the class
        Class.forName("com.mysql.cj.jdbc.Driver", true, Thread.currentThread().getContextClassLoader());

        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?"
                + "user=sqluser&password=sqluserpw");
    }
}
