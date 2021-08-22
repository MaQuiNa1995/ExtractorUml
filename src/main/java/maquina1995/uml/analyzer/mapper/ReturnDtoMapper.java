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
import maquina1995.uml.analyzer.util.SanitizeUtils;

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

		String name = SanitizeUtils.sanitizeTypeName(type);

		return ReturnDto.builder()
		        .name(name)
		        .isFromJavaCore(packageService.isJavaCoreClass(type, name))
		        .classParameters(classArguments)
		        .build();

	}

}
