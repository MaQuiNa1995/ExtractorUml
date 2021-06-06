package maquina1995.uml.analyzer.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.constants.RegExpConstants;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.InterfaceDto;
import maquina1995.uml.analyzer.dto.MethodDto;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Slf4j
@Service
public class DiagramService {

	@SneakyThrows
	public void createDiagramFile(String txt) {
		File file = new File(txt);

		StringBuilder stringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line)
				        .append("\n");
			}
		}
		this.createJavaDiagramFile(stringBuilder.toString());
	}

	public void createDiagramFile(List<ClassDiagramObject> classes) {
		String fullDiagram = this.createfullDiagramString(classes);
		this.createJavaDiagramFile(fullDiagram);
	}

	private void createJavaDiagramFile(String fullDiagram) {
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

		StringBuilder fullDiagram = new StringBuilder("@startuml\nhide empty members\n");
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
				        this.addAggregation(fullCompositionClasses, className, compositionClass, " *-- ");
			        });

			List<String> methods = this.processMethods(classDiagramObject, fullCompositionClasses);

			fullStringClasses.add(this.createFullStringClassLine(className, extendsString, implementsString, classType,
			        fieldsStrings, acccessModifier, methods, specialModifiers));

			fullStringClasses.add(fullCompositionClasses.toString());
		}
		return fullStringClasses;
	}

	private void addAggregation(StringBuilder fullCompositionClasses, String className, String compositionClass,
	        String aggregationType) {
		if (!fullCompositionClasses.toString()
		        .contains(compositionClass) && !compositionClass.matches(RegExpConstants.JAVA_CORE_REG_EXP)) {
			fullCompositionClasses.append(String.join(aggregationType, className.split("<")[0], compositionClass))
			        .append("\n");
		}
	}

	private List<String> processMethods(ClassDiagramObject classDiagramObject, StringBuilder fullCompositionClasses) {

		List<String> fullStringMethods = classDiagramObject.getMethods()
		        .stream()
		        .map(MethodDto::toString)
		        .collect(Collectors.toList());
		String className = classDiagramObject.getName();

		for (MethodDto e : classDiagramObject.getMethods()) {

			String returnAggregation = " o-- ";
			String returnTypeProcessed = e.getReturnType();
			if (returnTypeProcessed.contains("<") && returnTypeProcessed.contains(">") && e.getReturnType()
			        .split("<")[0].matches(
			                "Iterable|Collection|List|Queue|Set|ArrayList|LinkedList|Vector|Stack|PriorityQueue|Deque|ArrayDeque|HashSet|LinkedHashSet|SortedSet|TreeSet")) {
				returnAggregation = " \"1\" o-- \"N\" ";
				returnTypeProcessed = e.getReturnType()
				        .substring(e.getReturnType()
				                .indexOf("<") + 1, e.getReturnType()
				                        .indexOf(">"));
			} else if (returnTypeProcessed.contains("<") && returnTypeProcessed.contains(">")) {
				returnTypeProcessed = returnTypeProcessed.split("<")[0];
			}
			this.addAggregation(fullCompositionClasses, className, returnTypeProcessed, returnAggregation);
		}

		classDiagramObject.getMethods()
		        .stream()
		        .map(MethodDto::getParameters)
		        .forEach(parameters -> parameters.forEach(parameter -> {

			        String parameterProcessed = parameter;

			        String typeAggregation = " o-- ";
			        if (parameter.contains("<") && parameter.contains(">") && parameter.split("<")[0].matches(
			                "Iterable|Collection|List|Queue|Set|ArrayList|LinkedList|Vector|Stack|PriorityQueue|Deque|ArrayDeque|HashSet|LinkedHashSet|SortedSet|TreeSet")) {
				        typeAggregation = " \"1\" o-- \"N\" ";
				        parameterProcessed = parameterProcessed.substring(parameterProcessed.indexOf("<") + 1,
				                parameterProcessed.indexOf(">"));
			        } else if (parameter.contains("<") && parameter.contains(">")) {
				        parameterProcessed = parameterProcessed.split("<")[0];
			        }
			        this.addAggregation(fullCompositionClasses, className, parameterProcessed, typeAggregation);
		        }));

		return fullStringMethods;
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
