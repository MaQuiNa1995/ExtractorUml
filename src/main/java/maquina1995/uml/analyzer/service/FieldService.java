package maquina1995.uml.analyzer.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.global.Global;
import maquina1995.uml.analyzer.util.NodeUtils;

@Service
@RequiredArgsConstructor
public final class FieldService {

	private ParserService parserService;

	public void analyzeField(FieldDeclaration fieldDeclaration, List<FieldDto> fieldsDto) {

		String accessModifier = NodeUtils.parseAccesModifier(fieldDeclaration.getAccessSpecifier()
		        .toString());

		StringBuilder modifiers = NodeUtils.parseSpecialmodifiers(fieldDeclaration.findAll(Modifier.class));

		// TODO: si viene int a, b[], c; peta
		String type = fieldDeclaration.getElementType()
		        .toString();

		fieldDeclaration.getVariables()
		        .stream()
		        .map(VariableDeclarator::getNameAsString)
		        .forEach(this.createFieldDto(fieldsDto, accessModifier, modifiers.toString()
		                .trim()
		                .toLowerCase(), type));
	}

	private Consumer<String> createFieldDto(List<FieldDto> fieldsDto, String accessModifier, String modifiers,
	        String type) {
		return fieldName -> {
			FieldDto fieldDto = new FieldDto();
			fieldDto.setName(fieldName);
			fieldDto.setAccessModifier(accessModifier);
			fieldDto.setModifiers(Arrays.asList(modifiers.split(" ")));
			fieldDto.setType(type);

			fieldDto.setIsProjectObject(Global.TYPE_SOLVER.get()
			        .tryToSolveType(type)
			        .isSolved());

			fieldsDto.add(fieldDto);
		};
	}

}
