package Collections;

import java.util.Objects;

public class List<T> {

    private Node<T> firstNode;
    private Node<T> lastNode;
    private int size = 0;

    public List() {
        lastNode = new Node<T>(null, firstNode, null);
        firstNode = new Node<T>(null, null, lastNode);
    }

    private class Node<T> {

        private T thisElement;
        private Node<T> nextElement;
        private Node<T> backElement;


        public Node(T thisElement, Node<T> backElement, Node<T> nextElement){
            this.thisElement = thisElement;
            this.nextElement = nextElement;
            this.backElement = backElement;
        }

        public T getThisElement() {
            return thisElement;
        }

        public void setThisElement(T thisElement) {
            this.thisElement = thisElement;
        }

        public Node<T> getNextElement() {
            return nextElement;
        }

        public void setNextElement(Node<T> nextElement) {
            this.nextElement = nextElement;
        }

        public Node<T> getBackElement() {
            return backElement;
        }

        public void setBackElement(Node<T> backElement) {
            this.backElement = backElement;
        }
    }

    private Node<T> getNode(int index){
        Node<T> element;//System.out.println(element.getThisElement());

        if(index > size || index < 0){
            System.exit(3);
        }

        if(index <= size/2) {
            element = firstNode.getNextElement();
            for (int i = 0; i < index; i++) {
                element = getNext(element);
            }
        } else {
            element = lastNode.getBackElement();
            for (int i = size-1; index < i; i--) {
                element = getBack(element);
            }
        }

        return element;
    }

    private Node<T> getNext(Node<T> current){
        return current.getNextElement();
    }

    private Node<T> getBack(Node<T> current){
        return current.getBackElement();
    }

    public void add(T newElement){
        Node<T> back = lastNode;
        back.setThisElement(newElement);//System.out.println(back.getThisElement());
        lastNode = new Node<T>(null, back, null);
        back.setNextElement(lastNode);
        size++;
    }

    public T get(int index){
        Node<T> element = getNode(index);
        return element.getThisElement();
    }

    public int getSize(){
        return size;
    }

    public T remove(int index){
        Node<T> x = getNode(index);
        Node<T> prevX = x.getBackElement();//System.out.print(prevX.getThisElement());
        Node<T> nextX = x.getNextElement();//System.out.print(nextX.getThisElement());

        if(prevX == null){
            firstNode.setNextElement(nextX);
        } else {
            prevX.setNextElement(nextX);
            x.setNextElement(null);
        }

        if(nextX == null){
            lastNode.setBackElement(prevX);
        } else {
            nextX.setBackElement(prevX);
            x.setBackElement(null);
        }

        size--;

        return x.getThisElement();
    }

    public int contains(T t){
        Node<T> element = firstNode.getNextElement();
        for (int index = 0; index < size; index++){
            if(Objects.equals(element.getThisElement(), t)){
                return index;
            } else {
                element = getNext(element);
            }
        }
        return -1;
    }

    private String printList(){
        String s = "";
        for(int i = 0; i < size; i++){
            s = s + " " + get(i).toString();
        }
        return s;
    }

    @Override
    public String toString(){
        return printList();
    }
}