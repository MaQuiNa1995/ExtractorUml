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
	public void run(String... args) throws Exception {
		List<ClassDiagramObject> classes = analyzerService.analyzeFiles(Paths.get(args[0]));
		diagramService.createDiagramFile(classes);
	}

}
