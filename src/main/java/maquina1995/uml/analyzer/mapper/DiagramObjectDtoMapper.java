package maquina1995.uml.analyzer.mapper;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

import maquina1995.uml.analyzer.dto.DiagramObjectDto;

public interface DiagramObjectDtoMapper {

	DiagramObjectDto mapEnum(EnumDeclaration enumObject);

	DiagramObjectDto mapClassOrInterface(ClassOrInterfaceDeclaration classOrInterface);

}