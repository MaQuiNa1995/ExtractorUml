package maquina1995.uml.analyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.EnumDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;
import maquina1995.uml.analyzer.util.NodeUtils;

@Service
@RequiredArgsConstructor
public class ClassService {

	private final FieldService fieldService;

	public ClassDiagramObject analyzeEnum(EnumDeclaration enumObject) {
		ClassDiagramObject enumDto = new EnumDto();
		this.processEnum(enumObject, enumDto);
		return enumDto;
	}

	public ClassDiagramObject analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface) {

		ClassDiagramObject classDto = classOrInterface.isInterface() ? new InterfaceDto() : new ClassDto();
		this.processClassOrInterface(classOrInterface, classDto);
		return classDto;
	}

	private void processEnum(EnumDeclaration enumDeclaration, ClassDiagramObject enumDto) {
		enumDto.getMethods()
		        .addAll(this.parseMethodSignatureToString(enumDeclaration.getMethods()));

		enumDto.setModifiers(this.parseSpecialModifiers(enumDeclaration.getModifiers()));

		enumDto.setAccessModifier(NodeUtils.parseAccesModifier(enumDeclaration.getAccessSpecifier()
		        .toString()));

		enumDto.setName(enumDeclaration.getNameAsString());

		enumDto.getImplement()
		        .addAll(this.parseClassNodeListToString(enumDeclaration.getImplementedTypes()));

		enumDto.getFields()
		        .addAll(this.parseClassFields(enumDeclaration.getFields()));
	}

	private void processClassOrInterface(ClassOrInterfaceDeclaration classOrInterface, ClassDiagramObject classDto) {
		classDto.getMethods()
		        .addAll(this.parseMethodSignatureToString(classOrInterface.getMethods()));

		classDto.setModifiers(this.parseSpecialModifiers(classOrInterface.getModifiers()));

		classDto.setAccessModifier(NodeUtils.parseAccesModifier(classOrInterface.getAccessSpecifier()
		        .toString()));

		classDto.setName(this.processName(classOrInterface));

		classDto.getExtended()
		        .addAll(this.parseClassNodeListToString(classOrInterface.getExtendedTypes()));

		classDto.getImplement()
		        .addAll(this.parseClassNodeListToString(classOrInterface.getImplementedTypes()));

		classDto.getFields()
		        .addAll(this.parseClassFields(classOrInterface.getFields()));
	}

	private String processName(ClassOrInterfaceDeclaration classOrInterface) {

		StringBuilder classNameBuilder = new StringBuilder(classOrInterface.getNameAsString());

		if (!classOrInterface.getTypeParameters()
		        .isEmpty()) {
			String genericTypes = classOrInterface.getTypeParameters()
			        .stream()
			        .map(TypeParameter::getNameAsString)
			        .collect(Collectors.joining(","));

			classNameBuilder.append("<")
			        .append(genericTypes)
			        .append(">");
		}

		return classNameBuilder.toString();
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
		        .map(this.fullMethodAlchemist())
		        .map(this::parseSpecialModifiers)
		        .collect(Collectors.toList());
	}

	private Function<MethodDeclaration, String> fullMethodAlchemist() {
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
		return signature.replace("ABSTRACT", "{abstract}")
		        .replace("STATIC", "{static}");
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
