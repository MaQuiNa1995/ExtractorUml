package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldDto {

	private List<ModifierEnum> modifiers;
	private String type;
	private String name;

}
