package maquina1995.uml.analyzer.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Slf4j
@Service
public class DiagramService {

	public void createDiagramFile(List<ClassDiagramObject> classes) {

		StringBuilder diagramBuilder = new StringBuilder().append("@startuml\n")
		        .append(this.analyzeClasses(classes))
		        .append("@enduml\n");

		try {
			SourceStringReader reader = new SourceStringReader(diagramBuilder.toString());
			@Cleanup
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
			final String svg = new String(os.toByteArray(), Charsets.UTF_8);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}

	private List<String> analyzeClasses(List<ClassDiagramObject> classes) {
		return classes.stream()
		        .map(e -> this.createFullStringDiagram(e))
		        .collect(Collectors.toList());
	}

	private String createFullStringDiagram(ClassDiagramObject classDiagramObject) {
		// Class
		String classType = classDiagramObject instanceof ClassDto ? "class " : "interface ";

		// Interface / Extends
		String extendsString = this.createExtendsOrImplements(classDiagramObject.getExtended(), " extends ");
		String implementsString = this.createExtendsOrImplements(classDiagramObject.getImplement(), " implements ");

		String acccessModifier = classDiagramObject.getAccessModifier();
		String specialModifiers = classDiagramObject.getModifiers();
		String fieldsStrings = this.createFieldsString(classDiagramObject.getFields());

		return this.createFullStringClassLine(classDiagramObject, extendsString, implementsString, classType,
		        fieldsStrings, acccessModifier, classDiagramObject.getMethods(), specialModifiers);
	}

	private String createFieldsString(List<FieldDto> fields) {
		StringBuilder fieldsString = new StringBuilder("");

		if (!fields.isEmpty()) {
			fields.forEach(fieldDto -> {

				fieldsString.append(fieldDto.getAccessModifier())
				        .append(this.parseModifier(fieldDto.getModifiers()))
				        .append(" ")
				        .append(fieldDto.getType())
				        .append(" ")
				        .append(fieldDto.getName())
				        .append("\n");

			});
		}

		return fieldsString.toString();
	}

	private String parseModifier(List<String> modifiersFromDto) {

		StringBuilder modifiers = new StringBuilder("");

		modifiersFromDto.stream()
		        .map(String::toLowerCase)
		        .map(this::parseAbstractStatic)
		        .forEach(modifier -> modifiers.append(" ")
		                .append(modifier));

		return modifiers.toString()
		        .trim();
	}

	private String parseAbstractStatic(String modifier) {

		String parsedModifier = modifier;

		if (modifier.matches("(static|abstract)")) {
			parsedModifier = "{" + modifier + "}";
		}

		return parsedModifier;

	}

	private String createExtendsOrImplements(List<String> classes, String type) {
		return classes.isEmpty() ? "" : type + String.join(",", classes);
	}

	private String createFullStringClassLine(ClassDiagramObject javaTypeDto, String extendsString,
	        String implementsString, String classType, String fieldsStrings, String acccessModifier,
	        List<String> methods, String specialModifiers) {

		return acccessModifier + specialModifiers + classType + javaTypeDto.getName() + extendsString + implementsString
		        + "{\n" + fieldsStrings + "\n" + String.join("\n", methods) + "\n}\n";
	}

}
