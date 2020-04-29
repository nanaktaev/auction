package by.company.auction.console.menu;

import java.util.function.Supplier;

public class Command {
    private String name;
    private String description;
    private Supplier performedAction;


    Command(String name, String description, Supplier performedAction) {
        this.name = name;
        this.description = description;
        this.performedAction = performedAction;
    }

    void run() {
        performedAction.get();
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }

    @SuppressWarnings("WeakerAccess")
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

    public Supplier getPerformedAction() {
        return performedAction;
    }

    public void setPerformedAction(Supplier performedAction) {
        this.performedAction = performedAction;
    }
}
