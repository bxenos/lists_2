/**
 * Node for creating linked data structures
 * 
 * @author Brayden Xenos
 */
public class Node<T> {
    private Node <T> nextNode;
    private Node <T> previousNode;
    private T element;
    
    /**
     * Initialize Node with given element.
     * @param element
     */
    public Node(T element) {
        this.element = element;
        this.nextNode = null;
        this.previousNode = null;
    }
    
    // /**
    //  * Initialize Node with given element and next Node.
    //  * @param element
    //  * @param nextNode
    //  * @param previousNode
    //  */
    // public Node(T element, Node<T> nextNode, Node<T> previousNode) {
    //     this.element = element;
    //     this.nextNode = nextNode;
    //     this.previousNode = previousNode;
    // }
    
    /**
     * Getter for nextNode
     * @return the next node.
     */
    public Node<T> getNextNode() {
        return nextNode;
    }
    
    /**
     * Setter for nextNode
     * @param nextNode
     */
    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }
    
    /**
     * Getter for element
     * @return the nodes element
     */
    public T getElement() {
        return element;
    }
    
    /**
     * Setter for element
     * @param element
     */
    public void setElement(T element) {
        this.element = element;
    }

        /**
     * Getter for previousNode
     * @return the previous node.
     */
    public Node<T> getPreviousNode() {
        return previousNode;
    }
    
    /**
     * Setter for previousNode
     * @param previousNode
     */
    public void setPreviousNode(Node<T> previousNode) {
        this.previousNode = previousNode;
    }
    
    
}
