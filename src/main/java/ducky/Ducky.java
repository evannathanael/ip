package ducky;

/**
 * Coordinates the user interface, command parser, task list, and storage.
 */
public class Ducky {
    private static final String DATA_FILE_PATH = "data/duke.txt";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    /**
     * Creates a chatbot using the given data file.
     *
     * @param filePath the path of the task data file.
     */
    public Ducky(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /**
     * Loads saved tasks, falling back to an empty task list if loading fails.
     *
     * @return the loaded task list.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (DuckyException e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Runs the chatbot until the user enters {@code bye}.
     */
    public void run() {
        ui.showWelcome();
        while (true) {
            String command = ui.readCommand();
            try {
                if (!processCommand(command)) {
                    return;
                }
            } catch (DuckyException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Parses and executes one user command.
     *
     * @param command the user's command.
     * @return {@code false} when the chatbot should exit, otherwise {@code true}.
     * @throws DuckyException if the command is invalid or cannot be saved.
     */
    private boolean processCommand(String command) throws DuckyException {
        Parser.ParsedCommand parsedCommand = parser.parse(command);
        switch (parsedCommand.getType()) {
        case BYE:
            ui.showExitMessage();
            return false;
        case LIST:
            ui.showTasks(tasks);
            return true;
        case ADD:
            tasks.add(parsedCommand.getTask());
            ui.showTaskAdded(parsedCommand.getTask(), tasks.size());
            storage.save(tasks);
            return true;
        case MARK:
            Task taskToMark = getTask(parsedCommand.getTaskIndex());
            taskToMark.markAsDone();
            ui.showTaskMarkedAsDone(taskToMark);
            storage.save(tasks);
            return true;
        case UNMARK:
            Task taskToUnmark = getTask(parsedCommand.getTaskIndex());
            taskToUnmark.unmark();
            ui.showTaskUnmarked(taskToUnmark);
            storage.save(tasks);
            return true;
        case DELETE:
            Task deletedTask = tasks.delete(parsedCommand.getTaskIndex());
            ui.showTaskDeleted(deletedTask, tasks.size());
            storage.save(tasks);
            return true;
        default:
            throw new DuckyException("I didn't get what you said 🐥");
        }
    }

    /**
     * Returns the task at a parsed index after checking that it exists.
     *
     * @param index the zero-based task index.
     * @return the task at the index.
     * @throws DuckyException if the index does not identify a task.
     */
    private Task getTask(int index) throws DuckyException {
        if (index < 0 || index >= tasks.size()) {
            throw new DuckyException("That task number does not exist 🐥");
        }
        return tasks.get(index);
    }

    /**
     * Starts Ducky with the default data file.
     *
     * @param args command-line arguments, which are currently unused.
     */
    public static void main(String[] args) {
        new Ducky(DATA_FILE_PATH).run();
    }
}
