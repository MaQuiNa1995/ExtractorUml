package maquina1995.uml.analyzer.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MethodDto {
	private String name;
	private String accessModifier;
	private List<ParameterDto> parameters;
	private String returnType;
	private Boolean isReturnFromJavaCore;
	private String modifiers;

	@Override
	public String toString() {
		return String.join(" ", this.accessModifier, this.modifiers, this.returnType, this.name) + " ("
		        + String.join(",", this.parameters.stream()
		                .map(ParameterDto::getName)
		                .collect(Collectors.toList()))
		        + ")";
	}

}
