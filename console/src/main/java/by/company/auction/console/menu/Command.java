package by.company.auction.console.menu;

import java.util.function.Supplier;

public class Command {
    private String name;
    private String description;
    private Supplier action;


    public Command(String name, String description, Supplier action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public void run() {
        action.get();
    }


    @Override
    public String toString() {
        return name + " - " + description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Supplier getAction() {
        return action;
    }

    public void setAction(Supplier action) {
        this.action = action;
    }
}
