package ru.sapteh;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input zip or unzip: ");
        String archive = reader.readLine();



        if (archive.equals("zip")) {
            System.out.println("Input name: ");
            String path = reader.readLine();
            Path sourcePath = Paths.get(path);

            while (!Files.isDirectory(sourcePath)) {
                System.out.println("NO directory");
                System.out.println("Input true name directory: ");
                sourcePath = Paths.get(reader.readLine());
            }

            //Проход по дереву файлов
            MyVisitClass myVisitClass = new MyVisitClass();
            Files.walkFileTree(sourcePath, myVisitClass);

            //Создание архива
            FileOutputStream zipArchive = new FileOutputStream(sourcePath + ".zip");
            ZipOutputStream zip = new ZipOutputStream(zipArchive);
            ZipEntry ze;

            // перенос папок и файлов в архив и вывод размеров
            for (File filePath : myVisitClass.getFileList()) {
                File file = new File(Pruning(path, filePath.getPath()));
                if (filePath.isDirectory()) {
                    ze = new ZipEntry(file + "/");
                    zip.putNextEntry(ze);
                    zip.closeEntry();
                } else if (filePath.isFile()) {
                    ze = new ZipEntry(file.toString());
                    zip.putNextEntry(ze);
                    Files.copy(filePath.toPath(), zip);
                    zip.closeEntry();
                    System.out.printf(" %-35s %-5d (%-4d) %.0f%%\n", file.getPath(), ze.getSize(), ze.getCompressedSize(),
                            (100 - ((double) ze.getCompressedSize() / ze.getSize() * 100)));
                }

            }
            zip.close();
        }
        if (archive.equals("unzip")){
        //разархивация архива в папку
        System.out.println("Input name archive: ");
        String zipArch = reader.readLine();
        Path zipPath = Paths.get(zipArch);

        System.out.println("Input name Directory for unZiped: ");
        String  unzipet = reader.readLine();
        Path unzipedPath = Paths.get(unzipet);
        while (!unzipedPath.toFile().exists()) {
            System.out.println("Directory name not found: ");
            System.out.println("Input true name directory: ");
            unzipedPath = Paths.get(reader.readLine());
        }
            ZipFile zipFile = new ZipFile(zipPath.toFile());
            List<ZipEntry> zipEntryList = new ArrayList<>();
            for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
                zipEntryList.add(entries.nextElement());
                //System.out.println(zipEntryList);
            }
            zipEntryList.add(0, new ZipEntry("myDir/"));
            for (ZipEntry zipEntry : zipEntryList) {
                Path absolutePath = concatPath(unzipedPath, Paths.get(zipEntry.toString()));
                if (zipEntry.isDirectory() && Files.notExists(absolutePath)) {
                    Files.createDirectory(absolutePath);
                    System.out.println("Directory created " + absolutePath);
                } else if (Files.notExists(absolutePath)) {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    Files.copy(inputStream, absolutePath);
                    System.out.printf("%s (%d) %d\n", absolutePath,zipEntry.getCompressedSize(),zipEntry.getSize());
                }
            }

        }
    }


    public static Path concatPath(Path unziped,Path pathToZip){
        return unziped.resolve(pathToZip);
   }
    // Метод обрезки пути
    public static String Pruning(String path, String filePathPath) {
        Path sourcePath = Paths.get(path);
        Path fullPath = Paths.get(filePathPath);
        int stringSize = sourcePath.getParent().toString().length() + 1;
        String subPath = fullPath.toString().substring(stringSize);
        return subPath;
    }
}


//c:/test/myDir
//c:/test/myDir.zip
//C:/Users/student/Desktop/Новая папка
