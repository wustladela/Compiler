import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JackTokenizer
{
  private String input;
  private Token token;
  private static final String SYMBOLS = "{}()[].,;+-*/^|<>=~&";
  private static final String WHITE = "\r\n\t ";
  private int i;
  private String current;
  private static final Set<String> KEYWORDS = new HashSet(Arrays.asList(new String[] { "class", "method", "function", "constructor", "int", "boolean", "char", "void", "var", "static", "field", "let", "do", "if", "else", "while", "return", "true", "false", "null", "this" }));
  
  public JackTokenizer(String paramString)
    throws IOException
  {
    this.input = Util.readWholeFile(paramString);
    this.token = null;
    this.i = -1;
    this.current = "";
  }
  
  public boolean hasMoreTokens()
  {
    return this.i + 1 < this.input.length();
  }
  
  public void advance()
  {
    int j = 0;
    this.current = "";
    while (hasMoreTokens())
    {
      this.i += 1;
      char c = this.input.charAt(this.i);
      if (j == 0)
      {
        if ("\r\n\t ".indexOf(c) < 0) {
          if (c == '/')
          {
            int k = peek();
            if ("*/".indexOf(k) >= 0)
            {
              j = 1;
            }
            else
            {
              this.current += c;
              this.token = new Token(TokenType.SYMBOL, this.current);
              break;
            }
          }
          else if (c == '"')
          {
            this.current += c;
            j = 6;
          }
          else
          {
            if ("{}()[].,;+-*/^|<>=~&".indexOf(c) >= 0)
            {
              this.current += c;
              this.token = new Token(TokenType.SYMBOL, this.current);
              break;
            }
            this.current += c;
            j = 3;
          }
        }
      }
      else if (j == 1)
      {
        if (c == '/') {
          j = 2;
        } else if (c == '*') {
          j = 4;
        }
      }
      else if (j == 2)
      {
        if (c == '\n') {
          j = 0;
        }
      }
      else if (j == 3)
      {
        if ("{}()[].,;+-*/^|<>=~&".indexOf(c) >= 0)
        {
          this.i -= 1;
          this.token = new Token(maybeKeyword(this.current), this.current);
          break;
        }
        if ("\r\n\t ".indexOf(c) >= 0)
        {
          this.token = new Token(maybeKeyword(this.current), this.current);
          break;
        }
        this.current += c;
      }
      else if (j == 4)
      {
        if (c == '*') {
          j = 5;
        }
      }
      else if (j == 5)
      {
        if (c == '/') {
          j = 0;
        } else if (j != 42) {
          j = 4;
        }
      }
      else if (j == 6)
      {
        if (c == '"')
        {
          this.current += c;
          this.token = new Token(TokenType.STRING, this.current);
          break;
        }
        this.current += c;
      }
    }
    while ((this.i + 1 < this.input.length()) && ("\r\n\t ".indexOf(this.input.charAt(this.i + 1)) >= 0)) {
      this.i += 1;
    }
  }
  
  private TokenType maybeKeyword(String paramString)
  {
    if (Character.isDigit(paramString.charAt(0))) {
      return TokenType.INT;
    }
    if (KEYWORDS.contains(paramString)) {
      return TokenType.KEYWORD;
    }
    return TokenType.IDENTIFIER;
  }
  
  public char peek()
  {
    return this.input.charAt(this.i + 1);
  }
  
  public Token getToken()
  {
    return this.token;
  }
  
  public static String toXml(String paramString)
  {
    JackTokenizer localJackTokenizer = null;
    try
    {
      localJackTokenizer = new JackTokenizer(paramString);
    }
    catch (IOException localIOException) {}
    StringBuffer localStringBuffer = new StringBuffer("<tokens>\n");
    while (localJackTokenizer.hasMoreTokens())
    {
      localJackTokenizer.advance();
      Token localToken = localJackTokenizer.getToken();
      String str = Util.replaceEntities(localToken.token);
      localStringBuffer.append("<");
      localStringBuffer.append(localToken.type);
      localStringBuffer.append("> ");
      localStringBuffer.append(str);
      localStringBuffer.append(" </");
      localStringBuffer.append(localToken.type);
      localStringBuffer.append(">\n");
    }
    localStringBuffer.append("</tokens>\n");
    return localStringBuffer.toString();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    System.out.println(toXml(paramArrayOfString[0]));
  }
}
