import java.io.*;
import java.util.*;

class Stopwatch {
    private long startTime;

    public Stopwatch() {
        startTime = System.nanoTime();
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public double getElapsedTime() {
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000000.0;
    }
}

class Person {
    private String id;
    private String firstName;
    private String lastName;
    private int day;
    private int month;
    private int year;

    public Person(String id, String firstName, String lastName, String dateString) {
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

interface Sorter2<T> {
    void sort(T[] array, Comparator<T> comparator);
}

class InsertionSorter2<T> implements Sorter2<T> {
    @Override
    public void sort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) return;

        for (int i = 1; i < array.length; i++) {
            T current = array[i];
            int j = i - 1;

            while (j >= 0 && comparator.compare(array[j], current) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = current;
        }
    }
} 
/// Unused because of performance reasons but it is useful for small arrays and for Timsort which is Arrays.sort here

class MergeSorter2<T> implements Sorter2<T> {
    @Override
    public void sort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) return;

        @SuppressWarnings("unchecked")
        T[] tempArray = (T[]) new Object[array.length];
        mergeSort(array, tempArray, 0, array.length - 1, comparator);
    }

    private void mergeSort(T[] array, T[] tempArray, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(array, tempArray, left, mid, comparator);
            mergeSort(array, tempArray, mid + 1, right, comparator);
            merge(array, tempArray, left, mid, right, comparator);
        }
    }

    private void merge(T[] array, T[] tempArray, int left, int mid, int right, Comparator<T> comparator) {
        for (int i = left; i <= right; i++) {
            tempArray[i] = array[i];
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (comparator.compare(tempArray[i], tempArray[j]) <= 0) {
                array[k++] = tempArray[i++];
            } else {
                array[k++] = tempArray[j++];
            }
        }

        while (i <= mid) {
            array[k++] = tempArray[i++];
        }
    }
}

// QuickSort unique from DSA course :)
class QuickSorter2<T> implements Sorter2<T> {
    @Override
    public void sort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1) return;
        quickSort(array, array.length, 0, array.length - 1, comparator);
    }

    private void quickSort(T[] array, int arr_size, int left, int right, Comparator<T> comparator) {
        int i = left, j = right;
        T x = array[left + (right - left)/2];
        
        do {
            while (comparator.compare(array[i], x) < 0) i++;
            while (comparator.compare(array[j], x) > 0) j--;
            
            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        } while (i <= j);
        
        if (left < j) quickSort(array, arr_size, left, j, comparator);
        if (right > i) quickSort(array, arr_size, i, right, comparator);
    }

    private void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

class DataReader {
    public static Integer[] readIntegers(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String firstLine = reader.readLine().trim();
            Scanner scanner = new Scanner(firstLine);
            int count = scanner.nextInt();

            List<Integer> integersList = new ArrayList<>();

            while (scanner.hasNextInt()) {
                integersList.add(scanner.nextInt());
            }
            scanner.close();

            String line;
            while ((line = reader.readLine()) != null) {
                scanner = new Scanner(line);
                while (scanner.hasNextInt()) {
                    integersList.add(scanner.nextInt());
                    if (integersList.size() >= count) break;
                }
                scanner.close();
                if (integersList.size() >= count) break;
            }

            return integersList.toArray(new Integer[0]);
        }
    }

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

class SortingWithGenericSorter {
    
    private static Comparator<Person> createDateComparator() {
        return (p1, p2) -> {
            // First compare by age in years
            int ageCompare = Integer.compare(p1.getAge(), p2.getAge());
            if (ageCompare != 0) {
                return ageCompare; 
            }
            
            // Here was the problem
            int yearCompare = Integer.compare(p2.getYear(), p1.getYear());
            if (yearCompare != 0) {
                return yearCompare;
            }
            
            int monthCompare = Integer.compare(p2.getMonth(), p1.getMonth());
            if (monthCompare != 0) {
                return monthCompare;
            }
            
            return Integer.compare(p2.getDay(), p1.getDay());
        };
    }

