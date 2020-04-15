package ru.griga.tickets;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class Utils {

    public static String getNodeValue(TreeNode tree, String key, String defaultValue) {

        TreeNode targetNode = tree.get(key);
        if ((targetNode instanceof ValueNode) && !((ValueNode) targetNode).isNull()) {
            return ((ValueNode) targetNode).asText();
        }
        return defaultValue;

    }

}
