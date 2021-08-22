package maquina1995.uml.analyzer;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.dto.DiagramObjectDto;
import maquina1995.uml.analyzer.service.AnalyzerService;
import maquina1995.uml.analyzer.service.DiagramService;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	private final AnalyzerService analyzerService;
	private final DiagramService diagramService;

	@Override
	public void run(String... paths) {

		String firstPath = paths[0];

		if (StringUtils.hasText(firstPath) && firstPath.endsWith(".txt")) {
			log.info("Creando Imagen svg a partir de txt en ruta: {}", firstPath);
			diagramService.createDiagramFile(firstPath);
		} else {
			log.info("Analizando proyecto/s java en ruta/s: {}", Stream.of(paths)
			        .collect(Collectors.joining(",")));

			List<DiagramObjectDto> classes = Stream.of(paths)
			        .map(Paths::get)
			        .map(analyzerService::analyzeFilesFromPath)
			        .flatMap(Collection::stream)
			        .collect(Collectors.toList());

			log.info("Creando Diagrama...");

			diagramService.createDiagramFile(classes);
		}

	}

}
