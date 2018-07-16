package <%= group %>.util;

import net.minecraft.launchwrapper.Launch;
import <%= group %>.<%= baseClass %>;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
Use like this:

ResourceLoader loader = new ResourceLoader(new File(ConfigurationHandler.configDir, "types.d"), "assets/<%= modid %>/config/types.d/");
for(Map.Entry<String, InputStream> entry : loader.getResources().entrySet()) {
        String filename = entry.getKey();
        InputStream is = entry.getValue();
}
*/

public class ResourceLoader {
    public static final boolean DEVELOPMENT = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    private final String runtimePathName;
    private final String assetPathName;

    public ResourceLoader(File runtimeFile, String assetPathName) {
        this.assetPathName = assetPathName;
        this.runtimePathName = runtimeFile.getAbsolutePath();
    }

    public ResourceLoader(String runtimePathName, String assetPathName) {
        this.runtimePathName = runtimePathName;
        this.assetPathName = assetPathName;
    }

    public Map<String, InputStream> getResources() {
        Map<String, InputStream> result = new HashMap<>();

        // First grab InputStreams for all files in the config directory
        File runtimePath = new File(runtimePathName);
        if(runtimePath.exists() && runtimePath.isDirectory()) {
            for(File file : runtimePath.listFiles()) {
                try {
                    result.put(file.getName(), new FileInputStream(file));
                    Logz.debug(" - Loading file '%s' from config folder", file.getName());
                } catch (FileNotFoundException e) {
                }
            }
        }

        // Then search through the jar for any files we might have skipped up until now
        if(DEVELOPMENT) {
            File assetPath = new File(<%= baseClass %>.class.getResource("/" + assetPathName).getFile());
            if(assetPath.exists() && assetPath.isDirectory()) {
                for(File file : assetPath.listFiles()) {
                    if(result.keySet().contains(file.getName())) {
                        continue;
                    }
                    try {
                        result.put(file.getName(), new FileInputStream(file));
                        Logz.debug(" - Loading file '%s' from development assets folder", file.getName());
                    } catch (FileNotFoundException e) {
                    }
                }
            }
        } else {
            // We need to get an InputStream from within our jar file
            URL srcUrl = <%= baseClass %>.class.getResource("/" + assetPathName);
            if(srcUrl == null || !srcUrl.getProtocol().equals("jar")) {
                Logz.error("Error while reading files from jar: unable to get Resource URL for '%s'.", assetPathName);
                return null;
            }

            try {
                JarURLConnection jarURLConnection = (JarURLConnection) srcUrl.openConnection();
                ZipFile zipFile = jarURLConnection.getJarFile();
                Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

                while(zipEntries.hasMoreElements()) {
                    ZipEntry zipEntry = zipEntries.nextElement();
                    String zipName = zipEntry.getName();

                    if(!zipName.startsWith(assetPathName)) {
                        continue;
                    }

                    String filename = zipName.substring(assetPathName.length());

                    if(result.keySet().contains(filename) || filename.length() == 0) {
                        continue;
                    }

                    result.put(filename, zipFile.getInputStream(zipEntry));
                    Logz.debug(" - Loading file '%s' from jar", filename);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
