package maquina1995.uml.analyzer.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;

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
		for (ClassDiagramObject javaTypeDto : classes) {

			// Class
			String classType = javaTypeDto instanceof ClassDto ? "class " : "interface ";

			// Interface / Extends
			String extendsString = this.createExtendsOrImplements(javaTypeDto.getExtended(), " extends ");
			String implementsString = this.createExtendsOrImplements(javaTypeDto.getImplement(), " implements ");

			String fieldsStrings = this.createFieldsString(javaTypeDto.getFields());

			String fullStringClassLine = this.createFullStringClassLine(javaTypeDto, extendsString, implementsString,
			        classType, fieldsStrings);

			fileWritter.write(fullStringClassLine);
		}
	}

	private String createFieldsString(List<FieldDto> fields) {
		StringBuilder fieldsString = new StringBuilder("");

		if (!fields.isEmpty()) {
			fields.forEach(fieldDto -> {

				int startPosition = fieldsString.length();

				fieldsString.append(" ")
				        .append(fieldDto.getType())
				        .append(" ")
				        .append(fieldDto.getName())
				        .append("\n");

				fieldsString.insert(startPosition, this.parseModifier(fieldDto.getModifiers()));
			});
		}

		return fieldsString.toString();
	}

	private String parseModifier(List<String> modifiers) {

		StringBuilder modifierBuilder = new StringBuilder("");
		modifiers.stream()
		        .map(this::parseAccesModifier)
		        .forEach(modifierBuilder::append);

		return modifierBuilder.toString();
	}

	private String parseAccesModifier(String modifier) {
		String modifierParsed;
		switch (modifier) {
		case "public":
			modifierParsed = "+";
			break;
		case "private":
			modifierParsed = "-";
			break;
		case "protected":
			modifierParsed = "#";
			break;
		case "package":
			modifierParsed = "~";
			break;
		default:
			modifierParsed = modifier;
			break;
		}

		return modifierParsed;
	}

	private String createExtendsOrImplements(List<String> classes, String type) {
		return classes.isEmpty() ? "" : type + String.join(",", classes);
	}

	private String createFullStringClassLine(ClassDiagramObject javaTypeDto, String extendsString, String implementsString,
	        String classType, String fieldsStrings) {
		return classType + javaTypeDto.getName() + extendsString + implementsString + "{\n" + fieldsStrings + "\n}\n";
	}

}
