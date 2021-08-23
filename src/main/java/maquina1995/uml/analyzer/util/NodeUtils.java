package maquina1995.uml.analyzer.util;

import java.util.List;
import java.util.function.Predicate;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NodeUtils {

	public String parseAccesModifier(AccessSpecifier accessSpecifier) {
		String modifierParsed;

		switch (accessSpecifier.toString()) {
		case "PUBLIC":
			modifierParsed = "+";
			break;
		case "PRIVATE":
			modifierParsed = "-";
			break;
		case "PROTECTED":
			modifierParsed = "#";
			break;
		default:
			modifierParsed = "~";
			break;
		}

		return modifierParsed;
	}

	public String parseModifiers(List<Modifier> modifiersNodeList) {
		StringBuilder modifiers = new StringBuilder("");

		modifiersNodeList.stream()
		        .map(Modifier::getKeyword)
		        .filter(NodeUtils.createModifierFilter())
		        .map(Keyword::toString)
		        .map(e -> e.replace("ABSTRACT", "{abstract}")
		                .replace("STATIC", "{static}"))
		        .forEach(modifier -> modifiers.append(modifier)
		                .append(" "));

		return modifiers.toString()
		        .toLowerCase()
		        .trim();
	}

	public String parseClassModifiers(List<Modifier> modifiersNodeList) {
		StringBuilder modifiers = new StringBuilder();

		modifiersNodeList.stream()
		        .map(Modifier::getKeyword)
		        .filter(NodeUtils.createModifierFilter())
		        .map(Keyword::toString)
		        .forEach(modifier -> modifiers.append(modifier)
		                .append(" "));

		if (modifiers.length() != 0) {
			modifiers.append(" ");
		}

		return modifiers.toString()
		        .toLowerCase();
	}

	private Predicate<Keyword> createModifierFilter() {
		Predicate<Keyword> isStatic = NodeUtils.createModifierFilter(Keyword.STATIC);
		Predicate<Keyword> isDefault = NodeUtils.createModifierFilter(Keyword.DEFAULT);
		Predicate<Keyword> isAbstract = NodeUtils.createModifierFilter(Keyword.ABSTRACT);
		Predicate<Keyword> isNative = NodeUtils.createModifierFilter(Keyword.NATIVE);
		Predicate<Keyword> isStrictfp = NodeUtils.createModifierFilter(Keyword.STRICTFP);
		Predicate<Keyword> isSynchronized = NodeUtils.createModifierFilter(Keyword.SYNCHRONIZED);
		Predicate<Keyword> isTransient = NodeUtils.createModifierFilter(Keyword.TRANSIENT);
		Predicate<Keyword> isTransitive = NodeUtils.createModifierFilter(Keyword.TRANSITIVE);
		Predicate<Keyword> isVolatile = NodeUtils.createModifierFilter(Keyword.VOLATILE);

		return isStatic.or(isDefault)
		        .or(isAbstract)
		        .or(isNative)
		        .or(isStrictfp)
		        .or(isSynchronized)
		        .or(isTransient)
		        .or(isTransitive)
		        .or(isVolatile);
	}

	private Predicate<Keyword> createModifierFilter(Keyword keyword) {
		return modifier -> modifier.equals(keyword);
	}

}
