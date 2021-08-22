package maquina1995.uml.analyzer.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.javaparser.ast.body.MethodDeclaration;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.MethodDto;
import maquina1995.uml.analyzer.dto.ParameterDto;
import maquina1995.uml.analyzer.dto.ReturnDto;
import maquina1995.uml.analyzer.util.NodeUtils;

@Component
@RequiredArgsConstructor
public class MethodDtoMapper {

	private final ReturnDtoMapper returnDtoMapper;
	private final ParameterDtoMapper parameterDtoMapper;

	public List<MethodDto> parseMethodSignature(List<MethodDeclaration> methods) {
		return methods.stream()
		        .map(this::createMethodFromDeclaration)
		        .collect(Collectors.toList());
	}

	private MethodDto createMethodFromDeclaration(MethodDeclaration methodDeclaration) {

		ReturnDto returnType = returnDtoMapper.processReturn(methodDeclaration.getType());
		String className = methodDeclaration.getNameAsString();
		String modifiers = NodeUtils.parseModifiers(methodDeclaration.getModifiers())
		        .toString();
		String accessModifiers = NodeUtils.parseAccesModifier(methodDeclaration.getAccessSpecifier()
		        .asString());

		List<ParameterDto> parameterDtos = methodDeclaration.getParameters()
		        .stream()
		        .map(parameterDtoMapper::createParameterDto)
		        .collect(Collectors.toList());

		return MethodDto.builder()
		        .returnType(returnType)
		        .name(className)
		        .accessModifier(accessModifiers)
		        .modifiers(modifiers)
		        .parameters(parameterDtos)
		        .build();
	}
}
