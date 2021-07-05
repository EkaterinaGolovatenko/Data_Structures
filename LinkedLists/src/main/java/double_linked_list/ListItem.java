package main.java.double_linked_list;

import java.util.Objects;

public class ListItem<T> {
    private final T data;
    ListItem<T> prev;
    ListItem<T> next;

    public ListItem(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public ListItem<T> getNext() {
        return next;
    }
    public void setNext(ListItem<T> item) {
        next = item;
    }
    public ListItem<T> getPrev() {
        return prev;
    }
    public void setPrev(ListItem<T> item) {
        prev = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem<T> listItem = (ListItem<T>) o;

        if (!Objects.equals(data, listItem.data)) return false;
        if (prev != null ? !prev.data.equals(listItem.prev.data) : listItem.prev != null)
            return false;
        return next != null ? next.data.equals(listItem.next.data) : listItem.next == null;

    }

    @Override
    public String toString() {
        return "ListItem{data='" + data + "'}";
    }

    @Override
    public int hashCode() {
        int result = data != null ? data.hashCode() : 0;
        result = 31 * result + (prev != null ? prev.data.hashCode() : 0);
        result = 31 * result + (next != null ? next.data.hashCode() : 0);
        return result;
    }
}