package maquina1995.uml.analyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
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
import maquina1995.uml.analyzer.util.NodeUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService {

	private final FieldService fieldService;

	public void analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface,
	        List<ClassDiagramObject> classes) {

		ClassDiagramObject classDto = classOrInterface.isInterface() ? new InterfaceDto() : new ClassDto();
		classDto.getMethods()
		        .addAll(this.parseMethodSignatureToString(classOrInterface.getMethods()));
		classDto.setModifiers(this.parseSpecialModifiers(classOrInterface.getModifiers()));
		classDto.setAccessModifier(NodeUtils.parseAccesModifier(classOrInterface.getAccessSpecifier()
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

	private String parseSpecialModifiers(NodeList<Modifier> modifiers) {
		StringBuilder specialModifiers = NodeUtils.parseSpecialmodifiers(modifiers);

		if (specialModifiers.length() != 0) {
			specialModifiers.append(" ");
		}

		return specialModifiers.toString()
		        .toLowerCase();
	}

	private List<String> parseMethodSignatureToString(List<MethodDeclaration> methods) {
		return methods.stream()
		        .map(this.fullmethodAlchemist())
		        .map(this::parseSpecialModifiers)
		        .collect(Collectors.toList());
	}

	private Function<MethodDeclaration, String> fullmethodAlchemist() {
		return e -> this.createFullMethodSignature(e.getSignature()
		        .toString(),
		        e.getAccessSpecifier()
		                .toString(),
		        e.getTypeAsString());
	}

	private String createFullMethodSignature(String signature, String accessSpecifier, String returnType) {
		return String.join(" ", NodeUtils.parseAccesModifier(accessSpecifier), returnType, signature);
	}

	private String parseSpecialModifiers(String signature) {
		return signature.toLowerCase()
		        .replace("abstract", "{abstract}")
		        .replace("static", "{static}");
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
