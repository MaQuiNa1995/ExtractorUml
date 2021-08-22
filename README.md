Proyecto usado para analizar proyectos java y sacar el modelo en imagen formato SVG (imagen vectorial) o txt

Este proyecto usa PlantUml: https://plantuml.com/es/

para usar este proyecto solo tienes que:
1. clonarlo
2. compilar (mvn package)
3. ir a la carpeta target

Ahora tenemos 2 modos de ejecución:

Por consola de comandos ejecutar el jar pasándole como argumento una string que contiene la ruta de

y otro en el que te coge un txt en el que tengas el código referente a plantUml para poder sacar una imagen a traves de ahí sin tener que ir a la página oficial: http://www.plantuml.com/plantuml/uml/SyfFKj2rKt3CoKnELR1Io4ZDoSa70000 que por cierto tiene límite de longitud si en la web metes mucho contenido la misma petará

Esto te creará 2 archivos diagram.svg y diagram.txt que en este caso el diagram.txt te sacará lo mismo que le has dado (ya lo arreglaré ;)) 

Sacar la imagen svg de un proyecto java que tengas en local

Por consola de comandos ejecutar el jar pasándole como argumento una string que contiene la ruta de tu proyecto por ejemplo
	D:\Users\MaQuiNa1995\Workspace\Maquina1995_Github\uml.analyzer
esto te creará 2 archivos diagram.svg y diagram.txt los dos hacen referencia a lo mismo si quieres modificar el txt porque no te gusta como queda podrías hacerlo modificando el txt y usando el método 1 de ejecución descrito anteriormente para volver a generar 

OJO: puede contener algun error de casos no contemplados, podeis abrir issues en github y eventualmente cuando pueda me pongo a mirarlas :)

Un ejemplo de la imagen de este proyecto:

![alt text](https://github.com/MaQuiNa1995/ExtractorUml/blob/master/diagram.svg?raw=true)
