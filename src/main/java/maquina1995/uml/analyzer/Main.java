package maquina1995.uml.analyzer;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import maquina1995.uml.analyzer.service.AnalyzerService;
import maquina1995.uml.analyzer.service.ParserService;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Autowired
	private ParserService parserService;
	@Autowired
	private AnalyzerService analyzerService;

	@Override
	public void run(String... args) throws Exception {

		String srcPath = args[0];
		parserService.createParser(srcPath);
		analyzerService.createDiagram(Paths.get(srcPath));

	}

}
