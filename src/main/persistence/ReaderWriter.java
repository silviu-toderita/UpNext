package persistence;

import exceptions.InvalidJsonFileException;
import model.Task;
import model.TaskList;
import org.json.JSONArray;
import org.json.JSONException;
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
    private String path;

    // EFFECTS: Creates a ReaderWriter with given file path
    public ReaderWriter(String path) {
        this.path = path;
    }

    // MODIFIES: File at path
    // EFFECTS: Write the given TaskList to the file path.
    //          Throws FileNotFoundException if the file path is not valid
    public void write(TaskList taskList) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(path));
        JSONArray jsonArray = taskList.serialize();
        writer.print(jsonArray.toString(INDENT_FACTOR));
        writer.close();
    }

    // EFFECTS: Read from the file path and return a list of tasks from the JSON in that file
    //          Throws IOException if there is any error in reading from the file
    //          Throws InvalidJsonFileException if JSON file was invalid (file will be deleted before exception thrown)
    public TaskList read() throws IOException, InvalidJsonFileException {
        StringBuilder rawData = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.forEach(s -> rawData.append(s));
        }

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(rawData.toString());
            return parseTaskList(jsonArray);
        } catch (JSONException e) {
            delete();
            throw new InvalidJsonFileException();
        }

    }

    // EFFECTS: Deletes the file at the path
    public void delete() {
        File file = new File(path);
        file.delete();
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
