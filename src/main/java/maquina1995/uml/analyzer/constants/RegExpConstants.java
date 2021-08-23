package maquina1995.uml.analyzer.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegExpConstants {

	// Primitive Types
	public final String JAVA_PRIMITIVES = String.join("|", "boolean", "long", "byte", "int", "float", "double");

	public final String GENERIC_CORE_JAVA_OBJECT_PATTERN = "^(BiConsumer|BiFunction|BinaryOperator|BiPredicate|BooleanSupplier|Consumer|DoubleBinaryOperator|DoubleConsumer|DoubleFunction|DoublePredicate|DoubleSupplier|DoubleToIntFunction|DoubleToLongFunction|DoubleUnaryOperator|Function|IntBinaryOperator|IntConsumer|IntFunction|IntPredicate|IntSupplier|IntToDoubleFunction|IntToLongFunction|IntUnaryOperator|LongBinaryOperator|LongConsumer|LongFunction|LongPredicate|LongSupplier|LongToDoubleFunction|LongToIntFunction|LongUnaryOperator|ObjDoubleConsumer|ObjIntConsumer|ObjLongConsumer|Predicate|Supplier|ToDoubleBiFunction|ToDoubleFunction|ToIntBiFunction|ToIntFunction|ToLongBiFunction|ToLongFunction|UnaryOperator|Optional)";

	// Classes
	public final String JAVA_LANG_REFLECT_CLASSES = String.join("|", "AccessibleObject", "Array", "Constructor",
	        "Executable", "Field", "Method", "Modifier", "Parameter", "Proxy", "ReflectPermission");
	public final String JAVA_LANG_REF_CLASSES = String.join("|", "PhantomReference", "Reference", "ReferenceQueue",
	        "SoftReference", "WeakReference");
	public final String JAVA_LANG_MANAGEMENT_CLASSES = String.join("|", "LockInfo", "ManagementFactory",
	        "ManagementPermission", "MemoryNotificationInfo", "MemoryUsage", "MonitorInfo", "ThreadInfo");
	public final String JAVA_LANG_INVOKE_CLASSES = String.join("|", "CallSite", "ConstantCallSite", "LambdaMetafactory",
	        "MethodHandle", "MethodHandleProxies", "MethodHandles", "MethodHandles.Lookup", "MethodType",
	        "MutableCallSite", "SerializedLambda", "SwitchPoint", "VolatileCallSite");
	public final String JAVA_LANG_INSTRUMENT_CLASSES = "ClassDefinition";
	public final String JAVA_LANG_CLASSES = String.join("|", "Boolean", "Byte", "Character", "Character.Subset",
	        "Character.UnicodeBlock", "Class", "ClassLoader", "ClassValue", "Compiler", "Double", "Enum", "Float",
	        "InheritableThreadLocal", "Integer", "Long", "Math", "Number", "Object", "Package", "Process",
	        "ProcessBuilder", "ProcessBuilder.Redirect", "Runtime", "RuntimePermission", "SecurityManager", "Short",
	        "StackTraceElement", "StrictMath", "String", "StringBuffer", "StringBuilder", "System", "Thread",
	        "ThreadGroup", "ThreadLocal", "Throwable", "Void", "void", "Exception");

	// Interfaces
	public final String JAVA_LANG_INTERFACES = String.join("|", "Appendable", "AutoCloseable", "CharSequence",
	        "Cloneable", "Comparable", "Iterable", "Readable", "Runnable", "Thread.UncaughtExceptionHandler");
	public final String JAVA_LANG_ANNOTATIONS_INTERFACES = "Annotation";
	public final String JAVA_LANG_INSTRUMENT_INTERFACES = String.join("|", "ClassFileTransformer", "Instrumentation");
	public final String JAVA_LANG_INVOKE_INTERFACES = "MethodHandleInfo";
	public final String JAVA_LANG_MANAGEMENT_INTERFACES = String.join("|", "BufferPoolMXBean", "ClassLoadingMXBean",
	        "CompilationMXBean", "GarbageCollectorMXBean", "MemoryManagerMXBean", "MemoryMXBean", "MemoryPoolMXBean",
	        "OperatingSystemMXBean", "PlatformLoggingMXBean", "PlatformManagedObject", "RuntimeMXBean", "ThreadMXBean");
	public final String JAVA_LANG_REFLECT_INTERFACES = String.join("|", "AnnotatedArrayType", "AnnotatedElement",
	        "AnnotatedParameterizedType", "AnnotatedType", "AnnotatedTypeVariable", "AnnotatedWildcardType",
	        "GenericArrayType", "GenericDeclaration", "InvocationHandler", "Member", "ParameterizedType", "Type",
	        "TypeVariable", "WildcardType");

	// Classes + Interfaces
	public final String JAVA_LANG_REFLECT = String.join("|", JAVA_LANG_REFLECT_CLASSES, JAVA_LANG_REFLECT_INTERFACES);
	public final String JAVA_LANG_MANAGEMENT = String.join("|", JAVA_LANG_MANAGEMENT_CLASSES,
	        JAVA_LANG_MANAGEMENT_INTERFACES);
	public final String JAVA_LANG_INVOKE = String.join("|", JAVA_LANG_INVOKE_CLASSES, JAVA_LANG_INVOKE_INTERFACES);
	public final String JAVA_LANG_INTRUMENT = String.join("|", JAVA_LANG_INSTRUMENT_CLASSES,
	        JAVA_LANG_INSTRUMENT_INTERFACES);
	public final String JAVA_LANG = String.join("|", JAVA_LANG_CLASSES, JAVA_LANG_INTERFACES);

	public final String JAVA_LANG_REG_EXP = "^(" + String.join("|", JAVA_LANG_REFLECT, JAVA_LANG_MANAGEMENT,
	        JAVA_LANG_INVOKE, JAVA_LANG_INTRUMENT, JAVA_LANG, JAVA_PRIMITIVES) + ")$";

	public final String JAVA_CORE_PACKAGE_REGEXP = "^(((java\\.)|(javax\\.)|(org\\.ietf\\.jgss)|(org\\.omg\\.)|(org\\.w3c\\.dom)|(org\\.xml\\.sax)).*)";
}
