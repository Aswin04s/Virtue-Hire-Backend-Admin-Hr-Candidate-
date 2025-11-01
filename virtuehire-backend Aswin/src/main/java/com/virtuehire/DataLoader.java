package com.virtuehire;

import com.virtuehire.model.Question;
import com.virtuehire.repository.AssessmentResultRepository;
import com.virtuehire.repository.CandidateRepository;
import com.virtuehire.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final CandidateRepository candidateRepository;
    private final AssessmentResultRepository assessmentResultRepository;

    public DataLoader(QuestionRepository questionRepository,
                      CandidateRepository candidateRepository,
                      AssessmentResultRepository assessmentResultRepository) {
        this.questionRepository = questionRepository;
        this.candidateRepository = candidateRepository;
        this.assessmentResultRepository = assessmentResultRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only load questions if no questions exist - DON'T DELETE EXISTING DATA
        if (questionRepository.count() == 0) {
            loadQuestions();
            System.out.println("Questions loaded successfully!");
        } else {
            System.out.println("Questions already exist, skipping data loading.");
        }
    }

    private void loadQuestions() {

        int EASY = 1, MEDIUM = 2, HARD = 3;
        String SUBJECT = "Java"; // All questions are for Java subject

        List<Question> questions = Arrays.asList(
                // ================= EASY =================
                new Question(SUBJECT, EASY, "What is the default value of int in Java?",
                        Arrays.asList("0", "1", "null", "-1"), "0"),
                new Question(SUBJECT, EASY, "Which keyword is used to define a class?",
                        Arrays.asList("class", "Class", "define", "object"), "class"),
                new Question(SUBJECT, EASY, "Which method is the entry point for a Java program?",
                        Arrays.asList("main()", "start()", "run()", "init()"), "main()"),
                new Question(SUBJECT, EASY, "Which operator is used for addition?",
                        Arrays.asList("+", "-", "*", "/"), "+"),
                new Question(SUBJECT, EASY, "Which keyword is used to inherit a class?",
                        Arrays.asList("extends", "implements", "inherits", "super"), "extends"),
                new Question(SUBJECT, EASY, "Which data type is used to store true/false values?",
                        Arrays.asList("boolean", "int", "char", "String"), "boolean"),
                new Question(SUBJECT, EASY, "What is the size of a char in Java?",
                        Arrays.asList("16 bit", "8 bit", "32 bit", "64 bit"), "16 bit"),
                new Question(SUBJECT, EASY, "Which keyword is used to declare a constant?",
                        Arrays.asList("final", "const", "static", "immutable"), "final"),
                new Question(SUBJECT, EASY, "Which loop executes at least once?",
                        Arrays.asList("for", "while", "do-while", "foreach"), "do-while"),
                new Question(SUBJECT, EASY, "Which package contains fundamental classes like String and Math?",
                        Arrays.asList("java.lang", "java.util", "java.io", "java.net"), "java.lang"),
                new Question(SUBJECT, EASY, "Which keyword is used to create an object?",
                        Arrays.asList("new", "create", "object", "init"), "new"),
                new Question(SUBJECT, EASY, "Which symbol is used for single-line comments?",
                        Arrays.asList("//", "/*", "#", "--"), "//"),
                new Question(SUBJECT, EASY, "Which method is used to print to console?",
                        Arrays.asList("System.out.println()", "print()", "console.log()", "echo()"), "System.out.println()"),
                new Question(SUBJECT, EASY, "Which operator is used for equality check?",
                        Arrays.asList("==", "=", "!=", "==="), "=="),
                new Question(SUBJECT, EASY, "Which data type is used for decimal numbers?",
                        Arrays.asList("float", "int", "char", "boolean"), "float"),
                new Question(SUBJECT, EASY, "Which keyword is used to prevent inheritance?",
                        Arrays.asList("final", "static", "abstract", "private"), "final"),
                new Question(SUBJECT, EASY, "What is the default value of boolean?",
                        Arrays.asList("false", "true", "0", "null"), "false"),
                new Question(SUBJECT, EASY, "Which of these is a valid identifier?",
                        Arrays.asList("myVar", "123var", "var-name", "class"), "myVar"),
                new Question(SUBJECT, EASY, "Which loop is used when the number of iterations is known?",
                        Arrays.asList("for", "while", "do-while", "foreach"), "for"),
                new Question(SUBJECT, EASY, "Which keyword is used to inherit multiple interfaces?",
                        Arrays.asList("implements", "extends", "interface", "inherit"), "implements"),
                new Question(SUBJECT, EASY, "Which is the correct syntax to declare an array?",
                        Arrays.asList("int[] arr;", "arr int[];", "array int arr;", "int arr[];"), "int[] arr;"),
                new Question(SUBJECT, EASY, "Which keyword is used to exit a loop?",
                        Arrays.asList("break", "exit", "stop", "return"), "break"),
                new Question(SUBJECT, EASY, "Which operator is used for increment?",
                        Arrays.asList("++", "+", "--", "+="), "++"),
                new Question(SUBJECT, EASY, "Which class is the superclass of all classes?",
                        Arrays.asList("Object", "Class", "Base", "Super"), "Object"),
                new Question(SUBJECT, EASY, "Which method converts a string to integer?",
                        Arrays.asList("Integer.parseInt()", "String.toInt()", "int.parse()", "Integer.toInt()"), "Integer.parseInt()"),
                new Question(SUBJECT, EASY, "Which keyword is used to handle exceptions?",
                        Arrays.asList("try", "catch", "finally", "throw"), "try"),
                new Question(SUBJECT, EASY, "Which operator is used for logical AND?",
                        Arrays.asList("&", "&&", "|", "||"), "&&"),
                new Question(SUBJECT, EASY, "Which data type stores text values?",
                        Arrays.asList("String", "int", "char", "boolean"), "String"),
                new Question(SUBJECT, EASY, "Which method compares two strings for equality?",
                        Arrays.asList("equals()", "==", "compare()", "match()"), "equals()"),
                new Question(SUBJECT, EASY, "Which keyword is used to define a package?",
                        Arrays.asList("package", "import", "include", "module"), "package"),

                // ================= MEDIUM =================
                new Question(SUBJECT, MEDIUM, "Which interface does HashMap implement?",
                        Arrays.asList("Map", "List", "Set", "Collection"), "Map"),
                new Question(SUBJECT, MEDIUM, "What is the output of 10 / 3 in Java?",
                        Arrays.asList("3", "3.33", "3.0", "Error"), "3"),
                new Question(SUBJECT, MEDIUM, "Which collection class maintains insertion order?",
                        Arrays.asList("LinkedHashMap", "HashMap", "TreeMap", "Hashtable"), "LinkedHashMap"),
                new Question(SUBJECT, MEDIUM, "Which keyword prevents a method from being overridden?",
                        Arrays.asList("final", "static", "private", "abstract"), "final"),
                new Question(SUBJECT, MEDIUM, "Which exception is thrown when dividing by zero?",
                        Arrays.asList("ArithmeticException", "NullPointerException", "IOException", "ClassNotFoundException"), "ArithmeticException"),
                new Question(SUBJECT, MEDIUM, "Which method is used to start a thread?",
                        Arrays.asList("start()", "run()", "init()", "execute()"), "start()"),
                new Question(SUBJECT, MEDIUM, "What is the default value of an object reference?",
                        Arrays.asList("null", "0", "undefined", "false"), "null"),
                new Question(SUBJECT, MEDIUM, "Which keyword is used to access the parent class constructor?",
                        Arrays.asList("super", "this", "parent", "base"), "super"),
                new Question(SUBJECT, MEDIUM, "Which class is used for random numbers?",
                        Arrays.asList("Random", "Math", "Integer", "Number"), "Random"),
                new Question(SUBJECT, MEDIUM, "Which method compares two strings ignoring case?",
                        Arrays.asList("equalsIgnoreCase()", "compare()", "equals()", "match()"), "equalsIgnoreCase()"),
                new Question(SUBJECT, MEDIUM, "Which keyword is used to implement an interface?",
                        Arrays.asList("implements", "extends", "interface", "inherit"), "implements"),
                new Question(SUBJECT, MEDIUM, "What is the size of an int in Java?",
                        Arrays.asList("32 bit", "16 bit", "64 bit", "8 bit"), "32 bit"),
                new Question(SUBJECT, MEDIUM, "Which collection allows null keys?",
                        Arrays.asList("HashMap", "Hashtable", "TreeMap", "LinkedHashMap"), "HashMap"),
                new Question(SUBJECT, MEDIUM, "Which method returns the length of an array?",
                        Arrays.asList("length", "size()", "getLength()", "count()"), "length"),
                new Question(SUBJECT, MEDIUM, "Which keyword is used to refer to the current object?",
                        Arrays.asList("this", "super", "self", "current"), "this"),
                new Question(SUBJECT, MEDIUM, "Which class is used to read input from console?",
                        Arrays.asList("Scanner", "InputStream", "BufferedReader", "Console"), "Scanner"),
                new Question(SUBJECT, MEDIUM, "Which interface supports random access of elements?",
                        Arrays.asList("List", "Set", "Map", "Collection"), "List"),
                new Question(SUBJECT, MEDIUM, "Which keyword is used to create a thread by extending Thread class?",
                        Arrays.asList("extends", "implements", "new", "thread"), "extends"),
                new Question(SUBJECT, MEDIUM, "Which exception is checked in Java?",
                        Arrays.asList("IOException", "ArithmeticException", "NullPointerException", "ArrayIndexOutOfBoundsException"), "IOException"),
                new Question(SUBJECT, MEDIUM, "Which method is used to join threads?",
                        Arrays.asList("join()", "start()", "run()", "wait()"), "join()"),
                new Question(SUBJECT, MEDIUM, "Which method compares two objects?",
                        Arrays.asList("equals()", "compare()", "==", "match()"), "equals()"),
                new Question(SUBJECT, MEDIUM, "Which method removes an element from ArrayList?",
                        Arrays.asList("remove()", "delete()", "clear()", "discard()"), "remove()"),
                new Question(SUBJECT, MEDIUM, "Which class is immutable?",
                        Arrays.asList("String", "StringBuilder", "ArrayList", "HashMap"), "String"),
                new Question(SUBJECT, MEDIUM, "Which keyword is used to prevent subclassing?",
                        Arrays.asList("final", "static", "abstract", "private"), "final"),
                new Question(SUBJECT, MEDIUM, "Which method is called to finalize objects before garbage collection?",
                        Arrays.asList("finalize()", "destroy()", "cleanup()", "dispose()"), "finalize()"),
                new Question(SUBJECT, MEDIUM, "Which method is used to format output?",
                        Arrays.asList("printf()", "print()", "println()", "format()"), "printf()"),
                new Question(SUBJECT, MEDIUM, "Which keyword is used to throw exceptions?",
                        Arrays.asList("throw", "throws", "try", "catch"), "throw"),
                new Question(SUBJECT, MEDIUM, "Which interface is used to compare objects?",
                        Arrays.asList("Comparable", "Comparator", "Cloneable", "Runnable"), "Comparable"),
                new Question(SUBJECT, MEDIUM, "Which method is used to check if a collection is empty?",
                        Arrays.asList("isEmpty()", "size()", "checkEmpty()", "empty()"), "isEmpty()"),
                new Question(SUBJECT, MEDIUM, "Which class provides atomic operations in Java?",
                        Arrays.asList("AtomicInteger", "Integer", "Thread", "Object"), "AtomicInteger"),

                // ================= HARD =================
                new Question(SUBJECT, HARD, "What does 'transient' keyword do?",
                        Arrays.asList("Prevents serialization", "Prevents threading", "Prevents inheritance", "None"), "Prevents serialization"),
                new Question(SUBJECT, HARD, "Which interface is functional in Java?",
                        Arrays.asList("Runnable", "Serializable", "Cloneable", "Comparator"), "Runnable"),
                new Question(SUBJECT, HARD, "Which class is used to format dates?",
                        Arrays.asList("SimpleDateFormat", "DateFormat", "Calendar", "Date"), "SimpleDateFormat"),
                new Question(SUBJECT, HARD, "Which method is used to get class name at runtime?",
                        Arrays.asList("getClass()", "className()", "getName()", "getType()"), "getClass()"),
                new Question(SUBJECT, HARD, "What is the default capacity of ArrayList?",
                        Arrays.asList("10", "16", "0", "5"), "10"),
                new Question(SUBJECT, HARD, "Which collection is thread-safe?",
                        Arrays.asList("Hashtable", "HashMap", "ArrayList", "LinkedList"), "Hashtable"),
                new Question(SUBJECT, HARD, "Which keyword is used to handle checked exceptions?",
                        Arrays.asList("throws", "throw", "try", "catch"), "throws"),
                new Question(SUBJECT, HARD, "Which interface allows iteration over a collection?",
                        Arrays.asList("Iterator", "Iterable", "Collection", "Enumeration"), "Iterator"),
                new Question(SUBJECT, HARD, "Which collection sorts elements based on natural ordering?",
                        Arrays.asList("TreeSet", "HashSet", "LinkedHashSet", "ArrayList"), "TreeSet"),
                new Question(SUBJECT, HARD, "What does 'volatile' keyword do?",
                        Arrays.asList("Ensures visibility across threads", "Prevents inheritance", "Prevents serialization", "None"), "Ensures visibility across threads"),
                new Question(SUBJECT, HARD, "Which keyword is used to define an abstract class?",
                        Arrays.asList("abstract", "final", "interface", "class"), "abstract"),
                new Question(SUBJECT, HARD, "Which class is used for synchronization in Java?",
                        Arrays.asList("ReentrantLock", "Lock", "Object", "Thread"), "ReentrantLock"),
                new Question(SUBJECT, HARD, "Which method converts a string to uppercase?",
                        Arrays.asList("toUpperCase()", "upper()", "toupper()", "convertUpper()"), "toUpperCase()"),
                new Question(SUBJECT, HARD, "Which method returns a hash code for an object?",
                        Arrays.asList("hashCode()", "getHash()", "code()", "getCode()"), "hashCode()"),
                new Question(SUBJECT, HARD, "Which class represents a node in a linked list?",
                        Arrays.asList("Node", "ListNode", "LinkedNode", "Element"), "Node"),
                new Question(SUBJECT, HARD, "Which method removes duplicates in a Set?",
                        Arrays.asList("add()", "remove()", "clear()", "Sets inherently remove duplicates"), "Sets inherently remove duplicates"),
                new Question(SUBJECT, HARD, "Which interface is used for lambda expressions?",
                        Arrays.asList("FunctionalInterface", "Serializable", "Runnable", "Cloneable"), "FunctionalInterface"),
                new Question(SUBJECT, HARD, "Which method returns a sublist of a list?",
                        Arrays.asList("subList()", "getSubList()", "slice()", "subset()"), "subList()"),
                new Question(SUBJECT, HARD, "Which class is used for formatting numbers?",
                        Arrays.asList("DecimalFormat", "NumberFormat", "Formatter", "NumberFormatter"), "DecimalFormat"),
                new Question(SUBJECT, HARD, "Which exception is thrown when accessing invalid array index?",
                        Arrays.asList("ArrayIndexOutOfBoundsException", "NullPointerException", "IOException", "IndexException"), "ArrayIndexOutOfBoundsException"),
                new Question(SUBJECT, HARD, "Which method sorts a list?",
                        Arrays.asList("Collections.sort()", "List.sort()", "sortList()", "sort()"), "Collections.sort()"),
                new Question(SUBJECT, HARD, "Which keyword is used to define an enum?",
                        Arrays.asList("enum", "Enum", "enumeration", "constant"), "enum"),
                new Question(SUBJECT, HARD, "Which class represents a fixed-size array of bytes?",
                        Arrays.asList("ByteBuffer", "ByteArray", "Buffer", "ByteList"), "ByteBuffer"),
                new Question(SUBJECT, HARD, "Which method compares two strings lexicographically?",
                        Arrays.asList("compareTo()", "equals()", "compare()", "compareStrings()"), "compareTo()"),
                new Question(SUBJECT, HARD, "Which method converts a string to a char array?",
                        Arrays.asList("toCharArray()", "getChars()", "chars()", "toChars()"), "toCharArray()"),
                new Question(SUBJECT, HARD, "Which method joins strings using a delimiter?",
                        Arrays.asList("String.join()", "join()", "concat()", "merge()"), "String.join()"),
                new Question(SUBJECT, HARD, "Which method replaces characters in a string?",
                        Arrays.asList("replace()", "replaceChar()", "setChar()", "substitute()"), "replace()"),
                new Question(SUBJECT, HARD, "Which class allows reading/writing files?",
                        Arrays.asList("FileReader/Writer", "FileIO", "FileUtils", "FileManager"), "FileReader/Writer"),
                new Question(SUBJECT, HARD, "Which method is used to create a thread in Java?",
                        Arrays.asList("start()", "run()", "execute()", "init()"), "start()"),
                new Question(SUBJECT, HARD, "Which class is used for reading bytes from a file?",
                        Arrays.asList("FileInputStream", "FileReader", "BufferedReader", "InputStreamReader"), "FileInputStream"),
                new Question(SUBJECT, HARD, "Which method is used to write bytes to a file?",
                        Arrays.asList("write()", "writeBytes()", "putBytes()", "output()"), "write()"),
                new Question(SUBJECT, HARD, "Which class is used to implement a priority queue?",
                        Arrays.asList("PriorityQueue", "Queue", "LinkedList", "ArrayDeque"), "PriorityQueue"),
                new Question(SUBJECT, HARD, "Which method is used to stop a thread safely?",
                        Arrays.asList("interrupt()", "stop()", "kill()", "terminate()"), "interrupt()"),
                new Question(SUBJECT, HARD, "Which annotation is used to suppress compiler warnings?",
                        Arrays.asList("@SuppressWarnings", "@IgnoreWarnings", "@Warning", "@NoWarning"), "@SuppressWarnings"),
                new Question(SUBJECT, HARD, "Which class is used to create immutable strings?",
                        Arrays.asList("String", "StringBuilder", "StringBuffer", "StringImmutable"), "String"),
                new Question(SUBJECT, HARD, "Which method in Collections is used to reverse a list?",
                        Arrays.asList("Collections.reverse()", "List.reverse()", "List.revert()", "Collections.revert()"), "Collections.reverse()"),
                new Question(SUBJECT, HARD, "Which class is used to perform object cloning?",
                        Arrays.asList("Cloneable", "Object", "Serializable", "Copyable"), "Object"),
                new Question(SUBJECT, HARD, "Which exception is thrown when thread sleeps is interrupted?",
                        Arrays.asList("InterruptedException", "ThreadException", "IOException", "IllegalStateException"), "InterruptedException"),
                new Question(SUBJECT, HARD, "Which class is used for byte stream input/output?",
                        Arrays.asList("InputStream/OutputStream", "Reader/Writer", "FileStream", "BufferStream"), "InputStream/OutputStream"),
                new Question(SUBJECT, HARD, "Which keyword is used to implement inheritance?",
                        Arrays.asList("extends", "implements", "inherits", "super"), "extends")
        );

        questionRepository.saveAll(questions);
        System.out.println("Java questions loaded successfully!");
    }
}