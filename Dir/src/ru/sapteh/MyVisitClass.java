package ru.sapteh;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class MyVisitClass extends SimpleFileVisitor<Path> {


        private final List<File> fileList = new ArrayList<>();

        public List<File> getFileList(){
            return fileList;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            fileList.add(dir.toFile());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            fileList.add(file.toFile());
            return FileVisitResult.CONTINUE;
        }
    }
