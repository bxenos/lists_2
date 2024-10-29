import java.util.ConcurrentModificationException;
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

	/**
	 * checks if the list is empty. This ethod is to avoid code duplication
	 * 
	 * @author Brayden Xenos
	 * @throws NoSuchElementException
	 */
	private void checkIfEmpty() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>(element);
		newNode.setNextNode(head);
		head = newNode;
		if (isEmpty()) { //or size == 0 or isEmpty()
			tail = newNode;
		} 
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
		if (head == null) { 
			head = newNode;
		} else {
			tail.setNextNode(newNode);
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
		Node<T> currNode = head;
		while(currNode != null && !currNode.getElement().equals(target)) {
			currNode = currNode.getNextNode();
		}

		if (currNode == null) {
			throw new NoSuchElementException();
		}
		
		Node<T> newNode = new Node<T>(element);
		newNode.setNextNode(currNode.getNextNode());
		currNode.setNextNode(newNode);

		if(newNode.getNextNode() == null) {
			tail = newNode;
		}
		size++;
		modCount++;

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
		checkIfEmpty();
		T retVal;
		
		if (size == 1) {
			retVal = head.getElement();
			head = tail = null;
		}
		else {
			Node<T> curNode = head;
			retVal = curNode.getElement();
			head = curNode.getNextNode();
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T removeLast() {
		checkIfEmpty();
		Node<T> tempNode;
		if (size == 1) {
			tempNode = head;
			head = tail = null;
		} else {
			Node<T> curNode = head;
			for (int i=0; i < size - 2; i++) {
				curNode = curNode.getNextNode();
			}
			tempNode = curNode.getNextNode();
			curNode.setNextNode(null);
			tail = curNode;
		}
		size--;
		modCount++;
		return tempNode.getElement();
	}

	@Override
	public T remove(T element) {

		checkIfEmpty();

		T retVal = null;
		//checking if the element being removed is the head
		if (element.equals(head.getElement())) {
			retVal = head.getElement();
			head = head.getNextNode();
			if (head == null) {
				tail = null;
			}
		}
		else { //going ahead with the other cases
			Node<T> curNode = head;
			while (curNode.getNextNode() != null && !curNode.getNextNode().getElement().equals(element)) {
				curNode = curNode.getNextNode();
			}
			if (curNode.getNextNode() == null) { //or curNode == tail
				throw new NoSuchElementException();
			}
			retVal = curNode.getNextNode().getElement();
			curNode.setNextNode(curNode.getNextNode().getNextNode());
			if(curNode.getNextNode() == null) {
				tail = curNode;
			}
			
		}
		
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		//checks if the element is 0 and does the removeFirst method.
		T retVal;
		if (index == 0) {
			retVal = removeFirst();
		} else {
	
			Node<T> currNode = head;
			Node<T> tempNode = null;
			for (int i = 0; i < index; i++) {
				tempNode = currNode;
				currNode = currNode.getNextNode();
			}

			retVal = currNode.getElement();
			tempNode.setNextNode(currNode.getNextNode());

			if (currNode.getNextNode() == null) {
				tail = tempNode;
			}
			size--;
			modCount++;
		}
		return retVal;
		
	}

	@Override
	public void set(int index, T element) {
		//checking if the element already exists.
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		}

		Node<T> currNode = head;
		for (int i = 0; i < index; i++) {
			currNode = currNode.getNextNode();
		}
		currNode.setElement(element);		

		modCount++;
	}

	@Override
	public T get(int index) {
		//Checking if the index exists
		if (index < 0 ||index >= size) {
			throw new IndexOutOfBoundsException();
		}
		T retVal;
		if (index == 0) {
			retVal = head.getElement();
		} else {
			Node<T> currNode = head;
			for (int i = 0; i < index; i++) {
				currNode = currNode.getNextNode();
			}
			retVal = currNode.getElement();
		}
		return retVal;
	}

	@Override
	public int indexOf(T element) {
		Node<T> currentNode = head;
        int currentIndex = 0;

        while(currentNode != null && !currentNode.getElement().equals(element)){
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
		checkIfEmpty();
		return head.getElement();
	}

	@Override
	public T last() {
		checkIfEmpty();
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
		private Node<T> iterNextNode;
		private int iterModCount;
		private boolean canRemove;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			iterNextNode = head;
			iterModCount = modCount;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return iterNextNode != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = iterNextNode.getElement();
			iterNextNode = iterNextNode.getNextNode();
			canRemove = true;
			return retVal;
		}
		
		@Override
		public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			if (!canRemove) {
				throw new IllegalStateException();
			}
			
			canRemove = false;
			Node<T> prevPrevNode = null;
			if (head.getNextNode() == iterNextNode) {
				head = iterNextNode;
			} else {
				prevPrevNode = head;
				while (prevPrevNode.getNextNode().getNextNode() != iterNextNode) {
					prevPrevNode = prevPrevNode.getNextNode();
				}
				prevPrevNode.setNextNode(iterNextNode);
			}
			if (iterNextNode == null) {
				tail = prevPrevNode;
			}

			size--;
			modCount++;
			iterModCount++;
		}
	}
}