package maquina1995.uml.analyzer.global;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Global {

	public final ThreadLocal<TypeSolver> TYPE_SOLVER;

	static {
		TYPE_SOLVER = new ThreadLocal<>();
	}

}
