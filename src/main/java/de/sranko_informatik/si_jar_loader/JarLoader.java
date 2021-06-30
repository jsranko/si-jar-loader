package de.sranko_informatik.si_jar_loader;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader extends URLClassLoader{

    public JarLoader(URL[] urls) {
        super(urls);
    }

    public static void main(String[] args) throws SecurityException, IllegalArgumentException {

        String classToLoad = "com.mysql.jdbc.Driver";
        System.out.println(new File("").getAbsolutePath());

        try {
            // initialize with empty path
            URL urls[] = {};

            // create instance
            JarLoader loader = new JarLoader(urls);

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
