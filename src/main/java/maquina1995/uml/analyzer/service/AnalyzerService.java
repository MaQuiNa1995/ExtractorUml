package maquina1995.uml.analyzer.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.ClassDiagramObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerService {

	private final FileService fileService;
	private final JavaParser javaParser;
	private final ClassService classService;

	public List<ClassDiagramObject> analyzeFiles(Path srcPath) {

		List<ClassDiagramObject> classes = new ArrayList<>();

		fileService.iterateDirectory(srcPath)
		        .forEach(this.parseFile(classes));

		return classes;
	}

	private Consumer<Path> parseFile(List<ClassDiagramObject> classes) {
		return javaFile -> this.getCompilationUnitFromPath(javaFile)
		        .ifPresent(this.analyzeCompilationUnit(classes));
	}

	@SneakyThrows
	private Optional<CompilationUnit> getCompilationUnitFromPath(Path javaFile) {
		return javaParser.parse(javaFile)
		        .getResult();
	}

	private Consumer<CompilationUnit> analyzeCompilationUnit(List<ClassDiagramObject> analyzedClasses) {
		return compilationUnit -> compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
		        .forEach(classOrInterface -> classService.analyzeClassOrInterface(classOrInterface, analyzedClasses));
	}
}
