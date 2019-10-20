package ee.fj.merkletree;

import ee.fj.merkletree.node.LeafNode;
import ee.fj.merkletree.node.Node;
import ee.fj.merkletree.node.SignedNode;
import ee.fj.merkletree.node.TopNode;
import ee.fj.merkletree.node.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

class SimpleNode<T, S> implements SignedNode<S, T>, LeafNode<T>, TreeNode<T>, TopNode<T> {
    @Getter private final T hash;

    @Getter private S signature;

    private TreeNode<T> parent;

    @Getter @Setter private Node<T> left;
    @Getter @Setter private Node<T> right;

    SimpleNode(T hash) {
        this.hash = hash;
    }

    SimpleNode(Node<T> left, Node<T> right, T hash) {
        this.hash = hash;
        this.left = left;
        this.right = right;
        ((SimpleNode)this.left).parent = this;
        if (this.right != null)
            ((SimpleNode)this.right).parent = this;
    }


    @Override
    public void sign(Function<T, Optional<S>> signer) {
        signer.apply(hash).ifPresent( t -> signature = t);
    }

    @Override
    public boolean is(Class clazz) {
        Objects.requireNonNull(clazz, "The class should not be null!");
        if (this.signature != null && clazz.equals(SignedNode.class)) {
            return true;
        } else if (left == null && right == null) {
            return clazz.equals(LeafNode.class);
        } else if (parent != null) {
            return clazz.equals(TreeNode.class);
        }
        return clazz.equals(TopNode.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional as(Class clazz) {
        Objects.requireNonNull(clazz, "The class should not be null!");
        if (!is(clazz)) {
            return Optional.empty();
        }
        return Optional.of(this);
    }

    @Override
    public TreeNode<T> getParent() {
        if (parent == null) throw new UnsupportedOperationException("Top node does not have parent!");
        return parent;
    }
}
