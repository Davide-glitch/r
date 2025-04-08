import java.io.*;
import java.util.*;

public class FileReadingPerformanceComparison {
    public static void main(String[] args) {
        // File name, change after yours
        String filename = "RandomIntegers_1M.txt";
        
        StopwatchSmallerTask timer = new StopwatchSmallerTask();
        
        timer.start();
        int sumScanner = readWithScanner(filename);
        double scannerTime = timer.elapsedTime();
        
        timer.start();
        int sumBuffered = readWithBufferedReader(filename);
        double bufferedTime = timer.elapsedTime();
        
        System.out.println("File: " + filename);
        System.out.println("Sum calculated: " + sumScanner + " (Scanner), " + sumBuffered + " (BufferedReader)");
        System.out.println("Scanner time: " + scannerTime + " seconds");
        System.out.println("BufferedReader time: " + bufferedTime + " seconds");
        System.out.println("BufferedReader is " + (scannerTime / bufferedTime) + " times faster than Scanner");
    }
    
    public static int readWithScanner(String filename) {
        int sum = 0;
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextInt()) {
                sum += scanner.nextInt();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        }
        return sum;
    }
    
    public static int readWithBufferedReader(String filename) {
        int sum = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                sum += Integer.parseInt(line.trim());
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
        }
        return sum;
    }
}

class StopwatchSmallerTask {
    private long start;

    public void start() {
        start = System.currentTimeMillis();
    }

    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
}









// Below are used components for the Main important task GenericSorter.java
class PersonClassNeededInFuture {
    private String id;
    private String firstName;
    private String lastName;
    private int day;
    private int month;
    private int year;

    public PersonClassNeededInFuture(String id, String firstName, String lastName, String dateString) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

        String[] dateParts = dateString.split("/");
        if (dateParts.length == 3) {
            this.day = Integer.parseInt(dateParts[0]);
            this.month = Integer.parseInt(dateParts[1]);
            this.year = Integer.parseInt(dateParts[2]);
        }
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getAge() {
        Calendar currentCal = Calendar.getInstance();
        int currentYear = currentCal.get(Calendar.YEAR);
        int currentMonth = currentCal.get(Calendar.MONTH) + 1;
        int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - this.year;
        if (this.month > currentMonth || (this.month == currentMonth && this.day > currentDay)) {
            age--;
        }

        return age;
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName + " " +
                String.format("%02d/%02d/%04d", day, month, year);
    }
}

class DataReaderNeededInFuture {

    public static Person[] readPersons(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String firstLine = reader.readLine().trim();
            Scanner scanner = new Scanner(firstLine);
            int count = scanner.nextInt();
            scanner.close();

            List<Person> personsList = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 4) {
                    String id = parts[0];
                    String firstName = parts[1];
                    String lastName = parts[2];
                    String dateString = parts[3];

                    personsList.add(new Person(id, firstName, lastName, dateString));

                    if (personsList.size() >= count) break;
                }
            }

            return personsList.toArray(new Person[0]);
        }
    }
}