package maquina1995.uml.analyzer.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.javaparser.ast.body.FieldDeclaration;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.FieldDto;
import maquina1995.uml.analyzer.service.FieldService;

@Component
@RequiredArgsConstructor
public class FieldDtoMapper {

	private final FieldService fieldService;

	public List<FieldDto> parseClassFields(List<FieldDeclaration> fields) {
		return fields.stream()
		        .map(fieldService::analyzeField)
		        .flatMap(List::stream)
		        .collect(Collectors.toList());
	}
}
