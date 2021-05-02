package maquina1995.uml.analyzer.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import maquina1995.uml.analyzer.dto.FieldDto;

@Service
public class FieldService {

	public void analyzeField(FieldDeclaration fieldDeclaration, List<FieldDto> fieldsDto) {

		final String accessModifier = fieldDeclaration.getAccessSpecifier()
		        .toString();

		Predicate<Keyword> isFinal = this.createModifierFilter(Keyword.FINAL);
		Predicate<Keyword> isStatic = this.createModifierFilter(Keyword.STATIC);
		Predicate<Keyword> isDefault = this.createModifierFilter(Keyword.DEFAULT);
		Predicate<Keyword> isAbstract = this.createModifierFilter(Keyword.ABSTRACT);
		Predicate<Keyword> isNative = this.createModifierFilter(Keyword.NATIVE);
		Predicate<Keyword> isStrictfp = this.createModifierFilter(Keyword.STRICTFP);
		Predicate<Keyword> isSynchronized = this.createModifierFilter(Keyword.SYNCHRONIZED);
		Predicate<Keyword> isTransient = this.createModifierFilter(Keyword.TRANSIENT);
		Predicate<Keyword> isTransitive = this.createModifierFilter(Keyword.TRANSITIVE);
		Predicate<Keyword> isVolatile = this.createModifierFilter(Keyword.VOLATILE);

		Predicate<Keyword> modifierPredicate = isFinal.or(isStatic)
		        .or(isDefault)
		        .or(isAbstract)
		        .or(isNative)
		        .or(isStrictfp)
		        .or(isSynchronized)
		        .or(isTransient)
		        .or(isTransitive)
		        .or(isVolatile);

		StringBuilder modifiers = new StringBuilder(accessModifier);

		fieldDeclaration.findAll(Modifier.class)
		        .stream()
		        .peek(System.out::println)
		        .map(Modifier::getKeyword)
		        .peek(System.out::println)
		        .filter(modifierPredicate)
		        .forEach(modifier -> modifiers.append(" ")
		                .append(modifier));

		// TODO: si viene int a, b[], c; peta
		String type = fieldDeclaration.getElementType()
		        .toString();

		fieldDeclaration.getVariables()
		        .stream()
		        .map(VariableDeclarator::getNameAsString)
		        .forEach(this.createFieldDto(fieldsDto, modifiers.toString()
		                .toLowerCase(), type));
	}

	private Predicate<Keyword> createModifierFilter(Keyword keyword) {
		return e -> e.equals(keyword);
	}

	private Consumer<String> createFieldDto(List<FieldDto> fieldsDto, final String modifiers, String type) {
		return fieldName -> {
			FieldDto fieldDto = new FieldDto();
			fieldDto.setName(fieldName);
			fieldDto.setModifiers(Arrays.asList(modifiers.split(" ")));
			fieldDto.setType(type);
			fieldsDto.add(fieldDto);
		};
	}

}
