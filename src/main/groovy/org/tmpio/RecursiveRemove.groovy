package org.tmpio

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

import static java.nio.file.FileVisitResult.*
import static java.nio.file.Files.*

class RecursiveRemove extends SimpleFileVisitor<Path> {

    private static final RecursiveRemove instance = new RecursiveRemove()

    static removeRecursively(Path dir) {
        if(!isDirectory(dir))
            throw IllegalArgumentException(sprintf("cannot remove %s recursively", dir.toString()))
        instance.root = dir
        walkFileTree(dir, instance)
    }

    private Path root

    private RecursiveRemove() {}

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file)
        return CONTINUE
    }
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        Files.delete(file)
        return CONTINUE
    }
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if(!dir.equals(root))
        Files.delete(dir)
        return CONTINUE
    }
}
