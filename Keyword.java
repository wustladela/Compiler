public enum Keyword {
    CLASS, METHOD, FUNCTION, CONSTRUCTOR, 
    INT, BOOLEAN, CHAR, VOID, VAR, STATIC, FIELD, 
    LET, DO, IF, ELSE, WHILE, RETURN, TRUE, FALSE, NULL, THIS;
    
    public static boolean isKeyword(String word){
        for(Keyword k: values()){
            if(k.equals(word))
                return true;
        }
        return false;
    }
    
    public static Keyword getKeyword(String word){
        for(Keyword k: values()){
            if(k.equals(word))
                return k;
        }
        return null;
    }
    
    public boolean equals(String word){
        return name().toLowerCase().equals(word);
    }
}
