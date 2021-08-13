package maquina1995.uml.analyzer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterDto {

	private String name;
	private Boolean isFromJavaCore;

	@Override
	public String toString() {
		return this.name;
	}

}
