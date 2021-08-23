package maquina1995.uml.analyzer.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SimpleTypeDto {

	protected String name;
	protected String type;
	protected Boolean isFromJavaCore;
	@Singular
	protected List<ClassArgumentDto> classParameters;

	@Override
	public String toString() {
		String typeName = this.type == null ? "" : this.type;

		String typeArgumentProcessed = "<" + classParameters.stream()
		        .map(SimpleTypeDto::getType)
		        .collect(Collectors.joining(" ")) + ">";

		String typeArgument = classParameters.isEmpty() ? "" : typeArgumentProcessed;

		return typeName + typeArgument + " " + this.name;
	}

}