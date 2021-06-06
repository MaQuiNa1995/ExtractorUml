package maquina1995.uml.analyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import maquina1995.uml.analyzer.dto.MethodDto;
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

		enumDto.setModifiers(NodeUtils.parseClassModifiers(enumDeclaration.getModifiers()));

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

		classDto.setModifiers(NodeUtils.parseClassModifiers(classOrInterface.getModifiers()));

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

	private List<MethodDto> parseMethodSignatureToString(List<MethodDeclaration> methods) {
		return methods.stream()
		        .map(this::createFullMethodSignature)
		        .collect(Collectors.toList());
	}

	private MethodDto createFullMethodSignature(MethodDeclaration methodDeclaration) {

		MethodDto methodDto = new MethodDto();
		methodDto.setReturnType(methodDeclaration.getTypeAsString());
		methodDto.setName(methodDeclaration.getNameAsString());
		methodDto.setAccessModifier(NodeUtils.parseAccesModifier(methodDeclaration.getAccessSpecifier()
		        .asString()));
		methodDto.setModifiers(NodeUtils.parseMethodModifiers(methodDeclaration.getModifiers())
		        .toString());
		methodDto.getParameters()
		        .addAll(methodDeclaration.getTypeParameters()
		                .stream()
		                .map(TypeParameter::getNameAsString)
		                .collect(Collectors.toList()));
		return methodDto;
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
