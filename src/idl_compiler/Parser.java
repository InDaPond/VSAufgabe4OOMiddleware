package idl_compiler;

import idl_compiler.IDLCompiler.MethodData;
import idl_compiler.IDLCompiler.SupportedDataTypes;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parser main class.
 *
 * @author (c) H. Schulz, 2016
 * This programme is provided 'As-is', without any guarantee of any kind, implied or otherwise and is wholly
 * unsupported.
 * You may use and modify it as long as you state the above copyright.
 */
public class Parser {
    public static final String BEGIN = "{";
    public static final String BEGIN_REGEX = "\\{";
    public static final String END = "};";
    public static final String MODULE = "module";
    public static final String CLASS = "class";
    public static final String PARENTHESIS_OPEN = "\\(";
    public static final String PARENTHESIS_CLOSE = "\\)";
    public static final String PARENTHESES = "[(|)]";
    public static final String PARAM_SEPARATOR = ",";

    /**
     * File reader counting lines.
     *
     * @author (c) H. Schulz, 2016    This programme is provided 'As-is', without any guarantee of any kind, implied
     * or otherwise and is wholly unsupported.  You may use and modify it as long as you state the above copyright.
     */
    private static class IDLfileReader extends BufferedReader {
        /**
         * @uml.property name="lineNo"
         */
        private int lineNo;

        public IDLfileReader(Reader in) {
            super(in);
            lineNo = 0;
        }

        public String readLine() throws IOException {
            lineNo++;
            return super.readLine();
        }

        /**
         * @return
         * @uml.property name="lineNo"
         */
        public int getLineNo() {
            return lineNo;
        }
    }

    /**
     * For printing compilation errors
     *
     * @param lineNo
     * @param text
     */
    private static void printError(int lineNo, String text) {
        System.err.println("Line " + lineNo + ": " + text);
    }

    /**
     * Parse IDL Module in given file.
     *
     * @param in file reader
     * @return
     * @throws IOException
     */
    private static IDLmodule parseModule(IDLfileReader in) throws IOException {
        System.out.println("===parseModule START===");
        IDLclass newClass;

        String line = in.readLine();
        String tokens[] = (line.split(BEGIN_REGEX)[0]).trim().split(" ");

        if (tokens != null && tokens.length > 1 && tokens[0].equals(MODULE) && tokens[1] != null && tokens[1].length
                () > 0) {
            IDLmodule currentModule = new IDLmodule(tokens[1]);
            do {
                // parse containing classes
                newClass = parseClass(in, currentModule.getModuleName());
                if (newClass != null) currentModule.addClass(newClass);

                // try to read next module
                tokens = (line.split(BEGIN_REGEX)[0]).trim().split(" ");
            } while (newClass != null);

            System.out.println("===parseModule DONE===");
            return currentModule;


        } else {
            printError(in.getLineNo(), "Error parsing module. '" + line + "'");
            return null;
        }
    }

    /**
     * Parse (next) class in a file/module.
     *
     * @param in                file reader
     * @param currentModuleName name of the module currently being parsed.
     * @return the class parsed or null if there is no class left in the file
     * @throws IOException
     */
    private static IDLclass parseClass(IDLfileReader in, String currentModuleName) throws IOException {
        System.out.println("===parseClass START===");
        ArrayList<MethodData> methodList = new ArrayList<MethodData>();

        String line = in.readLine();
        if (line != null) {
            String tokens[] = (line.split(BEGIN_REGEX)[0]).trim().split(" ");
            if (tokens != null && tokens.length > 1 && tokens[0].equals(CLASS)
                    && tokens[1] != null && tokens[1].length() > 0) {
                // name of this class
                String className = tokens[1];

                // read methods
                line = in.readLine();
                while (line != null && !line.contains(END)) {
                    String[] tokens2 = line.trim().split(PARENTHESES);

                    String[] tokens3 = tokens2[0].split(" ");
                    String rTypeString = tokens3[0]; // return value
                    String methodName = tokens3[1]; // method name

                    SupportedDataTypes paramTypes[] = parseParams(in.getLineNo(),
                            tokens2[1]);
                    String paramNames[] = parseParamNames(in.getLineNo(), tokens2[1]);

                    // into data container
                    methodList
                            .add(new MethodData(methodName, IDLCompiler.getSupportedTypeForKeyword(rTypeString),
                                    paramTypes, paramNames));
                    line = in.readLine();
                }

                // read class end
                if (line == null || !line.contains(END)) {
                    printError(in.getLineNo(), "Error parsing class "
                            + className + ": no end mark '" + line + "'");
                }

                // method data -> array
                MethodData methodArray[] = new MethodData[methodList.size()];

                //return IDL class
                System.out.println("===parseClass DONE===");
                return new IDLclass(className, currentModuleName,
                        methodList.toArray(methodArray));
            } else {
                if (line.contains(END)) {
                    return null;
                } else {
                    printError(in.getLineNo(), "Error parsing class.'" + line
                            + "'");
                    return null;
                }
            }
        } else {
            printError(in.getLineNo(), "Attempt to read beyond end of file.");
            return null;
        }
    }

