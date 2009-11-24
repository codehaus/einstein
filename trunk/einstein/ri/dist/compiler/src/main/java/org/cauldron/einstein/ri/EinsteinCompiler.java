/******************************************************************************
 *  All works are (C) 2008 - Mangala Solutions Ltd and Paremus Ltd.           *
 *                                                                            *
 *  Jointly liicensed to Mangala Solutions and Paremus under one              *
 *  or more contributor license agreements.  See the NOTICE file              *
 *  distributed with this work for additional information                     *
 *  regarding copyright ownership.                                            *
 *                                                                            *
 *  This program is free software: you can redistribute it and/or modify      *
 *  it under the terms of the GNU Affero General Public License as published  *
 *  by the Free Software Foundation, either version 3 of the License, or      *
 *  (at your option) any later version.                                       *
 *                                                                            *
 *  This program is distributed in the hope that it will be useful,           *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of            *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             *
 *  GNU Affero General Public License for more details.                       *
 *                                                                            *
 *  You should have received a copy of the GNU Affero General Public License  *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.     *
 *                                                                            *
 ******************************************************************************/
package org.cauldron.einstein.ri;

import deesel.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.model.resource.exception.ProviderNotFoundRuntimeException;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.ResourceProvider;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.ri.core.providers.factory.ProviderFactory;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;
import org.cauldron.einstein.ri.core.registry.scan.RegisterClasses;

import java.util.List;

/**
 * The main compiler for the Einstein programming language.  It can be executed from the command line, or called
 * programmatically. It can be used translate Einstein source code into Java and/or all the way to Java byte codes
 * (class file).
 * <p/>
 * This class is not thread safe.  Be sure to create separate compilers if needed in multiple threads.
 *
 * @author Neil Ellis
 * @author (original) <a href="mailto:troyhen@comcast.net>Troy Heninger</a> Date: Oct 18, 2004
 */
@org.contract4j5.contract.Contract
public class EinsteinCompiler extends deesel.tool.DeeselCompiler {
    private static final Logger log = Logger.getLogger(EinsteinCompiler.class);

    private String[] files;
    private String sourceDir;
    private String tempDir;
    private String targetDir;
    private String classPath;
    private boolean noTemp;
    private boolean noClass;
    private static boolean stacktraces;


    public static void main(String[] args) {
        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(CompilerOptionsFactory.getOptions(), args);
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println(e.getMessage());
            CompilerHelp.displayOptions();
            return;

        }
        if (args.length == 0) {
            CompilerHelp.displayOptions();
            return;
        }

        RegisterClasses.register();
        stacktraces = cmd.hasOption("stacktrace");

        EinsteinCompiler comp = null;
        try {
            comp = new EinsteinCompiler();
            if (cmd.hasOption("provider-help")) {
                ResourceProvider registeredProvider = ProviderFactory.getInstance().getRegisteredProvider(cmd.getOptionValue("provider-help"));
                ProviderMetaData metadata = getMetaDataFromResource(registeredProvider);
                CompilerHelp.displayProviderInfo(metadata);
                return;

            }

            if (cmd.hasOption("instruction-help")) {
                Class<Instruction> instructionClass = (Class<Instruction>) EinsteinRegistryFactory.getInstance().get(RegisterInstruction.PATH + "/" + cmd.getOptionValue("instruction-help"));
                CompilerHelp.displayInstructionInfo(instructionClass.getAnnotation(RegisterInstruction.class).metadata());
                return;

            }
            if (cmd.hasOption("list-providers")) {

                List providers = EinsteinRegistryFactory.getInstance().getAll(RegisterProvider.PATH);
                for (Object o : providers) {
                    ResourceProvider registeredProvider;
                    registeredProvider = (ResourceProvider) o;
                    ProviderMetaData metadata = getMetaDataFromResource(registeredProvider);
                    System.out.printf("%-16s - %-60s\n", metadata.core().name(), metadata.core().shortDescription());
                }
                return;

            }

            if (cmd.hasOption("list-instructions")) {
                RegisterClasses.register();
                List providers = EinsteinRegistryFactory.getInstance().getAll(RegisterInstruction.PATH);
                for (Object o : providers) {
                    Class<Instruction> instructionClass;
                    instructionClass = (Class<Instruction>) o;
                    InstructionMetaData metadata = instructionClass.getAnnotation(RegisterInstruction.class).metadata();
                    System.out.printf("%-16s - %-60s\n", metadata.core().name(), metadata.core().shortDescription());
                }
                return;

            }
        } catch (ProviderNotFoundRuntimeException e) {
            handleException(e);
        }

