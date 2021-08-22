package maquina1995.uml.analyzer.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import lombok.RequiredArgsConstructor;
import maquina1995.uml.analyzer.dto.ClassArgumentDto;
import maquina1995.uml.analyzer.service.PackageService;

@Component
@RequiredArgsConstructor
public class ClassArgumentDtoMapper {

	private final PackageService packageService;

	public List<ClassArgumentDto> processClassArguments(Type type) {

		NodeList<Type> classArguments = ((ClassOrInterfaceType) type).getTypeArguments()
		        .orElse(new NodeList<>());

		return classArguments.stream()
		        .map(ClassOrInterfaceType.class::cast)
		        .map(classArgument -> ClassArgumentDto.builder()
		                .name(classArgument.getNameAsString())
		                .type(classArgument.getElementType()
		                        .toString())
		                .isFromJavaCore(packageService.isJavaCoreClass(classArgument, classArgument.getNameAsString()))
		                .classParameters(this.processClassArguments(classArgument))
		                .build())
		        .collect(Collectors.toList());
	}

	public List<ClassArgumentDto> processClassArguments(Parameter parameter) {

//		parameter.get
//		
//		((ClassOrInterfaceType) parameter).getTypeArguments()
//        .orElse(new NodeList<>());

		NodeList<Type> classArguments = new NodeList<>();

		return classArguments.stream()
		        .map(ClassOrInterfaceType.class::cast)
		        .map(classArgument -> ClassArgumentDto.builder()
		                .name(classArgument.getNameAsString())
		                .type(classArgument.getElementType()
		                        .toString())
		                .isFromJavaCore(packageService.isJavaCoreClass(classArgument, classArgument.getNameAsString()))
		                .classParameters(this.processClassArguments(classArgument))
		                .build())
		        .collect(Collectors.toList());
	}

}
