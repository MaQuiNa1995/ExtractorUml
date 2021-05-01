package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class JavaTypeDto {

	private List<ModifierEnum> modifiers;

	private List<String> implement;
	private List<String> extended;

	private String name;

	private List<FieldDto> fields;
//	private List<MethodDto> methods;

}