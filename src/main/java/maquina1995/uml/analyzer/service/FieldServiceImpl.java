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
import maquina1995.uml.analyzer.util.NodeUtils;

@Service
@RequiredArgsConstructor
public final class FieldServiceImpl implements FieldService {

	private final PackageService packageService;

	@Override
	public void analyzeField(FieldDeclaration fieldDeclaration, List<FieldDto> fieldsDto) {

		String accessModifier = NodeUtils.parseAccesModifier(fieldDeclaration.getAccessSpecifier()
		        .toString());

		String modifiers = NodeUtils.parseModifiers(fieldDeclaration.findAll(Modifier.class))
		        .toString()
		        .trim()
		        .toLowerCase();

		// TODO: si viene int a, b[], c; peta
		String type = fieldDeclaration.getElementType()
		        .toString();

		boolean isFromJavaCore = packageService.isjavaCoreClass(fieldDeclaration, type);

		fieldDeclaration.getVariables()
		        .stream()
		        .map(VariableDeclarator::getNameAsString)
		        .forEach(this.createFieldDto(fieldsDto, accessModifier, modifiers, type, isFromJavaCore));
	}

	private Consumer<String> createFieldDto(List<FieldDto> fieldsDto, String accessModifier, String modifiers,
	        String type, boolean isFromJavaCore) {
		return fieldName -> {
			FieldDto fieldDto = new FieldDto();
			fieldDto.setName(fieldName);
			fieldDto.setAccessModifier(accessModifier);
			fieldDto.setModifiers(Arrays.asList(modifiers.split(" ")));
			fieldDto.setType(type);
			fieldDto.setIsProjectObject(!isFromJavaCore);
			fieldsDto.add(fieldDto);
		};
	}
}