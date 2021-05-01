package maquina1995.uml.analyzer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;
import maquina1995.uml.analyzer.dto.JavaTypeDto;

@Slf4j
@Service
public class ClassService {

	public void analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface, List<JavaTypeDto> classes) {
		classOrInterface.getFields();
		classOrInterface.getMethods();

		JavaTypeDto classDto = classOrInterface.isInterface() ? new InterfaceDto() : new ClassDto();
		classDto.setName(classOrInterface.getNameAsString());

		classDto.setExtended(this.parseClassNodeListToString(classOrInterface.getExtendedTypes()));

		classDto.setImplement(this.parseClassNodeListToString(classOrInterface.getImplementedTypes()));

		classes.add(classDto);
	}

	private List<String> parseClassNodeListToString(NodeList<ClassOrInterfaceType> implementedTypes) {
		return implementedTypes.stream()
		        .map(ClassOrInterfaceType::getNameAsString)
		        .collect(Collectors.toList());
	}

}
