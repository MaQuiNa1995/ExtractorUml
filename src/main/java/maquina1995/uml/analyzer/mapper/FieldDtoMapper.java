package maquina1995.uml.analyzer.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.ClassArgumentDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.service.PackageService;
import maquina1995.uml.analyzer.util.NodeUtils;
import maquina1995.uml.analyzer.util.SanitizeUtils;

@Component
@RequiredArgsConstructor
public class FieldDtoMapper {

	private final PackageService packageService;
	private final ClassArgumentDtoMapper classArgumentDtoMapper;

	public List<FieldDto> parseClassFields(List<FieldDeclaration> fields) {
		return fields.stream()
		        .map(this::createFieldDto)
		        .flatMap(Function.identity())
		        .collect(Collectors.toList());
	}

	private Stream<FieldDto> createFieldDto(FieldDeclaration fieldDeclaration) {

		List<ClassArgumentDto> classArguments = new ArrayList<>();

		final String fieldType = SanitizeUtils.sanitizeTypeName(fieldDeclaration.getCommonType());

		if (fieldDeclaration.getCommonType()
		        .getClass()
		        .isAssignableFrom(ClassOrInterfaceType.class)) {
			classArguments.addAll(classArgumentDtoMapper.processClassArguments(fieldDeclaration.getCommonType()));
		}

		return fieldDeclaration.getVariables()
		        .stream()
		        .map(VariableDeclarator::getNameAsString)
		        .map(fieldName -> FieldDto.builder()
		                .name(fieldName)
		                .type(fieldType)
		                .isFromJavaCore(packageService.isJavaCoreClass(fieldDeclaration, fieldType))
		                .classParameters(classArguments)
		                .accessModifier(NodeUtils.parseAccesModifier(fieldDeclaration.getAccessSpecifier()))
		                .modifiers(NodeUtils.parseModifiers(fieldDeclaration.findAll(Modifier.class)))
		                .build());

	}

}
