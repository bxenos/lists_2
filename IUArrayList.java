import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class IUArrayList<T> implements IndexedUnsortedList<T> {
    public static final int DEFAULT_CAPACITY = 10;
    private static final int NOT_FOUND = -1;

    private T array[];
    private int rear;
    private int modCount;

    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity) {
        array = (T[])(new Object[initialCapacity]);
        rear = 0;
    }

    /**
     * Private method to check if the array is long enough,
     * and if it isn't, it doubles the array length.
     * 
     * @author Brayden Xenos
     */
    private void expandIfNecessary() {
        if (rear == array.length) {
            array = Arrays.copyOf(array, array.length*2);
        }
    }

    /**
     * Checks if the array is empty, and if it is, it
     * throws an NoSuchElementExcpetion
     * 
     * @author Brayden Xenos
     * @throws NoSuchElementException
     */
    private void checkIfEmpty() {
        if (isEmpty()){
            throw new NoSuchElementException();
        }
    }

    /**
     * Checks if the index is out of bounds
     * 
     * @author Brayden Xenos
     * @throws IndexOutOfBoundsException
     */
    private void checkBounds(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void addToFront(T element) {
        expandIfNecessary();
        //Shift all elements back by 1
        for (int index = rear; index > 0; index--) {
            array[index] = array[index-1];
        }
        //Insert new element
        array[0] = element;
        //increment rear
        rear++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        expandIfNecessary();
        array[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        //converting to int
        int targetIndex = indexOf(target);
        if (targetIndex < 0 || targetIndex >= rear) {
            throw new NoSuchElementException();
        }
        expandIfNecessary();

        //shift elements
        for(int i = rear; i > targetIndex + 1; i--) {
            array[i] = array[i-1];
        }
        array[targetIndex + 1] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        //checking bounds
        if (index < 0 || index > rear) {
            throw new IndexOutOfBoundsException();
        }
        expandIfNecessary();
        for(int i = rear; i > index; i--) {
            array[i] = array[i-1];
        }
        array[index] = element;
        rear++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        checkIfEmpty();
        T returnValue = array[0];
        for(int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        rear--;
        modCount++;
        return returnValue;

    }

    @Override
    public T removeLast() {
        checkIfEmpty();
        T returnValue = array[rear-1];
        array[rear-1] = null;
        rear--;
        modCount++;
        return returnValue;

    }

    @Override
    public T remove(T element) {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[index];
		
		rear--;
		//shift elements
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;
		modCount++;
		
		return retVal;
    }

    @Override
    public T remove(int index) {
        checkBounds(index);
        T returnValue = array[index];
        for (int i = index; i < rear; i++) {
            array[i] = array [i+1];
        }
        rear--;
        array[rear] = null;
        modCount++;
        return returnValue;
    }

    @Override
    public void set(int index, T element) {
        //check if element is valid
        checkBounds(index);
        array[index] = element;
        modCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(T element) {
        int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		
		return index;
    }

    @Override
    public T first() {
        checkIfEmpty();
        return array[0];
    }

    @Override
    public T last() {
        checkIfEmpty();
        return array[rear-1];
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
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
        return new IUArrayListIterator();
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

    /**
     * Basic Itterator for UIArrayListincluding remove()
     * 
     * @author Brayden Xenos
     */
    private class IUArrayListIterator implements Iterator<T> {
        private int nextIndex;
        private boolean canRemove; //Has next been called if it has. It is true. If it hasn't it is false.
        private int iterModCount;

        /**
         * Initialize iterator
         */
        public IUArrayListIterator() {
            nextIndex = 0;
            canRemove = false;
            iterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex < rear;
        }

        @Override
        public T next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            nextIndex++; //move first
            canRemove = true;
            return array[nextIndex-1]; //then returns nextIndex-1
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) { //is the iterator still valid?
                throw new ConcurrentModificationException();
            }
            if (!canRemove) { //am I allowed to remove?
                throw new IllegalStateException();
            }
            canRemove = false; //can't remove twice in a row without next()
            for (int i = nextIndex-1; i < rear - 1; i++) { //shift
                array[i] = array[i+1];
            }
            array[rear-1] = null; //clear duplicate last referance
            nextIndex--;  //moves the iterator so its in front of the actual next index
            rear--; //removed an element so rear has to move back
            modCount++; //the list is different
            iterModCount++; //but I know I did it
            
        }

    }
}

