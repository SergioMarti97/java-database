package db.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicTextIO<T> implements IBasicTextIO<T> {

    protected String fileName;

    public BasicTextIO(String fileName) {
        this.fileName = fileName;
    }

    public boolean save(List<T> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName))) {
            bw.flush();
            for (var v : list) {
                bw.write(write(v));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<T> read() {
        List<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    // System.out.println(line);
                    if (!isMalformed(line)) {
                        T o = build(line);
                        if (o != null) {
                            list.add(o);
                        } else {
                            System.out.printf("\"%s\" can not be built\n", line);
                        }
                    } else {
                        System.out.printf("Line: \"%s\" is malformed\n", line);
                    }
                } else {
                    break;
                }
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Getter & Setter

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "BasicIO{" +
                "fileName='" + fileName + '\'' +
                '}';
    }

}
