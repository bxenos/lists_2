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
		Node<T> newNode = new Node<T>(element);
		newNode.setNextNode(head);
		head = newNode;
		if (tail == null) { //or size == 0 or isEmpty()
			tail = newNode;
		} 
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
		if (!isEmpty()) { //head != null, tail != null, size != 0 ALL OF THESE WORK
		tail.setNextNode(newNode);
		} else {
			head = newNode;
		}
		tail = newNode;
		size++;
		modCount++;
		
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		// TODO 
		
	}

	@Override
	public void add(int index, T element) {//whenever adding, you are looking first for the node before where you are inserting the element.
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			addToFront(element);
		} else if (index == size) {
			addToRear(element);
		} else {
		Node<T> currentNode = head;
		for (int i = 0; i < index - 1; i++) {
			currentNode = currentNode.getNextNode();
		}
		Node<T> newNode = new Node<T>(element);
		newNode.setNextNode(currentNode.getNextNode());
		currentNode.setNextNode(newNode);
		size++;
		modCount++;
		}
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

		T retVal;
		//checking if the element being removed is the head
		if (head.getElement().equals(element)) {
			retVal = head.getElement();
			head = head.getNextNode();
			if (head == null) {
				tail = null;
			}
		}
		else { //going ahead with the other cases
		Node<T> curNode = head;
		while (curNode != tail && !curNode.getNextNode().getElement().equals(element)) {
			curNode = curNode.getNextNode();
		}
		if (curNode == tail) { //or curNode.getNextNoded() == null
			throw new NoSuchElementException();
		}
		retVal = curNode.getNextNode().getElement();
		if(curNode.getNextNode() == tail) {
			tail = curNode;
		}
		curNode.setNextNode(curNode.getNextNode().getNextNode());
		
		size--;
		modCount++;
	}

		return retVal;
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