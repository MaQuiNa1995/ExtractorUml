package maquina1995.uml.analyzer.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import lombok.extern.slf4j.Slf4j;
import maquina1995.uml.analyzer.global.Global;

@Slf4j
@Service
public class ParserService {

	public void createParser(String srcPath) {

		ReflectionTypeSolver reflectionTypeSolver = this.createReflectionTypeSolver();
//		final JavaParserTypeSolver javaParserTypeSolver = this.createjavaParserTypeSolver(srcPath);

		log.debug("Creating combined type solver");
		Global.TYPE_SOLVER.set(reflectionTypeSolver);
	}

	private JavaParserTypeSolver createjavaParserTypeSolver(String srcPath) {
		log.debug("Creating java project solver with path: {} with config: {}", srcPath);
		return new JavaParserTypeSolver(new File(srcPath), this.createConfig());
	}

	private ReflectionTypeSolver createReflectionTypeSolver() {
		log.debug("Creating reflection type solver, with reflection activated");
		return new ReflectionTypeSolver(true);
	}

	private ParserConfiguration createConfig() {
		log.debug("Creating type solver with config: {}", LanguageLevel.JAVA_8);
		return new ParserConfiguration().setLanguageLevel(LanguageLevel.JAVA_8);
	}

}
