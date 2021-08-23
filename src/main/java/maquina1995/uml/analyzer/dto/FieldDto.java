package maquina1995.uml.analyzer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FieldDto extends SimpleTypeDto {

	private String accessModifier;
	private String modifiers;

	@Override
	public String toString() {
		String modifiersSanitized = this.modifiers.isEmpty() ? "" : this.modifiers;

		return this.accessModifier + modifiersSanitized + super.toString();
	}

}
