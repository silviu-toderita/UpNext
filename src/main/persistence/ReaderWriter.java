package persistence;

import model.Task;
import model.TaskList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Reader/writer object for saving/loading application state to/from a JSON file
public class ReaderWriter {
    private static final int INDENT_FACTOR = 4;
    private PrintWriter writer;
    private String path;

    // EFFECTS: Creates a ReaderWriter with given file path
    public ReaderWriter(String path) {
        this.path = path;
    }

    // MODIFIES: this
    // EFFECTS: Write the given tasklist to the file path.
    //          If the file path is not valid, throw FileNotFoundException
    public void write(TaskList taskList) throws FileNotFoundException {
        writer = new PrintWriter(new File(path));
        JSONArray jsonArray = taskList.serialize();
        writer.print(jsonArray.toString(INDENT_FACTOR));
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: Read from the file path and return a list of tasks from the JSON in that file
    //          If there is any error in reading from the file, throw IOException
    public TaskList read() throws IOException {
        StringBuilder rawData = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.forEach(s -> rawData.append(s));
        }

        JSONArray jsonArray = new JSONArray(rawData.toString());
        return parseTaskList(jsonArray);
    }

    // EFFECTS: Return a list of tasks from the given JSON array
    private TaskList parseTaskList(JSONArray jsonArray) {
        TaskList taskList = new TaskList();
        for (Object each : jsonArray) {
            Task task = new Task((JSONObject)each);
            taskList.add(task);
        }
        return taskList;
    }


}
