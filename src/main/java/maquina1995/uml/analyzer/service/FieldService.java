package maquina1995.uml.analyzer.service;

import java.util.List;

import com.github.javaparser.ast.body.FieldDeclaration;

import maquina1995.uml.analyzer.dto.FieldDto;

public interface FieldService {

	List<FieldDto> analyzeField(FieldDeclaration fieldDeclaration);

}