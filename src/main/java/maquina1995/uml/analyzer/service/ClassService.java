package maquina1995.uml.analyzer.service;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

import maquina1995.uml.analyzer.dto.DiagramObject;

public interface ClassService {

	DiagramObject analyzeEnum(EnumDeclaration enumObject);

	DiagramObject analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface);

}