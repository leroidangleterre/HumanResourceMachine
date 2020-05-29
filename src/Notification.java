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
//        System.out.println("Notification: created Notification " + name + " options <" + optionsParam + ">");
    }

    public Notification(String nameParam, Object contentParam) {
        this(nameParam, contentParam, "");
    }

    public Notification(InstructionModel inst) {
//        this.name = inst.getName();
//        this.options = inst.getOptions();
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
//        System.out.println("        Notification " + name + " created for worker " + newWorker.getSerial());
    }
}