        if (cmd.hasOption("classpath")) {
            comp.setClasspath(cmd.getOptionValue("classpath"));

        }
        if (cmd.hasOption("sourcepath")) {
            comp.setSourceDir(cmd.getOptionValue("sourcepath"));

        }
        if (cmd.hasOption("directory")) {
            comp.setTargetDir(cmd.getOptionValue("directory"));

        }
        if (cmd.hasOption("temppath")) {
            comp.setTempDir(cmd.getOptionValue("temppath"));

        }
        comp.setNoTemp(cmd.hasOption("notemp"));
        comp.setNoClass(cmd.hasOption("noclass"));
        comp.setFiles(cmd.getArgs());
        try {
            comp.run();
        } catch (Exception e) {
            handleException(e);
        }
        System.exit(0);
    }

    private static void handleException(Exception e) {
        if (stacktraces) {
            e.printStackTrace();
        } else {
            System.out.println(e.getMessage());
        }
    }

    private static ProviderMetaData getMetaDataFromResource(ResourceProvider registeredProvider) {
        RegisterProvider registerProviderAnnotation = registeredProvider.getClass().getAnnotation(RegisterProvider.class);
        return registerProviderAnnotation.metadata();
    }



    /*
       public void compile(File inFile) throws IOException, ParseException {
           String tempDir = this.tempDir;
           if (tempDir == null) tempDir = inFile.getParent();
           File tempFile = new File(tempDir,
                   extractBaseName(inFile.getName()) + ".java");
           translate(inFile, tempFile);
           javac(new File[]{tempFile});
           if (noTemp) tempFile.delete();
       }
    */



//    public void run() {
//        // Return early if nothing to compile
//        if (getFiles() == null || getFiles().length == 0) return;
//        // Switch to a new classloader if classpath is set
//        if (getClassPath() != null) {
//            ClassLoader loader = new ClasspathLoader(getClassPath());
//            Thread.currentThread().setContextClassLoader(loader);
//        }
//        // The parser only supports one file at a time, currently, so
//        // send each file to the parser separatly
//        int count = 0;
//        File[] srcFiles = new File[getFiles().length];
//        for (int ix = 0, ixz = getFiles().length; ix < ixz; ix++) {
//            srcFiles[ix] = getSourceDir() == null
//                    ? new File(getFiles()[ix])
//                    : new File(getSourceDir(), getFiles()[ix]);
//
//        }
//        File[] tempFiles;
//        try {
//            File tempDir = this.getTempDir() != null
//                    ? new File(this.getTempDir())
//                    : File.createTempFile("yyyy", "yyyyy")
//                    .getParentFile();
//            tempFiles = translate(srcFiles, tempDir);
//
//        } catch (Throwable t) {
//            log.fatal(t, t);
//            throw new RuntimeException(t);
//        }
//        if (!isNoClass()) {
//            javac(tempFiles);
//        }
//        if (isNoTemp()) {
//            for (int ix = 0; ix < count; ix++) {
//                tempFiles[ix].delete();
//            }
//        }
//    }


