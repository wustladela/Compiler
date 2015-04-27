public enum NodeType {
    CLASS, CLASS_VAR_DEC, TYPE, SUBROUTINE_DEC, PARAMETER_LIST, VAR_DEC, 
    STATEMENTS, DO_STMT, LET_STMT, WHILE_STMT, RETURN_STMT, IF_STMT, 
    EXPRESSION, TERM, BRACKETS, EXPRESSION_LIST, SUBROUTINE_CALL, TOKEN;

    public String toString() {
        String s = name().replace("STMT", "STATEMENT").toLowerCase(); 
        StringBuffer sb = new StringBuffer();
        boolean cap = false;
        for(char c: s.toCharArray()){
            if(c=='_'){
                cap = true;
            }else if(cap){
                sb.append(Character.toUpperCase(c));
                cap = false;
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
