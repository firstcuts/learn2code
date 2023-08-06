package com.autonomaz.web.java.tools;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JavaMethodLister {


    private static String CLASS_NAME = "";
    public static void main(String[] args) {
        String projectPath = "C:\\working\\webauthn\\webauthn-java-example\\src\\main\\java";
        listAllMethods(projectPath);
    }

    private static void listAllMethods(String projectPath) {
        try {
            Files.walk(Paths.get(projectPath))
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".java"))
                    .forEach(JavaMethodLister::processJavaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processJavaFile(Path path) {
        try {

            CLASS_NAME = path.getFileName().toString();
            //System.out.println(path.getFileName() + "**************");

            CompilationUnit compilationUnit = StaticJavaParser.parse(path);

            compilationUnit.findAll(MethodDeclaration.class).forEach(JavaMethodLister::printMethodInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printMethodInfo(MethodDeclaration method) {
        String accessModifier = method.getAccessSpecifier().asString();
        String returnType = method.getTypeAsString();
        String methodName = method.getNameAsString();

        List<String> parameters = method.getParameters().stream()
                .map(parameter -> parameter.getType().asString() + " " + parameter.getNameAsString())
                .toList();

        System.out.println(CLASS_NAME + " , "+accessModifier + " " + returnType + " " + methodName + "(" + String.join(", ", parameters) + ")");

    }
}

