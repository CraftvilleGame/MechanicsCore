package me.deecaad.core.compatibility;

import com.cjcrafter.foliascheduler.util.MinecraftVersions;
import org.jetbrains.annotations.Nullable;

public class CompatibilitySetup {

    /**
     * Example return values:
     * 
     * <pre>
     * v1_8_R2
     * v1_11_R1
     * v1_13_R3
     * </pre>
     *
     * @return the server version as string
     */
    public String getVersionAsString() {
        MinecraftVersions.Version current = MinecraftVersions.getCurrent();

        // FoliaScheduler only knows the drops it was built against, so newer
        // year-based drops (26.2, 26.3, ...) parse with protocol -1. From 26.1
        // onward the protocol is always R1, so map unknown drops to that.
        if (current.getMajor() >= 26 && current.getProtocol() < 0)
            return "v" + current.getMajor() + "_" + current.getMinor() + "_R1";

        return current.toProtocolString();
    }

    /**
     * @param interfaceClazz the compatibility interface type
     * @param directory the directory in code where compatibility should exist
     * @return the compatible version from given directory
     */
    @Nullable public <T> T getCompatibleVersion(Class<T> interfaceClazz, String directory) {
        String version = getVersionAsString();
        Class<?> compatibilityClass;
        try {
            compatibilityClass = Class.forName(directory + "." + version, false, interfaceClazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        try {
            return interfaceClazz.cast(compatibilityClass.getConstructor().newInstance());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Found compatibility class " + compatibilityClass.getName() + " but failed to instantiate it", e);
        }
    }
}