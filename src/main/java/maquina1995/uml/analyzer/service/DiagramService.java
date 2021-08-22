package maquina1995.uml.analyzer.service;

import java.util.List;

import maquina1995.uml.analyzer.dto.DiagramObject;

public interface DiagramService {

	void createDiagramFile(String txt);

	void createDiagramFile(List<DiagramObject> classes);

}