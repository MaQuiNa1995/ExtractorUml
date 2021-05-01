package maquina1995.uml.analyzer.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerService {

	private final FileService fileService;

	public void createDiagram(Path srcPath) {

		List<ClassDto> classes = new ArrayList<>();

		final JavaParser javaParser = new JavaParser();
		try {
			for (final Path javaFile : fileService.iterateDirectory(srcPath)) {
				javaParser.parse(javaFile)
				        .getResult()
				        .ifPresent(this.analyzeCompilationUnit(classes));
			}
		} catch (IOException exception) {
			log.error(exception.getMessage());
		}
		this.createDiagramFile(classes);
	}

	private Consumer<CompilationUnit> analyzeCompilationUnit(List<ClassDto> classes) {
		return compilationUnit -> compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
		        .forEach(e -> this.analyzeClassOrInterface(e, classes));
	}

	private void analyzeClassOrInterface(ClassOrInterfaceDeclaration classOrInterface, List<ClassDto> classes) {
		classOrInterface.getFields();
		classOrInterface.getMethods();

		ClassDto classDto = ClassDto.builder()
		        .name(classOrInterface.getNameAsString())
		        .extended(classOrInterface.getExtendedTypes()
		                .stream()
		                .map(ClassOrInterfaceType::getNameAsString)
		                .collect(Collectors.toList()))
		        .build();

		classes.add(classDto);
	}

	private void createDiagramFile(List<ClassDto> classes) {

		File diagramFile = new File("diagram" + UUID.randomUUID() + ".txt");

		try (FileWriter fileWritter = new FileWriter(diagramFile)) {
			fileWritter.write("@startuml\n");
			this.analyzeClasses(classes, fileWritter);
			fileWritter.write("@enduml\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void analyzeClasses(List<ClassDto> classes, FileWriter fileWritter) throws IOException {
		for (ClassDto classDto : classes) {

			String extendsString = "";

			if (!classDto.getExtended()
			        .isEmpty()) {
				extendsString = " extends " + String.join(",", classDto.getExtended());
			}

			String aaaa = "class " + classDto.getName() + extendsString + "{}\n";

			fileWritter.write(aaaa);
		}
	}

}
