package main.java.double_linked_list;

import java.util.Objects;

public class DoubleLinkedList<T> {

  private ListItem<T> head;
  private ListItem<T> tail;
  private int size = 0;

  public ListItem<T> popHeadElement() {
    if(getSize() != 0){
      ListItem<T> temp = head;
      head = head.getNext();
      size--;
      return temp;
    }
    if (getSize() == 0) {
      tail = null;
    } else {
      head.setPrev(null);
    }
    return head;
  }

  public ListItem<T> popTailElement() {
    if(getSize() != 0){
      ListItem<T> temp = tail;
      tail = tail.getPrev();
      size--;
      return temp;
    }
    if (getSize() == 0) {
      head = null;
    } else {
      tail.setNext(null);
    }
    return tail;
  }

  public void removeHeadElement() {
    if (getSize() != 0) {
      head = head.getNext();
      size--;
    }
    if (getSize() == 0) {
      tail = null;
    } else {
      head.setPrev(null);
    }
  }

  public void removeTailElement() {
    if (getSize() != 0) {
      if (getSize() == 1) {
        head = null;
        tail = null;
      } else {
        tail.getPrev().setNext(null);
        tail = tail.getPrev();
      }
      size--;
    }
  }

  public void addToHead(T data) {
    ListItem<T> node = new ListItem<>(data);
    ListItem<T> temp = head;
    head = node;
    head.setNext(temp);
    if (getSize() == 0) {
      tail = head;
    } else {
      temp.setPrev(head);
    }
    size++;
  }

  public void addToTail(T data) {
    ListItem<T> node = new ListItem<>(data);
    if (getSize() == 0) {
      head = node;
    } else {
      tail.setNext(node);
      node.setPrev(tail);
    }
    tail = node;
    size++;
  }

  public int getSize() {
    return size;
  }

  public ListItem<T> getHeadElement() {
    return head;
  }

  public ListItem<T> getTailElement() {
    return tail;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoubleLinkedList<T> that = (DoubleLinkedList<T>) o;
    return Objects.equals(head, that.head) && Objects.equals(tail, that.tail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(head, tail);
  }

  @Override
  public String toString() {
    if (head == null) {
      return "DoubleLinkedList is empty size = " + size;
    }

    StringBuilder stringBuilder = new StringBuilder(head.toString());
    ListItem<T> item = head;
    while (item.next != null) {
      if (item.next.prev == item) {
        stringBuilder.append("<-");
      }

      stringBuilder.append(" -> ").append(item.next);
      item = item.next;
    }

    return "DoubleLinkedList{size=" + size + "\n" + stringBuilder.toString() + "}";
  }
}