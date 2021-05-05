package maquina1995.uml.analyzer.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.FieldDto;

@Slf4j
@Service
public class DiagramService {

	public void createDiagramFile(List<ClassDiagramObject> classes) {

		File diagramFile = new File("diagram.txt");

		try (FileWriter fileWritter = new FileWriter(diagramFile, Boolean.FALSE)) {
			fileWritter.write("@startuml\n");
			for (String fullClassString : this.analyzeClasses(classes, fileWritter)) {
				fileWritter.write(fullClassString);
			}
			fileWritter.write("@enduml\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private List<String> analyzeClasses(List<ClassDiagramObject> classes, FileWriter fileWritter) throws IOException {
		List<String> fullStringClasses = new ArrayList<>();

		StringBuilder fullCompositionClasses = new StringBuilder();
		for (ClassDiagramObject classDiagramObject : classes) {
			fullCompositionClasses.setLength(0);

			// Class
			String classType = classDiagramObject instanceof ClassDto ? "class " : "interface ";

			// Interface / Extends
			String extendsString = this.createExtendsOrImplements(classDiagramObject.getExtended(), " extends ");
			String implementsString = this.createExtendsOrImplements(classDiagramObject.getImplement(), " implements ");

			String acccessModifier = classDiagramObject.getAccessModifier();
			String specialModifiers = classDiagramObject.getModifiers();
			String fieldsStrings = this.createFieldsString(classDiagramObject.getFields());
			String className = classDiagramObject.getName();

			Predicate<FieldDto> isProjectObject = FieldDto::getIsProjectObject;

			classDiagramObject.getFields()
			        .stream()
			        .filter(isProjectObject)
			        .map(FieldDto::getName)
			        .forEach(fieldName -> fullCompositionClasses.append(String.join(" *-- ", className, fieldName))
			                .append("\n"));

			fullStringClasses.add(this.createFullStringClassLine(className, extendsString, implementsString, classType,
			        fieldsStrings, acccessModifier, classDiagramObject.getMethods(), specialModifiers));

			fullStringClasses.add(fullCompositionClasses.toString());
		}
		return fullStringClasses;
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
		        .map(this::parseAbstractStatic)
		        .forEach(modifier -> modifiers.append(" ")
		                .append(modifier));

		return modifiers.append(" ")
		        .toString()
		        .trim();
	}

	private String parseAbstractStatic(String modifier) {

		String parsedModifier = modifier;

		if (modifier.toLowerCase()
		        .matches("(static|abstract)")) {
			parsedModifier = "{" + modifier + "}";
		}

		return parsedModifier;

	}

	private String createExtendsOrImplements(List<String> classes, String type) {
		return classes.isEmpty() ? "" : type + String.join(",", classes);
	}

	private String createFullStringClassLine(String typeName, String extendsString, String implementsString,
	        String classType, String fieldsStrings, String acccessModifier, List<String> methods,
	        String specialModifiers) {

		return acccessModifier + specialModifiers + classType + typeName + extendsString + implementsString + "{\n"
		        + fieldsStrings + "\n" + String.join("\n", methods) + "\n}\n";
	}

}
