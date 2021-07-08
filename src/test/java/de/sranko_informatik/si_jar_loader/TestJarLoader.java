package de.sranko_informatik.si_jar_loader;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJarLoader {

    @Test
    public void testLoadMySQL() {

        try {

            // create instance
            JarLoader loader = new JarLoader("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxMIGO7GLsCUsLez8OEEb\n" +
                    "G92OiLnuwjDEoyU0nqBFsNmE3J/G77F/5yS34u/Lq4VLquGifw+dmNoWe3QLFFQP\n" +
                    "ZkdMSpBMJyHcNAAzAwuEvyzFjRgWM6PRlXmgXEEetVa62TOvsQdBP2NfR+JXwRs3\n" +
                    "xSrj75JCH42hACRsUU+2C1tI8hVvZmUJmIEl6oILSz7jZPK9OP5Rfs2CwS3NjdRL\n" +
                    "RwuTA1/B1cfbDZhcVxDAQEzuG39D+JFUEx+C1puGybY8IR9ad4Qzl2P21NiTBmdt\n" +
                    "2QqsDemlKc/7u0Ge4GmgVYIwXBhpdv4SI0lKDgwV1aMVzQlON3t7iU1HIUqauSyC\n" +
                    "SQIDAQAB");

            // widows
            String jarPath = "mysql-connector-java-8.0.25.jar";

            loader.addFile(jarPath);

            loader.loadClass("com.mysql.jdbc.Driver");


        } catch (Exception ex) {
            ex.printStackTrace();
            assertEquals(1, 0);
        }

        assertEquals(1, 1);
    }

}
