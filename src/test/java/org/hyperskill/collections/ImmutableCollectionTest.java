package org.hyperskill.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Objects;


class ImmutableCollectionTest {

    @Test
    public void immutableCollectionTest() {
        ImmutableCollection<?> emptyCollection = ImmutableCollection.of();
        Assertions.assertEquals(0, emptyCollection.size()); // 0
        Assertions.assertTrue(emptyCollection.isEmpty()); // true

        Student student1 = new Student(1, "Student 1", "Email 1");

        ImmutableCollection<Student> addresses = ImmutableCollection.of(
                student1,
                new Student(2, "Student 2", "Email 2"),
                new Student(3, "Student 3", "Email 3"),
                new Student(4, "Student 4", "Email 4"),
                new Student(5, "Student 5", "Email 5")
        );

        Student student = new Student(1, "Student 1", "Email 1");
        Assertions.assertTrue(addresses.contains(student));

        ImmutableCollection<Integer> collection = ImmutableCollection.of(1, 2, 3, 4, 5);
        Assertions.assertFalse(collection.isEmpty()); // false
        Assertions.assertEquals(5, collection.size()); // 5
    }

    private static class Student implements Comparable<Student> {

        private final int id;
        private final String name;
        private final String email;

        public Student(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        @Override
        public int compareTo(Student o) {
            return Comparator.comparingInt(Student::getId)
                    .thenComparing(Student::getName)
                    .thenComparing(Student::getEmail)
                    .compare(this, o);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Student student = (Student) o;
            return id == student.id && Objects.equals(name, student.name) && Objects.equals(email, student.email);
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + Objects.hashCode(name);
            result = 31 * result + Objects.hashCode(email);
            return result;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }


}