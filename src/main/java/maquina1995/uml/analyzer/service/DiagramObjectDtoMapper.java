package maquina1995.uml.analyzer.service;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

import maquina1995.uml.analyzer.dto.DiagramObjectDto;

public interface DiagramObjectDtoMapper {

	DiagramObjectDto analyzeEnum(EnumDeclaration enumObject);

	DiagramObjectDto analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface);

}