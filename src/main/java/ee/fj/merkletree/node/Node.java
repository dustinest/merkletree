package ee.fj.merkletree.node;

import java.util.Optional;

/**
 * Base node
 * @param <T> the type of the hash
 */
public interface Node<T> {
    /**
     * Checks if this node is instance of class provided
     * @param clazz interface to check against
     * @return true if is the interface otherwise false
     */
    boolean is(Class<? extends Node> clazz);

    /**
     * Returns this node as class provided
     * @param clazz class to check against
     * @param <S> Interface class
     * @return empty if is not instance of the class otherwise returns node as the type provided
     */
    <S extends Node> Optional<S> as(Class<S> clazz);

    /**
     * @return hash value
     */
    T getHash();
}
