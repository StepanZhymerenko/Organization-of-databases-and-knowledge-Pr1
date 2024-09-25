import java.io.*;
import java.util.*;

public class SchoolSearch {

    private static final String STUDENTS_FILE = "students.txt";
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

    private static void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String lastName = parts[0].trim();
                    String firstName = parts[1].trim();
                    int grade = Integer.parseInt(parts[2].trim());
                    int classroom = Integer.parseInt(parts[3].trim());
                    int bus = Integer.parseInt(parts[4].trim());
                    String teacherLastName = parts[5].trim();
                    String teacherFirstName = parts[6].trim();

                    students.add(new Student(lastName, firstName, grade, classroom, bus, teacherLastName, teacherFirstName));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
    }

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

    private static void searchStudentByLastName(String lastName) {
        for (Student student : students) {
            if (student.getLastName().equalsIgnoreCase(lastName)) {
                System.out.println(student.getLastName() + " " + student.getFirstName() + " is in class " + student.getClassroom() + ", Teacher: " + student.getTeacherFirstName() + " " + student.getTeacherLastName());
            }
        }
    }

    private static void searchStudentBusByLastName(String lastName) {
        for (Student student : students) {
            if (student.getLastName().equalsIgnoreCase(lastName)) {
                System.out.println(student.getLastName() + " " + student.getFirstName() + " takes bus " + student.getBus());
            }
        }
    }

    private static void searchStudentsByTeacherLastName(String teacherLastName) {
        for (Student student : students) {
            if (student.getTeacherLastName().equalsIgnoreCase(teacherLastName)) {
                System.out.println(student.getLastName() + " " + student.getFirstName());
            }
        }
    }

    private static void searchStudentsByBusNumber(int busNumber) {
        for (Student student : students) {
            if (student.getBus() == busNumber) {
                System.out.println(student.getLastName() + " " + student.getFirstName() + " in class " + student.getClassroom());
            }
        }
    }

    private static void searchStudentsByGradeLevel(int gradeLevel) {
        for (Student student : students) {
            if (student.getGrade() == gradeLevel) {
                System.out.println(student.getLastName() + " " + student.getFirstName());
            }
        }
    }

    private static void searchStudentsByClassroom(int classroom) {
        for (Student student : students) {
            if (student.getClassroom() == classroom) {
                System.out.println(student.getLastName() + " " + student.getFirstName());
            }
        }
    }

    private static void searchTeachersByClassroom(int classroom) {
        for (Teacher teacher : teachers) {
            if (teacher.getClassroom() == classroom) {
                System.out.println(teacher.getLastName() + " " + teacher.getFirstName());
            }
        }
    }

    private static void searchTeachersByGradeLevel(int gradeLevel) {
        for (Student student : students) {
            if (student.getGrade() == gradeLevel) {
                System.out.println("Teacher: " + student.getTeacherFirstName() + " " + student.getTeacherLastName());
            }
        }
    }

    static class Student {
        private String lastName;
        private String firstName;
        private int grade;
        private int classroom;
        private int bus;
        private String teacherLastName;
        private String teacherFirstName;

        public Student(String lastName, String firstName, int grade, int classroom, int bus, String teacherLastName, String teacherFirstName) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.grade = grade;
            this.classroom = classroom;
            this.bus = bus;
            this.teacherLastName = teacherLastName;
            this.teacherFirstName = teacherFirstName;
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

        public String getTeacherLastName() {
            return teacherLastName;
        }

        public String getTeacherFirstName() {
            return teacherFirstName;
        }
    }

    static class Teacher {
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
}
