import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Brayden Xenos
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head;
    private Node<T> tail;
	private int size;
	private int modCount;
	
	/** 
     * Initialized a new empty list.
    */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		// TODO 
		
	}

	@Override
	public void addToRear(T element) {
		// TODO 
		
	}

	@Override
	public void add(T element) {
		// TODO 
		
	}

	@Override
	public void addAfter(T element, T target) {
		// TODO 
		
	}

	@Override
	public void add(int index, T element) {
		// TODO 
		
	}

	@Override
	public T removeFirst() {
		// TODO 
		return null;
	}

	@Override
	public T removeLast() {
		// TODO 
		return null;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				// current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			// head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			// tail.setNext(null);
		} else { //somewhere in the middle
			// previous.setNext(current.getNext());
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		// TODO 
		return null;
	}

	@Override
	public void set(int index, T element) {
		// TODO 
		
	}

	@Override
	public T get(int index) {
		// TODO 
		return null;
	}

	@Override
	public int indexOf(T element) {
		Node<T> currentNode = head;
        int currentIndex = 0;

        while(currentNode != null && currentNode.getElement().equals(element)){
            currentNode = currentNode.getNextNode();
            currentIndex++;
        }
        if (currentNode == null) {  //didn't find it
            currentIndex = -1;  //best praacice: never have more than 1 return statement
        }
		return currentIndex;
	}

	@Override
	public T first() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return indexOf(target) > -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (T element : this) {
            sb.append(element.toString());
            sb.append(", ");
        }
        if (size() > 0) { //clear trailing ", "
            sb.delete(sb.length()-2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			// TODO 
			return false;
		}

		@Override
		public T next() {
			// TODO 
			return null;
		}
		
		@Override
		public void remove() {
			// TODO
		}
	}
}