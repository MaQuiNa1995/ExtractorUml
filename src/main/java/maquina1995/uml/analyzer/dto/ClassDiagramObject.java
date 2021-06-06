package maquina1995.uml.analyzer.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ClassDiagramObject {

	private String accessModifier;
	private String modifiers;
	private final List<String> implement = new ArrayList<>();
	private final List<String> extended = new ArrayList<>();
	private String name;
	private final List<FieldDto> fields = new ArrayList<>();
	private final List<String> methods = new ArrayList<>();

}