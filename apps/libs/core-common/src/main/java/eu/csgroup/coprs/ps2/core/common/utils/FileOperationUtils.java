package eu.csgroup.coprs.ps2.core.common.utils;

import eu.csgroup.coprs.ps2.core.common.exception.FileOperationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


@Slf4j
public final class FileOperationUtils {

    public static void deleteFolderContent(String folder) {

        log.info("Deleting content for folder: {}", folder);

        try (final Stream<Path> list = Files.list(Paths.get(folder))) {
            list.forEach(path -> {
                try {
                    if (path.toFile().isDirectory()) {
                        FileSystemUtils.deleteRecursively(path);
                    } else {
                        Files.delete(path);
                    }
                } catch (IOException e) {
                    throw new FileOperationException("Unable to delete file: " + path, e);
                }
            });
        } catch (Exception e) {
            throw new FileOperationException("Unable to clean folder: " + folder, e);
        }
    }

    public static void deleteFiles(String folder, String pattern) {

        log.info("Deleting files in folder: '{}' matching pattern: '{}'", folder, pattern);

        try (final Stream<Path> list = Files.list(Paths.get(folder))) {
            list
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .map(File::toPath)
                    .filter(path -> path.getFileName().toString().matches(pattern))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new FileOperationException("Unable to delete file: " + path, e);
                        }
                    });
        } catch (Exception e) {
            throw new FileOperationException("Unable to clean folder: " + folder, e);
        }
    }

    public static void deleteFolders(Set<String> folderSet) {
        folderSet.forEach(folder -> {
            log.info("Deleting folder: {}", folder);
            try {
                FileSystemUtils.deleteRecursively(Paths.get(folder));
            } catch (Exception e) {
                throw new FileOperationException("Unable to delete folder: " + folder, e);
            }
        });
    }

    public static void deleteFolderIfEmpty(String folder) {
        log.info("Deleting folder if empty: {}", folder);
        try {
            final Path folderPath = Paths.get(folder);
            if (FileUtils.isEmptyDirectory(folderPath.toFile())) {
                FileSystemUtils.deleteRecursively(folderPath);
            }
        } catch (Exception e) {
            throw new FileOperationException("Unable to delete folder: " + folder, e);
        }
    }

    public static void createFolders(Set<String> folderSet) {
        folderSet.forEach(folder -> {
            log.info("Creating folder: {}", folder);
            try {
                Files.createDirectories(Paths.get(folder));
            } catch (Exception e) {
                throw new FileOperationException("Unable to create folder: " + folder, e);
            }
        });
    }

    public static List<Path> findFiles(Path root, String regex) {
        try (Stream<Path> stream = Files.list(root)) {
            return stream.filter(path -> !Files.isDirectory(path))
                    .filter(path -> path.getFileName().toString().matches(regex))
                    .toList();
        } catch (IOException e) {
            throw new FileOperationException("Unable to list files in: " + root, e);
        }
    }

    public static List<Path> findFilesInTree(Path root, String regex) {
        try (Stream<Path> stream = Files.walk(root)) {
            return stream.filter(path -> !Files.isDirectory(path))
                    .filter(path -> path.getFileName().toString().matches(regex))
                    .toList();
        } catch (IOException e) {
            throw new FileOperationException("Unable to list files in: " + root, e);
        }
    }

    public static List<Path> findFolders(Path root, String regex) {
        try (Stream<Path> stream = Files.list(root)) {
            return stream.filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().matches(regex))
                    .toList();
        } catch (IOException e) {
            throw new FileOperationException("Unable to list folder: " + root, e);
        }
    }

    public static List<Path> findFoldersInTree(Path root, String regex) {
        try (Stream<Path> stream = Files.walk(root)) {
            return stream.filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().matches(regex))
                    .toList();
        } catch (IOException e) {
            throw new FileOperationException("Unable to list folder: " + root, e);
        }
    }

    public static long countFiles(Path folder) {
        try (Stream<Path> stream = Files.list(folder)) {
            return stream.count();
        } catch (IOException e) {
            throw new FileOperationException("Unable to access folder: " + folder, e);
        }
    }

    public static void move(Path sourcePath, Path destinationPath) {
        try {
            Files.move(sourcePath, destinationPath);
        } catch (IOException e) {
            throw new FileOperationException("Unable to move file", e);
        }
    }

    private FileOperationUtils() {
    }

}
