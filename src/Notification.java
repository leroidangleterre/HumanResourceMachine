/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class Notification {

    private String name;
    private Object content;
    private String options;

    public Notification(String nameParam, Object contentParam, String optionsParam) {
        name = nameParam;
        content = contentParam;
        options = optionsParam;
    }

    public Notification(String nameParam, Object contentParam) {
        this(nameParam, contentParam, "");
    }

    public Notification(InstructionModel inst) {
        this(inst.getName(), null, inst.getOptions());
    }

    public String getName() {
        return name;
    }

    public Object getObject() {
        return content;
    }

    public String getOptions() {
        return options;
    }

    public void setWorker(Worker newWorker) {
        this.content = newWorker;
    }

    /**
     * Add an option.
     *
     * @param newOption the added option must start with a alphabetic character.
     * If there is no pre-existing option, the new one is simply added;
     * if there is, a ' ' is appended before the new one is added.
     */
    public void addOption(String newOption) {
        if (options.isEmpty()) {
            options = newOption;
        } else {
            options += " " + newOption;
        }
    }
}
