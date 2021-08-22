package maquina1995.uml.analyzer.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.ClassArgumentDto;
import maquina1995.uml.analyzer.dto.ParameterDto;
import maquina1995.uml.analyzer.service.PackageService;

@Component
@RequiredArgsConstructor
public class ParameterDtoMapper {

	private final PackageService packageService;
	private final ClassArgumentDtoMapper classArgumentDtoMapper;

	public ParameterDto createParameterDto(Parameter parameter) {

		List<ClassArgumentDto> classArguments = new ArrayList<>();

		if (parameter.getClass()
		        .isAssignableFrom(ClassOrInterfaceType.class)) {
			classArguments = classArgumentDtoMapper.processClassArguments(parameter);
		}

		String className = parameter.getTypeAsString()
		        .replace("[", "")
		        .replace("]", "");
		return ParameterDto.builder()
		        .name(className)
		        .isFromJavaCore(packageService.isJavaCoreClass(parameter, className))
		        .classParameters(classArguments)
		        .build();
	}
}
