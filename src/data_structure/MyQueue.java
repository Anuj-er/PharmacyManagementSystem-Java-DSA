package data_structure;

import java.util.NoSuchElementException;

public class MyQueue <T>{
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T value) {
            this.data = value;
            this.next = null;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public MyQueue() {
        front = rear = null;
        size = 0;
    }

    // Add an element to the end of the queue
    public void enqueue(T value) {
        Node<T> newNode = new Node<>(value);
        if (rear != null) {
            rear.next = newNode;
        }
        rear = newNode;
        if (front == null) {
            front = rear;
        }
        size++;
    }

    // Remove and return the front element of the queue
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T value = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return value;
    }

    // Return the front element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return front == null;
    }

    // Return the number of elements in the queue
    public int size() {
        return size;
    }

    // Clear the queue
    public void clear() {
        front = rear = null;
        size = 0;
    }

    // Optional: print queue elements
    public void printQueue() {
        Node<T> current = front;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
