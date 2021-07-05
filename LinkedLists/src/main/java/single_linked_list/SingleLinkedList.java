package main.java.single_linked_list;

import java.util.Objects;

public class SingleLinkedList {
    private ListItem top;

    public void push(ListItem item) {
        if (top != null) {
            item.setNext(top);
        }
        top = item;
    }

    public ListItem pop() {
        ListItem item = top;
        if (top != null) {
            top = top.getNext();
            item.setNext(null);
        }
        return item;
    }

    public void removeTop() {
        if (top != null) {
            top = top.getNext();
        }
    }

    public void removeLast() {
        // TODO: remove last element
        if(top != null){
        ListItem last = top;
        ListItem previousToLast = null;
        while(last.getNext() != null){
            previousToLast = last;
            last = last.getNext();
        }
        previousToLast.setNext(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleLinkedList that = (SingleLinkedList) o;
        return Objects.equals(top, that.top);
    }

    @Override
    public String toString() {
        return "SingleLinkedList{" +
                "top=" + top +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(top);
    }
}