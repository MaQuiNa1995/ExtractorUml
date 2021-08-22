package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FieldDto {

	private String accessModifier;
	private List<String> modifiers;
	private String type;
	private String name;
	private Boolean isProjectObject;

}
