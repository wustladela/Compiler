import java.io.File;
import java.util.HashMap;

public class JackCompiler {
    private JackSymbolTable st;
    private VMWriter vm;
    private String className;
    
    private HashMap<String, Integer> labels;
    
    public JackCompiler(String infile, String outfile, boolean debug)
    throws Exception {
        JackParser p = new JackParser(infile);
        SyntaxTreeNode root = p.parseClass();
        vm = new VMWriter(outfile, debug);
        st = new JackSymbolTable();
        labels = new HashMap<String, Integer>();
        
        className = null;
        labels = new HashMap<String, Integer>();
        
        generateCode(root);
        
        vm.close();
    }
    /**NodeType:
     * CLASS, CLASS_VAR_DEC, TYPE, SUBROUTINE_DEC, PARAMETER_LIST, VAR_DEC,
     STATEMENTS, DO_STMT, LET_STMT, WHILE_STMT, RETURN_STMT, IF_STMT,
     EXPRESSION, TERM, BRACKETS, EXPRESSION_LIST, SUBROUTINE_CALL, TOKEN;
     
     * @param node
     *
     * //expression can have children: term or symbol;
     * term can have child symbol and symbol can have expression.
     * so THESE 3 needs to check for each other
     */
    public static boolean isToken(SyntaxTreeNode s){
        if(s.getToken()!=null){
            if("token".compareToIgnoreCase(s.getType().toString())==0){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isKeyword(SyntaxTreeNode s){
        if(s.getToken()!=null){
            if("keyword".compareToIgnoreCase(s.getToken().type.toString())==0){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isIdentifier(SyntaxTreeNode s){
        if(s.getToken()!=null){
            if("identifier".compareToIgnoreCase(s.getToken().type.toString())==0){
                return true;
            }
        }
        return false;
    }
    public static boolean isSymbol(SyntaxTreeNode s){
        //if(s.getType()!=null){
        //    return false;
        //}
        if(s.getToken()!=null){
            if("symbol".compareToIgnoreCase(s.getToken().type.toString())==0){
                return true;
            }
        }
        return false;
    }
    public static boolean isExpression(SyntaxTreeNode s){
        //System.out.println("is it expression? "+s.getType().toString());
        if(s.getType()!=null){
            if("expression".compareToIgnoreCase(s.getType().toString())==0){//THIS IS NODE TYPE!
                //System.out.println("yes! it is expression");
                return true;
            }
        }
        return false;
    }
    public static boolean isTerm(SyntaxTreeNode s){
        if(s.getType()!=null){
            if("term".compareToIgnoreCase(s.getType().toString())==0){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isExpressionList(SyntaxTreeNode s){
        if(s.getType()!=null){
            if("EXPRESSION_LIST".compareToIgnoreCase(s.getType().toString())==0){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isInt(SyntaxTreeNode s){
        if(s.getToken()!=null){
            if("integerConstant".compareToIgnoreCase(s.getToken().type.toString())==0){//OK!
                return true;
            }
        }
        return false;
    }
    
    public static boolean isString(SyntaxTreeNode s){
        if(s.getToken()!=null){
            if("stringrConstant".compareToIgnoreCase(s.getToken().type.toString())==0){
                return true;
            }
        }
        return false;
    }
    
    public void generateCode(SyntaxTreeNode node) {
        switch (node.getType()) {
            case CLASS:
                //System.out.println("CLASS: "+node.getChildren());
                for(int i=0; i<node.numChildren(); i++){
                    
                    for(TokenType tt: TokenType.values()){
                        //check every child to see if is not token, if not, Generate Code
                        if(!isToken(node.getChildren().get(i))){
                            //System.out.println("CLASS: "+node.getChildren());
                            generateCode(node.getChildren().get(i));
                            break;
                        }
                        else{
                            if(isIdentifier(node.getChildren().get(i))){
                                className = node.getChildren().get(i).getToken().toString();
                                
                            }
                            break;
                        }
                        
                    }
                }
                break;
                
            case SUBROUTINE_DEC:
                
                for(int i=0; i<node.numChildren(); i++){
                    if(!isToken(node.getChildren().get(i))){
                        generateCode(node.getChildren().get(i));
                    }
                    else{
                        if(isIdentifier(node.getChildren().get(i))){
                            parseSubroutineDec(node.getChildren().get(i).getToken().toString(),0);
                        }
                    }
                }
                break;
                
            case PARAMETER_LIST:
                
                for(int i=0; i<node.numChildren(); i++){
                    if(!isToken(node.getChildren().get(i))){
                        generateCode(node.getChildren().get(i));
                    }
                    //todo: else? parse param list?
                }
                break;
                
            case STATEMENTS:
                for(int i=0; i<node.numChildren(); i++){
                    if(!isToken(node.getChildren().get(i))){
                        generateCode(node.getChildren().get(i));
                    }
                    else{
                        
                    }
                }
                break;
                
            case RETURN_STMT:
                for(int i=0; i<node.numChildren(); i++){
                    if(!isToken(node.getChildren().get(i))){
                        parseExpressionList(node.getChildren().get(i));//??todo why?
                    }
                    else{
                        
                        
                    }
                }
                parseReturnStatement(node);
                
            case DO_STMT:
                //System.out.println("DO_STMT: "+node.getType());
                for(int i=0; i<node.numChildren(); i++){
                    if(!isToken(node.getChildren().get(i))){
                        generateCode(node.getChildren().get(i));
                    }
                }
                break;
                
            case SUBROUTINE_CALL:
                System.out.println("SUBROUTINE_CALL: ");
                String subNames = "";
                int subArgs = 0;
                //first check for expressionlist FIRST BECAUSE YOU WANT TO PUSH FIRST
                for(int i=0; i<node.numChildren(); i++){
                    if(isExpressionList(node.getChildren().get(i))){
                        generateCode(node.getChildren().get(i));
                    }
                }
                       
                       
                       for(int i=0; i<node.numChildren(); i++){
                           if(!isToken(node.getChildren().get(i))){
                               generateCode(node.getChildren().get(i));
                           }
                           else{
                               if(isIdentifier(node.getChildren().get(i))){
                                   subNames+=node.getChildren().get(i).getToken().token;
                                   
                               }
                               if(isSymbol(node.getChildren().get(i)) && node.getChildren().get(i).getToken().token.compareTo(".")==0){
                                   subNames+=node.getChildren().get(i).getToken().token;
                                   
                               }
                               
                           }
                       }
                       System.out.println("subNames = "+subNames);
                       if(subNames.compareToIgnoreCase("Output.printInt")==0){
                           subArgs=1;
                       }
                       //parseSubroutineCall(subNames,subArgs);
                       if(subNames.compareToIgnoreCase("Output.println")==0){
                           subArgs=0;
                       }
                       //parseSubroutineCall(subNames,subArgs);
                       if(subNames.compareToIgnoreCase("Math.multiply")==0){
                           subArgs=2;
                       }
                       //parseSubroutineCall(subNames,subArgs);
                       if(subNames.compareToIgnoreCase("Math.divide")==0){
                           subArgs=2;
                       }
                System.out.println("prepare to call func: "+subNames+" "+subArgs);
                       parseSubroutineCall(subNames,subArgs);
                break;
            case EXPRESSION_LIST:
                //System.out.println("EXPRESSION_LIST: ");
                for(int i=0; i<node.numChildren(); i++){
                    if(!isToken(node.getChildren().get(i))){
                        generateCode(node.getChildren().get(i));
                        
                    }
                }
                parseExpressionList(node);//
                break;//STOPPED HERE RESUME LATER NOT TESTED YET
            
            case EXPRESSION:
                //System.out.println("EXPRESSION: "+node.getType());
                for(int i=0; i<node.numChildren(); i++){
                    //if is not token
                    if(!isToken(node.getChildren().get(i))){
                        generateCode(node.getChildren().get(i));
                    }
                    //if is token SYMBOL:
                    if(isSymbol(node.getChildren().get(i))){
                        //parseSymbol(node.getChildren().get(i));
                    }
                    
                       
                       }
                       
                       break;
                       
                       
                       
                       
                       default:
                       System.err.println("Unhandled type " + node.getType());
                       }
                       
                       }
                       
                       
                       
                       public void parseSubroutineCall(String name, int nlocals){
                        //System.out.println("parsesubroutineDec: ");
                        vm.write_call(name, nlocals);
                           vm.writePop("temp", 0);//todo: always?
                           
                    }
                       
                       public void parseReturnStatement(SyntaxTreeNode node){
                        //System.out.println("parseReturnStatement: ");
                        vm.writePush("constant", 0);//todo: always like this?
                        vm.writeReturn();
                    }
                       
                       public void parseSubroutineCa11(String name, int nlocals){
                        vm.write_call(name, nlocals);
                        vm.writePop("temp", 0);//todo: always?
                    }
                       
                       public void parseSubroutineDec(String name, int nlocals){
                        //System.out.println("parsesubroutineDec: ");
                        vm.write_function(className+"."+name, nlocals);
                    }
    
    
    
                       public void parseExpression(SyntaxTreeNode node){
                        //System.out.println("parseExpression: ");
                        if(!node.hasChildren()) return;
                        for(int i=0; i<node.numChildren(); i++){
                            if(isTerm(node.getChildren().get(i))){
                                parseTerm(node.getChildren().get(i));
                            }
                            if(isSymbol(node.getChildren().get(i))){
                                //parseSymbol((node.getChildren().get(i)));
                            }
                        }
                    }
                       
                       public void parseTerm(SyntaxTreeNode node){
                        //System.out.println("parseTerm: ");
                        if(!node.hasChildren()) return;
                        for(int i=0; i<node.numChildren(); i++){
                            if(isInt(node.getChildren().get(i))){
                                
                                parseInt(node.getChildren().get(i));
                            }
                            if(isSymbol((node.getChildren().get(i)))){
                                //parseSymbol((node.getChildren().get(i)));
                            }
                            if(isTerm((node.getChildren().get(i)))){
                                parseTerm((node.getChildren().get(i)));
                            }
                        }
                    }
                       public void parseInt(SyntaxTreeNode node){
                        //System.out.println("parseInt: ");
                        vm.writePush("constant", Integer.parseInt(node.getToken().toString()));
                    }
                       
    public void parseExpressionList(SyntaxTreeNode node){
        //System.out.println("parseExpressionList: ");
        if(!node.hasChildren()) return;
        for (int i=0; i<node.numChildren(); i++){
            //System.out.println("prepare to parse children: "+node.getChildren().get(i));
            if(isExpression(node.getChildren().get(i))){
                parseExpression(node.getChildren().get(i));
            }
        }
    }

                       
                       private String getLabel(String keyword) {
                        if (!labels.containsKey(keyword)) {
                            labels.put(keyword, 0);
                        }
                        
                        int index = labels.get(keyword);
                        String label = keyword + index;
                        labels.put(keyword, index + 1);
                        return label;
                    }
                       public static void compile(String filename, boolean debug) throws Exception {
                           System.out.println("Compiling " + filename);
                           File f = new File(filename);
                           
                           String folder = f.getParent();
                           String base = Util.basename(filename);
                           
                           String outfile = folder + File.separator + "my" + base + ".vm";
                           
                           compile(filename, outfile, debug);
                       }
                       
                       public static void compile(String infile, String outfile, boolean debug)
                       throws Exception {
                           new JackCompiler(infile, outfile, debug);
                       }
                       
                       public static void main(String[] args) {
                        boolean debug = false;
                        for (String arg : args) {
                            if (arg.equals("-d"))
                                debug = true;
                                }
                        for (String file : Util.getJackFiles(args)) {
                            try {
                                compile(file, debug);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                       }
