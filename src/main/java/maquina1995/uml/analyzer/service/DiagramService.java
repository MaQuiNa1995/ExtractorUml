package maquina1995.uml.analyzer.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDto;
import maquina1995.uml.analyzer.dto.JavaTypeDto;

@Slf4j
@Service
public class DiagramService {

	public void createDiagramFile(List<JavaTypeDto> classes) {

		File diagramFile = new File("diagram.txt");

		try (FileWriter fileWritter = new FileWriter(diagramFile, Boolean.FALSE)) {
			fileWritter.write("@startuml\n");
			this.analyzeClasses(classes, fileWritter);
			fileWritter.write("@enduml\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void analyzeClasses(List<JavaTypeDto> classes, FileWriter fileWritter) throws IOException {
		for (JavaTypeDto javaTypeDto : classes) {

			// Class
			String classType = javaTypeDto instanceof ClassDto ? "class " : "interface ";

			// Interface / Extends
			String extendsString = this.createExtendsOrImplements(javaTypeDto.getExtended(), " extends ");
			String implementsString = this.createExtendsOrImplements(javaTypeDto.getImplement(), " implements ");

			javaTypeDto.get
			
			String fullStringClassLine = this.createFullStringClassLine(javaTypeDto, extendsString, implementsString,
			        classType);

			fileWritter.write(fullStringClassLine);
		}
	}

	private String createExtendsOrImplements(List<String> classes, String type) {
		return classes.isEmpty() ? "" : type + String.join(",", classes);
	}

	private String createFullStringClassLine(JavaTypeDto javaTypeDto, String extendsString, String implementsString,
	        String classType) {
		return classType + javaTypeDto.getName() + extendsString + implementsString + "{}\n";
	}

}
