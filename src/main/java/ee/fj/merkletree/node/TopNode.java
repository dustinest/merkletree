package ee.fj.merkletree.node;

/**
 * The top node
 * @param <T> the hash type
 */
public interface TopNode<T> extends Node<T> {
    /**
     * @return left node
     */
    Node<T> getLeft();

    /**
     * @return right node
     */
    Node<T> getRight();
}
