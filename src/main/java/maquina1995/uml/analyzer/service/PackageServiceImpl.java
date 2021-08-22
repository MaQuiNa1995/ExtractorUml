package maquina1995.uml.analyzer.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;

import maquina1995.uml.analyzer.constants.RegExpConstants;

@Service
public class PackageServiceImpl implements PackageService {

	@Override
	public boolean isJavaCoreClass(Node type, String name) {

		List<String> imports = this.getImportsFromClass(type);

		boolean isFromJavaLang = name.matches(RegExpConstants.JAVA_LANG_REG_EXP);
		boolean isFromJavaCore = this.getImportFromClass(imports, name)
		        .matches(RegExpConstants.JAVA_CORE_PACKAGE_REGEXP);

		return isFromJavaLang || isFromJavaCore;
	}

	private String getImportFromClass(List<String> importsAsString, String name) {

		AtomicInteger lastPointIndex = new AtomicInteger();

		return importsAsString.stream()
		        .filter(this.importFilterByClassName(name, lastPointIndex))
		        .findFirst()
		        .map(fullImport -> fullImport.substring(0, lastPointIndex.get()))
		        .orElse("");

	}

	private Predicate<String> importFilterByClassName(String name, AtomicInteger lastPointIndex) {
		return fullImport -> {
			lastPointIndex.set(fullImport.lastIndexOf("."));
			final String className = StringUtils.unqualify(fullImport);
			return className.equals(name);
		};
	}

	private List<String> getImportsFromClass(Node classOrInterface) {
		return classOrInterface.findRootNode()
		        .findAll(ImportDeclaration.class)
		        .stream()
		        .map(ImportDeclaration::getNameAsString)
		        .collect(Collectors.toList());
	}

}
