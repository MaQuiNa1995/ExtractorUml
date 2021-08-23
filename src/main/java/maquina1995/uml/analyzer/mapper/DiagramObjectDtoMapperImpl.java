package maquina1995.uml.analyzer.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.AccessSpecifier;
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
import maquina1995.uml.analyzer.dto.DiagramObjectDto;
import maquina1995.uml.analyzer.dto.EnumDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;
import maquina1995.uml.analyzer.util.NodeUtils;

@Service
@RequiredArgsConstructor
public class DiagramObjectDtoMapperImpl implements DiagramObjectDtoMapper {

	private final FieldDtoMapper fieldDtoMapper;
	private final MethodDtoMapper methodDtoMapper;

	@Override
	public DiagramObjectDto mapEnum(EnumDeclaration enumObject) {

		DiagramObjectDto enumDto = new EnumDto();
		enumDto.setName(enumObject.getNameAsString());

		List<MethodDeclaration> methods = enumObject.getMethods();
		NodeList<Modifier> modifiers = enumObject.getModifiers();
		NodeList<ClassOrInterfaceType> implementedTypes = enumObject.getImplementedTypes();
		List<FieldDeclaration> fields = enumObject.getFields();
		AccessSpecifier acessModifier = enumObject.getAccessSpecifier();

		this.processDiagramClass(enumDto, methods, modifiers, acessModifier, implementedTypes, null, fields);

		return enumDto;
	}

	@Override
	public DiagramObjectDto mapClassOrInterface(ClassOrInterfaceDeclaration classOrInterface) {

		DiagramObjectDto classDiagramObject = classOrInterface.isInterface() ? new InterfaceDto() : new ClassDto();
		String className = this.processClassNameWithGenerics(classOrInterface.getNameAsString(),
		        classOrInterface.getTypeParameters());
		List<MethodDeclaration> methods = classOrInterface.getMethods();
		NodeList<Modifier> modifiers = classOrInterface.getModifiers();
		NodeList<ClassOrInterfaceType> implementedTypes = classOrInterface.getImplementedTypes();
		NodeList<ClassOrInterfaceType> extendedTypes = classOrInterface.getExtendedTypes();
		List<FieldDeclaration> fields = classOrInterface.getFields();
		AccessSpecifier accessModifier = classOrInterface.getAccessSpecifier();

		classDiagramObject.setName(className);
		this.processDiagramClass(classDiagramObject, methods, modifiers, accessModifier, implementedTypes,
		        extendedTypes, fields);

		return classDiagramObject;
	}

	/**
	 * Rellenamos información a cualquiera de estos objetos:
	 * <li>{@link ClassDto}</li>
	 * <li>{@link InterfaceDto}</li>
	 * <li>{@link EnumDto}</li>
	 * 
	 * @param classDiagramDto
	 * @param methods
	 * @param modifiers
	 * @param accessModifier
	 * @param implementedTypes
	 * @param extendedTypes
	 * @param fields
	 */
	private void processDiagramClass(DiagramObjectDto classDiagramDto, List<MethodDeclaration> methods,
	        List<Modifier> modifiers, AccessSpecifier accessModifier, NodeList<ClassOrInterfaceType> implementedTypes,
	        NodeList<ClassOrInterfaceType> extendedTypes, List<FieldDeclaration> fields) {

		classDiagramDto.setModifiers(NodeUtils.parseClassModifiers(modifiers));
		classDiagramDto.setAccessModifier(NodeUtils.parseAccesModifier(accessModifier));
		classDiagramDto.setFields(fieldDtoMapper.parseClassFields(fields));
		classDiagramDto.setMethods(methodDtoMapper.parseMethodSignature(methods));
		classDiagramDto.setImplement(this.parseClassNodeListToString(implementedTypes));
		// Los Enum no tienen herencia
		if (extendedTypes != null) {
			classDiagramDto.setExtended(this.parseClassNodeListToString(extendedTypes));
		}
	}

	/**
	 * Procesamos el nombre de la clase para añadir genéricos si se requiere
	 * 
	 * @param className
	 * @param typeParameters
	 * @return
	 */
	private String processClassNameWithGenerics(String className, NodeList<TypeParameter> typeParameters) {

		StringBuilder classNameBuilder = new StringBuilder(className);

		if (!typeParameters.isEmpty()) {
			String genericTypes = typeParameters.stream()
			        .map(TypeParameter::getNameAsString)
			        .collect(Collectors.joining(","));

			classNameBuilder.append("<" + genericTypes + ">");
		}

		return classNameBuilder.toString();
	}

	private List<String> parseClassNodeListToString(NodeList<ClassOrInterfaceType> implementedTypes) {
		return implementedTypes.stream()
		        .map(ClassOrInterfaceType::getNameAsString)
		        .collect(Collectors.toList());
	}

}
