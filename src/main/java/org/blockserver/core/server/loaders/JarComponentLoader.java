/*
 * This file is part of BlockServer.
 *
 * BlockServer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlockServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BlockServer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.blockserver.core.server.loaders;


import org.blockserver.core.server.Server;
import org.blockserver.core.server.module.ComponentLoader;
import org.blockserver.core.server.module.JarComponent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * JarComponent Loader that can load modules from JARs
 *
 * @author BlockServer Team
 * @see ComponentLoader
 */
public class JarComponentLoader implements ComponentLoader {
    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    public void loadComponents(Server server) {
        File moduleFolder = new File("Components");
        if (moduleFolder.mkdirs())
            System.err.println("Could not find 'Components' folder, created 'Components' folder!");
        File[] files = moduleFolder.listFiles();
        if (files == null || files.length <= 0)
            return;

        for (File file : files) {
            System.err.println(file.getName());
            if (file.getName().endsWith(".jar")) {
                try {
                    JarFile jar = new JarFile(file);
                    Properties jarProp = getJarProperties(jar);
                    URLClassLoader loader = new URLClassLoader(new URL[]{file.toURL()});
                    String className = jarProp.getProperty("mainClass", "default");
                    try {
                        Class clazz = loader.loadClass(className);
                        try {
                            JarComponent component = (JarComponent) clazz.getConstructor(Server.class).newInstance(server);
                            server.addComponent(component);
                            System.out.println("[JarComponentLoader]: Loaded " + file.getName());
                        } catch (ClassCastException e) {
                            System.err.println("[JarComponentLoader]: Failed to load main class for " + file.getName() + ": main class does not extend JarComponent.");
                        } catch (NoSuchMethodException | InvocationTargetException e) {
                            System.err.println("[JarComponentLoader]: Failed to load main class for " + file.getName() + ": " + e.getClass().getSimpleName() + " -> " + e.getMessage());
                        }
                    } catch (ClassNotFoundException e) {
                        if (className.equals("default")) {
                            System.err.println("[JarComponentLoader]: Failed to load main class for " + file.getName() + ": main class not specified.");
                        }
                        System.err.println("[JarComponentLoader]: Failed to load main class for " + file.getName() + ": ClassNotFoundException -> " + e.getMessage());
                    } catch (InstantiationException | IllegalAccessException e) {
                        System.err.println("[JarComponentLoader]: Failed to load main class for " + file.getName() + ": " + e.getClass().getSimpleName() + " -> " + e.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Properties getJarProperties(JarFile jar) throws IOException {
        InputStream stream = jar.getInputStream(jar.getJarEntry("module.properties"));
        Properties p = new Properties();
        p.load(stream);
        return p;
    }
}
