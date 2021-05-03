package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldDto {

	private String accessModifier;
	private List<String> modifiers;
	private String type;
	private String name;
	private Boolean isProjectObject;

}
