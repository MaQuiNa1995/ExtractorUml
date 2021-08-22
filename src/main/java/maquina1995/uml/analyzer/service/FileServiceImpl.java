package maquina1995.uml.analyzer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

	@Override
	public List<Path> iterateDirectory(Path path) {

		log.debug("Iterando la ruta: {}", path);

		List<Path> paths;
		try (Stream<Path> fileStream = Files.walk(path)) {
			paths = fileStream.sorted()
			        .filter(this.createNonNullJavaFileFilter())
			        .collect(Collectors.toList());

		} catch (final IOException exception) {
			paths = new ArrayList<>();
			log.error(exception.getMessage());
		}
		return paths;
	}

	private Predicate<Path> createNonNullJavaFileFilter() {

		final Predicate<Path> isFile = path -> path.toFile()
		        .isFile();
		final Predicate<Path> hasJavaExtension = path -> path.toString()
		        .endsWith(".java");

		return isFile.and(hasJavaExtension);
	}
}
