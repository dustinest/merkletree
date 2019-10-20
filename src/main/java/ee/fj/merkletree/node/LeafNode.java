package ee.fj.merkletree.node;

/**
 * Leaf node
 * @param <T> the hash type
 */
public interface LeafNode<T> extends Node<T> {
    /**
     * @return parent node
     */
    public TreeNode<T> getParent();
}
