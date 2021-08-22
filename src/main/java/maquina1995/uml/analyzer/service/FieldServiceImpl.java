package maquina1995.uml.analyzer.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.util.NodeUtils;

@Service
@RequiredArgsConstructor
public final class FieldServiceImpl implements FieldService {

	private final PackageService packageService;

	@Override
	public List<FieldDto> analyzeField(FieldDeclaration fieldDeclaration) {

		String accessModifier = NodeUtils.parseAccesModifier(fieldDeclaration.getAccessSpecifier()
		        .toString());

		String modifiers = NodeUtils.parseModifiers(fieldDeclaration.findAll(Modifier.class))
		        .toString()
		        .trim()
		        .toLowerCase();

		// TODO: si viene int a, b[], c; peta
		String type = fieldDeclaration.getElementType()
		        .toString();

		boolean isFromJavaCore = packageService.isJavaCoreClass(fieldDeclaration, type);

		return fieldDeclaration.getVariables()
		        .stream()
		        .map(VariableDeclarator::getNameAsString)
		        .map(this.createFieldDto(accessModifier, modifiers, type, isFromJavaCore))
		        .collect(Collectors.toList());
	}

	private Function<String, FieldDto> createFieldDto(String accessModifier, String modifiers, String type,
	        boolean isFromJavaCore) {
		return fieldName -> FieldDto.builder()
		        .name(fieldName)
		        .accessModifier(accessModifier)
		        .modifiers(Arrays.asList(modifiers.split(" ")))
		        .type(type)
		        .isProjectObject(!isFromJavaCore)
		        .build();

	}
}
