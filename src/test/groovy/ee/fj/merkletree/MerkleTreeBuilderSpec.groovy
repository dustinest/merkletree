package ee.fj.merkletree

import ee.fj.merkletree.node.Node
import ee.fj.merkletree.node.LeafNode
import ee.fj.merkletree.node.TopNode
import ee.fj.merkletree.node.TreeNode
import spock.lang.Specification

import java.util.function.BiFunction
import java.util.function.Function

class MerkleTreeBuilderSpec extends Specification {
    def "Simple 2 argument merkle tree builder works"() {
        given:
            def leafNodeHasher = Mock(Function)
            def nodeHasher = Mock(BiFunction)
            MerkleTreeConfiguration merkleTreeConfiguration = new MerkleTreeConfiguration<>(leafNodeHasher, nodeHasher)
            MerkleTreeBuilder merkleTreeBuilder = merkleTreeConfiguration.getBuilder()
        when:
            merkleTreeBuilder.addDataBlock("0abc").addDataBlock("0cde").addDataBlock("0efg")
        then:
            1 * leafNodeHasher.apply("0abc") >> "1abc"
            1 * leafNodeHasher.apply("0cde") >> "1cde"
            1 * leafNodeHasher.apply("0efg") >> "1efg"
            0 * nodeHasher._
        when:
            def result = merkleTreeBuilder.buildTree()
            def result2 = merkleTreeBuilder.buildTree()
        then:
            0 * leafNodeHasher._
            1 * nodeHasher.apply("1abc", "1cde") >> "2abc"
            1 * nodeHasher.apply("1efg", null) >> "2cde"
            1 * nodeHasher.apply("2abc", "2cde") >> "3abc"
            0 * nodeHasher._
            result2.size() == 0
        and:
            result.size() == 3
            result[0].is(LeafNode)
            result[1].is(LeafNode)
            result[2].is(LeafNode)

            result[0].hash == "1abc"
            result[1].hash == "1cde"
            result[2].hash == "1efg"
        when:
            Node parent0 = result[0].as(LeafNode).map({ t -> t.getParent() }).get()
            Node parent1 = result[1].as(LeafNode).map({ t -> t.getParent() }).get()
            Node parent2 = result[2].as(LeafNode).map({ t -> t.getParent() }).get()
        then:
            0 * leafNodeHasher._
            0 * nodeHasher._
            parent0 == parent1
            parent0 != parent2

            parent0.hash == "2abc"
            parent1.hash == "2abc"
            parent2.hash == "2cde"

            parent0.is(TreeNode)
            parent1.is(TreeNode)
            parent2.is(TreeNode)
        when:
            Node topNode0 = parent0.as(TreeNode).map({ t -> t.getParent() }).get()
            Node topNode1 = parent1.as(TreeNode).map({ t -> t.getParent() }).get()
            Node topNode2 = parent2.as(TreeNode).map({ t -> t.getParent() }).get()
        then:
            0 * leafNodeHasher._
            0 * nodeHasher._
            topNode0 == topNode1
            topNode0 == topNode2

            topNode0.hash == "3abc"
            topNode1.hash == "3abc"
            topNode2.hash == "3abc"

            topNode0.is(TopNode)
            topNode1.is(TopNode)
            topNode2.is(TopNode)
    }
}
