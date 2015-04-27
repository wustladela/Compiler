
public enum Scope {
    STATIC, FIELD, VAR, ARGUMENT;
    
    public String getSegment()
    {
        switch(this){
        case ARGUMENT:
            return "argument";
        case FIELD:
            return "this";
        case STATIC:
            return "static";
        case VAR:
            return "local";
        }
        return "";
    }
}
