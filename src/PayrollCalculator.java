import java.io.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PayrollCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the employee file to process: ");
        String inputFileName = scanner.nextLine();

        System.out.print("Enter the name of the payroll file to create: ");
        String outputFileName = scanner.nextLine();

        List<Employee> employees = new ArrayList<>();

        // Read employee data using BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String header = reader.readLine(); // Skip header line
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|");

                try {
                    int employeeId = Integer.parseInt(tokens[0]);
                    String name = tokens[1];
                    double hoursWorked = Double.parseDouble(tokens[2]);
                    double payRate = Double.parseDouble(tokens[3]);

                    employees.add(new Employee(employeeId, name, hoursWorked, payRate));
                } catch (NumberFormatException e) {
                    System.out.println("Number format issue in line: " + line);
                }
            }
        }
                catch (IOException e) {
                    System.out.println("Failed to read input file: " + e.getMessage());
                    return;
        }

        // Write output using BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            if (outputFileName.toLowerCase().endsWith(".json")) {
                writeJson(writer, employees);
            } else {
                writeCsv(writer, employees);
            }
            System.out.println("Payroll report written to " + outputFileName);
        } catch (IOException e) {
            System.out.println("Failed to write output file: " + e.getMessage());
        }
    }

    private static void writeCsv(BufferedWriter writer, List<Employee> employees) throws IOException {
        writer.write("id|name|gross pay");
        writer.newLine();

        for (Employee e : employees) {
            writer.write(String.format("%d|%s|%.2f", e.getEmployeeId(), e.getName(), e.getGrossPay()));
            writer.newLine();
        }
    }

    private static void writeJson(BufferedWriter writer, List<Employee> employees) throws IOException {
        writer.write("[");
        writer.newLine();

        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            writer.write(String.format("  { \"id\": %d, \"name\": \"%s\", \"grossPay\": %.2f }",
                    e.getEmployeeId(), e.getName(), e.getGrossPay()));

            if (i < employees.size() - 1) {
                writer.write(",");
            }
            writer.newLine();
        }

        writer.write("]");
        writer.newLine();
    }
}