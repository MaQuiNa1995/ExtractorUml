package maquina1995.uml.analyzer.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.DiagramObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerServiceImpl implements AnalyzerService {

	private final FileService fileService;
	private final JavaParser javaParser;
	private final ClassService classService;

	@Override
	public List<DiagramObject> analyzeFilesFromPath(Path srcPath) {

		List<DiagramObject> classes = new ArrayList<>();
		fileService.iterateDirectory(srcPath)
		        .forEach(this.parseFile(classes));
		return classes;
	}

	private Consumer<Path> parseFile(List<DiagramObject> classes) {
		return javaFile -> this.getCompilationUnitFromPath(javaFile)
		        .ifPresent(e -> classes.addAll(this.analyzeCompilationUnit(e)));
	}

	@SneakyThrows
	private Optional<CompilationUnit> getCompilationUnitFromPath(Path javaFile) {
		return javaParser.parse(javaFile)
		        .getResult();
	}

	private List<DiagramObject> analyzeCompilationUnit(CompilationUnit compilationUnit) {

		List<DiagramObject> classorInterfaces = this.processClassOrInterfaces(compilationUnit);
		List<DiagramObject> enums = this.processEnums(compilationUnit);

		int listSize = classorInterfaces.size() + enums.size();

		List<DiagramObject> classDiagramObjects = new ArrayList<>(listSize);
		classDiagramObjects.addAll(classorInterfaces);
		classDiagramObjects.addAll(enums);

		return classDiagramObjects;
	}

	private List<DiagramObject> processClassOrInterfaces(CompilationUnit compilationUnit) {
		return compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
		        .stream()
		        .map(classService::analyzeClassOrInterface)
		        .filter(Objects::nonNull)
		        .collect(Collectors.toList());
	}

	private List<DiagramObject> processEnums(CompilationUnit compilationUnit) {
		return compilationUnit.findAll(EnumDeclaration.class)
		        .stream()
		        .map(classService::analyzeEnum)
		        .filter(Objects::nonNull)
		        .collect(Collectors.toList());
	}
}
