package ee.fj.merkletree;

import ee.fj.merkletree.node.LeafNode;
import java.util.List;

public interface MerkleTreeBuilder<T, S, D> {
    MerkleTreeBuilder<T, S, D> addDataBlock(D dataBlock);
    List<LeafNode<T>> buildTree();
}
