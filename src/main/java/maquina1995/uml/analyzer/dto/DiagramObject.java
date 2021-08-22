package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DiagramObject {

	protected String name;
	protected String modifiers;
	protected String accessModifier;
	protected List<String> implement;
	protected List<String> extended;
	protected List<FieldDto> fields;
	protected List<MethodDto> methods;

}