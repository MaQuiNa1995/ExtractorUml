package maquina1995.uml.analyzer;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.service.AnalyzerService;
import maquina1995.uml.analyzer.service.DiagramService;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Autowired
	private AnalyzerService analyzerService;
	@Autowired
	private DiagramService diagramService;

	@Override
	public void run(String... paths) {

		List<ClassDiagramObject> classes = Arrays.asList(paths)
		        .stream()
		        .map(Paths::get)
		        .map(analyzerService::analyzeFilesFromPath)
		        .flatMap(Collection::stream)
		        .collect(Collectors.toList());

		diagramService.createDiagramFile(classes);
	}

}
