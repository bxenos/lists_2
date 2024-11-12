import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>{
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
     * Constructor
     * initialize a new empty list
     */
    public IUDoubleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

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
        //alt method vv
        // ListIterator<T> lit = listIterator();
        // lit.add(element);
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
        // ListIterator<T> lit = listIterator(size);
        // lit.add(element);
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
    //     ListIterator<T> lit = listIterator();
    //     boolean found = false;
    //     T retVal = null;
    //     while (lit.hasNext() && !found) {
    //         retVal = lit.next();
    //         if (retVal.equals(element)) {
    //             found = true;
    //         }
    //     }
    //     if (!found) {
    //         throw new NoSuchElementException();
    //     }
    //     lit.remove();
    //     return retVal;
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
		// checkIfEmpty();
		// return head.getElement();
        ListIterator<T> lit = listIterator();
        return lit.next();
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
        /**
         * this one is funky becasue when it just calls the standard iterator, it will
         * ONLY use the ones in the standard iterator class(event though its ListIterator).
         * This is becasue ListIterator implements Iterator, which makes this possible.
         */
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }
    
    /**
     * Itterator for IUDoubleLinkedList
     * 
     * @author Brayden Xenos
     */
    private class DLLIterator implements ListIterator<T> {
        private Node<T> nextNode;
        private int nextIndex;
		private int iterModCount;
		private Node<T> lastReturnedNode;

        /**
         * DLLIterator constructor
         * initialize iterator before first element
         * 
         * @author Brayden Xenos
         */
        public DLLIterator() {
            this(0);
        }

        /**
         * DLLIterator constructor
         * initialize iterator before first element
         * 
         * @author Brayden Xenos
         */
        public DLLIterator(int startingIndex) {
            nextNode = head;
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }
            //should really pick which end to start from
            for (int i = 0; i < startingIndex; i++) { //only efficent enough in the front half
                nextNode = nextNode.getNextNode();
            }
            nextIndex = startingIndex;
			iterModCount = modCount;
			lastReturnedNode = null;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
            lastReturnedNode = nextNode;
			nextNode = nextNode.getNextNode();
            nextIndex++;
			return retVal;
        }

        @Override
		public void remove() { //this is not done and needsto be fixed
            if (iterModCount != modCount) { //iterator existential crisis
                throw new ConcurrentModificationException();
            }
            if (lastReturnedNode == null) { //is it possible to remove?
                throw new IllegalStateException();
            }

            //check if lastReturnedNode is the head
            if (lastReturnedNode != head) { //or previousNode == null
                lastReturnedNode.getPreviousNode().setNextNode(lastReturnedNode.getNextNode());
            } else {
                head = lastReturnedNode.getNextNode(); //need a new head
            }

            //check if lastReturnedNode is the tail
            if (lastReturnedNode != tail) { //or nextNode == null
                lastReturnedNode.getNextNode().setPreviousNode(lastReturnedNode.getPreviousNode());
            } else {
                tail = lastReturnedNode.getPreviousNode(); //need a new tail
            }

            // lastReturnedNode = null;
            
            if (lastReturnedNode == nextNode) { //last move was next//last move was previous
                nextNode = lastReturnedNode.getNextNode(); //that node isnt in the list anymore
            } else { //last move was next
                nextIndex--;  //there are fewer nodes/elements to my left than there used to be
            }
            
            lastReturnedNode = null;
            size--;
            iterModCount++;
            modCount++;
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextNode != head;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            if (nextNode == null) { //if at the end
                nextNode = tail;
            } else { //if in the middle
                nextNode = nextNode.getPreviousNode();
            }

            lastReturnedNode = nextNode;
            nextIndex--;

            return nextNode.getElement();

        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex - 1;
        }

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastReturnedNode == null) { //is it possible to remove?
                throw new IllegalStateException();
            }

            //set last returned node to the element inputed
            lastReturnedNode.setElement(e);

            iterModCount++;
            modCount++;
        }

        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (head == null) { //list is empty
                head = tail = new Node<T>(e);

            } else if (nextNode == head) { //add to the front
                addToFront(e);

            } else if (nextNode == null) { //add to the end
                addToRear(e);

            } else { //add to the middle
                Node<T> newNode = new Node<T>(e);
                Node<T> prevNode = nextNode.getPreviousNode();

                //changing pointers
                prevNode.setNextNode(newNode);
                newNode.setNextNode(nextNode);
                nextNode.setPreviousNode(newNode);
                newNode.setPreviousNode(prevNode);  
            }

            size++;
            nextIndex++;
            iterModCount++;
            modCount++;

        }
    }
}
