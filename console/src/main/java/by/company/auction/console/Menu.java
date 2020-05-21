package by.company.auction.console;

import java.util.Arrays;
import java.util.List;

public class Menu {
    private String header;
    private List<Command> commands;

    Menu(String header, Command... commands) {
        this.header = header;
        this.commands = Arrays.asList(commands);
    }

    public void open() {
        System.out.println(header);
        commands.forEach(System.out::println);
        System.out.println();
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @SuppressWarnings("WeakerAccess")
    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
