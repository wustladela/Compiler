import java.util.Vector;

/*
 * This class represents the parse tree for 
 *      the code. Each node can either represent a 
 *      terminal type (i.e. a token) or it can represent
 *      a non-terminal with children.
 */
public class SyntaxTreeNode {

    private NodeType type;
    private Token token;
    private Vector<SyntaxTreeNode> children;

    /*
     * Constructor with a node_type
     */
    public SyntaxTreeNode(NodeType type) {
        this.type = type;
        this.token = null;
        children = new Vector<SyntaxTreeNode>();
    }

    /* 
     * Constructor with a token
     */
    public SyntaxTreeNode(Token token) {
        type = NodeType.TOKEN;
        this.token = token;
        children = null;
    }

    /*
     * Adds a child node to this node
     */
    public void addChild(SyntaxTreeNode n) {
        children.add(n);
    }

    /*
     * Gets this node's type
     */
    public NodeType getType() {
        return type;
    }

    /*
     * Gets this node's token
     */
    public Token getToken() {
        return token;
    }

    /*
     * Returns a list of this node's children
     */
    public Vector<SyntaxTreeNode> getChildren() {
        return children;
    }

    /*
     * Returns the number of children
     */
    public int numChildren() {
        return children.size();
    }

    /*
     * Returns true if this is a non-terminal type
     */
    public boolean hasChildren() {
        return token == null;
    }

    /*
     * Debug function - Gets the string representations
     * of each of the type of each child.
     */
    public Vector<String> getTypes() {
        Vector<String> t = new Vector<String>();
        for (SyntaxTreeNode node : children) {
            NodeType type = node.type;
            if (type == NodeType.TOKEN) {
                t.add(node.token.type.toString());
            } else {
                t.add(type.toString());
            }
        }
        return t;
    }

    public String toString() {
        return toXml(0);
    }

    private String toXml(int indent) {
        String indentS = "";
        for (int i = 0; i < indent; i++)
            indentS += " ";

        String ts;
        if (type == NodeType.TOKEN)
            ts = token.type.toString();
        else
            ts = type.toString();

        StringBuffer sb = new StringBuffer(indentS);
        sb.append("<");
        sb.append(ts);
        sb.append(">");
        if (hasChildren()) {
            sb.append("\n");
            for (SyntaxTreeNode child : children) {
                if (child != null)
                    sb.append(child.toXml(indent + 1));
            }
            sb.append(indentS);
        } else {
            sb.append(" ");
            sb.append(Util.replaceEntities(token.token));
            sb.append(" ");
        }

        sb.append("</");
        sb.append(ts);
        sb.append(">\n");

        return sb.toString();

    }

}