    // Helper method for calculating speedup ratio
    private static String calculateSpeedup(double baselineTime, double comparisonTime) {
        double speedup = baselineTime / comparisonTime;
        return String.format("%.2fx %s", 
                speedup, 
                speedup > 1.0 ? "faster" : "slower");
    }

    // Helper method to identify fastest algorithm
    private static String findFastestAlgorithm(Map<String, Double> times) {
        return times.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
    }

    public static void main(String[] args) {
        // Display count here, you can increase it, even maximum :)
        // But be careful! If it is increased, not everything may be displayed sadly :(
        int displayCount = 1000; // put 1000000 for all but it will take a while

        // File names, change after yours
        String integersFile = "RandomIntegers_1M.txt";
        String personsFile = "people-1M.txt";

        try {
            System.out.println("Reading integers...");
            Integer[] integers = DataReader.readIntegers(integersFile);
            System.out.println("Read " + integers.length + " integers");

            System.out.println("\nReading persons...");
            Person[] persons = DataReader.readPersons(personsFile);
            System.out.println("Read " + persons.length + " persons");

            // Initialize sorters
            Sorter2<Integer> mergeSorterInt = new MergeSorter2<>();
            Sorter2<Integer> quickSorterInt = new QuickSorter2<>();
            Sorter2<Person> mergeSorterPerson = new MergeSorter2<>();
            Sorter2<Person> quickSorterPerson = new QuickSorter2<>();

            Comparator<Integer> intComparator = Integer::compareTo;

            Comparator<Person> lastNameComparator = Comparator.comparing(Person::getLastName);
            Comparator<Person> firstNameComparator = Comparator.comparing(Person::getFirstName);

            Comparator<Person> dateComparator = createDateComparator();
            Comparator<Person> ageComparator = dateComparator;

            Stopwatch timer = new Stopwatch();
            
            // Maps to store timing results for comparison
            Map<String, Double> intSortTimes = new HashMap<>();
            Map<String, Double> lastNameSortTimes = new HashMap<>();
            Map<String, Double> firstNameSortTimes = new HashMap<>();
            Map<String, Double> ageSortTimes = new HashMap<>();

            System.out.println("\n--- Sorting Integers ---");
            
            // Arrays.sort
            Integer[] integersCopy = Arrays.copyOf(integers, integers.length);
            timer.start();
            Arrays.sort(integersCopy);
            double arraysTime = timer.getElapsedTime();
            System.out.println("Arrays.sort time: " + arraysTime + " seconds");
            intSortTimes.put("Arrays.sort", arraysTime);

            // MergeSort
            integersCopy = Arrays.copyOf(integers, integers.length);
            timer.start();
            mergeSorterInt.sort(integersCopy, intComparator);
            double mergeTime = timer.getElapsedTime();
            System.out.println("Custom merge sort time: " + mergeTime + " seconds (" + 
                    calculateSpeedup(arraysTime, mergeTime) + ")");
            intSortTimes.put("MergeSort", mergeTime);

            // QuickSort
            integersCopy = Arrays.copyOf(integers, integers.length);
            timer.start();
            quickSorterInt.sort(integersCopy, intComparator);
            double quickTime = timer.getElapsedTime();
            System.out.println("Custom quick sort time: " + quickTime + " seconds (" + 
                    calculateSpeedup(arraysTime, quickTime) + ")");
            intSortTimes.put("QuickSort", quickTime);

            System.out.println("Fastest algorithm for integers: " + findFastestAlgorithm(intSortTimes));

            System.out.println("\n--- Sorting Persons by Last Name ---");
            
            // Arrays.sort
            Person[] personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            Arrays.sort(personsCopy, lastNameComparator);
            arraysTime = timer.getElapsedTime();
            System.out.println("Arrays.sort time: " + arraysTime + " seconds");
            lastNameSortTimes.put("Arrays.sort", arraysTime);

            // MergeSort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            mergeSorterPerson.sort(personsCopy, lastNameComparator);
            mergeTime = timer.getElapsedTime();
            System.out.println("Custom merge sort time: " + mergeTime + " seconds (" + 
                    calculateSpeedup(arraysTime, mergeTime) + ")");
            lastNameSortTimes.put("MergeSort", mergeTime);

            // QuickSort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            quickSorterPerson.sort(personsCopy, lastNameComparator);
            quickTime = timer.getElapsedTime();
            System.out.println("Custom quick sort time: " + quickTime + " seconds (" + 
                    calculateSpeedup(arraysTime, quickTime) + ")");
            lastNameSortTimes.put("QuickSort", quickTime);

            System.out.println("Fastest algorithm for last name sorting: " + findFastestAlgorithm(lastNameSortTimes));

            System.out.println("\n--- Sorting Persons by First Name ---");
            
            // Arrays.sort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            Arrays.sort(personsCopy, firstNameComparator);
            arraysTime = timer.getElapsedTime();
            System.out.println("Arrays.sort time: " + arraysTime + " seconds");
            firstNameSortTimes.put("Arrays.sort", arraysTime);

            // MergeSort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            mergeSorterPerson.sort(personsCopy, firstNameComparator);
            mergeTime = timer.getElapsedTime();
            System.out.println("Custom merge sort time: " + mergeTime + " seconds (" + 
                    calculateSpeedup(arraysTime, mergeTime) + ")");
            firstNameSortTimes.put("MergeSort", mergeTime);

            // QuickSort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            quickSorterPerson.sort(personsCopy, firstNameComparator);
            quickTime = timer.getElapsedTime();
            System.out.println("Custom quick sort time: " + quickTime + " seconds (" + 
                    calculateSpeedup(arraysTime, quickTime) + ")");
            firstNameSortTimes.put("QuickSort", quickTime);

            System.out.println("Fastest algorithm for first name sorting: " + findFastestAlgorithm(firstNameSortTimes));

            System.out.println("\n--- Sorting Persons by Age ---");
            
            // Arrays.sort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            Arrays.sort(personsCopy, ageComparator);
            arraysTime = timer.getElapsedTime();
            System.out.println("Arrays.sort time: " + arraysTime + " seconds");
            ageSortTimes.put("Arrays.sort", arraysTime);

            // MergeSort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            mergeSorterPerson.sort(personsCopy, ageComparator);
            mergeTime = timer.getElapsedTime();
            System.out.println("Custom merge sort time: " + mergeTime + " seconds (" + 
                    calculateSpeedup(arraysTime, mergeTime) + ")");
            ageSortTimes.put("MergeSort", mergeTime);

            // QuickSort
            personsCopy = Arrays.copyOf(persons, persons.length);
            timer.start();
            quickSorterPerson.sort(personsCopy, ageComparator);
            quickTime = timer.getElapsedTime();
            System.out.println("Custom quick sort time: " + quickTime + " seconds (" + 
                    calculateSpeedup(arraysTime, quickTime) + ")");
            ageSortTimes.put("QuickSort", quickTime);

            System.out.println("Fastest algorithm for age sorting: " + findFastestAlgorithm(ageSortTimes));

            // Overall summary
            System.out.println("\n--- Performance Summary ---");
            System.out.println("For integers: " + findFastestAlgorithm(intSortTimes) + " was fastest");
            System.out.println("For persons by last name: " + findFastestAlgorithm(lastNameSortTimes) + " was fastest");
            System.out.println("For persons by first name: " + findFastestAlgorithm(firstNameSortTimes) + " was fastest");
            System.out.println("For persons by age: " + findFastestAlgorithm(ageSortTimes) + " was fastest");

            // Remove "/*" and "*/" to see the results
            /*
            // Display results using the display count
            System.out.println("\n--- Sample of Sorted Results ---");
            System.out.println("First " + displayCount + " integers:");
            for (int i = 0; i < Math.min(displayCount, integersCopy.length); i++) {
                System.out.println(integersCopy[i]);
            }

            System.out.println("\nFirst " + displayCount + " persons sorted by age:");
            for (int i = 0; i < Math.min(displayCount, personsCopy.length); i++) {
                System.out.println(personsCopy[i] + " - Age: " + personsCopy[i].getAge());
            }
            */
            
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}