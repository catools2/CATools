package org.catools.common.io;

import org.apache.commons.lang3.tuple.Pair;
import org.catools.common.collections.CHashMap;
import org.catools.common.collections.CList;
import org.catools.common.collections.interfaces.CMap;
import org.catools.common.exception.CResourceNotFoundException;
import org.catools.common.utils.CInputStreamUtil;
import org.catools.common.utils.CStringUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.catools.common.utils.CSystemUtil.getPlatform;

public class CResource {
  private final String resourceFullName;
  private final Class clazz;

  public CResource(String resourceFullName, @Nullable Class clazz) {
    this.resourceFullName = resourceFullName;
    this.clazz = clazz;
  }

  public boolean exists() {
    try {
      getInputStream();
      return true;
    } catch (Throwable t2) {
      return false;
    }
  }

  public InputStream getInputStream() {
    AtomicReference<InputStream> inputStream = new AtomicReference<>();
    perform(resourceFullName, clazz, (resourceName, is) -> inputStream.set(is));
    return inputStream.get();
  }

  public CList<String> readLines() {
    return new CList<>(getString().split("\n"));
  }

  public CList<String> readLines(Predicate<String> predicate) {
    return readLines().getAll(predicate);
  }

  public String getResourceFullName() {
    return resourceFullName;
  }

  public String getResourceName() {
    if (resourceFullName.contains("\\")) {
      return CStringUtil.substringAfterLast(resourceFullName.replaceAll("\\\\", "/"), "/");
    }
    if (resourceFullName.contains("/")) {
      return CStringUtil.substringAfterLast(resourceFullName, "/");
    }
    return resourceFullName;
  }

  public String getString() {
    return new String(getByteArray());
  }

  public byte[] getByteArray() {
    try {
      return CInputStreamUtil.toByteArray(getInputStream());
    } catch (Throwable t) {
      throw new CResourceNotFoundException("Failed to read resource " + resourceFullName, t);
    }
  }

  public CList<CFile> saveToFolder(File targetFolder) {
    CList<CFile> files = new CList<>();
    perform(
        resourceFullName,
        clazz,
        (resourceName, inputStream) -> {
          CFile destFile = new CFile(targetFolder, resourceName);
          destFile.getParentFile().mkdirs();
          files.add(destFile.write(inputStream));
        });
    return files;
  }

  private static void perform(
      String resourceName, Class clazz, BiConsumer<String, InputStream> consumer) {
    if (CStringUtil.isBlank(resourceName)) {
      throw new CResourceNotFoundException("Resource name cannot be null or empty!");
    }

    Pair<Class, ClassLoader> pair = getClassLoader(resourceName, clazz);
    if (pair == null) {
      throw new CResourceNotFoundException(resourceName + " resource not found!");
    }

    clazz = pair.getKey();
    try {
      URI uri = pair.getValue().getResource(resourceName).toURI();
      if (uri.getScheme().contains("jar")) {
        URL jar = clazz.getProtectionDomain().getCodeSource().getLocation();
        Path path =
            Paths.get(
                CStringUtil.substringAfter(
                    jar.toString(), getPlatform().isWindows() ? "file:/" : "file:"));
        try (FileSystem fs = FileSystems.newFileSystem(path)) {
          Path resourcePath = fs.getPath(resourceName);
          if (Files.isDirectory(resourcePath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourcePath)) {
              for (Path p : directoryStream) {
                InputStream is = clazz.getResourceAsStream(p.toString());
                if (is == null) {
                  is = getResourceAsStream(p.toString(), clazz);
                }
                if (is == null) {
                  throw new CResourceNotFoundException("Failed to read " + p);
                }
                consumer.accept(Path.of(resourceName, p.getFileName().toString()).toString(), is);
              }
            }
          } else {
            try (JarFile jarFile = new JarFile(clazz.getProtectionDomain().getCodeSource().getLocation().getFile())) {
              JarEntry jarEntry = jarFile.getJarEntry(resourceName);
              consumer.accept(jarEntry.getName(), jarFile.getInputStream(jarFile.getEntry(resourceName)));
            }
          }
        }
      } else {
        Path path = Paths.get(uri);
        if (Files.isDirectory(path)) {
          try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path p : directoryStream) {
              consumer.accept(
                  Path.of(resourceName, p.getFileName().toString()).toString(),
                  new CFile(p.toFile()).getInputStream());
            }
          }
        } else {
          consumer.accept(path.getFileName().toString(), new CFile(uri).getInputStream());
        }
      }
    } catch (Throwable t) {
      throw new CResourceNotFoundException(
          "Unable to read " + resourceName + " resource or read it properly.", t);
    }
  }

  public static Pair<Class, ClassLoader> getClassLoader(String resource, Class clazz) {
    final CMap<Class, ClassLoader> classLoaders = getClassClassLoaders(clazz);
    for (Class c : classLoaders.keySet()) {
      if (c != null
          && classLoaders.get(c) != null
          && classLoaders.get(c).getResourceAsStream(resource) != null) {
        return Pair.of(clazz, classLoaders.get(c));
      }
    }
    return null;
  }

  public static URL getResource(String resource, Class clazz) {
    final CMap<Class, ClassLoader> classLoaders = getClassClassLoaders(clazz);
    for (Class c : classLoaders.keySet()) {
      if (c != null
          && classLoaders.get(c) != null
          && classLoaders.get(c).getResourceAsStream(resource) != null) {
        return classLoaders.get(c).getResource(resource);
      }
    }
    return null;
  }

  public static InputStream getResourceAsStream(String resource, Class clazz) {
    final CMap<Class, ClassLoader> classLoaders = getClassClassLoaders(clazz);
    for (Class c : classLoaders.keySet()) {
      if (c != null
          && classLoaders.get(c) != null
          && classLoaders.get(c).getResourceAsStream(resource) != null) {
        return classLoaders.get(c).getResourceAsStream(resource);
      }
    }
    return null;
  }

  private static CMap<Class, ClassLoader> getClassClassLoaders(Class clazz) {
    final CMap<Class, ClassLoader> classLoaders = new CHashMap<>();
    if (clazz != null) {
      classLoaders.put(clazz, clazz.getClassLoader());
      classLoaders.put(clazz.getClass(), clazz.getClass().getClassLoader());
    }
    classLoaders.put(CResource.class, CResource.class.getClassLoader());
    return classLoaders;
  }
}
