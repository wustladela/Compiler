public enum TokenType
{
    KEYWORD, SYMBOL, IDENTIFIER, INT, STRING;
    
    public String toString()
    {
        switch(this)
        {
            case KEYWORD:
                return "keyword";
            case SYMBOL:
                return "symbol";
            case IDENTIFIER:
                return "identifier";
            case INT:
                return "integerConstant";
            case STRING:
                return "stringConstant";
        }
        return "";
    }
}