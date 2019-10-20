package ee.fj.merkletree;

import ee.fj.merkletree.node.LeafNode;
import ee.fj.merkletree.node.Node;
import ee.fj.merkletree.node.TopNode;
import ee.fj.merkletree.node.TreeNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MerkleTreeBuilderTest {
    private static final MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simpleHashedMerkleTree() throws URISyntaxException, IOException {
        MerkleTreeBuilder<byte[], byte[], String> merkleTreeBuilder = new MerkleTreeConfiguration<byte[], byte[], String>(MerkleTreeBuilderTest::leafNodeHasher, MerkleTreeBuilderTest::fromByteArray).getBuilder();
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(MerkleTreeBuilderTest.class.getResource("/example_data.txt").toURI()), StandardCharsets.UTF_8)) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.trim().length() > 0) merkleTreeBuilder.addDataBlock(line.trim());
            }
        }
        List<LeafNode<byte[]>> result = merkleTreeBuilder.buildTree();
        Assert.assertEquals(92, result.size());
        Assert.assertTrue(result.get(0).is(LeafNode.class));
        LeafNode<byte[]> leaf1 = result.get(0).as(LeafNode.class).get();
        Assert.assertTrue(leaf1.getParent().is(TreeNode.class));
        TreeNode<byte[]> topNode = leaf1.getParent().getParent().getParent().getParent().getParent().getParent().getParent();
        Assert.assertTrue(topNode.is(TopNode.class));

        Assert.assertEquals("20275c8315ff851cc79a1774f68790fcc76c22bf2980b9ae7730363b34c96557", bytesToHex(topNode.getHash()));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer result = new StringBuffer();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                result.append('0');
            else
                result.append(hex);
        }
        return result.toString();
    }

    private static byte[] leafNodeHasher(String data) {
        return fromByteArray(data.getBytes(StandardCharsets.UTF_8), null);
    }

    private static byte[] fromByteArray(byte[] left, byte[] right) {
        digest.update(left);
        if (right != null) digest.update(right);
        return digest.digest();
    }
}
