package maquina1995.uml.analyzer.service;

import java.nio.file.Path;
import java.util.List;

import maquina1995.uml.analyzer.dto.DiagramObjectDto;

public interface AnalyzerService {

	List<DiagramObjectDto> analyzeFilesFromPath(Path srcPath);

}