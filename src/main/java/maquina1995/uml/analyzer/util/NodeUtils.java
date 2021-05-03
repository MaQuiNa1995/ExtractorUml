package maquina1995.uml.analyzer.util;

import java.util.List;
import java.util.function.Predicate;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NodeUtils {

	public String parseAccesModifier(String modifier) {
		String modifierParsed;
		switch (modifier.toLowerCase()) {

		case "public":
			modifierParsed = "+";
			break;
		case "private":
			modifierParsed = "-";
			break;
		case "protected":
			modifierParsed = "#";
			break;
		default:
			modifierParsed = "~";
			break;
		}

		return modifierParsed;
	}

	public StringBuilder parseSpecialmodifiers(List<Modifier> modifiersnodeList) {
		StringBuilder modifiers = new StringBuilder("");

		modifiersnodeList.stream()
		        .map(Modifier::getKeyword)
		        .filter(NodeUtils.createModifierFilter())
		        .forEach(modifier -> modifiers.append(modifier)
		                .append(" "));

		return modifiers;
	}

	private Predicate<Keyword> createModifierFilter(Keyword keyword) {
		return modifier -> modifier.equals(keyword);
	}

	private Predicate<Keyword> createModifierFilter() {
		Predicate<Keyword> isFinal = NodeUtils.createModifierFilter(Keyword.FINAL);
		Predicate<Keyword> isStatic = NodeUtils.createModifierFilter(Keyword.STATIC);
		Predicate<Keyword> isDefault = NodeUtils.createModifierFilter(Keyword.DEFAULT);
		Predicate<Keyword> isAbstract = NodeUtils.createModifierFilter(Keyword.ABSTRACT);
		Predicate<Keyword> isNative = NodeUtils.createModifierFilter(Keyword.NATIVE);
		Predicate<Keyword> isStrictfp = NodeUtils.createModifierFilter(Keyword.STRICTFP);
		Predicate<Keyword> isSynchronized = NodeUtils.createModifierFilter(Keyword.SYNCHRONIZED);
		Predicate<Keyword> isTransient = NodeUtils.createModifierFilter(Keyword.TRANSIENT);
		Predicate<Keyword> isTransitive = NodeUtils.createModifierFilter(Keyword.TRANSITIVE);
		Predicate<Keyword> isVolatile = NodeUtils.createModifierFilter(Keyword.VOLATILE);

		return isFinal.or(isStatic)
		        .or(isDefault)
		        .or(isAbstract)
		        .or(isNative)
		        .or(isStrictfp)
		        .or(isSynchronized)
		        .or(isTransient)
		        .or(isTransitive)
		        .or(isVolatile);
	}

}
