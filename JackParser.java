import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class JackParser {
    private SyntaxTreeNode head;
    
    public JackParser(String filename) throws IOException {
        File f = new File(filename);
        String folder = f.getParent();
        String base = Util.basename(filename);
        String xmlfile = folder + File.separator + base + ".xml";
        
        String contents = Util.readWholeFile(xmlfile);
        String[] lines = contents.split("\n");
        
        Vector<SyntaxTreeNode> stack = new Vector<SyntaxTreeNode>();
        head = null;

        for (String line : lines) {
            line = line.trim();
            int index = line.indexOf("</");

            if (index == 0) {
                // closing statement
                stack.remove(stack.size() - 1);
            } else if (index > 0) {
                // token
                String tokenName = getTagName(line);
                TokenType tt = TokenType.valueOf(translate(tokenName));
                Token t = new Token(tt, getText(line));
                SyntaxTreeNode node = new SyntaxTreeNode(t);
                stack.get(stack.size() - 1).addChild(node);

            } else {
                // opening statement
                String tagName = getTagName(line);
                NodeType nt = NodeType.valueOf(translate(tagName));
                SyntaxTreeNode node = new SyntaxTreeNode(nt);
                if (head == null)
                    head = node;
                else
                    stack.get(stack.size() - 1).addChild(node);

                stack.add(node);
            }
        }

    }


	public SyntaxTreeNode parseClass() {
		return head;
	}

    private static String translate(String name) {
        if (name.equals("integerConstant"))
            return "INT";
        else if (name.equals("stringConstant"))
            return "STRING";

        String s = name.replace("Statement", "Stmt");
        StringBuffer sb = new StringBuffer();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (sb.length() > 0)
                    sb.append('_');
            }
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }

    private static String getTagName(String line) {
        int index0 = line.indexOf('<');
        int index1 = line.indexOf('>', index0);
        return line.substring(index0 + 1, index1);
    }

    private static String getText(String line) {
        int index0 = line.indexOf('>');
        int index1 = line.indexOf('<', index0);
        String text = line.substring(index0 + 1, index1).trim();
        return text.replaceAll("&", "&").replaceAll("<", "<")
                .replaceAll(">", ">");
        
    }
}
