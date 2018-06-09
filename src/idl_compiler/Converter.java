package idl_compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Converter {


    public static void createPackage(Path parsedFileLocation, Path packageLocation) {
        String adjustedPackageLocation = packageLocation.toString()+File.separator;
        File fileToRead = new File(parsedFileLocation.toString());
        System.out.println("parsedFileLocation: "+ parsedFileLocation.toString()+"\n"+"packageLocation: " + packageLocation.toString());
        try {
            Scanner scanner = new Scanner(fileToRead);
            System.out.println("Reading file: "+fileToRead.getAbsolutePath());
            String packageLine = scanner.nextLine();
            String packageName = packageLine.replace("package", "").replace(";", "").trim();
            System.out.println("Creating package at: " + adjustedPackageLocation);

            new File(adjustedPackageLocation).mkdir();


            while (scanner.hasNextLine()) {
                String classLine = scanner.nextLine().trim();
                String className = classLine.replace("public abstract class ","").replace("{", "").trim();
                String nextLine;


                System.out.println("Creating class " + className+ " at: " + adjustedPackageLocation + packageName + File.separator+className+".java");

                File javaFile = new File(adjustedPackageLocation+packageName+File.separator+className+".java");
                FileWriter fileWriter = new FileWriter(javaFile);
                System.out.println("Path: " + javaFile.getAbsolutePath());

                System.out.println(packageLine);
                fileWriter.append(packageLine+"\n");
                System.out.println(classLine);
                fileWriter.append(classLine+"\n");
                while (!(nextLine = scanner.nextLine()).equals("}")) {
                    System.out.println(nextLine);
                    fileWriter.append(nextLine+"\n");
                }
                /*public static _CalculatorImplBase narrowCast(
                        Object rawObjectRef) { ... }*/
                System.out.println("public static "+className+" narrowCast(Object rawObjectRef) {return new "+className+"();}\n");
                fileWriter.append("public static "+className+" narrowCast(Object rawObjectRef) {return new "+className+"();}\n");

//                System.out.println("}");
//                fileWriter.append("}");
                fileWriter.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }
}