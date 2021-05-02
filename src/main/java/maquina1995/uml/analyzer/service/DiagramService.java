package maquina1995.uml.analyzer.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
			this.analyzeClasses(classes, fileWritter);
			fileWritter.write("@enduml\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void analyzeClasses(List<ClassDiagramObject> classes, FileWriter fileWritter) throws IOException {
		for (ClassDiagramObject classDiagramObject : classes) {

			// Class
			String classType = classDiagramObject instanceof ClassDto ? "class " : "interface ";

			// Interface / Extends
			String extendsString = this.createExtendsOrImplements(classDiagramObject.getExtended(), " extends ");
			String implementsString = this.createExtendsOrImplements(classDiagramObject.getImplement(), " implements ");

			String acccessModifier = this.parseAccesModifier(classDiagramObject.getAccessModifier());
			String fieldsStrings = this.createFieldsString(classDiagramObject.getFields());

			String fullStringClassLine = this.createFullStringClassLine(classDiagramObject, extendsString,
			        implementsString, classType, fieldsStrings, acccessModifier, classDiagramObject.getMethods());

			fileWritter.write(fullStringClassLine);
		}
	}

	private String createFieldsString(List<FieldDto> fields) {
		StringBuilder fieldsString = new StringBuilder("");

		if (!fields.isEmpty()) {
			fields.forEach(fieldDto -> {

				fieldsString.append(this.parseAccesModifier(fieldDto.getAccessModifier()))
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

	private String parseAccesModifier(String modifier) {
		String modifierParsed;
		switch (modifier.toLowerCase()) {

		case "public":
			modifierParsed = "+";
			break;
		case "private":
			modifierParsed = "-";
			break;
		case "protected":
			modifierParsed = "#";
			break;
		default:
			modifierParsed = "~";
			break;
		}

		return modifierParsed;
	}

	private String createExtendsOrImplements(List<String> classes, String type) {
		return classes.isEmpty() ? "" : type + String.join(",", classes);
	}

	private String createFullStringClassLine(ClassDiagramObject javaTypeDto, String extendsString,
	        String implementsString, String classType, String fieldsStrings, String acccessModifier,
	        List<String> methods) {

		return acccessModifier + classType + javaTypeDto.getName() + extendsString + implementsString + "{\n"
		        + fieldsStrings + "\n" + String.join("\n", methods) + "\n}\n";
	}

}
