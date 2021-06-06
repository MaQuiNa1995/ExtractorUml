package maquina1995.uml.analyzer.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MethodDto {
	private String name;
	private String accessModifier;
	private List<String> parameters = new ArrayList<>();
	private String returnType;
	private String modifiers;

	@Override
	public String toString() {
		return this.accessModifier + " " + this.modifiers + " " + this.returnType + " " + this.name + " ("
		        + String.join(",", this.parameters) + ")";
	}

}
