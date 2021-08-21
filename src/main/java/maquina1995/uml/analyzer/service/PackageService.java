package maquina1995.uml.analyzer.service;

import com.github.javaparser.ast.Node;

public interface PackageService {

	boolean isjavaCoreClass(Node type, String name);

}