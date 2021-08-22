package maquina1995.uml.analyzer.dto;

import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ReturnDto extends SimpleTypeDto {

	@Override
	public String toString() {

		String typeArgumentProcessed = "<" + classParameters.stream()
		        .map(SimpleTypeDto::getType)
		        .collect(Collectors.joining(" ")) + ">";

		String typeArgument = classParameters.isEmpty() ? "" : typeArgumentProcessed;

		return this.name + typeArgument;
	}

}
