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
    private boolean isExit;

    /**
     * Creates a chatbot using the default data file.
     */
    public Ducky() {
        this(DATA_FILE_PATH);
    }

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
            System.out.println(ui.showError(e.getMessage()));
            return new TaskList();
        }
    }

    /**
     * Runs the chatbot on the console until the user enters {@code bye}.
     */
    public void run() {
        System.out.println(ui.showWelcome());
        while (!isExit) {
            String command = ui.readCommand();
            try {
                System.out.println(processCommand(command));
            } catch (DuckyException e) {
                System.out.println(ui.showError(e.getMessage()));
            }
        }
    }

    /**
     * Returns the chatbot's welcome message, for display when a GUI session starts.
     *
     * @return the welcome message.
     */
    public String getWelcomeMessage() {
        return ui.showWelcome();
    }

    /**
     * Parses and executes one user command, returning the chatbot's reply.
     * Used by the GUI, which displays one reply per user input rather than printing to the console.
     *
     * @param input the user's command.
     * @return the chatbot's reply.
     */
    public String getResponse(String input) {
        try {
            return processCommand(input);
        } catch (DuckyException e) {
            return ui.showError(e.getMessage());
        }
    }

    /**
     * Returns whether the most recently processed command was {@code bye}.
     * Used by the GUI to decide when to close the application window.
     *
     * @return {@code true} if the chatbot should exit, otherwise {@code false}.
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Parses and executes one user command.
     *
     * @param command the user's command.
     * @return the chatbot's reply to the command.
     * @throws DuckyException if the command is invalid or cannot be saved.
     */
    private String processCommand(String command) throws DuckyException {
        Parser.ParsedCommand parsedCommand = parser.parse(command);
        switch (parsedCommand.getType()) {
            case BYE:
                isExit = true;
                return ui.showExitMessage();
            case LIST:
                return ui.showTasks(tasks);
            case FIND:
                return ui.showMatchingTasks(tasks.find(parsedCommand.getKeyword()));
            case ADD:
                tasks.add(parsedCommand.getTask());
                String taskAddedMessage = ui.showTaskAdded(parsedCommand.getTask(), tasks.size());
                storage.save(tasks);
                return taskAddedMessage;
            case MARK:
                Task taskToMark = getTask(parsedCommand.getTaskIndex());
                taskToMark.markAsDone();
                String taskMarkedMessage = ui.showTaskMarkedAsDone(taskToMark);
                storage.save(tasks);
                return taskMarkedMessage;
            case UNMARK:
                Task taskToUnmark = getTask(parsedCommand.getTaskIndex());
                taskToUnmark.unmark();
                String taskUnmarkedMessage = ui.showTaskUnmarked(taskToUnmark);
                storage.save(tasks);
                return taskUnmarkedMessage;
            case DELETE:
                Task deletedTask = tasks.delete(parsedCommand.getTaskIndex());
                String taskDeletedMessage = ui.showTaskDeleted(deletedTask, tasks.size());
                storage.save(tasks);
                return taskDeletedMessage;
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
     * Starts Ducky with the default data file, using the console-based UI.
     *
     * @param args command-line arguments, which are currently unused.
     */
    public static void main(String[] args) {
        new Ducky(DATA_FILE_PATH).run();
    }
}
