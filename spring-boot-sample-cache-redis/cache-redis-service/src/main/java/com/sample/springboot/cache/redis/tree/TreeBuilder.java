package com.sample.springboot.cache.redis.tree;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeBuilder<E, ID> {

    private Collection<E> elements;

    private Function<E, ID> id;

    private Function<E, ID> parentId;

    public TreeNode<E> build() {
        Map<ID, TreeNode<E>> idNodeMap = elements.stream().collect(Collectors.toMap(id, TreeNode::new));

        TreeNode<E> root = null;

        for (E element : elements) {
            ID id = this.id.apply(element);
            ID parentId = this.parentId.apply(element);

            TreeNode<E> node = idNodeMap.get(id);

            if (idNodeMap.containsKey(parentId)) {
                TreeNode<E> parentNode = idNodeMap.get(parentId);
                node.parent = parentNode;
                parentNode.children.add(node);
            } else {
                if (root != null) {
                    throw new IllegalArgumentException("root node more than one");
                }
                root = node;
            }
        }
        Objects.requireNonNull(root, "there is not root node");

        return root;
    }

    public static <E, ID> TreeBuilder<E, ID> builder() {
        return new TreeBuilder<>();
    }

    public TreeBuilder<E, ID> elements(Collection<E> elements) {
        this.elements = elements;
        return this;
    }

    public TreeBuilder<E, ID> id(Function<E, ID> id) {
        this.id = id;
        return this;
    }

    public TreeBuilder<E, ID> parentId(Function<E, ID> parentId) {
        this.parentId = parentId;
        return this;
    }

}
