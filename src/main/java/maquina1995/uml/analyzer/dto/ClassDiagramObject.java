package maquina1995.uml.analyzer.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ClassDiagramObject {

	private String accessModifier;
	private final List<String> modifiers;

	private final List<String> implement;
	private final List<String> extended;

	private String name;

	private final List<FieldDto> fields;
	private final List<String> methods;

	protected ClassDiagramObject() {
		this.modifiers = new ArrayList<>();
		this.implement = new ArrayList<>();
		this.extended = new ArrayList<>();
		this.fields = new ArrayList<>();
		this.methods = new ArrayList<>();
	}

}