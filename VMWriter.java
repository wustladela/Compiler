import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class VMWriter {

    private String filename;
    private Vector<String> lines;
    private boolean debug;

    public VMWriter(String filename, boolean debug) {
        this.filename = filename;
        this.debug = debug;
        lines = new Vector<String>();
    }
    
    private void addLine(String s){
        lines.add(s);
        if(debug)
            System.out.println(s);
    }
    
    public void writeComment(String s){
        addLine("  // " + s);
    }

    public void writePush(String segment, int index) {
        addLine("  push " + segment + " " + index);
    }

    public void writePop(String segment, int index) {
        addLine("  pop " + segment + " " + index);
    }

    public void writeArithmetic(String command) {
        addLine("  " + command);
    }

    public void write_label(String label) {
        addLine("label " + label);
    }

    public void write_goto(String label) {
        addLine("  goto " + label);
    }

    public void write_if(String label) {
        addLine("  if-goto " + label);
    }

    public void write_call(String name, int nargs) {
        addLine("  call " + name + " " + nargs);
    }

    public void write_function(String name, int nlocals) {
        addLine("function " + name + " " + nlocals);
    }

    public void writeReturn() {
        addLine("  return");
    }

    public void close() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(
                filename)));
        for (String line : lines) {
            out.write(line);
            out.write("\n");
        }

        out.close();

    }

}
