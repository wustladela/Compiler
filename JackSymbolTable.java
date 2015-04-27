import java.util.Hashtable;


public class JackSymbolTable {
//*maybe you want to use the type
	
	public Hashtable<String, String> scopeTable = new Hashtable<String, String>();
	//public Hashtable<String, String> typeTable = new Hashtable<String, String>();
	public Hashtable<String, Integer> indexTable = new Hashtable<String, Integer>();
	
	public void add(String name, String scope, int index){
		scopeTable.put(name, scope);
		//typeTable.put(name, type);
		indexTable.put(name, index);
	}
	
	public String getScope(String name){
		return scopeTable.get(name);
	}
//	public String getType(String name){
//		return typeTable.get(name);
//	}
	public int getIndex(String name){
		return indexTable.get(name);
	}
	
}