    /**
     * Evaluate parameter list. (No reading done here!)
     *
     * @param lineNo
     * @param paramList
     * @return
     */
    private static SupportedDataTypes[] parseParams(int lineNo, String paramList) {
        System.out.println("===parseParams START===");
        if (paramList != null && paramList.length() > 0) {
            String[] paramEntries = paramList.trim().split(PARAM_SEPARATOR);

            // param data container
            SupportedDataTypes paramTypes[] = new SupportedDataTypes[paramEntries.length];

            for (int i = 0; i < paramEntries.length; i++) {
                String[] typeAndParamName = paramEntries[i].trim().split(" ");

                // 0: type, 1: name
                paramTypes[i] = IDLCompiler.getSupportedTypeForKeyword(typeAndParamName[0]);
                if (paramTypes[i] == null) {
                    printError(lineNo, "Error parsing param list");
                    return null;
                }
            }
            System.out.println("===parseParams DONE===");
            return paramTypes;
        } else {
            return new SupportedDataTypes[0];  // empty list
        }
    }

    private static String[] parseParamNames(int lineNo, String paramList) {
        System.out.println("===parseParamNames START===");
        if (paramList != null && paramList.length() > 0) {
            String[] paramEntries = paramList.trim().split(PARAM_SEPARATOR);

            // param data container
            String paramNames[] = new String[paramEntries.length];

            for (int i = 0; i < paramEntries.length; i++) {
                String[] typeAndParamName = paramEntries[i].trim().split(" ");

                // 0: type, 1: name
                System.out.println("paramNames: " + Arrays.toString(paramNames));
                paramNames[i] = typeAndParamName[1];
                if (paramNames[i] == null) {
                    printError(lineNo, "Error parsing param list");
                    return null;
                }
            }
            System.out.println("paramNames: " + Arrays.toString(paramNames));
            System.out.println("===parseParamNames DONE===");
            return paramNames;
        } else {
            return new String[0];  // empty list
        }
    }


