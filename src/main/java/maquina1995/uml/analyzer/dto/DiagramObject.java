package maquina1995.uml.analyzer.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DiagramObject {

	protected String name;
	protected String modifiers;
	protected String accessModifier;
	protected final List<String> implement = new ArrayList<>();
	protected final List<String> extended = new ArrayList<>();
	protected final List<FieldDto> fields = new ArrayList<>();
	protected final List<MethodDto> methods = new ArrayList<>();

}