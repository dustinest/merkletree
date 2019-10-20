package ee.fj.merkletree.node;

/**
 * Tree node type connecting top node and leaf node
 * @param <T> the hash type
 */
public interface TreeNode<T>  extends Node<T> {
    /**
     * @return parent node of the tree node
     */
    public TreeNode<T> getParent();

    /**
     * @return left node
     */
    Node<T> getLeft();

    /**
     * @return right node
     */
    Node<T> getRight();
}
