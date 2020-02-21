import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public interface Observable {

    public void addObserver(Observer obs);

    public void removeObserver(Observer obs);

    public void notifyObservers(Notification n);

}
