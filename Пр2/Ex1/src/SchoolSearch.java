import java.io.*;
import java.util.*;

public class SchoolSearch {

    private static final String STUDENTS_FILE = "list.txt";
    private static final String TEACHERS_FILE = "teachers.txt";
    private static List<Student> students = new ArrayList<>();
    private static List<Teacher> teachers = new ArrayList<>();

    public static void main(String[] args) {
        loadStudents();
        loadTeachers();
        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Пошук за прізвищем студента (клас та викладач)");
            System.out.println("2. Пошук за прізвищем студента (автобусний маршрут)");
            System.out.println("3. Пошук учнів за прізвищем викладача");
            System.out.println("4. Пошук учнів за номером автобусного маршруту");
            System.out.println("5. Пошук учнів на певному рівні класу");
            System.out.println("6. Вивести всіх учнів класу за номером класу");
            System.out.println("7. Вивести всіх викладачів за номером класу");
            System.out.println("8. Вивести всіх викладачів за рівнем класу");
            System.out.println("9. Вихід");
            System.out.print("Оберіть опцію: ");

            command = scanner.nextLine().trim();
            long startTime = System.currentTimeMillis();

            switch (command) {
                case "1":
                    System.out.print("Введіть прізвище студента: ");
                    String studentLastName = scanner.nextLine().trim();
                    searchStudentByLastName(studentLastName);
                    break;
                case "2":
                    System.out.print("Введіть прізвище студента: ");
                    studentLastName = scanner.nextLine().trim();
                    searchStudentBusByLastName(studentLastName);
                    break;
                case "3":
                    System.out.print("Введіть прізвище викладача: ");
                    String teacherLastName = scanner.nextLine().trim();
                    searchStudentsByTeacherLastName(teacherLastName);
                    break;
                case "4":
                    System.out.print("Введіть номер автобуса: ");
                    int busNumber = Integer.parseInt(scanner.nextLine().trim());
                    searchStudentsByBusNumber(busNumber);
                    break;
                case "5":
                    System.out.print("Введіть рівень класу (Grade): ");
                    int gradeLevel = Integer.parseInt(scanner.nextLine().trim());
                    searchStudentsByGradeLevel(gradeLevel);
                    break;
                case "6":
                    System.out.print("Введіть номер класу: ");
                    int classroomNumber = Integer.parseInt(scanner.nextLine().trim());
                    searchStudentsByClassroom(classroomNumber);
                    break;
                case "7":
                    System.out.print("Введіть номер класу: ");
                    classroomNumber = Integer.parseInt(scanner.nextLine().trim());
                    searchTeachersByClassroom(classroomNumber);
                    break;
                case "8":
                    System.out.print("Введіть рівень класу (Grade): ");
                    gradeLevel = Integer.parseInt(scanner.nextLine().trim());
                    searchTeachersByGradeLevel(gradeLevel);
                    break;
                case "9":
                    System.out.println("Вихід...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Невірна команда. Спробуйте ще раз.");
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Час виконання: " + (endTime - startTime) + " ms");
        }
    }

    // Завантажуємо студентів із файлу
    private static void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String lastName = parts[0].trim();
                    String firstName = parts[1].trim();
                    int grade = Integer.parseInt(parts[2].trim());
                    int classroom = Integer.parseInt(parts[3].trim());
                    int bus = Integer.parseInt(parts[4].trim());

