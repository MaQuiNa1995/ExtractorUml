package maquina1995.uml.analyzer.dto;

import lombok.Getter;

@Getter
public enum ModifierEnum {

	PUBLIC("+"), PROTECTED("#"), PRIVATE("-"), STATIC("static"), PACKAGE("~"), ABSTRACT("abstract"), FINAL("final");

	private String modifier;

	ModifierEnum(final String modifier) {
		this.modifier = modifier;
	}

}
