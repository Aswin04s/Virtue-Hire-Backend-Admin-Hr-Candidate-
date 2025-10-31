package com.virtuehire;

import com.virtuehire.model.Candidate;
import com.virtuehire.model.Question;
import com.virtuehire.repository.AssessmentResultRepository;
import com.virtuehire.repository.CandidateRepository;
import com.virtuehire.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final CandidateRepository candidateRepository;
    private final AssessmentResultRepository assessmentResultRepository;
    private final Random random = new Random();

    public DataLoader(QuestionRepository questionRepository,
                      CandidateRepository candidateRepository,
                      AssessmentResultRepository assessmentResultRepository) {
        this.questionRepository = questionRepository;
        this.candidateRepository = candidateRepository;
        this.assessmentResultRepository = assessmentResultRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear previous data in correct order to avoid foreign key constraints
        System.out.println("Cleaning up previous data...");

        // Delete in correct order - children first, then parents
        assessmentResultRepository.deleteAll(); // Then delete assessment results
        candidateRepository.deleteAll();        // Then delete candidates
        questionRepository.deleteAll();         // Then delete questions

        System.out.println("Previous data cleaned successfully!");

        // Load questions first
        loadQuestions();

        // Load sample candidates
        loadSampleCandidates();

        System.out.println("Data loading completed successfully!");
    }

    private void loadQuestions() {
        // Your existing question loading code...
        int EASY = 1, MEDIUM = 2, HARD = 3;


        List<Question> questions = Arrays.asList(
                // ================= EASY =================
                new Question(EASY, "What is the default value of int in Java?",
                        Arrays.asList("0", "1", "null", "-1"), "0"),
                new Question(EASY, "Which keyword is used to define a class?",
                        Arrays.asList("class", "Class", "define", "object"), "class"),
                new Question(EASY, "Which method is the entry point for a Java program?",
                        Arrays.asList("main()", "start()", "run()", "init()"), "main()"),
                new Question(EASY, "Which operator is used for addition?",
                        Arrays.asList("+", "-", "*", "/"), "+"),
                new Question(EASY, "Which keyword is used to inherit a class?",
                        Arrays.asList("extends", "implements", "inherits", "super"), "extends"),
                new Question(EASY, "Which data type is used to store true/false values?",
                        Arrays.asList("boolean", "int", "char", "String"), "boolean"),
                new Question(EASY, "What is the size of a char in Java?",
                        Arrays.asList("16 bit", "8 bit", "32 bit", "64 bit"), "16 bit"),
                new Question(EASY, "Which keyword is used to declare a constant?",
                        Arrays.asList("final", "const", "static", "immutable"), "final"),
                new Question(EASY, "Which loop executes at least once?",
                        Arrays.asList("for", "while", "do-while", "foreach"), "do-while"),
                new Question(EASY, "Which package contains fundamental classes like String and Math?",
                        Arrays.asList("java.lang", "java.util", "java.io", "java.net"), "java.lang"),
                new Question(EASY, "Which keyword is used to create an object?",
                        Arrays.asList("new", "create", "object", "init"), "new"),
                new Question(EASY, "Which symbol is used for single-line comments?",
                        Arrays.asList("//", "/*", "#", "--"), "//"),
                new Question(EASY, "Which method is used to print to console?",
                        Arrays.asList("System.out.println()", "print()", "console.log()", "echo()"), "System.out.println()"),
                new Question(EASY, "Which operator is used for equality check?",
                        Arrays.asList("==", "=", "!=", "==="), "=="),
                new Question(EASY, "Which data type is used for decimal numbers?",
                        Arrays.asList("float", "int", "char", "boolean"), "float"),
                new Question(EASY, "Which keyword is used to prevent inheritance?",
                        Arrays.asList("final", "static", "abstract", "private"), "final"),
                new Question(EASY, "What is the default value of boolean?",
                        Arrays.asList("false", "true", "0", "null"), "false"),
                new Question(EASY, "Which of these is a valid identifier?",
                        Arrays.asList("myVar", "123var", "var-name", "class"), "myVar"),
                new Question(EASY, "Which loop is used when the number of iterations is known?",
                        Arrays.asList("for", "while", "do-while", "foreach"), "for"),
                new Question(EASY, "Which keyword is used to inherit multiple interfaces?",
                        Arrays.asList("implements", "extends", "interface", "inherit"), "implements"),
                new Question(EASY, "Which is the correct syntax to declare an array?",
                        Arrays.asList("int[] arr;", "arr int[];", "array int arr;", "int arr[];"), "int[] arr;"),
                new Question(EASY, "Which keyword is used to exit a loop?",
                        Arrays.asList("break", "exit", "stop", "return"), "break"),
                new Question(EASY, "Which operator is used for increment?",
                        Arrays.asList("++", "+", "--", "+="), "++"),
                new Question(EASY, "Which class is the superclass of all classes?",
                        Arrays.asList("Object", "Class", "Base", "Super"), "Object"),
                new Question(EASY, "Which method converts a string to integer?",
                        Arrays.asList("Integer.parseInt()", "String.toInt()", "int.parse()", "Integer.toInt()"), "Integer.parseInt()"),
                new Question(EASY, "Which keyword is used to handle exceptions?",
                        Arrays.asList("try", "catch", "finally", "throw"), "try"),
                new Question(EASY, "Which operator is used for logical AND?",
                        Arrays.asList("&", "&&", "|", "||"), "&&"),
                new Question(EASY, "Which data type stores text values?",
                        Arrays.asList("String", "int", "char", "boolean"), "String"),
                new Question(EASY, "Which method compares two strings for equality?",
                        Arrays.asList("equals()", "==", "compare()", "match()"), "equals()"),
                new Question(EASY, "Which keyword is used to define a package?",
                        Arrays.asList("package", "import", "include", "module"), "package"),

                // ================= MEDIUM =================
                new Question(MEDIUM, "Which interface does HashMap implement?",
                        Arrays.asList("Map", "List", "Set", "Collection"), "Map"),
                new Question(MEDIUM, "What is the output of 10 / 3 in Java?",
                        Arrays.asList("3", "3.33", "3.0", "Error"), "3"),
                new Question(MEDIUM, "Which collection class maintains insertion order?",
                        Arrays.asList("LinkedHashMap", "HashMap", "TreeMap", "Hashtable"), "LinkedHashMap"),
                new Question(MEDIUM, "Which keyword prevents a method from being overridden?",
                        Arrays.asList("final", "static", "private", "abstract"), "final"),
                new Question(MEDIUM, "Which exception is thrown when dividing by zero?",
                        Arrays.asList("ArithmeticException", "NullPointerException", "IOException", "ClassNotFoundException"), "ArithmeticException"),
                new Question(MEDIUM, "Which method is used to start a thread?",
                        Arrays.asList("start()", "run()", "init()", "execute()"), "start()"),
                new Question(MEDIUM, "What is the default value of an object reference?",
                        Arrays.asList("null", "0", "undefined", "false"), "null"),
                new Question(MEDIUM, "Which keyword is used to access the parent class constructor?",
                        Arrays.asList("super", "this", "parent", "base"), "super"),
                new Question(MEDIUM, "Which class is used for random numbers?",
                        Arrays.asList("Random", "Math", "Integer", "Number"), "Random"),
                new Question(MEDIUM, "Which method compares two strings ignoring case?",
                        Arrays.asList("equalsIgnoreCase()", "compare()", "equals()", "match()"), "equalsIgnoreCase()"),
                new Question(MEDIUM, "Which keyword is used to implement an interface?",
                        Arrays.asList("implements", "extends", "interface", "inherit"), "implements"),
                new Question(MEDIUM, "What is the size of an int in Java?",
                        Arrays.asList("32 bit", "16 bit", "64 bit", "8 bit"), "32 bit"),
                new Question(MEDIUM, "Which collection allows null keys?",
                        Arrays.asList("HashMap", "Hashtable", "TreeMap", "LinkedHashMap"), "HashMap"),
                new Question(MEDIUM, "Which method returns the length of an array?",
                        Arrays.asList("length", "size()", "getLength()", "count()"), "length"),
                new Question(MEDIUM, "Which keyword is used to refer to the current object?",
                        Arrays.asList("this", "super", "self", "current"), "this"),
                new Question(MEDIUM, "Which class is used to read input from console?",
                        Arrays.asList("Scanner", "InputStream", "BufferedReader", "Console"), "Scanner"),
                new Question(MEDIUM, "Which interface supports random access of elements?",
                        Arrays.asList("List", "Set", "Map", "Collection"), "List"),
                new Question(MEDIUM, "Which keyword is used to create a thread by extending Thread class?",
                        Arrays.asList("extends", "implements", "new", "thread"), "extends"),
                new Question(MEDIUM, "Which exception is checked in Java?",
                        Arrays.asList("IOException", "ArithmeticException", "NullPointerException", "ArrayIndexOutOfBoundsException"), "IOException"),
                new Question(MEDIUM, "Which method is used to join threads?",
                        Arrays.asList("join()", "start()", "run()", "wait()"), "join()"),
                new Question(MEDIUM, "Which method compares two objects?",
                        Arrays.asList("equals()", "compare()", "==", "match()"), "equals()"),
                new Question(MEDIUM, "Which method removes an element from ArrayList?",
                        Arrays.asList("remove()", "delete()", "clear()", "discard()"), "remove()"),
                new Question(MEDIUM, "Which class is immutable?",
                        Arrays.asList("String", "StringBuilder", "ArrayList", "HashMap"), "String"),
                new Question(MEDIUM, "Which keyword is used to prevent subclassing?",
                        Arrays.asList("final", "static", "abstract", "private"), "final"),
                new Question(MEDIUM, "Which method is called to finalize objects before garbage collection?",
                        Arrays.asList("finalize()", "destroy()", "cleanup()", "dispose()"), "finalize()"),
                new Question(MEDIUM, "Which method is used to format output?",
                        Arrays.asList("printf()", "print()", "println()", "format()"), "printf()"),
                new Question(MEDIUM, "Which keyword is used to throw exceptions?",
                        Arrays.asList("throw", "throws", "try", "catch"), "throw"),
                new Question(MEDIUM, "Which interface is used to compare objects?",
                        Arrays.asList("Comparable", "Comparator", "Cloneable", "Runnable"), "Comparable"),
                new Question(MEDIUM, "Which method is used to check if a collection is empty?",
                        Arrays.asList("isEmpty()", "size()", "checkEmpty()", "empty()"), "isEmpty()"),
                new Question(MEDIUM, "Which class provides atomic operations in Java?",
                        Arrays.asList("AtomicInteger", "Integer", "Thread", "Object"), "AtomicInteger"),

                // ================= HARD =================

                new Question(HARD, "What does 'transient' keyword do?",
                        Arrays.asList("Prevents serialization", "Prevents threading", "Prevents inheritance", "None"), "Prevents serialization"),
                new Question(HARD, "Which interface is functional in Java?",
                        Arrays.asList("Runnable", "Serializable", "Cloneable", "Comparator"), "Runnable"),
                new Question(HARD, "Which class is used to format dates?",
                        Arrays.asList("SimpleDateFormat", "DateFormat", "Calendar", "Date"), "SimpleDateFormat"),
                new Question(HARD, "Which method is used to get class name at runtime?",
                        Arrays.asList("getClass()", "className()", "getName()", "getType()"), "getClass()"),
                new Question(HARD, "What is the default capacity of ArrayList?",
                        Arrays.asList("10", "16", "0", "5"), "10"),
                new Question(HARD, "Which collection is thread-safe?",
                        Arrays.asList("Hashtable", "HashMap", "ArrayList", "LinkedList"), "Hashtable"),
                new Question(HARD, "Which keyword is used to handle checked exceptions?",
                        Arrays.asList("throws", "throw", "try", "catch"), "throws"),
                new Question(HARD, "Which interface allows iteration over a collection?",
                        Arrays.asList("Iterator", "Iterable", "Collection", "Enumeration"), "Iterator"),
                new Question(HARD, "Which collection sorts elements based on natural ordering?",
                        Arrays.asList("TreeSet", "HashSet", "LinkedHashSet", "ArrayList"), "TreeSet"),
                new Question(HARD, "What does 'volatile' keyword do?",
                        Arrays.asList("Ensures visibility across threads", "Prevents inheritance", "Prevents serialization", "None"), "Ensures visibility across threads"),
                new Question(HARD, "Which keyword is used to define an abstract class?",
                        Arrays.asList("abstract", "final", "interface", "class"), "abstract"),
                new Question(HARD, "Which class is used for synchronization in Java?",
                        Arrays.asList("ReentrantLock", "Lock", "Object", "Thread"), "ReentrantLock"),
                new Question(HARD, "Which method converts a string to uppercase?",
                        Arrays.asList("toUpperCase()", "upper()", "toupper()", "convertUpper()"), "toUpperCase()"),
                new Question(HARD, "Which method returns a hash code for an object?",
                        Arrays.asList("hashCode()", "getHash()", "code()", "getCode()"), "hashCode()"),
                new Question(HARD, "Which class represents a node in a linked list?",
                        Arrays.asList("Node", "ListNode", "LinkedNode", "Element"), "Node"),
                new Question(HARD, "Which method removes duplicates in a Set?",
                        Arrays.asList("add()", "remove()", "clear()", "Sets inherently remove duplicates"), "Sets inherently remove duplicates"),
                new Question(HARD, "Which interface is used for lambda expressions?",
                        Arrays.asList("FunctionalInterface", "Serializable", "Runnable", "Cloneable"), "FunctionalInterface"),
                new Question(HARD, "Which method returns a sublist of a list?",
                        Arrays.asList("subList()", "getSubList()", "slice()", "subset()"), "subList()"),
                new Question(HARD, "Which class is used for formatting numbers?",
                        Arrays.asList("DecimalFormat", "NumberFormat", "Formatter", "NumberFormatter"), "DecimalFormat"),
                new Question(HARD, "Which exception is thrown when accessing invalid array index?",
                        Arrays.asList("ArrayIndexOutOfBoundsException", "NullPointerException", "IOException", "IndexException"), "ArrayIndexOutOfBoundsException"),
                new Question(HARD, "Which method sorts a list?",
                        Arrays.asList("Collections.sort()", "List.sort()", "sortList()", "sort()"), "Collections.sort()"),
                new Question(HARD, "Which keyword is used to define an enum?",
                        Arrays.asList("enum", "Enum", "enumeration", "constant"), "enum"),
                new Question(HARD, "Which class represents a fixed-size array of bytes?",
                        Arrays.asList("ByteBuffer", "ByteArray", "Buffer", "ByteList"), "ByteBuffer"),
                new Question(HARD, "Which method compares two strings lexicographically?",
                        Arrays.asList("compareTo()", "equals()", "compare()", "compareStrings()"), "compareTo()"),
                new Question(HARD, "Which method converts a string to a char array?",
                        Arrays.asList("toCharArray()", "getChars()", "chars()", "toChars()"), "toCharArray()"),
                new Question(HARD, "Which method joins strings using a delimiter?",
                        Arrays.asList("String.join()", "join()", "concat()", "merge()"), "String.join()"),
                new Question(HARD, "Which method replaces characters in a string?",
                        Arrays.asList("replace()", "replaceChar()", "setChar()", "substitute()"), "replace()"),
                new Question(HARD, "Which class allows reading/writing files?",
                        Arrays.asList("FileReader/Writer", "FileIO", "FileUtils", "FileManager"), "FileReader/Writer"),
                new Question(HARD, "Which method is used to create a thread in Java?",
                        Arrays.asList("start()", "run()", "execute()", "init()"), "start()"),
                new Question(HARD, "Which class is used for reading bytes from a file?",
                        Arrays.asList("FileInputStream", "FileReader", "BufferedReader", "InputStreamReader"), "FileInputStream"),
                new Question(HARD, "Which method is used to write bytes to a file?",
                        Arrays.asList("write()", "writeBytes()", "putBytes()", "output()"), "write()"),
                new Question(HARD, "Which class is used to implement a priority queue?",
                        Arrays.asList("PriorityQueue", "Queue", "LinkedList", "ArrayDeque"), "PriorityQueue"),
                new Question(HARD, "Which method is used to stop a thread safely?",
                        Arrays.asList("interrupt()", "stop()", "kill()", "terminate()"), "interrupt()"),
                new Question(HARD, "Which annotation is used to suppress compiler warnings?",
                        Arrays.asList("@SuppressWarnings", "@IgnoreWarnings", "@Warning", "@NoWarning"), "@SuppressWarnings"),
                new Question(HARD, "Which class is used to create immutable strings?",
                        Arrays.asList("String", "StringBuilder", "StringBuffer", "StringImmutable"), "String"),
                new Question(HARD, "Which method in Collections is used to reverse a list?",
                        Arrays.asList("Collections.reverse()", "List.reverse()", "List.revert()", "Collections.revert()"), "Collections.reverse()"),
                new Question(HARD, "Which class is used to perform object cloning?",
                        Arrays.asList("Cloneable", "Object", "Serializable", "Copyable"), "Object"),
                new Question(HARD, "Which exception is thrown when thread sleeps is interrupted?",
                        Arrays.asList("InterruptedException", "ThreadException", "IOException", "IllegalStateException"), "InterruptedException"),
                new Question(HARD, "Which class is used for byte stream input/output?",
                        Arrays.asList("InputStream/OutputStream", "Reader/Writer", "FileStream", "BufferStream"), "InputStream/OutputStream"),
                new Question(HARD, "Which keyword is used to implement inheritance?",
                        Arrays.asList("extends", "implements", "inherits", "super"), "extends")

        );

        questionRepository.saveAll(questions);
        System.out.println("All questions loaded successfully!");
    }

    private void loadSampleCandidates() {
        List<Candidate> candidates = Arrays.asList(
                createCandidate("John Smith", "john.smith@email.com", "Java, Spring Boot", 3, "Bachelor's", "New York", "NY", 85),
                createCandidate("Sarah Johnson", "sarah.j@email.com", "Python, Django", 2, "Master's", "Boston", "MA", 78),
                createCandidate("Mike Chen", "mike.chen@email.com", "JavaScript, React", 4, "Bachelor's", "San Francisco", "CA", 92),
                createCandidate("Emily Davis", "emily.davis@email.com", "Java, Hibernate", 1, "Bachelor's", "Chicago", "IL", 65),
                createCandidate("David Wilson", "david.w@email.com", "Python, Flask", 5, "PhD", "Austin", "TX", 88),
                createCandidate("Lisa Brown", "lisa.b@email.com", "JavaScript, Node.js", 2, "Master's", "Seattle", "WA", 76),
                createCandidate("Robert Taylor", "robert.t@email.com", "Java, Spring Cloud", 6, "Bachelor's", "Denver", "CO", 95),
                createCandidate("Maria Garcia", "maria.g@email.com", "Python, FastAPI", 3, "Master's", "Miami", "FL", 82),
                createCandidate("James Miller", "james.m@email.com", "C++, Qt", 7, "Bachelor's", "Atlanta", "GA", 90),
                createCandidate("Jennifer Lee", "jennifer.lee@email.com", "JavaScript, Vue.js", 1, "Bachelor's", "Portland", "OR", 70),
                createCandidate("Thomas Anderson", "thomas.a@email.com", "Java, Microservices", 4, "Master's", "Los Angeles", "CA", 87),
                createCandidate("Jessica Martinez", "jessica.m@email.com", "Python, ML", 2, "PhD", "San Diego", "CA", 89),
                createCandidate("Christopher Clark", "chris.c@email.com", "Go, Docker", 3, "Bachelor's", "Phoenix", "AZ", 81),
                createCandidate("Amanda Rodriguez", "amanda.r@email.com", "JavaScript, Angular", 5, "Master's", "Dallas", "TX", 84),
                createCandidate("Kevin White", "kevin.w@email.com", "Java, Kafka", 8, "Bachelor's", "Houston", "TX", 96),
                createCandidate("Nicole Harris", "nicole.h@email.com", "Python, Data Science", 2, "Master's", "Philadelphia", "PA", 79),
                createCandidate("Daniel Martin", "daniel.m@email.com", "React, TypeScript", 3, "Bachelor's", "San Antonio", "TX", 83),
                createCandidate("Michelle Thompson", "michelle.t@email.com", "Java, AWS", 4, "Bachelor's", "San Jose", "CA", 86),
                createCandidate("Richard Moore", "richard.m@email.com", "Python, Django REST", 6, "Master's", "Jacksonville", "FL", 91),
                createCandidate("Stephanie Allen", "stephanie.a@email.com", "JavaScript, Express", 1, "Bachelor's", "Columbus", "OH", 68),
                createCandidate("Charles Young", "charles.y@email.com", "Java, Spring Security", 5, "Bachelor's", "Charlotte", "NC", 89),
                createCandidate("Elizabeth King", "elizabeth.k@email.com", "Python, Pandas", 3, "Master's", "Indianapolis", "IN", 77),
                createCandidate("Matthew Scott", "matthew.s@email.com", "React Native", 2, "Bachelor's", "San Francisco", "CA", 74),
                createCandidate("Patricia Green", "patricia.g@email.com", "Java, JUnit", 7, "Master's", "Seattle", "WA", 93),
                createCandidate("Joshua Baker", "joshua.b@email.com", "Python, NumPy", 4, "Bachelor's", "Denver", "CO", 85),
                createCandidate("Laura Adams", "laura.a@email.com", "Vue.js, MongoDB", 1, "Bachelor's", "Washington", "DC", 66),
                createCandidate("Ryan Parker", "ryan.p@email.com", "Java, Maven", 8, "Master's", "Sacramento", "CA", 95),
                createCandidate("Deborah Evans", "deborah.e@email.com", "Django, Celery", 4, "Bachelor's", "Kansas City", "MO", 84),
                createCandidate("Gary Edwards", "gary.e@email.com", "Vue 3, Pinia", 2, "Bachelor's", "Mesa", "AZ", 75),
                createCandidate("Sandra Collins", "sandra.c@email.com", "Java, JVM", 10, "PhD", "Atlanta", "GA", 98),
                createCandidate("Jacob Stewart", "jacob.s@email.com", "Python, OpenCV", 3, "Master's", "Omaha", "NE", 81),
                createCandidate("Donna Sanchez", "donna.s@email.com", "React, Next.js", 5, "Bachelor's", "Raleigh", "NC", 86),
                createCandidate("Jason Morris", "jason.m@email.com", "Spring, RabbitMQ", 6, "Master's", "Miami", "FL", 90),
                createCandidate("Carol Rogers", "carol.r@email.com", "JavaScript, Webpack", 2, "Bachelor's", "Long Beach", "CA", 73),
                createCandidate("Eric Reed", "eric.r@email.com", "Java, Design Patterns", 7, "Bachelor's", "Virginia Beach", "VA", 92),
                createCandidate("Ruth Cook", "ruth.c@email.com", "FastAPI, SQLAlchemy", 3, "Master's", "Oakland", "CA", 83),
                createCandidate("Jonathan Morgan", "jonathan.m@email.com", "Angular, RxJS", 4, "Bachelor's", "Minneapolis", "MN", 79),
                createCandidate("Sharon Bell", "sharon.b@email.com", "Java, JSP", 1, "Bachelor's", "Tulsa", "OK", 64),
                createCandidate("Stephen Murphy", "stephen.m@email.com", "Python, PyTorch", 8, "PhD", "Arlington", "TX", 96),
                createCandidate("Anna Bailey", "anna.b@email.com", "React, GraphQL", 2, "Bachelor's", "New Orleans", "LA", 71),
                createCandidate("Larry Cooper", "larry.c@email.com", "Spring Boot, OAuth2", 5, "Master's", "Wichita", "KS", 89),
                createCandidate("Betty Richardson", "betty.r@email.com", "Vue, Vite", 3, "Bachelor's", "Cleveland", "OH", 78),
                // ================= FRESHER CANDIDATES =================
                createCandidate("Aiden Walker", "aiden.w@email.com", "Java, JDBC", 0, "Bachelor's", "New York", "NY", 68),
                createCandidate("Chloe Rivera", "chloe.r@email.com", "Python, Flask", 1, "Bachelor's", "Los Angeles", "CA", 72),
                createCandidate("Ethan Hill", "ethan.h@email.com", "HTML, CSS, JavaScript", 0, "Bachelor's", "Chicago", "IL", 66),
                createCandidate("Olivia Scott", "olivia.s@email.com", "Java, OOP", 1, "Bachelor's", "Houston", "TX", 70),
                createCandidate("William Adams", "william.a@email.com", "C, C++", 0, "Bachelor's", "Phoenix", "AZ", 63),
                createCandidate("Sophia Baker", "sophia.b@email.com", "Python, Pandas", 1, "Bachelor's", "San Antonio", "TX", 75),
                createCandidate("Liam Carter", "liam.c@email.com", "JavaScript, React", 0, "Bachelor's", "Philadelphia", "PA", 69),
                createCandidate("Isabella Perez", "isabella.p@email.com", "Java, Spring Boot", 1, "Bachelor's", "San Diego", "CA", 73),
                createCandidate("Mason Turner", "mason.t@email.com", "HTML, CSS, Bootstrap", 0, "Bachelor's", "Dallas", "TX", 67),
                createCandidate("Mia Phillips", "mia.p@email.com", "Python, Django", 1, "Bachelor's", "San Jose", "CA", 76),
                createCandidate("Noah Evans", "noah.e@email.com", "Java, Collections", 0, "Bachelor's", "Austin", "TX", 65),
                createCandidate("Charlotte Murphy", "charlotte.m@email.com", "JavaScript, Node.js", 1, "Bachelor's", "San Francisco", "CA", 74),
                createCandidate("James Reed", "james.r@email.com", "C#, ASP.NET", 0, "Bachelor's", "Columbus", "OH", 64),
                createCandidate("Amelia Bell", "amelia.b@email.com", "Python, Data Analysis", 1, "Bachelor's", "Seattle", "WA", 77),
                createCandidate("Benjamin Ward", "benjamin.w@email.com", "Java, MySQL", 0, "Bachelor's", "Charlotte", "NC", 69),
                createCandidate("Evelyn Torres", "evelyn.t@email.com", "HTML, CSS, JavaScript", 1, "Bachelor's", "Detroit", "MI", 71),
                createCandidate("Lucas Flores", "lucas.f@email.com", "Python, NumPy", 0, "Bachelor's", "Denver", "CO", 66),
                createCandidate("Harper Ramirez", "harper.r@email.com", "Java, JSP", 1, "Bachelor's", "Orlando", "FL", 70),
                createCandidate("Alexander Brooks", "alexander.b@email.com", "C, Data Structures", 0, "Bachelor's", "Indianapolis", "IN", 62),
                createCandidate("Ella Sanders", "ella.s@email.com", "JavaScript, React", 1, "Bachelor's", "Portland", "OR", 75)

        );

        candidateRepository.saveAll(candidates);
        System.out.println("Loaded " + candidates.size() + " sample candidates!");
    }

    private Candidate createCandidate(String fullName, String email, String skills,
                                      Integer experience, String education, String city,
                                      String state, Integer score) {
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setEmail(email);
        candidate.setPhoneNumber(generatePhoneNumber());
        candidate.setPassword("password123"); // Default password
        candidate.setSkills(skills);
        candidate.setExperience(experience);
        candidate.setHighestEducation(education);
        candidate.setCity(city);
        candidate.setState(state);
        candidate.setScore(score);
        candidate.setAssessmentTaken(true);
        candidate.setExperienceLevel(experience > 0 ? "Experienced" : "Fresher");
        candidate.setLanguagePreference(getPrimaryLanguage(skills));
        candidate.setScore(score); // ← This sets the assessment score
        candidate.setExperience(experience); // ← This sets years of experience

        // Random college
        String[] colleges = {"Stanford University", "MIT", "Harvard University", "UC Berkeley",
                "University of Texas", "Georgia Tech", "University of Michigan",
                "Carnegie Mellon", "University of Illinois", "Caltech"};
        candidate.setCollegeUniversity(colleges[random.nextInt(colleges.length)]);
        candidate.setYearOfGraduation(2015 + random.nextInt(8));

        return candidate;
    }

    private String generatePhoneNumber() {
        return String.format("(%03d) %03d-%04d",
                random.nextInt(1000),
                random.nextInt(1000),
                random.nextInt(10000));
    }

    private String getPrimaryLanguage(String skills) {
        if (skills.toLowerCase().contains("java")) return "Java";
        if (skills.toLowerCase().contains("python")) return "Python";
        if (skills.toLowerCase().contains("javascript")) return "JavaScript";
        if (skills.toLowerCase().contains("c++")) return "C++";
        if (skills.toLowerCase().contains("go")) return "Go";
        return "Java"; // default
    }
}