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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.constants.RegExpConstants;
import maquina1995.uml.analyzer.dto.DiagramObjectDto;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.dto.MethodDto;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * 
 * @author MaQuiNa1995
 * 
 */
@Slf4j
@Service
public class DiagramServiceImpl implements DiagramService {

	@Override
	@SneakyThrows
	public void createDiagramFile(String txtPath) {

		@Cleanup
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(txtPath)));
		StringBuilder stringBuilder = new StringBuilder();

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}

		this.createJavaDiagramFile(stringBuilder.toString());
	}

	@Override
	public void createDiagramFile(List<DiagramObjectDto> classes) {
		String fullDiagram = this.createfullDiagramString(classes);
		this.createJavaDiagramFile(fullDiagram);
	}

	private void createJavaDiagramFile(String fullDiagram) {
		File diagramFile = new File("diagram.svg");
		File diagramFileText = new File("diagram.txt");

		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
		        FileWriter svgWriter = new FileWriter(diagramFile, Boolean.FALSE);
		        FileWriter txtWriter = new FileWriter(diagramFileText, Boolean.FALSE)) {

			txtWriter.write(fullDiagram);
			this.createSvgFile(fullDiagram, os, svgWriter);

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

	private String createfullDiagramString(List<DiagramObjectDto> classes) {

		StringBuilder fullDiagram = new StringBuilder("@startuml\nhide empty members\nskinparam groupInheritance 2\n");
		this.diagramsToString(classes)
		        .forEach(fullDiagram::append);
		fullDiagram.append("@enduml");

		return fullDiagram.toString();
	}

	private List<String> diagramsToString(List<DiagramObjectDto> classes) {
		List<String> fullStringClasses = new ArrayList<>();

		StringBuilder fullCompositionClasses = new StringBuilder();
		classes.forEach(this.diagramToString(fullStringClasses, fullCompositionClasses));

		return fullStringClasses;
	}

	private Consumer<DiagramObjectDto> diagramToString(List<String> fullStringClasses,
	        StringBuilder fullCompositionClasses) {
		return classDiagramObject -> {

			fullCompositionClasses.setLength(0);

			// Class
			String className = classDiagramObject.getName();
			String classType = this.getType(classDiagramObject);

			// Interface / Extends
			String extendsString = this.createExtendsOrImplements(classDiagramObject.getExtended(), " extends ");
			String implementsString = this.createExtendsOrImplements(classDiagramObject.getImplement(), " implements ");

			// Fields
			String fieldsStrings = this.createFieldsString(classDiagramObject.getFields());
			this.processCompositionFields(fullCompositionClasses, classDiagramObject.getFields(),
			        classDiagramObject.getName());

			// Modifiers
			String acccessModifier = classDiagramObject.getAccessModifier();
			String specialModifiers = classDiagramObject.getModifiers();

			// Methods
			List<String> methods = classDiagramObject.getMethods()
			        .stream()
			        .map(MethodDto::toString)
			        .collect(Collectors.toList());

			// Full class text
			String fullClassLine = acccessModifier + specialModifiers + classType + className + extendsString
			        + implementsString + "{\n" + fieldsStrings + "\n" + String.join("\n", methods) + "\n}\n";
			fullStringClasses.add(fullClassLine);

			// Composition text
			fullStringClasses.add(fullCompositionClasses.toString());

		};
	}

	private void processCompositionFields(StringBuilder fullCompositionClasses, List<FieldDto> fields,
	        String className) {
		Predicate<FieldDto> typeIsNotGeneric = e -> !e.getType()
		        .contains("<");

		fields.stream()
		        .filter(typeIsNotGeneric)
		        .map(FieldDto::getType)
		        .filter(e -> !e.matches(RegExpConstants.GENERIC_CORE_JAVA_OBJECT_PATTERN))
		        .forEach(fieldName -> this.addAggregation(fullCompositionClasses, className, fieldName, " *-- "));
	}

	private void addAggregation(StringBuilder fullCompositionClasses, String className, String compositionClass,
	        String aggregationType) {
		if (!fullCompositionClasses.toString()
		        .contains(compositionClass) && !compositionClass.matches(RegExpConstants.JAVA_LANG_REG_EXP)
		        && !compositionClass.matches(RegExpConstants.GENERIC_CORE_JAVA_OBJECT_PATTERN)) {

			fullCompositionClasses.append(String.join(aggregationType, className, compositionClass))
			        .append("\n");
		}
	}

	private String getType(DiagramObjectDto classDiagramObject) {
		String type;

		switch (classDiagramObject.getClass()
		        .getSimpleName()) {
		case "ClassDto":
			type = "class ";
			break;
		case "InterfaceDto":
			type = "interface ";
			break;
		default:
			type = "enum ";
			break;
		}
		return type;
	}

	private String createFieldsString(List<FieldDto> fields) {

		return fields.stream()
		        .map(FieldDto::toString)
		        .collect(Collectors.joining("\n")) + "\n";
	}

	private String createExtendsOrImplements(List<String> classes, String extendsOrImplements) {
//		TODO: Ñapa para quitar los Serializable
		classes.remove("Serializable");
		return classes.isEmpty() ? "" : extendsOrImplements + String.join(",", classes);
	}

}
