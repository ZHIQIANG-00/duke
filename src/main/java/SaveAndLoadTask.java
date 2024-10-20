import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SaveAndLoadTask {
    private static final String FILE = "./data/duke.txt";

    // save all the task in the txt file
    public static void saveTasks (ArrayList<Task> taskList){
        try{
            File file = new File(FILE);
            // check if file exist else create the directory file
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(FILE);

//            for (int i =0; i < taskList.size(); i++){
//                Task task = taskList.get(i);
//                writer.write((i + 1) + ". " + task.toFileString() + System.lineSeparator());
//            }
//            writer.close();
            for (Task task : taskList) {
                writer.write(task.toFileString() + System.lineSeparator());
            }
            writer.close();
        }
        catch (IOException e){
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Load all the task from the task File
    public static ArrayList<Task> loadTasks(){
        ArrayList<Task> taskList = new ArrayList<>();
        File file = new File(FILE);

        // If there is no file or file does not exist. return empty list
        if (!file.exists()){
            System.out.println("No task file found, starting with empty task list. " );
            return taskList;
        }

        try{
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                System.out.println("Reading line: " + line); // Debug print

                String[] taskDetail = line.split(" \\| ");

                // Ensure we have enough details
                if (taskDetail.length < 3) {
                    System.out.println("Skipping line due to insufficient details: " + line);
                    continue; // Skip this line
                }

                // Get the task type from the first element
                String taskType = taskDetail[0].trim();
                Task task = null;

                // Load task with task type
                switch (taskType) {
                    case "T":
                        task = new ToDo(taskDetail[2].trim());
                        break;
                    case "D":
                        if (taskDetail.length < 4) {
                            System.out.println("Skipping line due to insufficient details for Deadline: " + line);
                            continue;
                        }
                        task = new Deadline(taskDetail[2].trim(), taskDetail[3].trim());
                        break;
                    case "E":
                        if (taskDetail.length < 5) {
                            System.out.println("Skipping line due to insufficient details for Event: " + line);
                            continue;
                        }
                        task = new Event(taskDetail[2].trim(), taskDetail[3].trim(), taskDetail[4].trim());
                        break;
                    default:
                        System.out.println("Invalid task type in the file: " + taskType);
                        continue; // Skip invalid task types
                }

                // Mark task as done if it's marked in the file
                if (taskDetail[1].trim().equals("1")) {
                    task.markAsDone();
                }

                taskList.add(task);
                System.out.println("Loaded task: " + task.toString()); // Debug print
            }
            scanner.close();
        }

        catch(FileNotFoundException e){
            System.out.println("Task file not found.");
        }
        catch(Exception e){
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        return taskList;
    }
}
