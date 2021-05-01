package maquina1995.uml.analyzer.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClassDto {

	private String name;
	private List<String> implement;
	private List<String> extended;
}