//    File[] translate(File[] inFiles, File outDir) throws IOException,
//            ParseException {
//        InputStream in;
//        if (outDir == null) {
//            throw new GeneralParserFailureException(
//                    "Could not locate the parent directory for files +"
//                            + Arrays.toString(inFiles));
//        }
//
//        outDir.mkdirs();
//        ASTCompilationGroup astGroup = new ASTCompilationGroup(-1);
//        for (int i = 0; i < inFiles.length; i++) {
//            File inFile = inFiles[i];
//            in = new FileInputStream(inFile);
//            ASTCompilationUnit compilationUnit = translateToCompilationUnit(
//                    extractBaseName(inFile.getName()), in);
//            astGroup.jjtAddChild(compilationUnit, i);
//        }
//
//        CompilationGroup group = (CompilationGroup) astGroup.jjtAccept(
//                new ParsletVisitor(), null);
//        group.validate();
//        TranslationVisitor visitor = new TranslationVisitor(outDir);
//        group.accept(visitor,
//                new BasicCompilerContext());
//        return visitor.getTranslatedFiles();
//
//    }
//
//
//    public File[] translate(URL[] inFiles, File outDir) throws IOException,
//            ParseException {
//        InputStream in;
//        if (outDir == null) {
//            throw new GeneralParserFailureException("Output directory was null.");
//        }
//
//        outDir.mkdirs();
//        ASTCompilationGroup astGroup = new ASTCompilationGroup(-1);
//        for (int i = 0; i < inFiles.length; i++) {
//            URL url = inFiles[i];
//            in = url.openStream();
//            ASTCompilationUnit compilationUnit = translateToCompilationUnit(
//                    extractBaseName(url.getPath()), in);
//            astGroup.jjtAddChild(compilationUnit, i);
//        }
//
//        CompilationGroup group = (CompilationGroup) astGroup.jjtAccept(
//                new ParsletVisitor(), null);
//        group.validate();
//        TranslationVisitor visitor = new TranslationVisitor(outDir);
//        group.accept(visitor,
//                new BasicCompilerContext());
//        return visitor.getTranslatedFiles();
//
//    }
//
//
//    ASTCompilationUnit translateToCompilationUnit(
//            String name, InputStream in)
//            throws IOException, ParseException {
//        log.debug("Translating " + name + " to Java");
//        String basename = extractBaseName(name);
//        DeeselParser p = new DeeselParser(in);
//        try {
//            return p.CompilationUnit(basename);
//        } catch (ParseException pe) {
//            throw new ParseException(pe, MessageFormat.format("Failed to compile {0}.", name));
//        }
//    }
//
//
//    void javac(File[] javaFiles) {
//        int fileCount = 0;
//        for (int iy = 0, iyz = javaFiles.length; iy < iyz; iy++) {
//            if (javaFiles[iy] != null) {
//                log.debug(MessageFormat.format("Compiling {0} to byte codes.", javaFiles[iy]));
//                fileCount = iy + 1;
//            }
//        }
//        int argCount = fileCount + 4; // -source 1.5 -d ????
//        if (classPath != null) argCount += 2; // -classpath classPath
//        if (tempDir != null) argCount += 2; // -sourcepath tempDir
//        if (targetDir == null) {
//            targetDir = ".";
//        }
//
//        String[] args = new String[argCount];
//        int ix = 0;
//        args[ix++] = "-source";
//        args[ix++] = "1.5";
//        if (classPath != null) {
//            args[ix++] = "-classpath";
//            args[ix++] = classPath;
//        }
//        if (tempDir != null) {
//            args[ix++] = "-sourcepath";
//            args[ix++] = tempDir;
//        }
//        if (targetDir != null) {
//            args[ix++] = "-d";
//            args[ix++] = targetDir;
//        }
//        for (int iy = 0, iyz = fileCount; iy < iyz; iy++) {
//            args[ix++] = javaFiles[iy].getAbsolutePath();
//        }
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        PrintWriter errors = new PrintWriter(buffer);
//        if (targetDir != null) new File(targetDir).mkdirs();
//        int result = Main.compile(args, errors);
//        errors.flush();
//        String output = buffer.toString();
//        if (result != 0) {
//            log.fatal(output);
//            throw new GeneralParserFailureException(output);
//        } else {
//            if (output.length() > 0) {
//                log.debug(output);
//            }
//        }
//    }
//

}
