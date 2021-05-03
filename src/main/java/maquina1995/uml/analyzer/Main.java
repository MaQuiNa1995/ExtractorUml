package maquina1995.uml.analyzer;

import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import maquina1995.uml.analyzer.dto.ClassDiagramObject;
import maquina1995.uml.analyzer.service.AnalyzerService;
import maquina1995.uml.analyzer.service.DiagramService;
import maquina1995.uml.analyzer.service.ParserService;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Autowired
	private AnalyzerService analyzerService;
	@Autowired
	private DiagramService diagramService;
	@Autowired
	private ParserService parserService;

	@Override
	public void run(String... args) throws Exception {
		String fileStringPath = args[0];
		parserService.createParser(fileStringPath);
		List<ClassDiagramObject> classes = analyzerService.analyzeFiles(Paths.get(fileStringPath));
		diagramService.createDiagramFile(classes);
	}

}
