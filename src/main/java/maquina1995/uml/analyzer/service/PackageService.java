package maquina1995.uml.analyzer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;

import maquina1995.uml.analyzer.constants.RegExpConstants;

@Service
public class PackageService {

	public boolean isjavaCoreClass(Node type, String name) {

		List<String> imports = this.getImportsFromClass(type);

		return name.matches(RegExpConstants.JAVA_CORE_REG_EXP) || this.getImportFromClass(imports, name)
		        .matches(RegExpConstants.JAVA_CORE_PACKAGE_REGEXP);
	}

//	public boolean isjavaCoreClass(EnumDeclaration enumeration) {
//
//		List<String> imports = this.getImportsFromClass(enumeration);
//		String enumName = enumeration.getNameAsString();
//
//		return enumName.matches(RegExpConstants.JAVA_CORE_REG_EXP) || this.getImportFromClass(imports, enumName)
//		        .matches(RegExpConstants.JAVA_CORE_PACKAGE_REGEXP);
//	}

	private String getImportFromClass(List<String> importsAsString, String name) {

		String importDeclaration = "";
		for (String fullImport : importsAsString) {
			final int lastPointIndex = fullImport.lastIndexOf(".");
			final String className = StringUtils.unqualify(fullImport);
			if (className.equals(name)) {
				importDeclaration = fullImport.substring(0, lastPointIndex);
				break;
			}
		}
		return importDeclaration;
	}

	private List<String> getImportsFromClass(Node classOrInterface) {
		return classOrInterface.findRootNode()
		        .findAll(ImportDeclaration.class)
		        .stream()
		        .map(ImportDeclaration::getNameAsString)
		        .collect(Collectors.toList());
	}

}
