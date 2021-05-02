package maquina1995.uml.analyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService {

	private final FieldService fieldService;

	public void analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface, List<ClassDiagramObject> classes) {

		classOrInterface.getMethods();

		ClassDiagramObject classDto = classOrInterface.isInterface() ? new InterfaceDto() : new ClassDto();

		classDto.setName(classOrInterface.getNameAsString());
		classDto.getExtended()
		        .addAll(this.parseClassNodeListToString(classOrInterface.getExtendedTypes()));
		classDto.getImplement()
		        .addAll(this.parseClassNodeListToString(classOrInterface.getImplementedTypes()));
		classDto.getFields()
		        .addAll(this.parseClassFields(classOrInterface.getFields()));

		classes.add(classDto);
	}

	private List<FieldDto> parseClassFields(List<FieldDeclaration> fields) {
		List<FieldDto> fieldDeclarations = new ArrayList<>();
		fields.stream()
		        .forEach(fieldDeclaration -> fieldService.analyzeField(fieldDeclaration, fieldDeclarations));

		return fieldDeclarations;
	}

	private List<String> parseClassNodeListToString(NodeList<ClassOrInterfaceType> implementedTypes) {
		return implementedTypes.stream()
		        .map(ClassOrInterfaceType::getNameAsString)
		        .collect(Collectors.toList());
	}

}
