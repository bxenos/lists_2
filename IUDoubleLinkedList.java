import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>{
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
	 * checks if the list is empty. This method is to avoid code duplication
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
        if (isEmpty()) {   //list is empty
            head = tail = newNode;
        } else {  //list has element values
            newNode.setNextNode(head);
            head.setPreviousNode(newNode);
            head = newNode;
        }

        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<T>(element);
        if (isEmpty()) {   //list is empty
            head = tail = newNode;
        } else {   //hist has element values
            Node<T> targetNode = tail;
            targetNode.setNextNode(newNode);
            newNode.setPreviousNode(targetNode);
            tail = newNode;
        }

        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        Node<T> targetNode = head;
        while (targetNode != null && !targetNode.getElement().equals(target)) {
            targetNode = targetNode.getNextNode();
        }
        if (targetNode == null) {
            throw new NoSuchElementException();
        }
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(targetNode.getNextNode()); //connect newNode first
        newNode.setPreviousNode(targetNode); 
        targetNode.setNextNode(newNode); //then update pre existing node
        if (newNode.getNextNode() == null) { //tail, or targetNode == tail
            tail = newNode;
        } else {
            newNode.getNextNode().setPreviousNode(newNode); //dangerous
        }

        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) { //check if index is valid
            throw new IndexOutOfBoundsException();
        }
    
        Node<T> newNode = new Node<T>(element);
        
        if (index == 0) {  // Insert at the front
            if (isEmpty()) {  // List is empty
                head = tail = newNode;
            } else {  // List is not empty
                newNode.setNextNode(head);
                head.setPreviousNode(newNode);
                head = newNode;
            }
        } else if (index == size) {  // Insert at the end
            newNode.setPreviousNode(tail);
            tail.setNextNode(newNode);
            tail = newNode;
        } else {   // ist isnt empty or index isnt = size
            Node<T> currentNode = head;
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.getNextNode();
            }
            Node<T> afterNode = currentNode.getNextNode();
            
            //setting pointers for newNode
            currentNode.setNextNode(newNode);
            newNode.setPreviousNode(currentNode);
            newNode.setNextNode(afterNode);
            afterNode.setPreviousNode(newNode);
        }
    
        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        checkIfEmpty();
		T retVal;
		
		if (size == 1) { //check if there is only one element in the list
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
        checkIfEmpty();  // checks if the list is empty and throws an exception if so
        Node<T> tempNode = tail;
    
        if (size == 1) { // Only one element in the list
            head = tail = null;
        } else { // More than one element in the list
            tail = tail.getPreviousNode();  
            tail.setNextNode(null); 
        }
    
        size--;
        modCount++;
        return tempNode.getElement();
    }

    @Override
    public T remove(T element) {
        Node<T> targetNode = head;
        while (targetNode != null && !targetNode.getElement().equals(element)) {
            targetNode = targetNode.getNextNode();
        }
        if (targetNode == null) {
            throw new NoSuchElementException();
        }
        if (targetNode != tail) {
            targetNode.getNextNode().setPreviousNode(targetNode.getPreviousNode());
        } else {
            tail = targetNode.getPreviousNode();
        }
        if (targetNode != head) {
            targetNode.getPreviousNode().setNextNode(targetNode.getNextNode());
        } else {
            head = targetNode.getNextNode();
        }

        size--;
        modCount++;
        return targetNode.getElement();
    }

    @Override
public T remove(int index) {
    if (index < 0 || index >= size) { //check if index is valid. if not, throws an error
        throw new IndexOutOfBoundsException();
    }
    Node<T> targetNode;

    if (index == 0) {  //if index is at the head
        targetNode = head;
        head = head.getNextNode();
        if (head == null) {  //if head == null, then there was only 1 element
            tail = null;
        } else {
            head.setPreviousNode(null);
        }
    } else if (index == size - 1) { //If index is at the tail
        targetNode = tail;
        tail = tail.getPreviousNode();
        if (tail == null) {  //if tail == null, there is only 1 element.
            head = null; 
        } else {
            tail.setNextNode(null);
        }
    } else {//the index is somewhere in the middle
        targetNode = head;
        for (int i = 0; i < index; i++) {
            targetNode = targetNode.getNextNode();
        }
        Node<T> previousNode = targetNode.getPreviousNode();
        Node<T> nextNode = targetNode.getNextNode();
        
        previousNode.setNextNode(nextNode);
        nextNode.setPreviousNode(previousNode);
    }

    size--;
    modCount++;
    return targetNode.getElement();
}

    @Override
    public void set(int index, T element) {
        if(index < 0 || index >= size) { //check if the index is valid
            throw new IndexOutOfBoundsException();
        }

        Node<T> targetNode = head;

        if (index == 0) {
            targetNode.setElement(element);
        }

        for (int i = 0; i < index; i++) {
            targetNode = targetNode.getNextNode(); // gets the node that is 1 prior
        }
        targetNode.setElement(element);

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
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Itterator for IUDoubleLinkedList
     * 
     * @author Brayden Xenos
     */
    private class DLLIterator implements ListIterator<T> {
        private Node<T> iterNextNode;
		private int iterModCount;
		private boolean canRemove;

        /**
         * DLLIterator constructor
         * 
         * @author Brayden Xenos
         */
        public DLLIterator() {
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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'remove'");
        }

        @Override
        public boolean hasPrevious() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'hasPrevious'");
        }

        @Override
        public T previous() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'previous'");
        }

        @Override
        public int nextIndex() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'nextIndex'");
        }

        @Override
        public int previousIndex() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'previousIndex'");
        }

        @Override
        public void set(T e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }

        @Override
        public void add(T e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'add'");
        }
    }
}
