package maquina1995.uml.analyzer.service;

import java.nio.file.Path;
import java.util.List;

import maquina1995.uml.analyzer.dto.DiagramObject;

public interface AnalyzerService {

	List<DiagramObject> analyzeFilesFromPath(Path srcPath);

}