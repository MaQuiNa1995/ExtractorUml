package maquina1995.uml.analyzer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {

	public List<Path> iterateDirectory(final Path path) {

		log.debug("ITERATING PATH: {}", path);

		final List<Path> paths = new ArrayList<>();

		try (Stream<Path> fileStream = Files.walk(path)) {
			fileStream.sorted()
			        .filter(this.createNonNullJavaFileFilter())
			        .forEach(paths::add);

		} catch (final IOException exception) {
			log.error(exception.getMessage());
		}
		return paths;
	}

	private Predicate<Path> createNonNullJavaFileFilter() {

		final Predicate<Path> isFile = path -> path.toFile()
		        .isFile();

		final Predicate<Path> hasJavaExtension = path -> path.toString()
		        .endsWith(".java");

		return isFile.and(hasJavaExtension)
		        .and(Objects::nonNull);
	}
}
