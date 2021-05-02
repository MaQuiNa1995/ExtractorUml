package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MethodDto {
	private String name;
	private List<String> parameters;

}
