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

    public Notification(String nameParam, Object contentParam) {
        name = nameParam;
        content = contentParam;
    }

    public String getName() {
        return name;
    }

    public Object getObject() {
        return content;
    }
}
