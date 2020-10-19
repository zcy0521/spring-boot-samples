package com.sample.springboot.cache.redis.tree;

import lombok.Getter;
import lombok.Setter;
import org.assertj.core.util.Sets;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
public class TreeNode<T> implements Serializable {

    TreeNode<T> parent;

    Vector<TreeNode<T>> children;

    T data;

    public TreeNode(T data) {
        this.data = data;
        children = new Vector<>();
    }

    /**
     * 祖先节点
     */
    public TreeNode[] getAncestors() {
        return getPathToRoot(this, 0);
    }

    /**
     * 子孙节点
     */
    public Set<TreeNode<T>> getDescendants() {
        if (children == null || children.size() == 0) {
            return null;
        }

        Queue<TreeNode<T>> nodeQueue = new LinkedList<>(this.getChildren());

        Set<TreeNode<T>> descendants = Sets.newHashSet();
        while (!nodeQueue.isEmpty()) {
            int count = nodeQueue.size();
            for (int i = 0; i < count; i++) {
                TreeNode<T> node = nodeQueue.peek();
                Objects.requireNonNull(node);
                nodeQueue.remove();

                // 将节点添加至返回集合
                descendants.add(node);

                // 如果节点有child 则将节点的child添加至队列
                if (!node.getChildren().isEmpty()) {
                    nodeQueue.addAll(node.getChildren());
                }
            }
        }
        return descendants;
    }

    private TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
        TreeNode[] retNodes;
        if(aNode == null) {
            if(depth == 0) {
                return null;
            } else {
                retNodes = new TreeNode[depth];
            }
        } else {
            depth++;
            retNodes = getPathToRoot(aNode.getParent(), depth);
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

}
