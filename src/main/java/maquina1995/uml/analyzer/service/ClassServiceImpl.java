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
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.DiagramObject;
import maquina1995.uml.analyzer.dto.EnumDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;
import maquina1995.uml.analyzer.dto.MethodDto;
import maquina1995.uml.analyzer.dto.ParameterDto;
import maquina1995.uml.analyzer.util.NodeUtils;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

	private final FieldService fieldService;
	private final PackageService packageService;

	@Override
	public DiagramObject analyzeEnum(EnumDeclaration enumObject) {
		DiagramObject enumDto = new EnumDto();
		enumDto.setName(enumObject.getNameAsString());

		this.processDiagramClass(enumDto, enumObject.getMethods(), enumObject.getModifiers(),
		        enumObject.getAccessSpecifier()
		                .toString(),
		        enumObject.getImplementedTypes(), null, enumObject.getFields());

		return enumDto;
	}

	@Override
	public DiagramObject analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface) {
		DiagramObject classDiagramObject = classOrInterface.isInterface() ? new InterfaceDto() : new ClassDto();
		classDiagramObject.setName(this.processName(classOrInterface));

		this.processDiagramClass(classDiagramObject, classOrInterface.getMethods(), classOrInterface.getModifiers(),
		        classOrInterface.getAccessSpecifier()
		                .toString(),
		        classOrInterface.getImplementedTypes(), classOrInterface.getExtendedTypes(),
		        classOrInterface.getFields());

		return classDiagramObject;
	}

	private void processDiagramClass(DiagramObject classDiagramDto, List<MethodDeclaration> methods,
	        List<Modifier> modifiers, String accessEspecifier, NodeList<ClassOrInterfaceType> implementedTypes,
	        NodeList<ClassOrInterfaceType> extendedTypes, List<FieldDeclaration> fields) {

		classDiagramDto.getMethods()
		        .addAll(this.parseMethodSignature(methods));
		classDiagramDto.setModifiers(NodeUtils.parseClassModifiers(modifiers));
		classDiagramDto.setAccessModifier(NodeUtils.parseAccesModifier(accessEspecifier));
		classDiagramDto.getImplement()
		        .addAll(this.parseClassNodeListToString(implementedTypes));
		classDiagramDto.getFields()
		        .addAll(this.parseClassFields(fields));
		if (extendedTypes != null) {
			classDiagramDto.getExtended()
			        .addAll(this.parseClassNodeListToString(extendedTypes));
		}
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

	private List<MethodDto> parseMethodSignature(List<MethodDeclaration> methods) {
		return methods.stream()
		        .map(this::createFullMethodSignature)
		        .collect(Collectors.toList());
	}

	private MethodDto createFullMethodSignature(MethodDeclaration methodDeclaration) {

		String returnTypeAsString = methodDeclaration.getTypeAsString();
		String modifiers = NodeUtils.parseModifiers(methodDeclaration.getModifiers())
		        .toString();
		String accessModifiers = NodeUtils.parseAccesModifier(methodDeclaration.getAccessSpecifier()
		        .asString());
		String className = methodDeclaration.getNameAsString();

		boolean isjavaCoreClass = packageService.isjavaCoreClass(methodDeclaration, returnTypeAsString);

		List<ParameterDto> parameterDtos = methodDeclaration.getTypeParameters()
		        .stream()
		        .map(this.createParameterDto())
		        .collect(Collectors.toList());

		MethodDto methodDto = MethodDto.builder()
		        .returnType(returnTypeAsString)
		        .isReturnFromJavaCore(isjavaCoreClass)
		        .name(className)
		        .accessModifier(accessModifiers)
		        .modifiers(modifiers)
		        .build();

		methodDto.getParameters()
		        .addAll(parameterDtos);

		return methodDto;
	}

	private Function<TypeParameter, ParameterDto> createParameterDto() {
		return parameter -> {
			ParameterDto parameterDto = new ParameterDto();
			parameterDto.setIsFromJavaCore(packageService.isjavaCoreClass(parameter, parameter.getNameAsString()));
			parameterDto.setName(parameter.getNameAsString());
			return parameterDto;
		};
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
