package maquina1995.uml.analyzer.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DiagramObjectDto {

	protected String name;
	protected String modifiers;
	protected String accessModifier;
	protected List<String> implement = new ArrayList<>();
	protected List<String> extended = new ArrayList<>();
	protected List<FieldDto> fields = new ArrayList<>();
	protected List<MethodDto> methods = new ArrayList<>();

}