package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SimpleTypeDto {

	protected String name;
	protected Boolean isFromJavaCore;
	@Singular
	protected List<SimpleTypeDto> classParameters;

	@Override
	public String toString() {
		return this.name;
	}

}