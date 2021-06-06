package maquina1995.uml.analyzer.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import maquina1995.uml.analyzer.constants.RegExpConstants;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.util.NodeUtils;

@Service
public final class FieldService {

	public void analyzeField(FieldDeclaration fieldDeclaration, List<FieldDto> fieldsDto) {

		String accessModifier = NodeUtils.parseAccesModifier(fieldDeclaration.getAccessSpecifier()
		        .toString());

		String modifiers = NodeUtils.parseMethodModifiers(fieldDeclaration.findAll(Modifier.class))
		        .toString()
		        .trim()
		        .toLowerCase();

		// TODO: si viene int a, b[], c; peta
		String type = fieldDeclaration.getElementType()
		        .toString();

		fieldDeclaration.getVariables()
		        .stream()
		        .map(VariableDeclarator::getNameAsString)
		        .forEach(this.createFieldDto(fieldsDto, accessModifier, modifiers, type));
	}

	private Consumer<String> createFieldDto(List<FieldDto> fieldsDto, String accessModifier, String modifiers,
	        String type) {
		return fieldName -> {
			FieldDto fieldDto = new FieldDto();
			fieldDto.setName(fieldName);
			fieldDto.setAccessModifier(accessModifier);
			fieldDto.setModifiers(Arrays.asList(modifiers.split(" ")));
			fieldDto.setType(type);
			fieldDto.setIsProjectObject(!type.matches(RegExpConstants.JAVA_CORE_REG_EXP));
			fieldsDto.add(fieldDto);
		};
	}
}
