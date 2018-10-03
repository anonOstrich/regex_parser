package utils;

import domain.OwnLinkedList;
import domain.PairNode;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SetIterator<T> implements Iterator{

    private OwnLinkedList<T, T> elements;
    private PairNode<T, T> current;

    public SetIterator(OwnLinkedList<T, T>[] table) {
        elements = new OwnLinkedList();

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            }

            PairNode<T, T> node = table[i].getFirstNode();
            while (node != null) {
                PairNode<T, T> copy = new PairNode(node.getKey(), null);
                elements.insert(copy);
                node = node.getNext();
            }
        }

        current = elements.getFirstNode();

    }

    @Override
    public boolean hasNext() {
        return current != null; 
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T result = current.getKey(); 
        current = current.getNext(); 
        return result;
    }

}
