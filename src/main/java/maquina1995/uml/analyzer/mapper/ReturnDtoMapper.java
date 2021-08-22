package maquina1995.uml.analyzer.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.ClassArgumentDto;
import maquina1995.uml.analyzer.dto.ReturnDto;
import maquina1995.uml.analyzer.service.PackageService;

@Component
@RequiredArgsConstructor
public class ReturnDtoMapper {

	private final PackageService packageService;
	private final ClassArgumentDtoMapper classArgumentDtoMapper;

	public ReturnDto processReturn(Type type) {

		List<ClassArgumentDto> classArguments = new ArrayList<>();

		if (type.getClass()
		        .isAssignableFrom(ClassOrInterfaceType.class)) {
			classArguments = classArgumentDtoMapper.processClassArguments(type);
		}

		String name = type.asString();
		if (name.contains("<")) {
			int genericCharIndex = name.indexOf("<");
			name = name.substring(0, genericCharIndex);
		}

		return ReturnDto.builder()
		        .name(name)
		        .isFromJavaCore(packageService.isJavaCoreClass(type, name))
		        .classParameters(classArguments)
		        .build();

	}

}
