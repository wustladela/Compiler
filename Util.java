import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Util {
    public static String replaceEntities(String s) {
        return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    public static String readWholeFile(String filename) throws IOException {
        Scanner in = new Scanner(new File(filename));
        in.useDelimiter("\\Z");
        if(!in.hasNext()) return "";
        String s = in.next();
        in.close();

        s = s.trim();

        s = s.replaceAll("" + (char) 13, "");

        return s;
    }

    public static String basename(String path) {
        int slash = Math.max(0, path.lastIndexOf(File.separator) + 1);
        int dot = path.lastIndexOf('.');
        return ((dot == -1) ? path : path.substring(slash, dot));
    }

    public static void dumpToFile(String filename, String contents)
            throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(
                filename)));
        out.write(contents);
        out.close();
    }

    private static boolean isJack(String filename) {
        return filename.substring(filename.length() - 4).equals("jack");
    }

    public static Vector<String> getJackFiles(String[] args) {
        Vector<String> files = new Vector<String>();
        Vector<String> queue = new Vector<String>();

        for (String arg : args) {
            queue.add(arg);
        }

        while (queue.size() > 0) {
            String arg = queue.remove(0);
            File f = new File(arg);
            if (!f.isDirectory()) {
                if (isJack(arg))
                    files.add(arg);
            } else {
                for (File file : f.listFiles()) {
                    if (file.isDirectory())
                        queue.add(file.getPath());
                    else {
                        String name = file.getName();
                        if (isJack(name))
                            files.add(file.getPath());
                    }
                }
            }
        }
        return files;
    }
}
