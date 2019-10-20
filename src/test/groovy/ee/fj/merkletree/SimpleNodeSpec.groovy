package ee.fj.merkletree

import ee.fj.merkletree.node.LeafNode
import ee.fj.merkletree.node.Node
import ee.fj.merkletree.node.SignedNode
import ee.fj.merkletree.node.TopNode
import ee.fj.merkletree.node.TreeNode
import spock.lang.Specification

class SimpleNodeSpec extends Specification {
    def "Leaf type works"() {
        when:
            def leaf = new SimpleNode("Lorem ipsum est")
        then:
            !leaf.is(SimpleNode.class)

            leaf.is(LeafNode.class)
            !leaf.is(Node.class)
            !leaf.is(SignedNode.class)
            !leaf.is(TopNode.class)
            !leaf.is(TreeNode.class)
        when:
            leaf.sign({ t -> Optional.of(t) })
        then:
            !leaf.is(SimpleNode.class)

            leaf.is(LeafNode.class)
            !leaf.is(Node.class)
            leaf.is(SignedNode.class)
            !leaf.is(TopNode.class)
            !leaf.is(TreeNode.class)
    }

    def "Tree node works"() {
        when:
            def leftLeaf = new SimpleNode("Lorem ipsum est")
            def rightLeaf = new SimpleNode("Lorem ipsum est")

            def leftLeaf1 = new SimpleNode("Lorem ipsum est")

            def treeNode = new SimpleNode(leftLeaf, rightLeaf, "Lorem ipsum est")

            def leftTreeNode = new SimpleNode(leftLeaf1, null, "Lorem ipsum est")

            // mark as tree node
            new SimpleNode<>(treeNode, leftTreeNode, "Lorem ipsum Est")
        then:
            !leftLeaf.is(SimpleNode.class)
            leftLeaf.is(LeafNode.class)
            !leftLeaf.is(Node.class)
            !leftLeaf.is(SignedNode.class)
            !leftLeaf.is(TopNode.class)
            !leftLeaf.is(TreeNode.class)

            !leftLeaf1.is(SimpleNode.class)
            leftLeaf1.is(LeafNode.class)
            !leftLeaf1.is(Node.class)
            !leftLeaf1.is(SignedNode.class)
            !leftLeaf1.is(TopNode.class)
            !leftLeaf1.is(TreeNode.class)

            !rightLeaf.is(SimpleNode.class)
            rightLeaf.is(LeafNode.class)
            !rightLeaf.is(Node.class)
            !rightLeaf.is(SignedNode.class)
            !rightLeaf.is(TopNode.class)
            !rightLeaf.is(TreeNode.class)

            !treeNode.is(SimpleNode.class)
            !treeNode.is(LeafNode.class)
            !treeNode.is(Node.class)
            !treeNode.is(SignedNode.class)
            !treeNode.is(TopNode.class)
            treeNode.is(TreeNode.class)

            !leftTreeNode.is(SimpleNode.class)
            !leftTreeNode.is(LeafNode.class)
            !leftTreeNode.is(Node.class)
            !leftTreeNode.is(SignedNode.class)
            !leftTreeNode.is(TopNode.class)
            leftTreeNode.is(TreeNode.class)
        when:
            treeNode.sign({ t -> Optional.of(t) })
        then:
            !treeNode.is(SimpleNode.class)
            !treeNode.is(LeafNode.class)
            !treeNode.is(Node.class)
            treeNode.is(SignedNode.class)
            !treeNode.is(TopNode.class)
            treeNode.is(TreeNode.class)

        when:
            leftTreeNode.sign({ t -> Optional.of(t) })
        then:
            !leftTreeNode.is(SimpleNode.class)
            !leftTreeNode.is(LeafNode.class)
            !leftTreeNode.is(Node.class)
            leftTreeNode.is(SignedNode.class)
            !leftTreeNode.is(TopNode.class)
            leftTreeNode.is(TreeNode.class)
    }

    def "Top Tree node works"() {
        when:
            def topTreeNode = new SimpleNode(new SimpleNode("Lorem ipsum est"), new SimpleNode("Lorem ipsum est"), "Lorem ipsum est")
        then:
            !topTreeNode.is(SimpleNode.class)
            !topTreeNode.is(LeafNode.class)
            !topTreeNode.is(Node.class)
            !topTreeNode.is(SignedNode.class)
            topTreeNode.is(TopNode.class)
            !topTreeNode.is(TreeNode.class)
        when:
            topTreeNode.sign({ t -> Optional.of(t) })
        then:
            !topTreeNode.is(SimpleNode.class)
            !topTreeNode.is(LeafNode.class)
            !topTreeNode.is(Node.class)
            topTreeNode.is(SignedNode.class)
            topTreeNode.is(TopNode.class)
            !topTreeNode.is(TreeNode.class)

    }
}
