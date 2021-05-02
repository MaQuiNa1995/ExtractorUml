package maquina1995.uml.analyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService {

	private final FieldService fieldService;

	public void analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface,
	        List<ClassDiagramObject> classes) {

		ClassDiagramObject classDto = classOrInterface.isInterface() ? new InterfaceDto() : new ClassDto();

		List<String> methods = classDto.getMethods();
		classOrInterface.getMethods()
		        .stream()
		        .map(MethodDeclaration::getSignature)
		        .map(Signature::toString)
		        .map(this::parseSpecialModifiers)
		        .forEach(methods::add);

		classDto.setAccessModifier(this.parseAccessmodifier(classOrInterface.getAccessSpecifier()
		        .toString()));
		classDto.setName(classOrInterface.getNameAsString());
		classDto.getExtended()
		        .addAll(this.parseClassNodeListToString(classOrInterface.getExtendedTypes()));
		classDto.getImplement()
		        .addAll(this.parseClassNodeListToString(classOrInterface.getImplementedTypes()));
		classDto.getFields()
		        .addAll(this.parseClassFields(classOrInterface.getFields()));

		classes.add(classDto);
	}

	private String parseSpecialModifiers(String signature) {
		return signature.replace("abstract", "{abstract}")
		        .replace("static", "{static}");
	}

	private String parseAccessmodifier(String accessModifier) {
		return StringUtils.hasText(accessModifier) ? accessModifier.toLowerCase() : "";
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
