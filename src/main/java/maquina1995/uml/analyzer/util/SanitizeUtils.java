package maquina1995.uml.analyzer.util;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SanitizeUtils {

	public String sanitizeTypeName(Type type) {
		String name = type.asString();
		if (name.contains("<")) {
			int genericCharIndex = name.indexOf("<");
			name = name.substring(0, genericCharIndex);
		}
		return name.replace("[", "")
		        .replace("]", "");
	}

	public String sanitizeTypeName(Parameter type) {
		String name = type.getTypeAsString();
		if (name.contains("<")) {
			int genericCharIndex = name.indexOf("<");
			name = name.substring(0, genericCharIndex);
		}
		return name.replace("[", "")
		        .replace("]", "");
	}

}
