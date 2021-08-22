package maquina1995.uml.analyzer.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.DiagramObjectDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerServiceImpl implements AnalyzerService {

	private final FileService fileService;
	private final JavaParser javaParser;
	private final DiagramObjectDtoMapper classService;

	@Override
	public List<DiagramObjectDto> analyzeFilesFromPath(Path srcPath) {

		log.info("Analizing DiagramObjects from {}", srcPath);

		return fileService.iterateDirectory(srcPath)
		        .stream()
		        .map(this::getCompilationUnitFromPath)
		        .filter(Objects::nonNull)
		        .map(this::analyzeCompilationUnit)
		        .flatMap(List::stream)
		        .collect(Collectors.toList());
	}

	/**
	 * Retornamos null en caso de que no exista valor para una mayor limpieza de
	 * código y no tener que lidiar con 2 pasos en el lambda que llama a este método
	 * 
	 * @param javaFile
	 * @return
	 */
	@SneakyThrows
	private CompilationUnit getCompilationUnitFromPath(Path javaFile) {
		return javaParser.parse(javaFile)
		        .getResult()
		        .orElse(null);
	}

	private List<DiagramObjectDto> analyzeCompilationUnit(CompilationUnit compilationUnit) {

		Stream<DiagramObjectDto> classOrInterfaces = this.processClassOrInterfaces(compilationUnit);
		Stream<DiagramObjectDto> enums = this.processEnums(compilationUnit);

		return Stream.concat(classOrInterfaces, enums)
		        .collect(Collectors.toList());
	}

	private Stream<DiagramObjectDto> processClassOrInterfaces(CompilationUnit compilationUnit) {
		return compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
		        .stream()
		        .map(classService::analyzeClassOrInterface)
		        .filter(Objects::nonNull);
	}

	private Stream<DiagramObjectDto> processEnums(CompilationUnit compilationUnit) {
		return compilationUnit.findAll(EnumDeclaration.class)
		        .stream()
		        .map(classService::analyzeEnum)
		        .filter(Objects::nonNull);
	}
}
