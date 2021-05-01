package maquina1995.uml.analyzer.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import com.github.javaparser.JavaParser;

@SpringBootConfiguration
public class SpringBeanConfig {

	@Bean
	public JavaParser createJavaparser() {
		return new JavaParser();
	}

}