    /**
     * @param args (Path of idlFile, Path where to store the .convert File, Path where to create packages which
     *             contain compiled .java Files)
     */
    public static void main(String[] args) {
        Path IDLfileName = Paths.get(args[0]);
        Path packageLocation = Paths.get(args[1]);

        try {
            System.out.printf("%s, %s,\n", IDLfileName.toString(), packageLocation.toString());
            IDLfileReader in = new IDLfileReader(new FileReader(IDLfileName.toString()));
            IDLmodule module = parseModule(in);  // Parse IDL file
            System.out.println("Got here");

            // output of what we parsed from IDL file (just for testing)
//            printModule(module);
            writeToFile(module, packageLocation);
            System.out.println("===writeToFile DONE===");
            //            Converter.createPackage(parsedFileLocation,packageLocation);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * testing output & example on how to access class and method data of an IDL module.
     *
     * @param module
     * @param packageLocation
     */
//    private static void printModule(IDLmodule module) {
//        System.out.println();
//        System.out.println("module: " + module.getModuleName());
//
//        // classes
//        IDLclass[] classes = module.getClasses();
//        for (int i = 0; i < classes.length; i++) {
//            System.out.println(" class: " + classes[i].getClassName());
//
//            // methods
//            MethodData[] methods = classes[i].getMethods();
//            for (int k = 0; k < methods.length; k++) {
//                System.out.print("  method: " + IDLCompiler.getSupportedIDLDataTypeName(methods[k].getReturnType())
//                        + " " + methods[k].getName() + " ");
//
//                // parameters
//                SupportedDataTypes[] paramTypes = methods[k].getParamTypes();
//                for (int m = 0; m < paramTypes.length; m++) {
//                    System.out.print(IDLCompiler.getSupportedIDLDataTypeName(paramTypes[m]) + " ");
//                }
//                System.out.println();
//            }
//        }
//    }
    private static void writeToFile(IDLmodule module, Path packageLocation) {
        String adjustedPackageLocation = packageLocation.toString() + File.separator + module.getModuleName();

        try {
            //Create package
            System.out.println("Creating package at: " + adjustedPackageLocation);
            new File(adjustedPackageLocation).mkdir();


            // classes
            IDLclass[] classes = module.getClasses();
            for (IDLclass currentClass : classes) {
                String className = "_" + currentClass.getClassName() + "ImplBase";
                String fileName = adjustedPackageLocation + File.separator + className + ".java";
                File javaFile = new File(fileName);
                FileWriter fileWriter = new FileWriter(javaFile);
                System.out.println("package " + module.getModuleName() + ";");
                fileWriter.append("package " + module.getModuleName() + ";\n");

                //imports

                fileWriter.append("import mware_lib.RemoteDelegator;\n");

                //className
                System.out.println("public abstract class " + className + " {");
                fileWriter.append("public abstract class " + className + " {\n");
                // methods
                MethodData[] methods = currentClass.getMethods();
                for (MethodData currentMethod : methods) {
                    System.out.print("public abstract " + IDLCompiler.getSupportedJavaDataTypeName(currentMethod
                            .getReturnType())
                            + " " + currentMethod.getName() + " (");
                    fileWriter.append("public abstract " + IDLCompiler.getSupportedJavaDataTypeName(currentMethod
                            .getReturnType())
                            + " " + currentMethod.getName() + " (");

                    // parameters
                    SupportedDataTypes[] paramTypes = currentMethod.getParamTypes();
                    String[] paramNames = currentMethod.getParamNames();
//                    System.out.println("paramNames in fileW: " + Arrays.toString(paramNames));
                    for (int m = 0; m < paramTypes.length; m++) {
                        System.out.print(IDLCompiler.getSupportedJavaDataTypeName(paramTypes[m]) + " " + paramNames[m]);
                        fileWriter.append(IDLCompiler.getSupportedJavaDataTypeName(paramTypes[m]) + " " +
                                paramNames[m]);
                        if (paramNames.length > 1 && m < paramTypes.length - 1) {
                            System.out.print(", ");
                            fileWriter.append(", ");
                        }
                    }

                    System.out.print(");");
                    fileWriter.append(");\n");
                    System.out.println();

                }


                //create narrowCast-Method
                fileWriter.append("public static " + className + " narrowCast(Object rawObjectRef){\n" +
                        "return new " + className + "(){\n");
                //reference variables
                fileWriter.append("public String name = rawObjectRef.toString().split(\",\")[0];\n" +
                        "public String host = rawObjectRef.toString().split(\",\")[1];\n" +
                        "public int port = Integer.parseInt(rawObjectRef.toString().split(\",\")[2]);\n");
                for (MethodData currentMethod : methods) {
                    fileWriter.append("@Override\n" +
                            "public " + IDLCompiler.getSupportedJavaDataTypeName(currentMethod
                            .getReturnType())
                            + " " + currentMethod.getName() + " (");
                    SupportedDataTypes[] paramTypes = currentMethod.getParamTypes();
                    String[] paramNames = currentMethod.getParamNames();
                    for (int m = 0; m < paramTypes.length; m++) {
                        fileWriter.append(IDLCompiler.getSupportedJavaDataTypeName(paramTypes[m]) + " " +
                                paramNames[m]);
                        if (paramNames.length > 1 && m < paramTypes.length - 1) {
                            fileWriter.append(", ");
                        }
                    }
                    fileWriter.append(") throws RuntimeException {\nreturn (" + IDLCompiler.getSupportedJavaDataTypeName(currentMethod
                            .getReturnType()) + ") RemoteDelegator.invokeMethod(name, host, port,"+"\"" + className + "\",\"" +
                            currentMethod.getName() +
                            "\"");
                    for (int n = 0; n < paramNames.length; n++) {
                        if (n < paramTypes.length) {
                            fileWriter.append(", ");
                        }
                        fileWriter.append(paramNames[n]);

                    }
                    fileWriter.append(");}\n");
                }
                ;
                System.out.println("}");
                fileWriter.append("};\n}\n}");
                fileWriter.flush();
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

        }
    }

}
