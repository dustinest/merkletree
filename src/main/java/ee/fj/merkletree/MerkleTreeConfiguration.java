package ee.fj.merkletree;

import ee.fj.merkletree.node.LeafNode;
import ee.fj.merkletree.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @param <T> The hash type
 * @param <S> The signature type
 * @param <D> The data type
 */
public class MerkleTreeConfiguration<T, S, D> {
    private final Function<D, T> leafNodeHasher;
    private final BiFunction<T, T, T> treeNodeHasher;
    private final BiFunction<T, T, T> topNodeHasher;

    private final Function<T, Optional<S>> leafNodeSigner;
    private final Function<T, Optional<S>> treeNodeSigner;
    private final Function<T, Optional<S>> topNodeSigner;

    public MerkleTreeConfiguration(Function<D, T> leafNodeHasher, BiFunction<T, T, T> treeNodeHasher, BiFunction<T, T, T> topNodeHasher,
                                   Function<T, Optional<S>> leafNodeSigner, Function<T, Optional<S>> treeNodeSigner, Function<T, Optional<S>> topNodeSigner) {
        Objects.requireNonNull(leafNodeHasher, "Leaf node hasher should not be null!");
        Objects.requireNonNull(treeNodeHasher, "Tree node hasher should not be null!");
        Objects.requireNonNull(topNodeHasher, "Top node hasher should not be null!");
        this.leafNodeHasher = leafNodeHasher;
        this.treeNodeHasher = treeNodeHasher;
        this.topNodeHasher = topNodeHasher;
        this.leafNodeSigner = leafNodeSigner != null ? leafNodeSigner : o -> Optional.empty();;
        this.treeNodeSigner = treeNodeSigner != null ? treeNodeSigner : o -> Optional.empty();;
        this.topNodeSigner = topNodeSigner != null ? topNodeSigner : o -> Optional.empty();
    }

    public MerkleTreeConfiguration(Function<D, T> leafNodeHasher, BiFunction<T, T, T> nodeHasher,  Function<T, Optional<S>> nodeSigner) {
        this(leafNodeHasher, nodeHasher, nodeHasher, nodeSigner, nodeSigner, nodeSigner);
    }

    public MerkleTreeConfiguration(Function<D, T> leafNodeHasher, BiFunction<T, T, T> nodeHasher) {
        this(leafNodeHasher, nodeHasher, nodeHasher, null, null, null);
    }

    public MerkleTreeBuilder<T, S, D> getBuilder() {
        final List<SimpleNode<T, S>> leafNodes = new ArrayList<>();
        final Object mutex = new Object();

        return new MerkleTreeBuilder<>() {

            @Override
            public MerkleTreeBuilder<T, S, D> addDataBlock(D dataBlock) {
                SimpleNode<T, S> leaf = new SimpleNode<>(leafNodeHasher.apply(dataBlock));
                leaf.sign(leafNodeSigner);
                synchronized (mutex) {
                    leafNodes.add(leaf);
                }
                return this;
            }

            @Override
            public List<LeafNode<T>> buildTree() {
                synchronized (mutex) {
                    generateMerkleTree(leafNodes, MerkleTreeConfiguration.this);
                    List<LeafNode<T>> result = new ArrayList<>(leafNodes);
                    leafNodes.clear();
                    return result;
                }
            }
        };
    }

    private static <T, S, D> void generateMerkleTree(List<SimpleNode<T, S>> leafNodes, MerkleTreeConfiguration<T, S, D> merkleTreeConfiguration) {
        final List<SimpleNode<T, S>> buffer = new ArrayList<>(leafNodes);
        final List<SimpleNode<T, S>> parents = new ArrayList<>();

        while(buffer.size() > 0) {
            Node<T> leftNode = buffer.remove(0);
            Node<T> rightNode =  buffer.size() > 0 ? buffer.remove(0) : null;

            T leftNodeHash = leftNode.getHash();
            T rightNodeHash = rightNode != null ? rightNode.getHash() : null;
            if (buffer.size() <= 2 && parents.size() == 0) {
                SimpleNode<T, S> topNode = new SimpleNode<>(leftNode, rightNode, merkleTreeConfiguration.topNodeHasher.apply(leftNodeHash, rightNodeHash));
                topNode.sign(merkleTreeConfiguration.topNodeSigner);
                parents.add(topNode);
            } else {
                SimpleNode<T, S> treeNode = new SimpleNode<>(leftNode, rightNode, merkleTreeConfiguration.treeNodeHasher.apply(leftNodeHash, rightNodeHash));
                treeNode.sign(merkleTreeConfiguration.treeNodeSigner);
                parents.add(treeNode);
            }
            if (buffer.size() == 0 && parents.size() > 1) {
                buffer.addAll(parents);
                parents.clear();
            }
        }
    }
}
