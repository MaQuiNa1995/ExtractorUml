package maquina1995.uml.analyzer.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Slf4j
@Service
public class DiagramService {

	public void createDiagramFile(List<ClassDiagramObject> classes) {
		String fullDiagram = this.createfullDiagramString(classes);
		this.createDiagramFile(fullDiagram);
	}

	private void createDiagramFile(String fullDiagram) {
		File diagramFile = new File("diagram.svg");
		File diagramFileText = new File("diagram.txt");

		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
		        FileWriter fileWritter = new FileWriter(diagramFile, Boolean.FALSE);
		        FileWriter fileWritter2 = new FileWriter(diagramFileText, Boolean.FALSE)) {

			fileWritter2.write(fullDiagram);
			this.createSvgFile(fullDiagram, os, fileWritter);

			log.info("Se ha generado la imagen SVG en la ruta: {}", diagramFile.getAbsolutePath());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void createSvgFile(String fullDiagram, ByteArrayOutputStream os, FileWriter fileWritter)
	        throws IOException {
		new SourceStringReader(fullDiagram).outputImage(os, new FileFormatOption(FileFormat.SVG));

		final String fullSvgDiagram = new String(os.toByteArray(), StandardCharsets.UTF_8);
		fileWritter.write(fullSvgDiagram);
	}

	private String createfullDiagramString(List<ClassDiagramObject> classes) {

		StringBuilder fullDiagram = new StringBuilder("@startuml\n");
		this.convertToString(classes)
		        .forEach(fullDiagram::append);
		fullDiagram.append("@enduml");

		return fullDiagram.toString();
	}

	private List<String> convertToString(List<ClassDiagramObject> classes) {
		List<String> fullStringClasses = new ArrayList<>();

		StringBuilder fullCompositionClasses = new StringBuilder();
		for (ClassDiagramObject classDiagramObject : classes) {
			fullCompositionClasses.setLength(0);

			// Class
			String classType = this.getType(classDiagramObject);

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
			        .filter(isProjectObject.and(e -> !e.getType()
			                .contains("<")))
			        .map(FieldDto::getType)
			        .forEach(fieldName -> {
				        String compositionClass = fieldName.split("<")[0];
				        if (!fullCompositionClasses.toString()
				                .contains(compositionClass)) {
					        fullCompositionClasses
					                .append(String.join(" *-- ", className.split("<")[0], compositionClass))
					                .append("\n");
				        }
			        });

			fullStringClasses.add(this.createFullStringClassLine(className, extendsString, implementsString, classType,
			        fieldsStrings, acccessModifier, classDiagramObject.getMethods(), specialModifiers));

			fullStringClasses.add(fullCompositionClasses.toString());
		}
		return fullStringClasses;
	}

	private String getType(ClassDiagramObject classDiagramObject) {
		String type;
		if (classDiagramObject instanceof ClassDto) {
			type = "class ";
		} else if (classDiagramObject instanceof InterfaceDto) {
			type = "interface ";
		} else {
			type = "enum ";
		}
		return type;
	}

	private String createFieldsString(List<FieldDto> fields) {
		StringBuilder fieldsString = new StringBuilder();

		fields.forEach(fieldDto -> fieldsString.append(fieldDto.getAccessModifier())
		        .append(this.parseModifier(fieldDto.getModifiers()) + " ")
		        .append(fieldDto.getType() + " ")
		        .append(fieldDto.getName() + "\n"));

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
