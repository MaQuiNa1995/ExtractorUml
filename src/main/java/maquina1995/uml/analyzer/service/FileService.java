package maquina1995.uml.analyzer.service;

import java.nio.file.Path;
import java.util.List;

public interface FileService {

	List<Path> iterateDirectory(Path path);

}