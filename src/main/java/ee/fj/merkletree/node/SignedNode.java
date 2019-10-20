package ee.fj.merkletree.node;

import java.util.Optional;
import java.util.function.Function;

public interface SignedNode<S, T> {
    /**
     * @return signature for the node
     */
    public S getSignature();

    /**
     * Signs the node with function provided
     * @param signer signer to sign with
     */
    public void sign(Function<T, Optional<S>> signer);
}
