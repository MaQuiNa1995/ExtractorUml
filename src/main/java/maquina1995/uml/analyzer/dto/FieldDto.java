package maquina1995.uml.analyzer.dto;

import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public String toString() {
		String modifiersSanitized = this.modifiers.isEmpty() ? ""
		        : this.modifiers.stream()
		                .collect(Collectors.joining(" "));

		return this.accessModifier + modifiersSanitized + " " + this.type + " " + this.name;
	}

}
