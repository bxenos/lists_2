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
        newNode.setNextNode(head);
        head = newNode;
        if (newNode.getNextNode() == null) { //if its the first element in the list, its making the new node tail
            tail = newNode;
        } else {
            newNode.getNextNode().setPreviousNode(newNode);
        }

        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<T>(element);
        if (isEmpty()) {   //list is empty
            newNode = tail = head;
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

        if (index < 0 || index > size) { //checking if index is in bounds
            throw new IndexOutOfBoundsException();
        }
        
        if (index == 0) {  //checking if the index is at the beginning
            addToFront(element);
        } else if (index == size) {  //checking if the index is at the tail.
            addToRear(element);
        } else {  //else, the list isnt empty or the index isn't at the tail and goes thorugh the regular process.
        Node<T> currentNode = head;
        for (int i = 0; i < index - 1; i++) {
			currentNode = currentNode.getNextNode();
		}
		Node<T> newNode = new Node<T>(element);
        Node<T> afterNode = currentNode.getNextNode();
		newNode.setNextNode(afterNode);
		currentNode.setNextNode(newNode);
        newNode.setPreviousNode(currentNode);
        afterNode.setPreviousNode(newNode);
        }

        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFirst'");
    }

    @Override
    public T removeLast() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLast'");
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
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> targetNode = head;
        for (int i = 0; i < index - 1; i++) {
            targetNode = targetNode.getNextNode();
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
    public Iterator iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }
    
}
