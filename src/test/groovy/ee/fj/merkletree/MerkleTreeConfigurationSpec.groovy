package ee.fj.merkletree

import spock.lang.Specification

class MerkleTreeConfigurationSpec extends Specification {
    def "Leaf node hasher for 2 argument constructor should not be null"() {
        when:
            new MerkleTreeConfiguration<>(null, null)
        then:
            def exception = thrown NullPointerException
            exception.message == "Leaf node hasher should not be null!"
    }

    def "Tree and top node hasher for 2 argument constructor should not be null"() {
        when:
            new MerkleTreeConfiguration<>({data -> null}, null)
        then:
            def exception = thrown NullPointerException
            exception.message == "Tree node hasher should not be null!"
    }

    def "2 argument constructor works"() {
        when:
            MerkleTreeConfiguration merkleTreeConfiguration = new MerkleTreeConfiguration<>({data -> null}, {data -> null})
            MerkleTreeBuilder merkleTreeBuilder = merkleTreeConfiguration.getBuilder()
        then:
            merkleTreeBuilder != null
    }


    def "Leaf node hasher for 3 argument constructor should not be null"() {
        when:
            new MerkleTreeConfiguration<>(null, null,null)
        then:
            def exception = thrown NullPointerException
            exception.message == "Leaf node hasher should not be null!"
    }

    def "Node hasher for 3 argument constructor should not be null"() {
        when:
            new MerkleTreeConfiguration<>({data -> null}, null,null)
        then:
            def exception = thrown NullPointerException
            exception.message == "Tree node hasher should not be null!"
    }

    def "Node signer for 3 arguments constructor can be null!"() {
        when:
            MerkleTreeConfiguration merkleTreeConfiguration = new MerkleTreeConfiguration<>({data -> null}, {leftHash, rightHash -> null },null)
            MerkleTreeBuilder merkleTreeBuilder = merkleTreeConfiguration.getBuilder()
        then:
            merkleTreeBuilder != null
    }

    def "Leaf node hasher for the main constructor should not be null"() {
        when:
            new MerkleTreeConfiguration<>(null, null,null, null, null, null)
        then:
            def exception = thrown NullPointerException
            exception.message == "Leaf node hasher should not be null!"
    }

    def "Tree Node hasher for for the main constructor should not be null"() {
        when:
            new MerkleTreeConfiguration<>({data -> null}, null,null, null, null, null)
        then:
            def exception = thrown NullPointerException
            exception.message == "Tree node hasher should not be null!"
    }

    def "Top Node hasher for for the main constructor should not be null"() {
        when:
            new MerkleTreeConfiguration<>({data -> null}, {leftHash, rightHash -> null },null, null, null, null)
        then:
            def exception = thrown NullPointerException
            exception.message == "Top node hasher should not be null!"
    }

    def "Signers for for the main constructor can be null"() {
        when:
            def merkleTreeConfiguration = new MerkleTreeConfiguration<>({data -> null}, {leftHash, rightHash -> null },{leftHash, rightHash -> null }, null, null, null)
            MerkleTreeBuilder merkleTreeBuilder = merkleTreeConfiguration.getBuilder()
        then:
            merkleTreeBuilder != null
    }
}
