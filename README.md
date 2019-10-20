# Simple Merkle tree implementation

The purpose of this implementation is to hash and sign each type of tree node using algorithms algorithms.

Available types are:
- LeafNode - the node which usually contains the data
- TreeNode - the tree node between leaf node and top node
- TopNode - top node - usually needs to be signed

To check the node type:

`node.is(LeafNode.class);` or `node.as(LeafNode.class).isPresent();`

To get parent of the leaf node for instance:
```java
LeafNode parentNode = node.as(LeafNode.class).map(LeafNode::getParent).orElse(null);
```

https://en.wikipedia.org/wiki/Merkle_tree

## Initiate the merkle tree configuratino

See [MerkleTreeConfiguration constructors](src/main/java/ee/fj/merkletree/MerkleTreeConfiguration.java) 
First let's create a function to transform byte to String signature

For instance, when you have local methods:

To hash the leaf data
```java
    private static byte[] leafNodeHasher(String data);
```

To combine two byte arrays (hash the tree and top node

```java
    private static byte[] fromByteArray(byte[] left, byte[] right);
```

Then you can initiate the configuration:
```java
MerkleTreeConfiguration<byte[], byte[], String> merkleTreeConfiguration = new MerkleTreeConfiguration<byte[], byte[], String>(MerkleTreeBuilderTest::leafNodeHasher, MerkleTreeBuilderTest::fromByteArray);
```

Create the builder:
```java   
MerkleTreeBuilder<byte[], byte[], String> merkleTreeBuilder = merkleTreeConfiguration.getBuilder();
```

Next add data:

```java
merkleTreeBuilder.addDataBlock("lorem");
merkleTreeBuilder.addDataBlock("ipsum");
merkleTreeBuilder.addDataBlock("est");
//etc..
```

When you are ready to build the tree just call the method below. Note that it will clean up data added

```java
List<LeafNode<byte[]>> result = merkleTreeBuilder.buildTree();
```

Check out the tests for use cases and acessing the nodes.