                    students.add(new Student(lastName, firstName, grade, classroom, bus));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
    }

    // Завантажуємо викладачів із файлу
    private static void loadTeachers() {
        try (BufferedReader br = new BufferedReader(new FileReader(TEACHERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String lastName = parts[0].trim();
                    String firstName = parts[1].trim();
                    int classroom = Integer.parseInt(parts[2].trim());

                    teachers.add(new Teacher(lastName, firstName, classroom));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading teachers: " + e.getMessage());
        }
    }

    // Пошук студента за прізвищем
    private static void searchStudentByLastName(String lastName) {
        for (Student student : students) {
            if (student.getLastName().equalsIgnoreCase(lastName)) {
                Teacher teacher = findTeacherByClassroom(student.getClassroom());
                String teacherInfo = (teacher != null) ? "Teacher: " + teacher.getFirstName() + " " + teacher.getLastName() : "No teacher found";
                System.out.println(student.getLastName() + " " + student.getFirstName() + " is in class " + student.getClassroom() + ", " + teacherInfo);
            }
        }
    }

    // Пошук автобуса за прізвищем студента
    private static void searchStudentBusByLastName(String lastName) {
        for (Student student : students) {
            if (student.getLastName().equalsIgnoreCase(lastName)) {
                System.out.println(student.getLastName() + " " + student.getFirstName() + " takes bus " + student.getBus());
            }
        }
    }

    // Пошук студентів за прізвищем вчителя
    private static void searchStudentsByTeacherLastName(String teacherLastName) {
        boolean teacherFound = false;

        // Шукаємо всіх викладачів з заданим прізвищем
        for (Teacher teacher : teachers) {
            if (teacher.getLastName().equalsIgnoreCase(teacherLastName)) {
                teacherFound = true;
                System.out.println("\nУчні в класі викладача " + teacher.getFirstName() + " " + teacher.getLastName() + ":");

                // Виводимо студентів, які навчаються в класі цього викладача
                boolean studentFound = false;
                for (Student student : students) {
                    if (student.getClassroom() == teacher.getClassroom()) {
                        System.out.println(student.getLastName() + " " + student.getFirstName());
                        studentFound = true;
                    }
                }

                if (!studentFound) {
                    System.out.println("Немає студентів у цьому класі.");
                }
            }
        }

        if (!teacherFound) {
            System.out.println("Викладача з прізвищем " + teacherLastName + " не знайдено.");
        }
    }


    // Пошук студентів за номером автобуса
    private static void searchStudentsByBusNumber(int busNumber) {
        for (Student student : students) {
            if (student.getBus() == busNumber) {
                System.out.println(student.getLastName() + " " + student.getFirstName() + " in class " + student.getClassroom());
            }
        }
    }

    // Пошук студентів за рівнем класу
    private static void searchStudentsByGradeLevel(int gradeLevel) {
        for (Student student : students) {
            if (student.getGrade() == gradeLevel) {
                System.out.println(student.getLastName() + " " + student.getFirstName());
            }
        }
    }

    // Пошук студентів за номером класу
    private static void searchStudentsByClassroom(int classroom) {
        for (Student student : students) {
            if (student.getClassroom() == classroom) {
                System.out.println(student.getLastName() + " " + student.getFirstName());
            }
        }
    }

    // Пошук вчителів за номером класу
    private static void searchTeachersByClassroom(int classroom) {
        for (Teacher teacher : teachers) {
            if (teacher.getClassroom() == classroom) {
                System.out.println(teacher.getLastName() + " " + teacher.getFirstName());
            }
        }
    }

    // Пошук вчителів за рівнем класу
    private static void searchTeachersByGradeLevel(int gradeLevel) {
        // Перебираємо список студентів
        for (Student student : students) {
            // Якщо рівень класу студента відповідає введеному
            if (student.getGrade() == gradeLevel) {
                // Виводимо інформацію про студента
                System.out.println("Студент: " + student.getLastName() + " " + student.getFirstName() + " з класу " + student.getClassroom());

                // Шукаємо всіх вчителів для цього класу (кімнати)
                boolean teacherFound = false;
                for (Teacher teacher : teachers) {
                    if (teacher.getClassroom() == student.getClassroom()) {
                        System.out.println("Викладач: " + teacher.getLastName() + " " + teacher.getFirstName());
                        teacherFound = true;
                    }
                }

                // Якщо жодного викладача для класу не знайдено
                if (!teacherFound) {
                    System.out.println("Викладачів для класу " + student.getClassroom() + " не знайдено.");
                }
            }
        }
    }



    // Знайти викладача за класом
    private static Teacher findTeacherByClassroom(int classroom) {
        for (Teacher teacher : teachers) {
            if (teacher.getClassroom() == classroom) {
                return teacher;
            }
        }
        return null;
    }
}

// Клас Student
class Student {
    private String lastName;
    private String firstName;
    private int grade;
    private int classroom;
    private int bus;

    public Student(String lastName, String firstName, int grade, int classroom, int bus) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.grade = grade;
        this.classroom = classroom;
        this.bus = bus;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getGrade() {
        return grade;
    }

    public int getClassroom() {
        return classroom;
    }

    public int getBus() {
        return bus;
    }
}

// Клас Teacher
class Teacher {
    private String lastName;
    private String firstName;
    private int classroom;

    public Teacher(String lastName, String firstName, int classroom) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.classroom = classroom;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getClassroom() {
        return classroom;
    }
}
