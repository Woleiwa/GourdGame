package Gui;

public class StackException extends Exception{
    final private String message = "The stack is empty!";
    public StackException(){

    };

    @Override
    public String getMessage(){
        return this.message;
    }
}